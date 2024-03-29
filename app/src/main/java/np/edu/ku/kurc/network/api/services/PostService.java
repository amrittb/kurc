package np.edu.ku.kurc.network.api.services;

import java.util.List;

import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.models.collection.PostCollection;
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
    Call<PostCollection> getStickyPost();

    @GET("posts/{id}?_embed")
    Call<Post> getPost(@Path("id") int postId); // @TODO no _embed needed. Remove after saving posts to DB

    @GET("pages/{id}?_embed")
    Call<Post> getPage(@Path("id") int pageId);

    @GET("posts?_embed")
    Call<PostCollection> getPosts(@Query("per_page") int perPage);

    @GET("posts?_embed")
    Call<PostCollection> getPostsAfter(@Query("after") String after, @Query("per_page") int perPage);

    @GET("posts?_embed")
    Call<PostCollection> getPostsBefore(@Query("before") String before, @Query("per_page") int perPage);
}
