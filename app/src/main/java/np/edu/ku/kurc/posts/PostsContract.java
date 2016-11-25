package np.edu.ku.kurc.posts;

import java.util.List;

import np.edu.ku.kurc.framework.BaseView;
import np.edu.ku.kurc.models.Post;

public interface PostsContract {

    interface ListView extends BaseView<ListPresenter> {
        void setLoadingIndicator(boolean active);

        void showPosts(List<Post> posts);

        void showPostsLoadError();

        void showPostsNotFound();

        boolean isActive();
    }

    interface ExtendedListView extends ListView {
        void setOlderPostsLoadingIndicator(boolean active);

        void showOlderPosts(List<Post> posts);

        void showOlderPostsLoadError();

        void showOlderPostsNotFound();

        void setNewerPostsLoadingIndicator(boolean active);

        void showNewerPosts(List<Post> posts);

        void showNewerPostsLoadError();

        void showNewerPostsNotFound();
    }

    interface ListPresenter {
        void loadPostsForCategory(String category, boolean forceUpdate);

        void loadTopStories(boolean forceUpdate);
    }

    interface ExtendedListPresenter extends ListPresenter {
        void loadOlderPostsForCategory(String category, String postsBefore);

        void loadNewerPostsForCategory(String category, String postsAfter);
    }
}
