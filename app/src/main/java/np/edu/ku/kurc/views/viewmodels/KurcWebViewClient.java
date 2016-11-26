package np.edu.ku.kurc.views.viewmodels;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class KurcWebViewClient extends WebViewClient {

    private boolean isFinished;
    private String content;

    @Override
    public void onPageFinished(WebView view, String url) {
        isFinished = true;

        if(content != null) {
            setPostContent(view, content);
            view.setVisibility(View.VISIBLE);
            content = null;
        }
    }

    public void setContentWhenPageLoaded(String content) {
        this.content = content;
    }

    private void setPostContent(WebView view, String content) {
        view.loadUrl("javascript:showContent('" + content + "')");
    }

    public boolean isPageLoaded() {
        return isFinished;
    }
}
