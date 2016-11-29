package np.edu.ku.kurc.committee;

import np.edu.ku.kurc.committee.data.CommitteeDataSource;
import np.edu.ku.kurc.models.Post;

public class CommitteePresenter implements CommitteeContract.Presenter {

    private CommitteeDataSource committeeRepository;
    private CommitteeContract.View committeeView;

    public CommitteePresenter(CommitteeDataSource committeeRepository, CommitteeContract.View committeeView) {
        this.committeeRepository = committeeRepository;
        this.committeeView = committeeView;
    }

    @Override
    public void loadCommittee() {
        committeeView.setLoadingIndicator(true);
        committeeRepository.loadCommittee(new CommitteeDataSource.LoadCommitteeCallback() {

            @Override
            public void onLoaded(Post committee) {
                if(!committeeView.isActive()) {
                    return;
                }

                committeeView.setLoadingIndicator(false);

                committeeView.showCommittee(committee);
            }

            @Override
            public void onLoadError() {
                if(!committeeView.isActive()) {
                    return;
                }

                committeeView.setLoadingIndicator(false);

                committeeView.showCommitteeLoadError();
            }
        });
    }
}
