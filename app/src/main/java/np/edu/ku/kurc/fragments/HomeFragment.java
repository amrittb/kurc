package np.edu.ku.kurc.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.network.api.ServiceFactory;
import np.edu.ku.kurc.network.api.services.PostService;
import np.edu.ku.kurc.views.adapters.TopStoriesAdapter;
import np.edu.ku.kurc.views.widget.PreCachingLinearLayoutManager;
import np.edu.ku.kurc.views.widget.SnappingRecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private SnappingRecyclerView topStoriesView;
    private CoordinatorLayout coordinatorLayout;

    private TopStoriesAdapter topStoriesAdapter;

    private ImageView postFeaturedImage;
    private TextView postTitle;
    private TextView postDate;
    private TextView postAuthor;
    private TextView postContent;

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
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);

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

        postFeaturedImage = (ImageView) pinnedPostContainer.findViewById(R.id.post_featured_image);
        postTitle = (TextView) pinnedPostContainer.findViewById(R.id.post_title);
        postDate = (TextView) pinnedPostContainer.findViewById(R.id.post_date);
        postAuthor = (TextView) pinnedPostContainer.findViewById(R.id.post_author);
        postContent = (TextView) pinnedPostContainer.findViewById(R.id.post_content);

        initStories();
        initStoriesView();
        initPinnedPost();

        loadStories();
        loadPinnedPost();
    }

    /**
     * Initializes Stories.
     */
    private void initStories() {
        Post dummyPost = new Post(0,"Loading...", Calendar.getInstance().getTime(),Calendar.getInstance().getTime(),"slug","link","content","excerpt");
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
        fetchStories();
    }


    /**
     * Fetches new stories.
     */
    private void fetchStories() {
        Call<List<Post>> call = ServiceFactory.makeService(PostService.class).getTopStories();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                consumeStories(response.body());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                consumeStories(null);
            }
        });
    }

    /**
     * Consumes new stories.
     *
     * @param posts List of posts.
     */
    private void consumeStories(List<Post> posts) {
        if(posts != null) {
            stories.clear();

            if(posts.isEmpty()) {
                topStoriesLoadingBar.setVisibility(View.VISIBLE);
                topStoriesView.setVisibility(View.INVISIBLE);
                topStoriesRetryContainer.setVisibility(View.VISIBLE);
            } else {
                topStoriesLoadingBar.setVisibility(View.INVISIBLE);
                topStoriesView.setVisibility(View.VISIBLE);

                stories.addAll(posts);

                topStoriesAdapter.notifyDataSetChanged();
            }
        } else {
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
                if(response.body().isEmpty()) {
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
            postTitle.setText(post.title);
            postDate.setText(post.getDateString(getContext()));

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
}
