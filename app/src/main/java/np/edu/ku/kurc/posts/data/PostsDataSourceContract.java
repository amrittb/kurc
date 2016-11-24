package np.edu.ku.kurc.posts.data;

import java.util.List;

import np.edu.ku.kurc.models.Post;

public interface PostsDataSourceContract {

    void refreshPosts();

    void getPosts(int perPage, String category, String postsAfter, String postsBefore, LoadPostsCallback callback);

    interface LoadPostsCallback {
        void onPostsLoaded(List<Post> posts);

        void onPostsLoadError();
    }
}
