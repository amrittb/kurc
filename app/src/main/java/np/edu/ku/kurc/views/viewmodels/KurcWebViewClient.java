package np.edu.ku.kurc.views.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import np.edu.ku.kurc.common.Const;

public class KurcWebViewClient extends WebViewClient {

    private Context context;

    private String content;

    private boolean isFinished;

    private boolean isCommitteeMode;

    public KurcWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        isFinished = true;

        if(content != null) {
            setPostContent(view, content);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Pattern pattern = Pattern.compile(Const.KURC_POST_VIEW_URL_PATTERN);
        Matcher matcher = pattern.matcher(url);

        if(matcher.find()) {
            // matcher.group(2) is correct slug of post.
            // @TODO: Add implementation to load posts by slug.

            return false;
        }
        // If did not match,
        // Send the request to other browsers.

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);

        return true;
    }

    /**
     * Sets Content of page when loaded.
     *
     * @param content   Content to be loaded.
     */
    public void setContentWhenPageLoaded(WebView view, String content) {
        this.isCommitteeMode = false;

        setContent(view, content);
    }

    /**
     * Sets Committee page when loaded.
     *
     * @param content   Content to be loaded.
     */
    public void setCommitteeWhenPageLoaded(WebView view, String content) {
        isCommitteeMode = true;

        setContent(view, content);
    }

    private void setContent(WebView view, String content) {
        this.content = content;

        if(isFinished) {
            setPostContent(view, this.content);
        }
    }

    /**
     * Set Post Content.
     *
     * @param view      View to load content into.
     * @param content   Content to be loaded.
     */
    private void setPostContent(WebView view, String content) {
        if(isCommitteeMode) {
            view.loadUrl("javascript:showCommittee('" + content + "')");
        } else {
            view.loadUrl("javascript:showContent('" + content + "')");
        }

        view.setVisibility(View.VISIBLE);

        content = null;
    }

    /**
     * Checks if the page is loaded.
     *
     * @return  Flag to determine if page is loaded.
     */
    public boolean isPageLoaded() {
        return isFinished;
    }
}
