package np.edu.ku.kurc;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.Gson;

import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.fragments.PostViewFragment;
import np.edu.ku.kurc.models.Post;

public class PostActivity extends AppCompatActivity {

    private Post post;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post);

        parsePostFromIntent();

        initToolbar();

        initPostViewFragment();
    }

    /**
     * Parses post from intent.
     */
    private void parsePostFromIntent() {
        if(getIntent().getExtras() != null) {
            post = new Gson().fromJson(getIntent().getExtras().getString(Const.KEY_POST),Post.class);
        } else {
            onBackPressed();
        }
    }

    /**
     * Initializes app toolbar.
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(post.title);
    }

    /**
     * Initializes Post View Fragment.
     *
     * @TODO Find existing post view fragment and repopulate the post if exists.
     */
    private void initPostViewFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.post_container, PostViewFragment.instance(post), getPostViewFragmentTag())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Returns Post View Fragment Tag
     *
     * @return Returns post view fragment tag.
     */
    @NonNull
    private String getPostViewFragmentTag() {
        return "POST_VIEW_FRAGMENT_" + Integer.toString(post.id);
    }
}
