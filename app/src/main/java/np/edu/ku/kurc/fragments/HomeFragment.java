package np.edu.ku.kurc.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.posts.PostsPresenter;
import np.edu.ku.kurc.posts.TopStoriesFragment;
import np.edu.ku.kurc.posts.data.PostsRepository;
import np.edu.ku.kurc.views.viewmodels.PostViewModel;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private CoordinatorLayout coordinatorLayout;

    private View pinnedPostContainer;
    private View pinnedPostLoadingBar;
    private View pinnedPostLoadingContainer;
    private View retryPinnedPostContainer;
    private Button retryPinnedPostBtn;

    private PostViewModel postViewModel;
    private SwipeRefreshLayout swipeContainer;

    private PostsRepository postsRepository;
    private TopStoriesFragment topStoriesFragment;

    /**
     * Creates home fragment instance.
     *
     * @return HomeFragment Instance.
     */
    public static HomeFragment instance(PostsRepository repository) {
        HomeFragment homeFragment = new HomeFragment();

        homeFragment.setPostsRepository(repository);

        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        pinnedPostContainer = view.findViewById(R.id.post_container);
        pinnedPostLoadingBar = view.findViewById(R.id.pinned_post_loading_bar);
        pinnedPostLoadingContainer = view.findViewById(R.id.pinned_post_loading_container);

        retryPinnedPostContainer = pinnedPostLoadingContainer.findViewById(R.id.retry_container);
        retryPinnedPostBtn = (Button) retryPinnedPostContainer.findViewById(R.id.retry_btn);

        postViewModel = new PostViewModel(pinnedPostContainer);

        initSwipeContainer();

        initStoriesContainer();
        initPinnedPost();

        loadPinnedPost();
    }

    private void initSwipeContainer() {
        swipeContainer.setOnRefreshListener(this);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
    }

    /**
     * Initializes stories container.
     */
    private void initStoriesContainer() {
        Fragment f = getFragmentManager().findFragmentById(R.id.top_stories_fragment_container);

        if(f == null) {
            if(topStoriesFragment == null) {
                topStoriesFragment = TopStoriesFragment.instance();
                topStoriesFragment.setPresenter(new PostsPresenter(postsRepository, topStoriesFragment));
            }

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.top_stories_fragment_container,topStoriesFragment,Const.TOP_STORIES_FRAGMENT_TAG)
                    .commit();
        }
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
        pinnedPostLoadingBar.setVisibility(View.VISIBLE);
        retryPinnedPostContainer.setVisibility(View.GONE);

        Post p = new Post();

        Post post = p.getLatestPinned(getActivity().getApplicationContext());

        consumePinnedPost(post);
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

    }

    /**
     * Sets Posts Repository instance.
     *
     * @param repository    Posts Repository instance.
     */
    public void setPostsRepository(PostsRepository repository) {
        this.postsRepository = repository;
    }
}
