package np.edu.ku.kurc.committee.data;

import np.edu.ku.kurc.models.Post;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CommitteeService {

    @GET("pages/33?_embed")
    Call<Post> getCommitteePage();
}
