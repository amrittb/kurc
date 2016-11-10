package np.edu.ku.kurc.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Embedded {

    @SerializedName("wp:featuredmedia")
    public List<FeaturedMedia> featured = new ArrayList<>();

    @SerializedName("author")
    public List<Author> authors = new ArrayList<>();

    @SerializedName("wp:term")
    public Category[][] terms;  // @TODO Use generic Term model instead of Category as term type.
}
