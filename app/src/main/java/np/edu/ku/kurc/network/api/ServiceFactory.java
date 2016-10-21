package np.edu.ku.kurc.network.api;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;

import java.util.Date;

import np.edu.ku.kurc.network.api.typeadpaters.DateTypeAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    /**
     * Retrofit instance.
     */
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ApiConstants.API_URL)
            .addConverterFactory(getConverterFactory());

    @NonNull
    private static GsonConverterFactory getConverterFactory() {
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Date.class, new DateTypeAdapter());

        return GsonConverterFactory.create(builder.create());
    }

    /**
     * Creates a service instance.
     *
     * @param serviceClass  Service class to be instantiated.
     * @param <T>           Type of Service class.
     * @return              Service class instance.
     */
    public static <T> T makeService(Class<T> serviceClass) {
        return builder.build().create(serviceClass);
    }
}
