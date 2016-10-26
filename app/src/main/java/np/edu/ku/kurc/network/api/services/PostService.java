package np.edu.ku.kurc.network.api.services;

import java.util.List;

import np.edu.ku.kurc.models.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostService {

    @GET("posts")
    Call<List<Post>> getPostsForCategory(@Query("category_name") String categorySlug);

    @GET("posts?per_page=5")
    Call<List<Post>> getTopStories();

    @GET("posts?per_page=1&category_name=pinned&include_content")
    Call<List<Post>> getPinnedPost();

    @GET("posts/{id}")
    Call<Post> getPost(@Path("id") int postId);
}
