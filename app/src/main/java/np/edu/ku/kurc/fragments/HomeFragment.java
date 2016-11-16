package np.edu.ku.kurc.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.Author;
import np.edu.ku.kurc.models.Embedded;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.models.collection.PostCollection;
import np.edu.ku.kurc.network.api.ServiceFactory;
import np.edu.ku.kurc.network.api.services.PostService;
import np.edu.ku.kurc.services.PostSyncService;
import np.edu.ku.kurc.views.adapters.TopStoriesAdapter;
import np.edu.ku.kurc.views.viewmodels.PostViewModel;
import np.edu.ku.kurc.views.widget.PreCachingLinearLayoutManager;
import np.edu.ku.kurc.views.widget.SnappingRecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SnappingRecyclerView topStoriesView;
    private CoordinatorLayout coordinatorLayout;

    private TopStoriesAdapter topStoriesAdapter;

    private View pinnedPostContainer;
    private View pinnedPostLoadingBar;
    private View pinnedPostLoadingContainer;
    private View retryPinnedPostContainer;
    private Button retryPinnedPostBtn;

    private List<Post> stories = new ArrayList<>();

    private View topStoriesContainer;
    private View topStoriesLoadingBar;
    private View topStoriesRetryContainer;
    private Button topStoriesRetryBtn;
    private PostViewModel postViewModel;
    private SwipeRefreshLayout swipeContainer;

    private IntentFilter postsSyncFilter;
    private LocalBroadcastManager localBroadcastManager;

    private boolean isSyncedPreviously = false;

    private BroadcastReceiver postsSyncBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra(Const.SERVICE_KEY_RESULT_CODE,Const.SERVICE_RESULT_FAILURE);

            if(resultCode != Const.SERVICE_RESULT_OK) {
                isSyncedPreviously = false;

                showStoriesError();
            } else {
                loadStories();
            }
        }
    };

    /**
     * Creates home fragment instance.
     *
     * @return HomeFragment Instance.
     */
    public static HomeFragment instance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        postsSyncFilter = new IntentFilter(PostSyncService.ACTION_POSTS_SYNC);

        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        topStoriesContainer = view.findViewById(R.id.top_stories_container);
        topStoriesLoadingBar = topStoriesContainer.findViewById(R.id.top_stories_loading_bar);
        topStoriesView = (SnappingRecyclerView) topStoriesContainer.findViewById(R.id.top_stories);

        topStoriesRetryContainer = topStoriesContainer.findViewById(R.id.retry_container);
        topStoriesRetryBtn = (Button) topStoriesRetryContainer.findViewById(R.id.retry_btn);

        pinnedPostContainer = view.findViewById(R.id.post_container);
        pinnedPostLoadingBar = view.findViewById(R.id.pinned_post_loading_bar);
        pinnedPostLoadingContainer = view.findViewById(R.id.pinned_post_loading_container);

        retryPinnedPostContainer = pinnedPostLoadingContainer.findViewById(R.id.retry_container);
        retryPinnedPostBtn = (Button) retryPinnedPostContainer.findViewById(R.id.retry_btn);

        initSwipeContainer();

        initStories();
        initStoriesView();
        initPinnedPost();

        loadStories();
        loadPinnedPost();

        postViewModel = new PostViewModel(pinnedPostContainer);
    }

    private void initSwipeContainer() {
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
    }

    /**
     * Initializes Stories.
     */
    private void initStories() {
        Post dummyPost = new Post(0,"Loading...", Calendar.getInstance().getTime(),Calendar.getInstance().getTime(),"slug","link","content","excerpt");
        Author dummyAuthor = new Author();
        dummyAuthor.name = "kurc";

        dummyPost.embedded = new Embedded();
        dummyPost.embedded.authors = new ArrayList<>();
        dummyPost.embedded.authors.add(dummyAuthor);

        stories.add(dummyPost);
        topStoriesAdapter = new TopStoriesAdapter(getContext(), stories);
    }

    /**
     * Initializes stories view.
     */
    private void initStoriesView() {
        PreCachingLinearLayoutManager layoutManager = new PreCachingLinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager.setExtraLayoutSpace(getContext().getResources().getDisplayMetrics().widthPixels);

        topStoriesView.setLayoutManager(layoutManager);
        topStoriesView.setAdapter(topStoriesAdapter);
        topStoriesView.enableViewScaling(true);

        topStoriesRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStories();
            }
        });
    }

    /**
     * Loads stories.
     */
    private void loadStories() {
        topStoriesView.setVisibility(View.INVISIBLE);
        topStoriesLoadingBar.setVisibility(View.VISIBLE);
        topStoriesRetryContainer.setVisibility(View.GONE);

        Post p = new Post();

        PostCollection posts = p.latestPaginated(getActivity().getApplicationContext(),5,1,true,true);

        consumeStories(posts);
    }

    /**
     * Consumes new stories.
     *
     * @param posts List of posts.
     */
    private void consumeStories(List<Post> posts) {
        swipeContainer.setRefreshing(false);

        if(posts != null) {
            if(posts.isEmpty()) {
                topStoriesLoadingBar.setVisibility(View.VISIBLE);
                topStoriesView.setVisibility(View.INVISIBLE);
                topStoriesRetryContainer.setVisibility(View.GONE);

                if((! PostSyncService.isSyncingPosts()) && (! isSyncedPreviously)) {
                    isSyncedPreviously = true;

                    PostSyncService.startPostsSync(getContext());

                    localBroadcastManager.registerReceiver(postsSyncBroadcastReceiver, postsSyncFilter);
                }
            } else {
                stories.clear();

                topStoriesLoadingBar.setVisibility(View.GONE);
                topStoriesView.setVisibility(View.VISIBLE);

                stories.addAll(posts);

                topStoriesAdapter.notifyDataSetChanged();
            }
        } else {
            showStoriesError();
        }
    }

    private void showStoriesError() {
        topStoriesLoadingBar.setVisibility(View.INVISIBLE);
        topStoriesRetryContainer.setVisibility(View.VISIBLE);

        Snackbar.make(coordinatorLayout,"Could not fetch top stories.", Snackbar.LENGTH_LONG)
                .setAction("TRY AGAIN", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        loadStories();
                    }
                }).show();
    }

    /**
     * Initializes pinned post.
     */
    private void initPinnedPost() {
        retryPinnedPostBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadPinnedPost();
            }
        });
    }

    /**
     * Loads Pinned Post.
     */
    private void loadPinnedPost() {
        fetchPinnedPost();
        pinnedPostLoadingBar.setVisibility(View.VISIBLE);
        retryPinnedPostContainer.setVisibility(View.GONE);
    }

    /**
     * Fetches pinned post.
     */
    private void fetchPinnedPost() {
        Call<List<Post>> call = ServiceFactory.makeService(PostService.class).getPinnedPost();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.body() == null) {
                    consumePinnedPost(null);
                } else if(response.body().isEmpty()) {
                    consumePinnedPost(null);
                } else {
                    consumePinnedPost(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                consumePinnedPost(null);
            }
        });
    }

    private void consumePinnedPost(Post post) {
        if(post != null) {
            postViewModel.onBindModel(post);

            pinnedPostLoadingContainer.setVisibility(View.GONE);
            pinnedPostContainer.setVisibility(View.VISIBLE);
        } else {
            pinnedPostLoadingBar.setVisibility(View.GONE);
            retryPinnedPostContainer.setVisibility(View.VISIBLE);

            Snackbar.make(coordinatorLayout,"Could not fetch pinned post.", Snackbar.LENGTH_LONG)
                    .setAction("TRY AGAIN", new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            loadPinnedPost();
                        }
                    }).show();
        }
    }

    @Override
    public void onRefresh() {
        fetchPinnedPost();
    }
}
