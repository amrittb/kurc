package np.edu.ku.kurc.views.viewmodels;

import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import np.edu.ku.kurc.BuildConfig;
import np.edu.ku.kurc.R;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.utils.Metrics;
import np.edu.ku.kurc.views.viewmodels.contracts.ViewModel;

public class PostViewModel extends ViewModel<Post> {

    private View postContainer;

    private TextView postTitle;
    private TextView postDate;
    private TextView postAuthor;
    private WebView postContent;
    private ImageView postAuthorAvatar;
    private int avatarSize;
    private KurcWebViewClient webViewClient;
    private CropCircleTransformation cropCircleTransformation;

    public PostViewModel(View root) {
        super(root);
    }

    public PostViewModel(View root, Post model) {
        super(root, model);
    }

    @Override
    public void onBindView(View root) {
        avatarSize = (int) Metrics.dipToPixels(context,context.getResources().getDimension(R.dimen.avatar_size));

        if(cropCircleTransformation == null) {
            cropCircleTransformation = new CropCircleTransformation();
        }

        postContainer = root.findViewById(R.id.post_container);

        postTitle = (TextView) postContainer.findViewById(R.id.post_title);
        postDate = (TextView) postContainer.findViewById(R.id.post_date);
        postAuthor = (TextView) postContainer.findViewById(R.id.post_author);
        postContent = (WebView) postContainer.findViewById(R.id.post_content);
        postAuthorAvatar = (ImageView) postContainer.findViewById(R.id.post_author_avatar);

        initPostContentView();

        hidePostView();
    }

    /**
     * Initializes Post Content View.
     */
    public void initPostContentView() {
        WebSettings webSettings = postContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webViewClient = new KurcWebViewClient(postContent.getContext());

        postContent.setWebViewClient(webViewClient);
        postContent.addJavascriptInterface(new KurcJsInterface(),"Android");

        if(BuildConfig.DEBUG) {
            postContent.setWebChromeClient(new KurcWebChromeClient());
        }

        if(Build.VERSION.SDK_INT >= 19) {
            postContent.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            postContent.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        postContent.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onBindModel(Post model) {
        showPostView();

        postTitle.setText(model.title);
        postDate.setText(model.getDateString(context));
        postAuthor.setText(model.getAuthor().name);

        if(model.hasContent()) {
            // If page is not loaded, we defer adding content when the page completes loading.
            if(webViewClient.isPageLoaded()) {
                postContent.loadUrl("javascript:showContent('" + model.content +"')");
                showContent();
            } else {
                webViewClient.setContentWhenPageLoaded(model.content);
            }
        } else {
            hideContent();
        }

        Picasso.with(context)
                .load(model.getAuthor().avatar)
                .resize(avatarSize,avatarSize)
                .centerCrop()
                .transform(cropCircleTransformation)
                .into(postAuthorAvatar);
    }

    /**
     * Shows Post View.
     */
    public void showPostView() {
        postContainer.setVisibility(View.VISIBLE);
    }

    /**
     * Hides Post View.
     */
    public void hidePostView() {
        postContainer.setVisibility(View.GONE);
    }

    /**
     * Shows Post Content.
     */
    public void showContent() {
        postContent.setVisibility(View.VISIBLE);
    }

    /**
     * Hides Post Content.
     */
    public void hideContent() {
        postContent.setVisibility(View.GONE);
    }
}
