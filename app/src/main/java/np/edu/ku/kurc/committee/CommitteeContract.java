package np.edu.ku.kurc.committee;

import np.edu.ku.kurc.framework.BaseView;
import np.edu.ku.kurc.models.Post;

public interface CommitteeContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showCommittee(Post committee);

        void showCommitteeLoadError();

        void showCommitteeNotFound();

        boolean isActive();
    }

    interface Presenter {
        void loadCommittee();
    }
}
