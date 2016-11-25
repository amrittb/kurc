package np.edu.ku.kurc.posts;

import java.util.List;

import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.posts.data.PostsDataSourceContract;
import np.edu.ku.kurc.posts.data.PostsRepository;

public class PostsPresenter implements PostsContract.ListPresenter {

    protected PostsRepository postsRepository;

    protected PostsContract.ListView postsView;

    private PostsDataSourceContract.LoadPostsCallback loadPostsCallback = new PostsDataSourceContract.LoadPostsCallback() {

        @Override
        public void onPostsLoaded(List<Post> posts) {
            if (!postsView.isActive()) {
                return;
            }

            postsView.setLoadingIndicator(false);

            if (posts.isEmpty()) {
                postsView.showPostsNotFound();
            } else {
                postsView.showPosts(posts);
            }
        }

        @Override
        public void onPostsLoadError() {
            if (!postsView.isActive()) {
                return;
            }

            postsView.setLoadingIndicator(false);

            postsView.showPostsLoadError();
        }
    };

    public PostsPresenter(PostsRepository postsRepository, PostsContract.ListView listView) {
        this.postsRepository = postsRepository;
        this.postsView = listView;

        this.postsView.setPresenter(this);
    }

    @Override
    public void loadPostsForCategory(String category, boolean forceUpdate) {
        postsView.setLoadingIndicator(true);

        if(forceUpdate) {
            postsRepository.refreshPosts();
        }

        postsRepository.getPostsForCategory(category, loadPostsCallback);
    }

    @Override
    public void loadTopStories(boolean forceUpdate) {
        postsView.setLoadingIndicator(true);

        if(forceUpdate) {
            postsRepository.refreshPosts();
        }

        postsRepository.getTopStories(loadPostsCallback);
    }
}
