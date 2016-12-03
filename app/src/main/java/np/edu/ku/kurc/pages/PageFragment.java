package np.edu.ku.kurc.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.views.viewmodels.PostViewModel;

public class PageFragment extends Fragment implements PageContract.View {

    private PageContract.Presenter presenter;

    private View postLoadingContainer;
    private View postLoadingBar;

    private View postNotFoundText;

    private View postRetryContainer;

    protected PostViewModel postViewModel;

    private boolean isViewActive;

    private int pageId;

    /**
     * Returns an instance of page fragment.
     *
     * @return Instance of page fragment.
     */
    public static PageFragment instance(int pageId) {
        PageFragment pageFragment = new PageFragment();

        pageFragment.setPageId(pageId);

        return pageFragment;
    }

    /**
     * Setter for page id.
     *
     * @param pageId    Page of id to be loaded.
     */
    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        postLoadingContainer = view.findViewById(R.id.post_loading_container);
        postLoadingBar = view.findViewById(R.id.post_loading_bar);

        postNotFoundText = view.findViewById(R.id.posts_not_found);

        postRetryContainer = postLoadingContainer.findViewById(R.id.retry_container);
        Button postRetryBtn = (Button) postRetryContainer.findViewById(R.id.retry_btn);

        postRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPage();
            }
        });

        postViewModel = new PostViewModel(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        isViewActive = true;

        loadPage();
    }

    @Override
    public void onPause() {
        super.onPause();

        isViewActive = false;
    }

    /**
     * Loads Committee.
     */
    private void loadPage() {
        presenter.loadPage(pageId, false);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        postRetryContainer.setVisibility(View.GONE);
        postNotFoundText.setVisibility(View.GONE);

        if(active) {
            postLoadingContainer.setVisibility(View.VISIBLE);
            postLoadingBar.setVisibility(View.VISIBLE);
        } else {
            postLoadingBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPage(Post page) {
        postLoadingContainer.setVisibility(View.GONE);

        postViewModel.onBindModel(page);
    }

    @Override
    public void showPageLoadError() {
        postRetryContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPageNotFound() {
        postRetryContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return isViewActive;
    }

    @Override
    public void setPresenter(PageContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
