package np.edu.ku.kurc.aboutus;

import np.edu.ku.kurc.BuildConfig;
import np.edu.ku.kurc.pages.PageFragment;

public class AboutUsFragment extends PageFragment {

    /**
     * Returns About Page fragment instance.
     *
     * @return  AboutUsFragment instance.
     */
    public static AboutUsFragment instance() {
        AboutUsFragment fragment = new AboutUsFragment();

        fragment.setPageId(BuildConfig.KURC_ABOUT_US_PAGE_ID);

        return fragment;
    }
}
