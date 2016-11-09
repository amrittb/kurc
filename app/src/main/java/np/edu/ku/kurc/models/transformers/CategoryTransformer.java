package np.edu.ku.kurc.models.transformers;

import android.content.ContentValues;
import android.database.Cursor;

import np.edu.ku.kurc.database.schema.CategorySchema;
import np.edu.ku.kurc.models.Category;

public class CategoryTransformer implements ModelTransformerContract<Category,CategorySchema> {

    private ContentValues transformed;

    public CategoryTransformer() {
        transformed = new ContentValues();
    }

    @Override
    public ContentValues toContentValues(Category model, CategorySchema schema) {
        transformed.put(schema.COLUMN_ID,model.id);
        transformed.put(schema.COLUMN_NAME,model.name);
        transformed.put(schema.COLUMN_SLUG,model.slug);
        transformed.put(schema.COLUMN_LINK,model.link);

        return transformed;
    }

    @Override
    public Category toModel(Cursor c) {
        Category category = new Category();

        CategorySchema schema = (CategorySchema) category.getSchema();

        category.id = c.getInt(c.getColumnIndexOrThrow(schema.COLUMN_ID));
        category.name = c.getString(c.getColumnIndexOrThrow(schema.COLUMN_NAME));
        category.slug = c.getString(c.getColumnIndexOrThrow(schema.COLUMN_SLUG));
        category.link = c.getString(c.getColumnIndexOrThrow(schema.COLUMN_LINK));

        return category;
    }
}