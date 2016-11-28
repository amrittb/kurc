package np.edu.ku.kurc.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.posts.PostPresenter;
import np.edu.ku.kurc.posts.PostViewFragment;
import np.edu.ku.kurc.posts.PostsPresenter;
import np.edu.ku.kurc.posts.TopStoriesFragment;
import np.edu.ku.kurc.posts.data.PostsRepository;

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeContainer;

    private PostsRepository postsRepository;
    private TopStoriesFragment topStoriesFragment;
    private PostViewFragment stickyPostFragment;

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
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        initSwipeContainer();

        initStoriesContainer();
        initStickyPostContainer();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    /**
     * Initializes swipe container.
     */
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
    private void initStickyPostContainer() {
        Fragment f = getFragmentManager().findFragmentById(R.id.sticky_post_fragment_container);

        if(f == null) {
            if(stickyPostFragment == null) {
                stickyPostFragment = PostViewFragment.stickyInstance();
                stickyPostFragment.setPresenter(new PostPresenter(postsRepository, stickyPostFragment));
            }

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.sticky_post_fragment_container,stickyPostFragment,Const.STICKY_POST_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public void onRefresh() {
        refreshTopStories();
        refreshStickyPost();

        swipeContainer.setRefreshing(false);
    }

    /**
     * Refreshes top stories.
     */
    private void refreshTopStories() {
        if(topStoriesFragment == null) {
            initStoriesContainer();
        }

        topStoriesFragment.refreshTopStories();
    }

    /**
     * Refreshes sticky post.
     */
    private void refreshStickyPost() {
        if(stickyPostFragment == null) {
            initStickyPostContainer();
        }

        stickyPostFragment.refreshStickyPost();
    }

    /**
     * Sets Posts Repository stickyInstance.
     *
     * @param repository    Posts Repository stickyInstance.
     */
    public void setPostsRepository(PostsRepository repository) {
        this.postsRepository = repository;
    }
}
