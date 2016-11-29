package np.edu.ku.kurc.committee.data;

import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.network.api.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommitteeRepository implements CommitteeDataSource {

    @Override
    public void loadCommittee(final LoadCommitteeCallback callback) {
        Call<Post> committeePage = ServiceFactory.makeService(CommitteeService.class).getCommitteePage();

        committeePage.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                callback.onLoaded(response.body());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                callback.onLoadError();
            }
        });
    }
}
