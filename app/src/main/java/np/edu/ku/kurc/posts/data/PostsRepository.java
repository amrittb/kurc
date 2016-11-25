package np.edu.ku.kurc.posts.data;

import java.util.List;

import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.network.api.ApiConstants;

public class PostsRepository implements PostsDataSourceContract {

    private static PostsRepository instance;

    private boolean isPostCacheOld;
    private boolean isPostsCacheOld;

    private PostsDataSourceContract localDataSource;
    private PostsRemoteDataSourceContract remoteDataSource;

    /**
     * Returns repository instance if created.
     *
     * @return              PostsRepository Instance.
     * @throws Exception
     */
    public static PostsRepository getInstance() throws Exception {
        if(instance == null) {
            throw new Exception("PostsRepository not instantiated previously. Call getInstance(PostsDataSourceContract, PostsRemoteDataSourceContract) to create the source first");
        }

        return instance;
    }

    /**
     * Returns repository instance, creates if needed.
     *
     * @param localDataSource       Local data source instance.
     * @param remoteDataSource      Remote data source instance.
     * @return                      PostsRepository instance.
     */
    public static PostsRepository getInstance(PostsDataSourceContract localDataSource, PostsRemoteDataSourceContract remoteDataSource) {
        if(instance == null) {
            instance = new PostsRepository(localDataSource,  remoteDataSource);
        }

        return instance;
    }

    private PostsRepository(PostsDataSourceContract localDataSource, PostsRemoteDataSourceContract remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public void refreshPosts() {
        isPostsCacheOld = true;
    }

    @Override
    public void getPosts(final int perPage, final String category, final String postsAfter, final String postsBefore, final LoadPostsCallback callback) {
        if(isPostsCacheOld) {
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

    @Override
    public void refreshPost() {
        isPostCacheOld = true;
    }

    @Override
    public void getPost(final int id, final LoadPostCallback callback) {
        if(isPostCacheOld) {
            getPostFromRemoteSource(id, callback);
        } else {
            localDataSource.getPost(id, new LoadPostCallback() {

                @Override
                public void onPostLoaded(Post post) {
                    if(!post.hasContent()) {
                        getPostFromRemoteSource(id, callback);
                    }

                    callback.onPostLoaded(post);
                }

                @Override
                public void onPostLoadError() {
                    getPostFromRemoteSource(id, callback);
                }
            });
        }
    }

    /**
     * Loads post from remote source.
     *
     * @param id            Id of post to be loaded.
     * @param callback      Post Load Callback.
     */
    private void getPostFromRemoteSource(final int id, final LoadPostCallback callback) {
        remoteDataSource.getPost(id, new PostsRemoteDataSourceContract.LoadFromRemoteCallback() {
            @Override
            public void onLoaded(String action) {
                isPostCacheOld = false;

                getPostFromLocalDataSource(id,callback);
            }

            @Override
            public void onLoadError(String action) {
                callback.onPostLoadError();
            }
        });
    }

    /**
     * Loads post from local data source.
     *
     * @param id            Id of post to be loaded.
     * @param callback      Post Load callback.
     */
    private void getPostFromLocalDataSource(int id, LoadPostCallback callback) {
        localDataSource.getPost(id,callback);
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
        remoteDataSource.getPosts(perPage, category, postsAfter, postsBefore, new PostsRemoteDataSourceContract.LoadFromRemoteCallback() {

            @Override
            public void onLoaded(String action) {
                isPostsCacheOld = false;
                // When posts are loaded from remote source, load its copy from local data source.
                getPostsFromLocalDataSource(perPage, category, postsAfter, postsBefore, callback);
            }

            @Override
            public void onLoadError(String action) {
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

    /**
     * Registers broadcast receivers.
     */
    public void registerReceivers() {
        remoteDataSource.registerReceivers();
    }

    /**
     * Unregisters broadcast receivers.
     */
    public void unregisterReceivers() {
        remoteDataSource.unregisterReceivers();
    }
}
