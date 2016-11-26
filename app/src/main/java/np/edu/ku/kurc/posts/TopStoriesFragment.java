package np.edu.ku.kurc.posts;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.models.Author;
import np.edu.ku.kurc.models.Embedded;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.views.adapters.TopStoriesAdapter;
import np.edu.ku.kurc.views.widget.PreCachingLinearLayoutManager;
import np.edu.ku.kurc.views.widget.SnappingRecyclerView;

public class TopStoriesFragment extends Fragment implements PostsContract.ListView {

    private CoordinatorLayout coordinatorLayout;

    private SnappingRecyclerView topStoriesView;

    private View topStoriesRetryContainer;
    private View topStoriesLoadingBar;
    private Button topStoriesRetryBtn;

    private TopStoriesAdapter topStoriesAdapter;

    private PostsContract.ListPresenter presenter;

    private boolean isViewActive;

    public static TopStoriesFragment instance() {
        return new TopStoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_stories,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinator_layout);

        topStoriesLoadingBar = view.findViewById(R.id.top_stories_loading_bar);
        topStoriesView = (SnappingRecyclerView) view.findViewById(R.id.top_stories);

        topStoriesRetryContainer = view.findViewById(R.id.retry_container);
        topStoriesRetryBtn = (Button) view.findViewById(R.id.retry_btn);

        initStories();
    }

    /**
     * Initializes Stories.
     */
    private void initStories() {
        List<Post> stories = new ArrayList<>();

        Post dummyPost = new Post(0,"Loading...", Calendar.getInstance().getTime(),Calendar.getInstance().getTime(),"slug","link","content","excerpt");
        Author dummyAuthor = new Author();
        dummyAuthor.name = "kurc";

        dummyPost.embedded = new Embedded();
        dummyPost.embedded.authors = new ArrayList<>();
        dummyPost.embedded.authors.add(dummyAuthor);

        stories.add(dummyPost);

        topStoriesAdapter = new TopStoriesAdapter(getContext(), stories);

        initStoriesView();
    }

    /**
     * Initializes stories view.
     */
    private void initStoriesView() {
        PreCachingLinearLayoutManager layoutManager = new PreCachingLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        layoutManager.setExtraLayoutSpace(getContext().getResources().getDisplayMetrics().widthPixels);

        topStoriesView.setLayoutManager(layoutManager);
        topStoriesView.setAdapter(topStoriesAdapter);
        topStoriesView.enableViewScaling(true);

        topStoriesRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadTopStories(true);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        isViewActive = true;

        presenter.loadTopStories(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        isViewActive = false;
    }

    /**
     * Refreshes top stories.
     */
    public void refreshTopStories() {
        presenter.loadTopStories(true);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if(active) {
            topStoriesView.setVisibility(View.INVISIBLE);
            topStoriesLoadingBar.setVisibility(View.VISIBLE);
            topStoriesRetryContainer.setVisibility(View.GONE);
        } else {
            topStoriesLoadingBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPosts(List<Post> posts) {
        topStoriesAdapter.replaceStories(posts);

        topStoriesView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPostsLoadError() {
        topStoriesRetryContainer.setVisibility(View.VISIBLE);

        Snackbar.make(coordinatorLayout,"Could not fetch top stories.", Snackbar.LENGTH_LONG)
                .setAction("TRY AGAIN", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        presenter.loadTopStories(false);
                    }
                }).show();
    }

    @Override
    public void showPostsNotFound() {
        topStoriesRetryContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return isViewActive;
    }

    @Override
    public void setPresenter(PostsContract.ListPresenter listPresenter) {
        this.presenter = listPresenter;
    }
}