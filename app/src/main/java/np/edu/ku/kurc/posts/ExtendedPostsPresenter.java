package np.edu.ku.kurc.posts;

import java.util.List;

import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.posts.data.PostsDataSourceContract;
import np.edu.ku.kurc.posts.data.PostsRepository;

public class ExtendedPostsPresenter extends PostsPresenter implements PostsContract.ExtendedListPresenter {

    private PostsDataSourceContract.LoadPostsCallback loadOlderPostsCallback = new PostsDataSourceContract.LoadPostsCallback() {

        @Override
        public void onPostsLoaded(List<Post> posts) {
            PostsContract.ExtendedListView view = getView();

            if (!view.isActive()) {
                return;
            }

            view.setOlderPostsLoadingIndicator(false);

            if (posts.isEmpty()) {
                view.showOlderPostsNotFound();
            } else {
                view.showOlderPosts(posts);
            }
        }

        @Override
        public void onPostsLoadError() {
            PostsContract.ExtendedListView view = getView();

            if (!view.isActive()) {
                return;
            }

            view.setOlderPostsLoadingIndicator(false);

            view.showOlderPostsLoadError();
        }
    };

    private PostsDataSourceContract.LoadPostsCallback loadNewerPostsCallback = new PostsDataSourceContract.LoadPostsCallback() {

        @Override
        public void onPostsLoaded(List<Post> posts) {
            PostsContract.ExtendedListView view = getView();

            if (!view.isActive()) {
                return;
            }

            view.setNewerPostsLoadingIndicator(false);

            if (posts.isEmpty()) {
                view.showNewerPostsNotFound();
            } else {
                view.showNewerPosts(posts);
            }
        }

        @Override
        public void onPostsLoadError() {
            PostsContract.ExtendedListView view = getView();

            if (!view.isActive()) {
                return;
            }

            view.setNewerPostsLoadingIndicator(false);

            view.showNewerPostsLoadError();
        }
    };

    public ExtendedPostsPresenter(PostsRepository postsRepository, PostsContract.ExtendedListView listView) {
        super(postsRepository, listView);
    }

    @Override
    public void loadOlderPostsForCategory(String category, String postsBefore) {
        getView().setOlderPostsLoadingIndicator(true);

        postsRepository.getOlderPostsForCategory(category, postsBefore, loadOlderPostsCallback);
    }

    @Override
    public void loadNewerPostsForCategory(String category, String postsAfter) {
        getView().setNewerPostsLoadingIndicator(true);

        postsRepository.getNewerPostsForCategory(category, postsAfter, loadNewerPostsCallback);
    }

    public PostsContract.ExtendedListView getView() {
        return (PostsContract.ExtendedListView) postsView;
    }
}
