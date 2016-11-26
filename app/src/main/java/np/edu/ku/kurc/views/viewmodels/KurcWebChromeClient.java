package np.edu.ku.kurc.views.viewmodels;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

import np.edu.ku.kurc.common.Const;

public class KurcWebChromeClient extends WebChromeClient {

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.d(Const.TAG,"Console Message: " + consoleMessage.message());
        return false;
    }
}
