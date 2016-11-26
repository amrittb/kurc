package np.edu.ku.kurc.developers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutDevelopersFragment extends DialogFragment {

    public AboutDevelopersFragment() {}

    /**
     * Creates new About Developers Dialog instance.
     *
     * @return New About Developers Dialog instance.
     */
    public static AboutDevelopersFragment instance() {
        return new AboutDevelopersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
