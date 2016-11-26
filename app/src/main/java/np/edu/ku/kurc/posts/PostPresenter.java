package np.edu.ku.kurc.posts;

import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.posts.data.PostsDataSourceContract;
import np.edu.ku.kurc.posts.data.PostsRepository;

public class PostPresenter implements PostsContract.ItemPresenter {

    private PostsRepository postsRepository;
    private PostsContract.ItemView postView;

    private PostsDataSourceContract.LoadPostCallback loadPostCallback = new PostsDataSourceContract.LoadPostCallback() {

        @Override
        public void onPostLoaded(Post post) {
            if(!postView.isActive()) {
                return;
            }

            postView.setLoadingIndicator(false);

            postView.showPost(post);
        }

        @Override
        public void onPostLoadError() {
            if(!postView.isActive()) {
                return;
            }

            postView.setLoadingIndicator(false);

            postView.showPostLoadError();
        }
    };

    public PostPresenter(PostsRepository postsRepository, PostsContract.ItemView postView) {
        this.postsRepository = postsRepository;
        this.postView = postView;

        this.postView.setPresenter(this);
    }

    @Override
    public void loadPost(int id, boolean forceUpdate) {
        postView.setLoadingIndicator(true);

        if(forceUpdate) {
            postsRepository.refreshPost();
        }

        postsRepository.getPost(id,loadPostCallback);
    }

    @Override
    public void loadStickyPost(boolean forceUpdate) {
        postView.setLoadingIndicator(true);

        if(forceUpdate) {
            postsRepository.refreshPost();
        }

        postsRepository.getStickyPost(loadPostCallback);
    }
}
