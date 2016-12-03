package np.edu.ku.kurc.committee;

import np.edu.ku.kurc.BuildConfig;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.posts.data.PostsDataSourceContract;
import np.edu.ku.kurc.posts.data.PostsRepository;

public class CommitteePresenter implements CommitteeContract.Presenter {

    private PostsRepository committeeRepository;
    private CommitteeContract.View committeeView;

    public CommitteePresenter(PostsRepository committeeRepository, CommitteeContract.View committeeView) {
        this.committeeRepository = committeeRepository;
        this.committeeView = committeeView;
    }

    @Override
    public void loadCommittee() {
        committeeView.setLoadingIndicator(true);
        committeeRepository.getPage(BuildConfig.KURC_COMMITTEE_PAGE_ID, new PostsDataSourceContract.LoadPostCallback() {

            @Override
            public void onPostLoaded(Post post) {
                if(!committeeView.isActive()) {
                    return;
                }

                committeeView.setLoadingIndicator(false);

                committeeView.showCommittee(post);
            }

            @Override
            public void onPostNotFound() {
                if(!committeeView.isActive()) {
                    return;
                }

                committeeView.setLoadingIndicator(false);

                committeeView.showCommitteeNotFound();
            }

            @Override
            public void onPostLoadError() {
                if(!committeeView.isActive()) {
                    return;
                }

                committeeView.setLoadingIndicator(false);

                committeeView.showCommitteeLoadError();
            }
        });
    }
}
