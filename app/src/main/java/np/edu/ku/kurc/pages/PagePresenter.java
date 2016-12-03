package np.edu.ku.kurc.pages;

import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.posts.data.PostsDataSourceContract;
import np.edu.ku.kurc.posts.data.PostsRepository;

public class PagePresenter implements PageContract.Presenter {

    private PostsRepository pageRepository;
    private PageContract.View pageView;

    public PagePresenter(PostsRepository pageRepository, PageContract.View pageView) {
        this.pageRepository = pageRepository;
        this.pageView = pageView;
    }

    @Override
    public void loadPage(int pageId, boolean refreshPage) {
        if(refreshPage) {
            pageRepository.refreshPage();
        }

        pageView.setLoadingIndicator(true);
        pageRepository.getPage(pageId, new PostsDataSourceContract.LoadPostCallback() {

            @Override
            public void onPostLoaded(Post post) {
                if(!pageView.isActive()) {
                    return;
                }

                pageView.setLoadingIndicator(false);

                pageView.showPage(post);
            }

            @Override
            public void onPostNotFound() {
                if(!pageView.isActive()) {
                    return;
                }

                pageView.setLoadingIndicator(false);

                pageView.showPageNotFound();
            }

            @Override
            public void onPostLoadError() {
                if(!pageView.isActive()) {
                    return;
                }

                pageView.setLoadingIndicator(false);

                pageView.showPageLoadError();
            }
        });
    }
}
