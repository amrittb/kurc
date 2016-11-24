package np.edu.ku.kurc.posts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.utils.DateUtils;
import np.edu.ku.kurc.views.adapters.PostsAdapter;

public class PostsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PostsContract.View {

    private List<Post> postList = new ArrayList<>();

    private String categorySlug;

    private PostsAdapter adapter;

    private RecyclerView recyclerView;
    private AppCompatTextView noPostsTxt;
    private SwipeRefreshLayout swipeContainer;
    private CoordinatorLayout coordinatorLayout;

    private PostsContract.Presenter presenter;

    private boolean isViewActive;


    /**
     * Creates a new instance of PostsFragment.
     *
     * @param categorySlug  Category slug for which posts fragment is to be created.
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        isViewActive = true;

        presenter.loadPostsForCategory(categorySlug, false);
    }

    @Override
    public void onStop() {
        super.onStop();

        isViewActive = false;
    }

    /**
     * OnRefresh Listener.
     */
    @Override
    public void onRefresh() {
        // Get newer posts after the date of the top post.
    }

    /**
     * Returns latest post date in the post list.
     *
     * @return  Latest post date.
     */
    @Nullable
    private String getLatestPostDate() {
        return getPostDateOfIndex(0);
    }

    /**
     * Returns oldest post date in the post list.
     *
     * @return  Oldest post date.
     */
    @Nullable
    private String getOldestPostDate() {
        return getPostDateOfIndex(postList.size() - 1);
    }

    /**
     * Returns post date of the index.
     *
     * @param index Index of the post in the list.
     * @return      Post date of the index.
     */
    @Nullable
    private String getPostDateOfIndex(int index) {
        if(postList == null || postList.isEmpty()) {
            return null;
        }

        return DateUtils.toString(postList.get(index).date);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        swipeContainer.post(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(active);
            }
        });
    }

    @Override
    public void showPosts(List<Post> posts) {
        postList.clear();

        postList.addAll(posts);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void showPostsLoadError() {
        Snackbar.make(coordinatorLayout,"Could not fetch entries.", Snackbar.LENGTH_LONG)
                .setAction("TRY AGAIN", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // Show retry container.
                    }
                }).show();
    }

    @Override
    public void showPostsNotFound() {
        // Show no posts container.
    }

    @Override
    public boolean isActive() {
        return isViewActive;
    }

    @Override
    public void setPresenter(PostsContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
