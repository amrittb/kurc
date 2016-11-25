package np.edu.ku.kurc.posts.data;

import java.util.List;

import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.network.api.ApiConstants;

public class PostsRepository implements PostsDataSourceContract {

    private boolean isCacheOld;

    private PostsDataSourceContract localDataSource;
    private PostsRemoteDataSourceContract remoteDataSource;

    public PostsRepository(PostsDataSourceContract localDataSource, PostsRemoteDataSourceContract remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public void refreshPosts() {
        isCacheOld = true;
    }

    @Override
    public void getPosts(final int perPage, final String category, final String postsAfter, final String postsBefore, final LoadPostsCallback callback) {
        if(isCacheOld) {
            getPostsFromRemoteDataSource(perPage, category, postsAfter, postsBefore, callback);
        } else {
            // First fetch from local data source.
            localDataSource.getPosts(perPage, category, postsAfter, postsBefore, new PostsDataSourceContract.LoadPostsCallback() {

                @Override
                public void onPostsLoaded(List<Post> posts) {
                    if( ! posts.isEmpty()) {
                        // If the loaded posts are not empty call the posts loaded method on callback.
                        callback.onPostsLoaded(posts);
                    } else {
                        // If the posts are empty try remote source.
                        getPostsFromRemoteDataSource(perPage, category, postsAfter, postsBefore, callback);
                    }
                }

                @Override
                public void onPostsLoadError() {
                    // If there are any posts load error try from remote source.
                    getPostsFromRemoteDataSource(perPage, category, postsAfter, postsBefore, callback);
                }
            });
        }
    }

    /**
     * Fetches posts from remote data source.
     *
     * @param perPage       Posts per page.
     * @param category      Post category to be fetched.
     * @param postsAfter    Posts After this date to be fetched.
     * @param postsBefore   Posts before this date to be fetched.
     * @param callback      Posts Fetched callback.
     */
    private void getPostsFromRemoteDataSource(final int perPage, final String category, final String postsAfter, final String postsBefore, final LoadPostsCallback callback) {
        remoteDataSource.getPosts(perPage, category, postsAfter, postsBefore, new PostsRemoteDataSourceContract.LoadPostsFromRemoteCallback() {

            @Override
            public void onPostsLoaded(String action) {
                // When posts are loaded from remote source, load its copy from local data source.
                getPostsFromLocalDataSource(perPage, category, postsAfter, postsBefore, callback);
            }

            @Override
            public void onPostsLoadError(String action) {
                // If there is any error show on posts load error.
                callback.onPostsLoadError();
            }
        });
    }

    /**
     * Fetches posts from local data source.
     *
     * @param perPage       Posts per page.
     * @param category      Post category to be loaded.
     * @param postsAfter    Posts After this date to be loaded.
     * @param postsBefore   Posts before this date to be loaded.
     * @param callback      Posts Loaded callback.
     */
    private void getPostsFromLocalDataSource(int perPage, String category, String postsAfter, String postsBefore, LoadPostsCallback callback) {
        localDataSource.getPosts(perPage, category, postsAfter, postsBefore, callback);
    }

    /**
     * Fetches Top Stories.
     *
     * @param callback  Posts Load Callback.
     */
    public void getTopStories(LoadPostsCallback callback) {
        getPosts(Const.TOP_STORIES_POST_COUNT, null, null, null, callback);
    }

    /**
     * Fetches Posts for category.
     *
     * @param category  Posts of category.
     * @param callback  Posts Load Callback.
     */
    public void getPostsForCategory(String category, LoadPostsCallback callback) {
        getPosts(ApiConstants.POSTS_PER_PAGE_DEFAULT, category, null, null, callback);
    }

    /**
     * Fetches Older posts for category.
     *
     * @param category      Posts of category.
     * @param postsBefore   Posts Older than this date.
     * @param callback      Posts Load Callback.
     */
    public void getOlderPostsForCategory(String category, String postsBefore, LoadPostsCallback callback) {
        getPosts(ApiConstants.POSTS_PER_PAGE_DEFAULT, category, null, postsBefore, callback);
    }

    /**
     * Fetches Newer posts for category.
     *
     * @param category      Posts of category.
     * @param postsAfter    Posts Newer than this date.
     * @param callback      Posts Load Callback.
     */
    public void getNewerPostsForCategory(String category, String postsAfter, LoadPostsCallback callback) {
        getPosts(ApiConstants.POSTS_PER_PAGE_DEFAULT, category, postsAfter, null, callback);
    }
}
