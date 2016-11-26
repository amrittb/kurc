package np.edu.ku.kurc.posts;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.FeaturedMedia;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.posts.data.PostsLocalDataSource;
import np.edu.ku.kurc.posts.data.PostsRemoteDataSource;
import np.edu.ku.kurc.posts.data.PostsRepository;

public class PostActivity extends AppCompatActivity {

    private Post post;
    private ImageView featuredImage;
    private CollapsingToolbarLayout collapsingToolbar;

    private PostsRepository postsRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post);

        initPostsRepository();

        parsePostFromIntent();

        initToolbar();
    }

    @Override
    protected void onStart() {
        super.onStart();

        postsRepository.registerReceivers();

        initPostViewFragment();
    }

    @Override
    protected void onStop() {
        super.onStop();

        postsRepository.unregisterReceivers();
    }

    private void initPostsRepository() {
        if(postsRepository == null) {
            try {
                postsRepository = PostsRepository.getInstance();
            } catch (Exception e) {
                postsRepository = PostsRepository.getInstance(new PostsLocalDataSource(getApplicationContext()),
                        new PostsRemoteDataSource(getApplicationContext()));
            }
        }
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

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(post.title);

        featuredImage = (ImageView) findViewById(R.id.featured_image);

        loadFeaturedMedia();
    }

    /**
     * Loads Featured Image.
     */
    private void loadFeaturedMedia() {
        if(post.hasFeaturedMedia()) {
            featuredImage.post(new Runnable() {

                @Override
                public void run() {
                    int width = featuredImage.getWidth();
                    int height = featuredImage.getHeight();

                    FeaturedMedia media = post.getFeaturedMedia();
                    String url = media.getOptimalSourceUrl(width,height);

                    Picasso.with(PostActivity.this)
                            .load(url)
                            .resize(width,height)
                            .centerCrop()
                            .placeholder(R.drawable.hero_bg)
                            .error(R.drawable.hero_bg)
                            .into(featuredImage, new Callback() {

                                @Override
                                public void onSuccess() {
                                    changeToolbarColors();
                                }

                                @Override
                                public void onError() {}
                            });
                }
            });
        }
    }

    /**
     * Changes toolbar colors.
     */
    private void changeToolbarColors() {
        Bitmap bitmap = ((BitmapDrawable) featuredImage.getDrawable()).getBitmap();

        new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();

                int mutedColor = palette.getMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                int mutedDarkColor = palette.getDarkMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

                if(mutedColor != ResourcesCompat.getColor(getResources(),R.color.colorPrimary,null) &&
                mutedDarkColor != ResourcesCompat.getColor(getResources(),R.color.colorPrimaryDark,null)) {
                    if (vibrant != null) {
                        collapsingToolbar.setCollapsedTitleTextColor(vibrant.getTitleTextColor());
                    }

                    collapsingToolbar.setContentScrimColor(mutedColor);
                    collapsingToolbar.setStatusBarScrimColor(mutedDarkColor);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setNavigationBarColor(mutedDarkColor);
                    }
                }
            }
        });
    }

    /**
     * Initializes Post View Fragment.
     */
    private void initPostViewFragment() {
        PostViewFragment fragment = (PostViewFragment) getSupportFragmentManager().findFragmentById(R.id.post_container);

        if(fragment != null) {
            fragment.loadPost(post.id);

            return;
        }

        PostViewFragment instance = PostViewFragment.instance(post.id);
        instance.setPresenter(new PostPresenter(postsRepository,instance));

        getSupportFragmentManager().beginTransaction()
                .add(R.id.post_container, instance, getPostViewFragmentTag())
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
