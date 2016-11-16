package np.edu.ku.kurc.models.transformers;

import android.content.ContentValues;
import android.database.Cursor;

import np.edu.ku.kurc.database.schema.MediaSizeSchema;
import np.edu.ku.kurc.models.MediaSize;

public class MediaSizeTransformer extends BaseTransformer<MediaSize> {

    @Override
    public ContentValues toContentValues(MediaSize model) {
        transformed.put(MediaSizeSchema.COLUMN_FILE,model.file);
        transformed.put(MediaSizeSchema.COLUMN_WIDTH,model.width);
        transformed.put(MediaSizeSchema.COLUMN_HEIGHT,model.height);
        transformed.put(MediaSizeSchema.COLUMN_MIME_TYPE,model.mimeType);
        transformed.put(MediaSizeSchema.COLUMN_SOURCE_URL,model.sourceUrl);
        transformed.put(MediaSizeSchema.COLUMN_SIZE_NAME,model.sizeName);
        transformed.put(MediaSizeSchema.COLUMN_MEDIA_ID,model.mediaId);

        return transformed;
    }

    @Override
    public MediaSize toModel(Cursor c) {
        MediaSize model = new MediaSize();

        toModel(c,model);

        return model;
    }

    @Override
    public void toModel(Cursor c, MediaSize model) {
        model.id = c.getInt(c.getColumnIndexOrThrow(MediaSizeSchema.COLUMN_ID));
        model.file = c.getString(c.getColumnIndexOrThrow(MediaSizeSchema.COLUMN_FILE));
        model.width = c.getInt(c.getColumnIndexOrThrow(MediaSizeSchema.COLUMN_WIDTH));
        model.height = c.getInt(c.getColumnIndexOrThrow(MediaSizeSchema.COLUMN_HEIGHT));
        model.mimeType = c.getString(c.getColumnIndexOrThrow(MediaSizeSchema.COLUMN_MIME_TYPE));
        model.sourceUrl = c.getString(c.getColumnIndexOrThrow(MediaSizeSchema.COLUMN_SOURCE_URL));
        model.sizeName = c.getString(c.getColumnIndexOrThrow(MediaSizeSchema.COLUMN_SIZE_NAME));
        model.mediaId = c.getInt(c.getColumnIndexOrThrow(MediaSizeSchema.COLUMN_MEDIA_ID));
    }
}
