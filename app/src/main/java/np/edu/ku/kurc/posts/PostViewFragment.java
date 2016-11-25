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

    private int postId;

    private View coordinatorLayout;

    private View postLoadingContainer;
    private View postLoadingBar;

    private PostViewModel postViewModel;

    private View postRetryContainer;

    private boolean isViewActive;

    private PostsContract.ItemPresenter presenter;

    /**
     * Builds instance of PostViewFragment.
     *
     * @param id    Id of post to be loaded.
     * @return      PostViewFragment Instance.
     */
    public static PostViewFragment instance(int id) {
        PostViewFragment fragment = new PostViewFragment();

        fragment.setPostId(id);

        return fragment;
    }

    public void setPostId(int postId) {
        this.postId = postId;
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
    public void loadPost() {
        presenter.loadPost(postId,false);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        postRetryContainer.setVisibility(View.GONE);

        if(active) {
            postLoadingContainer.setVisibility(View.VISIBLE);
            postLoadingBar.setVisibility(View.VISIBLE);
        } else {
            postLoadingContainer.setVisibility(View.GONE);
            postLoadingBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPost(Post post) {
        postViewModel.onBindModel(post);

        setLoadingIndicator(!post.hasContent());
    }

    @Override
    public void showPostLoadError() {
        postLoadingContainer.setVisibility(View.VISIBLE);
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
        Snackbar.make(coordinatorLayout,"Could not load this post.", Snackbar.LENGTH_LONG)
                .setAction("TRY AGAIN", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        loadPost();
                    }
                }).show();
    }
}
