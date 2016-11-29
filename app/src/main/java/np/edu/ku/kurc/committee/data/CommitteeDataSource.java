package np.edu.ku.kurc.committee.data;

import np.edu.ku.kurc.models.Post;

public interface CommitteeDataSource {

    void loadCommittee(LoadCommitteeCallback callback);

    interface LoadCommitteeCallback {
        void onLoaded(Post committee);

        void onLoadError();
    }
}
