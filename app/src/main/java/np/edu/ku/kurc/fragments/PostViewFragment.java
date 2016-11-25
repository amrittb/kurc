package np.edu.ku.kurc.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.network.api.ServiceFactory;
import np.edu.ku.kurc.network.api.services.PostService;
import np.edu.ku.kurc.services.PostSyncService;
import np.edu.ku.kurc.views.viewmodels.PostViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewFragment extends Fragment {

    private Post post;
    private int postId;

    private View coordinatorLayout;

    private View postLoadingContainer;
    private View postLoadingBar;

    private PostViewModel postViewModel;

    private View postRetryContainer;

    private LocalBroadcastManager localBroadcastManager;

    private IntentFilter postSyncFilter;

    private boolean isSynced;

    private BroadcastReceiver postSyncBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra(Const.SERVICE_KEY_RESULT_CODE,Const.SERVICE_RESULT_FAILURE);

            if(resultCode != Const.SERVICE_RESULT_OK) {
                isSynced = false;
                showFetchError();
            } else {
                reloadPost();
            }
        }
    };

    /**
     * Builds instance of PostViewFragment.
     *
     * @param post Post to be loaded.
     * @return      PostViewFragment Instance.
     */
    public static PostViewFragment instance(Post post) {
        PostViewFragment fragment = new PostViewFragment();

        fragment.setPost(post);

        return fragment;
    }

    public static PostViewFragment instance(int id) {
        PostViewFragment fragment = new PostViewFragment();

        fragment.setPostId(id);

        return fragment;
    }

    /**
     * Sets Post field.
     *
     * @param post Post field to be set.
     */
    public void setPost(Post post) {
        this.post = post;
        this.postId = post.id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        postSyncFilter = new IntentFilter(PostSyncService.ACTION_POST_SYNC);

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

        loadPost();
    }

    /**
     * Loads Post.
     */
    private void loadPost() {
        postLoadingContainer.setVisibility(View.VISIBLE);
        postLoadingBar.setVisibility(View.VISIBLE);
        postRetryContainer.setVisibility(View.GONE);

        if(post != null && post.content != null) {
            consumePost(post);
        } else {
            post = new Post();

            post = post.findById(getContext(),postId);

            consumePost(post);
        }
    }

    private void reloadPost() {
        post = null;

        loadPost();
    }

    /**
     * Consumes post.
     *
     * @param post Post object.
     */
    private void consumePost(Post post) {
        if(post != null) {
            postLoadingContainer.setVisibility(View.GONE);

            if((post.content == null || post.content.contentEquals("")) && (!PostSyncService.isSyncingPost()) && (!isSynced)) {
                isSynced = true;

                postLoadingContainer.setVisibility(View.VISIBLE);

                PostSyncService.startPostSync(getContext(),post.id);

                localBroadcastManager.registerReceiver(postSyncBroadcastReceiver,postSyncFilter);
            }

            postViewModel.onBindModel(post);
        } else {
            showFetchError();
        }
    }

    private void showFetchError() {
        postLoadingBar.setVisibility(View.GONE);
        postRetryContainer.setVisibility(View.VISIBLE);

        Snackbar.make(coordinatorLayout,"Could not fetch post.", Snackbar.LENGTH_LONG)
                .setAction("TRY AGAIN", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        loadPost();
                    }
                }).show();
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
