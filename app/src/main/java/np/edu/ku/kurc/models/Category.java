package np.edu.ku.kurc.models;

import java.util.List;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.database.schema.CategorySchema;
import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.collection.CategoryCollection;
import np.edu.ku.kurc.models.transformers.CategoryTransformer;
import np.edu.ku.kurc.models.transformers.ModelTransformerContract;

public class Category extends BaseModel<Category,CategorySchema> {

    public int id;
    public String name;
    public String slug;
    public String link;

    private static CategorySchema schema;

    private static CategoryTransformer transformer;

    @Override
    public CategorySchema getSchema() {
        if(schema == null) {
            schema = new CategorySchema();
        }

        return schema;
    }

    @Override
    public ModelTransformerContract<Category,CategorySchema> getTransformer() {
        if(transformer == null) {
            transformer = new CategoryTransformer();
        }

        return transformer;
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
