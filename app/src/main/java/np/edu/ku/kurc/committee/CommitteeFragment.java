package np.edu.ku.kurc.committee;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import np.edu.ku.kurc.BuildConfig;
import np.edu.ku.kurc.pages.PageFragment;

public class CommitteeFragment extends PageFragment {

    /**
     * CommitteeFragment Instance.
     *
     * @return  CommitteeFragment Instance.
     */
    public static CommitteeFragment instance() {
        CommitteeFragment fragment = new CommitteeFragment();

        fragment.setPageId(BuildConfig.KURC_COMMITTEE_PAGE_ID);

        return fragment;
    }

    public CommitteeFragment() {}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postViewModel.setCommitteeMode();
    }
}
