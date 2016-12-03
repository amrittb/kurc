package np.edu.ku.kurc.pages;

import np.edu.ku.kurc.framework.BaseView;
import np.edu.ku.kurc.models.Post;

public interface PageContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean active);

        void showPage(Post page);

        void showPageLoadError();

        void showPageNotFound();

        boolean isActive();
    }

    interface Presenter {
        void loadPage(int id, boolean refresh);
    }
}
