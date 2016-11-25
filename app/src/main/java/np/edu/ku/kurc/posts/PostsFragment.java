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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.utils.DateUtils;
import np.edu.ku.kurc.views.adapters.PostsAdapter;
import np.edu.ku.kurc.views.widget.EndlessRecyclerViewScrollListener;

public class PostsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
                                                        PostsContract.ExtendedListView{

    private String categorySlug;

    private PostsAdapter adapter;

    private RecyclerView recyclerView;
    private AppCompatTextView noPostsTxt;
    private SwipeRefreshLayout swipeContainer;
    private CoordinatorLayout coordinatorLayout;

    private PostsContract.ExtendedListPresenter presenter;

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

        adapter = new PostsAdapter(getContext(), new ArrayList<Post>(), new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loadOlderPosts();
            }
        });

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
     * Initializes Recycler ListView.
     */
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                view.post(new Runnable() {

                    @Override
                    public void run() {
                        loadOlderPosts();
                    }
                });
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
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
        loadNewerPosts();
    }

    /**
     * Loads older posts.
     */
    public void loadOlderPosts() {
        String olderThan = getOldestPostDate();
        if(olderThan != null) {
            presenter.loadOlderPostsForCategory(categorySlug, olderThan);
        }
    }

    /**
     * Loads Newer posts.
     */
    public void loadNewerPosts() {
        String newerThan = getLatestPostDate();
        if(newerThan != null) {
            presenter.loadNewerPostsForCategory(categorySlug, newerThan);
        } else {
            swipeContainer.setRefreshing(false);
        }
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
        return getPostDateOfIndex(adapter.getItemCount() - 1);
    }

    /**
     * Returns post date of the index.
     *
     * @param index Index of the post in the list.
     * @return      Post date of the index.
     */
    @Nullable
    private String getPostDateOfIndex(int index) {
        Post post = adapter.getItem(index);

        // Post may be null because of footer item which is triggered when adding null to post list.
        if(post == null) {
            // If null check the item before it to fetch the date.
            int i = index - 1;

            if(i < 0) {
                return null;
            }

            post = adapter.getItem(i);
        }

        return DateUtils.toString(post.date);
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
        adapter.replacePosts(posts);
    }

    @Override
    public void showPostsLoadError() {
        showNetworkError();

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
    public void setPresenter(PostsContract.ListPresenter listPresenter) {
        this.presenter = (PostsContract.ExtendedListPresenter) listPresenter;
    }

    @Override
    public void setOlderPostsLoadingIndicator(boolean active) {
        adapter.setOlderPostsLoadingIndicator(true);
    }

    @Override
    public void showOlderPosts(List<Post> posts) {
        adapter.addOlderPosts(posts);
    }

    @Override
    public void showOlderPostsLoadError() {
        adapter.showOlderPostsLoadError();
        showNetworkError();
    }

    @Override
    public void showOlderPostsNotFound() {
        adapter.showOlderPostsNotFound();
    }

    @Override
    public void setNewerPostsLoadingIndicator(boolean active) {
        swipeContainer.setRefreshing(active);
    }

    @Override
    public void showNewerPosts(List<Post> posts) {
        adapter.addNewerPosts(posts);
        Snackbar.make(coordinatorLayout,"New posts.", Snackbar.LENGTH_INDEFINITE)
                .setAction("GOTO TOP", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        recyclerView.smoothScrollToPosition(0);
                    }
                }).show();
    }

    @Override
    public void showNewerPostsLoadError() {
        Snackbar.make(coordinatorLayout,"Check your network connection.", Snackbar.LENGTH_INDEFINITE)
                .setAction("TRY AGAIN", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        loadNewerPosts();
                    }
                }).show();
    }

    @Override
    public void showNewerPostsNotFound() {
        Toast.makeText(getContext(),"No new entries.", Toast.LENGTH_LONG).show();
    }

    public void showNetworkError() {
        Toast.makeText(getContext(),"Please, Check your network connection.", Toast.LENGTH_LONG).show();
    }
}
