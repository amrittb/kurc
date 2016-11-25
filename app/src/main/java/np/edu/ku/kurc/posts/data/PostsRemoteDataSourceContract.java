package np.edu.ku.kurc.posts.data;

public interface PostsRemoteDataSourceContract {

    void registerReceivers();

    void unregisterReceivers();

    void getPosts(int perPage, String category, String postsAfter, String postsBefore, LoadFromRemoteCallback calback);

    void getPost(int id, LoadFromRemoteCallback callback);

    interface LoadFromRemoteCallback {
        void onLoaded(String action);

        void onLoadError(String action);
    }
}
