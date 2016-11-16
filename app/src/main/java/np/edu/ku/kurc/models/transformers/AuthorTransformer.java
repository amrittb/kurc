package np.edu.ku.kurc.models.transformers;

import android.content.ContentValues;
import android.database.Cursor;

import np.edu.ku.kurc.database.schema.AuthorSchema;
import np.edu.ku.kurc.models.Author;

public class AuthorTransformer extends BaseTransformer<Author> {

    @Override
    public ContentValues toContentValues(Author model) {
        transformed.put(AuthorSchema.COLUMN_ID,model.id);
        transformed.put(AuthorSchema.COLUMN_NAME,model.name);
        transformed.put(AuthorSchema.COLUMN_SLUG,model.slug);
        transformed.put(AuthorSchema.COLUMN_LINK,model.link);
        transformed.put(AuthorSchema.COLUMN_AVATAR,model.avatar);

        return transformed;
    }

    @Override
    public Author toModel(Cursor c) {
        Author a = new Author();

        toModel(c,a);

        return a;
    }

    @Override
    public void toModel(Cursor c, Author model) {
        model.id = c.getInt(c.getColumnIndexOrThrow(AuthorSchema.COLUMN_ID));
        model.name = c.getString(c.getColumnIndexOrThrow(AuthorSchema.COLUMN_NAME));
        model.slug = c.getString(c.getColumnIndexOrThrow(AuthorSchema.COLUMN_SLUG));
        model.link = c.getString(c.getColumnIndexOrThrow(AuthorSchema.COLUMN_LINK));
        model.avatar = c.getString(c.getColumnIndexOrThrow(AuthorSchema.COLUMN_AVATAR));
    }
}
