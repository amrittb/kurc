package np.edu.ku.kurc.models.transformers;

import android.content.ContentValues;
import android.database.Cursor;

import np.edu.ku.kurc.database.schema.PostSchema;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.models.transformers.contracts.ModelTransformerContract;
import np.edu.ku.kurc.utils.DateUtils;

public class PostTransformer implements ModelTransformerContract<Post,PostSchema> {
    @Override
    public ContentValues toContentValues(Post model, PostSchema schema) {
        ContentValues values = new ContentValues();

        values.put(schema.COLUMN_ID,model.id);
        values.put(schema.COLUMN_TITLE,model.title);
        values.put(schema.COLUMN_SLUG,model.slug);
        values.put(schema.COLUMN_LINK,model.link);
        values.put(schema.COLUMN_CONTENT,model.content);
        values.put(schema.COLUMN_EXCERPT,model.excerpt);
        values.put(schema.COLUMN_CREATED_AT,DateUtils.toString(model.date));
        values.put(schema.COLUMN_UPDATED_AT,DateUtils.toString(model.modified));

        if(model.featuredMediaId != 0) {
            values.put(schema.COLUMN_FEATURED_MEDIA_ID,model.featuredMediaId);
        }

        values.put(schema.COLUMN_AUTHOR_ID,model.authorId);

        return values;
    }

    @Override
    public Post toModel(Cursor c) {
        Post p = new Post(
            c.getInt(c.getColumnIndexOrThrow(PostSchema.COLUMN_ID)),
            c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_TITLE)),
            DateUtils.fromString(c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_CREATED_AT))),
            DateUtils.fromString(c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_UPDATED_AT))),
            c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_SLUG)),
            c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_LINK)),
            c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_CONTENT)),
            c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_EXCERPT))
        );

        p.featuredMediaId = c.getInt(c.getColumnIndexOrThrow(PostSchema.COLUMN_FEATURED_MEDIA_ID));
        p.authorId = c.getInt(c.getColumnIndexOrThrow(PostSchema.COLUMN_AUTHOR_ID));

        return p;
    }
}
