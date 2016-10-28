package np.edu.ku.kurc.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Embedded {

    @SerializedName("wp:featuredmedia")
    public List<FeaturedMedia> featured = new ArrayList<>();
}
