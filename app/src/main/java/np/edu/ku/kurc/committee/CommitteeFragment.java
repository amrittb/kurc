package np.edu.ku.kurc.committee;

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

public class CommitteeFragment extends Fragment implements CommitteeContract.View {

    private CommitteeContract.Presenter presenter;

    private View postLoadingContainer;
    private View postLoadingBar;

    private View postNotFoundText;

    private View postRetryContainer;

    private PostViewModel postViewModel;

    private boolean isViewActive;

    /**
     * Returns an instance of committee fragment.
     *
     * @return  Instance of committee fragment.
     */
    public static CommitteeFragment instance() {
        return new CommitteeFragment();
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
                loadCommittee();
            }
        });

        postViewModel = new PostViewModel(view);
        postViewModel.setCommitteeMode();
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

        loadCommittee();
    }

    @Override
    public void onPause() {
        super.onPause();

        isViewActive = false;
    }

    /**
     * Loads Committee.
     */
    private void loadCommittee() {
        presenter.loadCommittee();
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
    public void showCommittee(Post committee) {
        postLoadingContainer.setVisibility(View.GONE);

        postViewModel.onBindModel(committee);
    }

    @Override
    public void showCommitteeLoadError() {
        postRetryContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return isViewActive;
    }

    @Override
    public void setPresenter(CommitteeContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
