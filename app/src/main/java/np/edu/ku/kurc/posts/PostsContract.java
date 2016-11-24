package np.edu.ku.kurc.posts;

import java.util.List;

import np.edu.ku.kurc.framework.BaseView;
import np.edu.ku.kurc.models.Post;

public interface PostsContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showPosts(List<Post> posts);

        void showPostsLoadError();

        void showPostsNotFound();

        boolean isActive();
    }

    interface Presenter {
        void loadPostsForCategory(String category, boolean forceUpdate);

        void loadTopStories(boolean forceUpdate);
    }
}
