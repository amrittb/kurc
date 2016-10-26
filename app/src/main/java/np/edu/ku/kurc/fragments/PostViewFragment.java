package np.edu.ku.kurc.fragments;

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
import np.edu.ku.kurc.network.api.ServiceFactory;
import np.edu.ku.kurc.network.api.services.PostService;
import np.edu.ku.kurc.views.viewmodels.PostViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostViewFragment extends Fragment {

    private Post post;

    private View coordinatorLayout;

    private View postLoadingContainer;
    private View postLoadingBar;

    private PostViewModel postViewModel;

    private View postRetryContainer;

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

    /**
     * Sets Post field.
     *
     * @param post Post field to be set.
     */
    public void setPost(Post post) {
        this.post = post;
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

        loadPost();
    }

    /**
     * Loads Post.
     */
    private void loadPost() {
        postLoadingContainer.setVisibility(View.VISIBLE);
        postLoadingBar.setVisibility(View.VISIBLE);
        postRetryContainer.setVisibility(View.GONE);

        fetchPost();
    }

    /**
     * Fetches post from server.
     */
    private void fetchPost() {
        Call<Post> call = ServiceFactory.makeService(PostService.class).getPost(post.id);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                consumePost(response.body());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                consumePost(null);
            }
        });
    }

    /**
     * Consumes post.
     *
     * @param post Post object.
     */
    private void consumePost(Post post) {
        if(post != null) {
            postLoadingContainer.setVisibility(View.GONE);

            postViewModel.onBindModel(post);
        } else {
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
    }
}
