package np.edu.ku.kurc.network.api.services;

import java.util.List;

import np.edu.ku.kurc.models.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostService {

    @GET("posts?_embed")
    Call<List<Post>> getPostsForCategory(@Query("filter[category_name]") String categorySlug);

    @GET("posts?per_page=5&_embed")
    Call<List<Post>> getTopStories();

    @GET("posts?per_page=1&sticky&include_content&_embed")
    Call<List<Post>> getPinnedPost();

    @GET("posts/{id}?_embed")
    Call<Post> getPost(@Path("id") int postId); // @TODO no _embed needed. Remove after saving posts to DB
}
