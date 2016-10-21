package np.edu.ku.kurc.network.api.services;

import java.util.List;

import np.edu.ku.kurc.models.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PostService {

    @GET("posts")
    Call<List<Post>> getPostsForCategory(@Query("category_slug") String categorySlug);
}
