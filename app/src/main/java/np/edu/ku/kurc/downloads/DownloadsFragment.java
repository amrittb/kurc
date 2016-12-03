package np.edu.ku.kurc.downloads;

import np.edu.ku.kurc.BuildConfig;
import np.edu.ku.kurc.pages.PageFragment;

public class DownloadsFragment extends PageFragment {

    /**
     * Creates downloads fragment instance.
     *
     * @return  DownloadsFragment Instance.
     */
    public static DownloadsFragment instance() {
        DownloadsFragment fragment = new DownloadsFragment();

        fragment.setPageId(BuildConfig.KURC_DOWNLOADS_PAGE_ID);

        return fragment;
    }
}
