package np.edu.ku.kurc.posts.data;

public interface PostsRemoteDataSourceContract {

    void getPosts(int perPage, String category, String postsAfter, String postsBefore, LoadPostsFromRemoteCallback calback);

    interface LoadPostsFromRemoteCallback {
        void onPostsLoaded(String action);

        void onPostsLoadError(String action);
    }
}
