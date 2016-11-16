package np.edu.ku.kurc.models.transformers;

import android.content.ContentValues;
import android.database.Cursor;

import np.edu.ku.kurc.database.schema.CategorySchema;
import np.edu.ku.kurc.models.Category;
import np.edu.ku.kurc.models.transformers.contracts.ModelTransformerContract;

public class CategoryTransformer extends BaseTransformer<Category> {

    @Override
    public ContentValues toContentValues(Category model) {
        transformed.put(CategorySchema.COLUMN_ID,model.id);
        transformed.put(CategorySchema.COLUMN_NAME,model.name);
        transformed.put(CategorySchema.COLUMN_SLUG,model.slug);
        transformed.put(CategorySchema.COLUMN_LINK,model.link);

        return transformed;
    }

    @Override
    public Category toModel(Cursor c) {
        Category category = new Category();

        toModel(c,category);

        return category;
    }

    @Override
    public void toModel(Cursor c, Category model) {
        model.id = c.getInt(c.getColumnIndexOrThrow(CategorySchema.COLUMN_ID));
        model.name = c.getString(c.getColumnIndexOrThrow(CategorySchema.COLUMN_NAME));
        model.slug = c.getString(c.getColumnIndexOrThrow(CategorySchema.COLUMN_SLUG));
        model.link = c.getString(c.getColumnIndexOrThrow(CategorySchema.COLUMN_LINK));
    }
}