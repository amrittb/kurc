package np.edu.ku.kurc.network.api.services;

import np.edu.ku.kurc.models.collection.CategoryCollection;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {

    @GET("categories?exclude=1&parent=0")
    Call<CategoryCollection> getCategories();
}