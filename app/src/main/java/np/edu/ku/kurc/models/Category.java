package np.edu.ku.kurc.models;

import np.edu.ku.kurc.database.schema.BaseSchema;
import np.edu.ku.kurc.database.schema.CategorySchema;
import np.edu.ku.kurc.models.transformers.CategoryTransformer;
import np.edu.ku.kurc.models.transformers.ModelTransformerContract;

public class Category extends BaseModel{

    public int id;
    public String name;
    public String slug;
    public String link;

    private static CategorySchema schema;

    private static CategoryTransformer transformer;

    @Override
    public BaseSchema getSchema() {
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
}
