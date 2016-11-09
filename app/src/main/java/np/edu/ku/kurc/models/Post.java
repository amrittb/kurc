package np.edu.ku.kurc.models;

import android.content.Context;
import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import np.edu.ku.kurc.database.schema.PostSchema;
import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.collection.PostCollection;
import np.edu.ku.kurc.models.transformers.PostTransformer;
import np.edu.ku.kurc.models.transformers.contracts.ModelTransformerContract;

public class Post extends BaseModel<Post,PostSchema> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

    public int id;
    public String title;
    public Date date;
    public Date modified;

    public String slug;
    public String link;

    public String content;
    public String excerpt;

    @SerializedName("sticky")
    public boolean isSticky;

    @SerializedName("_embedded")
    public Embedded embedded;

    @SerializedName("author")
    public int authorId;

    @SerializedName("featured_media")
    public int featuredMediaId;

    @SerializedName("categories")
    public List<Integer> categoryIds;

    private static PostSchema schema;
    private static PostTransformer transformer;

    public Post(int id,String title, Date date, Date modified, String slug, String link, String content, String excerpt) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.modified = modified;
        this.slug = slug;
        this.link = link;
        this.content = content;
        this.excerpt = excerpt;
    }

    public String getDateString(Context context) {
        return (String) DateUtils.getRelativeDateTimeString(context, date.getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
    }

    public String getModifiedDateString(Context context) {
        return (String) DateUtils.getRelativeDateTimeString(context, modified.getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);

    }

    /**
     * Checks if the post has featured media.
     *
     * @return Flag to check if featured media exists.
     */
    public boolean hasFeaturedMedia() {
        return embedded != null && embedded.featured != null && (! embedded.featured.isEmpty());
    }

    /**
     * Returns featured media.
     *
     * @return Featured media instance.
     */
    public FeaturedMedia getFeaturedMedia() {
        return embedded.featured.get(0);
    }

    @Override
    public PostSchema getSchema() {
        if(schema == null) {
            schema = new PostSchema();
        }

        return schema;
    }

    @Override
    public BaseCollection<Post> getCollection(List<Post> list) {
        return new PostCollection(list);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public ModelTransformerContract<Post, PostSchema> getTransformer() {
        if(transformer == null) {
            transformer = new PostTransformer();
        }

        return transformer;
    }
}
