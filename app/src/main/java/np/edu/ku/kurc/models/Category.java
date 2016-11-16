package np.edu.ku.kurc.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.database.schema.CategorySchema;
import np.edu.ku.kurc.database.schema.SchemaFactory;
import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.collection.CategoryCollection;
import np.edu.ku.kurc.models.transformers.BaseTransformer;
import np.edu.ku.kurc.models.transformers.CategoryTransformer;
import np.edu.ku.kurc.models.transformers.TransformerFactory;

public class Category extends BaseModel<Category,CategorySchema> {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("slug")
    public String slug;

    @SerializedName("link")
    public String link;

    @Override
    public CategorySchema getSchema() {
        return SchemaFactory.getInstance(CategorySchema.class);
    }

    @Override
    public BaseTransformer<Category> getTransformer() {
        return TransformerFactory.getInstance(CategoryTransformer.class);
    }

    @Override
    public BaseCollection<Category> getCollection(List<Category> list) {
        return new CategoryCollection(list);
    }

    @Override
    public int getId() {
        return id;
    }

    public int getMenuIcon() {
        switch(slug) {
            case "events":
                return R.drawable.ic_event_black_24dp;
            case "notice":
                return R.drawable.ic_info_black_24dp;
            case "projects":
                return R.drawable.ic_event_note_black_24dp;
            default:
                // @TODO change the drawable to generic post icon.
                return R.drawable.ic_info_black_24dp;
        }
    }
}
