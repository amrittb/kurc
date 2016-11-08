package np.edu.ku.kurc.models.transformers;

import android.content.ContentValues;

import np.edu.ku.kurc.database.schema.CategorySchema;
import np.edu.ku.kurc.models.Category;

public class CategoryTransformer implements ModelTransformerContract<Category,CategorySchema> {

    private ContentValues transformed;

    public CategoryTransformer() {
        transformed = new ContentValues();
    }

    @Override
    public ContentValues transform(Category model, CategorySchema schema) {
        transformed.put(schema.COLUMN_ID,model.id);
        transformed.put(schema.COLUMN_NAME,model.name);
        transformed.put(schema.COLUMN_SLUG,model.slug);
        transformed.put(schema.COLUMN_LINK,model.link);

        return transformed;
    }
}