package np.edu.ku.kurc.models.transformers;

import android.content.ContentValues;
import android.database.Cursor;

import np.edu.ku.kurc.database.schema.MediaSchema;
import np.edu.ku.kurc.models.FeaturedMedia;

public class MediaTransformer extends BaseTransformer<FeaturedMedia>{

    @Override
    public ContentValues toContentValues(FeaturedMedia model) {
        transformed.put(MediaSchema.COLUMN_ID,model.id);
        transformed.put(MediaSchema.COLUMN_TITLE,model.title);

        return transformed;
    }

    @Override
    public FeaturedMedia toModel(Cursor c) {
        FeaturedMedia media = new FeaturedMedia();

        toModel(c,media);

        return media;
    }

    @Override
    public void toModel(Cursor c, FeaturedMedia model) {
        model.id = c.getInt(c.getColumnIndexOrThrow(MediaSchema.COLUMN_ID));
        model.title = c.getString(c.getColumnIndexOrThrow(MediaSchema.COLUMN_TITLE));
    }
}
