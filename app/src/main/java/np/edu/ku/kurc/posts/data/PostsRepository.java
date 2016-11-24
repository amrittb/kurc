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
            localDataSource.getPosts(perPage, category, postsAfter, postsBefore, new PostsDataSourceContract.LoadPostsCallback() {

                @Override
                public void onPostsLoaded(List<Post> posts) {
                    callback.onPostsLoaded(posts);
                }

                @Override
                public void onPostsLoadError() {
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
                getPostsFromLocalDataSource(perPage, category, postsAfter, postsBefore, callback);
            }

            @Override
            public void onPostsLoadError(String action) {
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
}
