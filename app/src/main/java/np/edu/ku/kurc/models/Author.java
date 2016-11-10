package np.edu.ku.kurc.models;

import com.google.gson.annotations.SerializedName;

public class Author {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("slug")
    public String slug;

    @SerializedName("link")
    public String link;

    @SerializedName("avatar")
    public String avatar;
}
