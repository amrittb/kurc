package np.edu.ku.kurc.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.network.api.ServiceFactory;
import np.edu.ku.kurc.network.api.services.PostService;
import np.edu.ku.kurc.views.adapters.PostsAdapter;
import np.edu.ku.kurc.views.widget.PreCachingLinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private List<Post> postList = new ArrayList<>();

    private String categorySlug;

    private PostsAdapter adapter;

    private RecyclerView recyclerView;
    private AppCompatTextView noPostsTxt;
    private SwipeRefreshLayout swipeContainer;
    private CoordinatorLayout coordinatorLayout;

    /**
     * Creates a new instance of PostsFragment.
     *
     * @param categorySlug Category slug for which posts fragment is to be created.
     * @return              PostsFragment instance.
     */
    public static PostsFragment instance(String categorySlug) {
        PostsFragment fragment = new PostsFragment();

        fragment.setCategory(categorySlug);

        return fragment;
    }

    /**
     * Sets the category slug.
     *
     * @param category Category slug to be set.
     */
    public void setCategory(String category) {
        this.categorySlug = category;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.post_list);
        noPostsTxt = (AppCompatTextView) view.findViewById(R.id.txt_no_posts);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);

        adapter = new PostsAdapter(getContext(), postList);

        initSwipeContainer();
        initRecyclerView();

        loadPosts();
    }

    /**
     * Initializes swipe container.
     */
    private void initSwipeContainer() {
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
    }

    /**
     * Initializes Recycler View.
     */
    private void initRecyclerView() {
        recyclerView.setLayoutManager(new PreCachingLinearLayoutManager(getContext(),getContext().getResources().getDisplayMetrics().heightPixels));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Loads posts on next UI update tick for swipe container.
     */
    private void loadPosts() {
        swipeContainer.post(new Runnable() {

            @Override
            public void run() {
            swipeContainer.setRefreshing(true);
            onRefresh();
            }
        });
    }

    /**
     * OnRefresh Listener.
     *
     * @TODO Instead of simply fetching posts fetch posts from database and cache it then load it from datsbase.
     */
    @Override
    public void onRefresh() {
        fetchPosts();
    }

    /**
     * Fetches posts from posts service for given category.
     */
    private void fetchPosts() {
        Call<List<Post>> call = ServiceFactory.makeService(PostService.class).getPostsForCategory(categorySlug);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                consumePosts(response.body());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                consumePosts(null);
            }
        });
    }

    /**
     * Consumes Posts.
     *
     * @param posts Posts to be consumed.
     */
    private void consumePosts(List<Post> posts) {
        swipeContainer.setRefreshing(false);

        if(posts != null) {
            postList.clear();

            if(posts.isEmpty()) {
                noPostsTxt.setVisibility(View.VISIBLE);
            } else {
                postList.addAll(posts);

                adapter.notifyDataSetChanged();
            }
        } else {
            Snackbar.make(coordinatorLayout,"Could not fetch latest entries.", Snackbar.LENGTH_LONG)
                    .setAction("TRY AGAIN", new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            loadPosts();
                        }
                    }).show();
        }
    }
}
