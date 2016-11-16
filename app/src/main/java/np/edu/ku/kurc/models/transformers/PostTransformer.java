package np.edu.ku.kurc.models.transformers;

import android.content.ContentValues;
import android.database.Cursor;

import np.edu.ku.kurc.database.schema.PostSchema;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.utils.DateUtils;

public class PostTransformer extends BaseTransformer<Post> {

    @Override
    public ContentValues toContentValues(Post model) {
        transformed.put(PostSchema.COLUMN_ID,model.id);
        transformed.put(PostSchema.COLUMN_TITLE,model.title);
        transformed.put(PostSchema.COLUMN_SLUG,model.slug);
        transformed.put(PostSchema.COLUMN_LINK,model.link);
        transformed.put(PostSchema.COLUMN_CONTENT,model.content);
        transformed.put(PostSchema.COLUMN_EXCERPT,model.excerpt);
        transformed.put(PostSchema.COLUMN_STICKY,model.isSticky);
        transformed.put(PostSchema.COLUMN_CREATED_AT,DateUtils.toString(model.date));
        transformed.put(PostSchema.COLUMN_UPDATED_AT,DateUtils.toString(model.modified));

        if(model.featuredMediaId != 0) {
            transformed.put(PostSchema.COLUMN_FEATURED_MEDIA_ID,model.featuredMediaId);
        }

        transformed.put(PostSchema.COLUMN_AUTHOR_ID,model.authorId);

        return transformed;
    }

    @Override
    public Post toModel(Cursor c) {
        Post p = new Post();

        toModel(c,p);

        return p;
    }

    public void toModel(Cursor c, Post p) {
        p.id = c.getInt(c.getColumnIndexOrThrow(PostSchema.COLUMN_ID));
        p.title = c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_TITLE));
        p.date = DateUtils.fromString(c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_CREATED_AT)));
        p.modified = DateUtils.fromString(c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_UPDATED_AT)));
        p.slug = c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_SLUG));
        p.link = c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_LINK));
        p.excerpt = c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_EXCERPT));
        p.content = c.getString(c.getColumnIndexOrThrow(PostSchema.COLUMN_CONTENT));
        p.isSticky = (c.getInt(c.getColumnIndexOrThrow(PostSchema.COLUMN_STICKY)) > 0);

        p.authorId = c.getInt(c.getColumnIndexOrThrow(PostSchema.COLUMN_AUTHOR_ID));
        p.featuredMediaId = c.getInt(c.getColumnIndexOrThrow(PostSchema.COLUMN_FEATURED_MEDIA_ID));
    }
}
