package np.edu.ku.kurc.posts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.views.viewmodels.PostViewModel;

public class PostViewFragment extends Fragment implements PostsContract.ItemView {

    private static final String MODE_POST_BY_ID = "MODE_POST_BY_ID";
    private static final String MODE_POST_STICKY = "MODE_POST_STICKY";

    private String loadMode;

    private int postId;

    private View coordinatorLayout;

    private View postLoadingContainer;
    private View postLoadingBar;

    private View postNotFoundText;

    private View postRetryContainer;

    private PostViewModel postViewModel;

    private boolean isViewActive;

    private PostsContract.ItemPresenter presenter;

    /**
     * Builds stickyInstance of PostViewFragment.
     *
     * @param id Id of post to be loaded.
     * @return PostViewFragment Instance.
     */
    public static PostViewFragment instance(int id) {
        PostViewFragment fragment = new PostViewFragment();

        fragment.setPostId(id);

        return fragment;
    }

    /**
     * Builds stickyInstance of PostViewFragment.
     *
     * @return      PostViewFragment Instance.
     */
    public static PostViewFragment stickyInstance() {
        PostViewFragment fragment = new PostViewFragment();

        fragment.setLoadMode(MODE_POST_STICKY);

        return fragment;
    }

    /**
     * Setter for post id.
     *
     * @param postId    Post Id to be set.
     */
    private void setPostId(int postId) {
        setLoadMode(MODE_POST_BY_ID);
        this.postId = postId;
    }

    /**
     * Sets the post loading mode.
     *
     * @param loadMode  Post Loading Mode.
     */
    private void setLoadMode(String loadMode) {
        this.loadMode = loadMode;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_view,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        coordinatorLayout = getActivity().findViewById(R.id.coordinator_layout);

        postLoadingContainer = view.findViewById(R.id.post_loading_container);
        postLoadingBar = view.findViewById(R.id.post_loading_bar);

        postNotFoundText = view.findViewById(R.id.posts_not_found);

        postRetryContainer = postLoadingContainer.findViewById(R.id.retry_container);
        Button postRetryBtn = (Button) postRetryContainer.findViewById(R.id.retry_btn);

        postRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPost();
            }
        });

        postViewModel = new PostViewModel(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        isViewActive = true;

        loadPost();
    }

    @Override
    public void onPause() {
        super.onPause();

        isViewActive = false;
    }

    /**
     * Loads Post.
     */
    private void loadPost() {
        if(loadMode.equals(MODE_POST_BY_ID)) {
            presenter.loadPost(postId,false);
        } else if(loadMode.equals(MODE_POST_STICKY)) {
            presenter.loadStickyPost(false);
        }
    }

    /**
     * Loads post of given id.
     *
     * @param id    Id Of post to be loaded.
     */
    public void loadPost(int id) {
        setPostId(id);
        loadPost();
    }

    /**
     * Refreshes Sticky Post.
     */
    public void refreshStickyPost() {
        postViewModel.hidePostView();
        presenter.loadStickyPost(true);
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
    public void showPost(Post post) {
        postLoadingContainer.setVisibility(View.GONE);

        postViewModel.onBindModel(post);

        setLoadingIndicator(!post.hasContent());
    }

    @Override
    public void showNotPost() {
        postNotFoundText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPostLoadError() {
        postRetryContainer.setVisibility(View.VISIBLE);

        showFetchError();
    }

    @Override
    public boolean isActive() {
        return isViewActive;
    }

    @Override
    public void setPresenter(PostsContract.ItemPresenter presenter) {
        this.presenter = presenter;
    }

    private void showFetchError() {
        Snackbar.make(coordinatorLayout,"Could not load post.", Snackbar.LENGTH_LONG)
                .setAction("TRY AGAIN", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        loadPost();
                    }
                }).show();
    }
}
