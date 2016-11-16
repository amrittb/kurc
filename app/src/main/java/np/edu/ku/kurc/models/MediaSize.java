package np.edu.ku.kurc.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import np.edu.ku.kurc.BuildConfig;
import np.edu.ku.kurc.database.DatabaseHelper;
import np.edu.ku.kurc.database.schema.MediaSizeSchema;
import np.edu.ku.kurc.database.schema.SchemaFactory;
import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.collection.MediaSizeCollection;
import np.edu.ku.kurc.models.transformers.BaseTransformer;
import np.edu.ku.kurc.models.transformers.MediaSizeTransformer;
import np.edu.ku.kurc.models.transformers.TransformerFactory;
import np.edu.ku.kurc.utils.StringUtils;

public class MediaSize extends BaseModel<MediaSize,MediaSizeSchema> {

    @SerializedName("file")
    public String file;

    @SerializedName("width")
    public int width;

    @SerializedName("height")
    public int height;

    @SerializedName("mime_type")
    public String mimeType;

    @SerializedName("source_url")
    public String sourceUrl;

    public int id;
    public int mediaId;
    public String sizeName;

    /**
     * Returns Correct Size URL.
     *
     * @return  URL of media size.
     */
    public String getUrl() {
        if(BuildConfig.DEBUG) {
            return StringUtils.replaceLocalhost(sourceUrl);
        }

        return sourceUrl;
    }

    @Override
    public MediaSizeSchema getSchema() {
        return SchemaFactory.getInstance(MediaSizeSchema.class);
    }

    @Override
    public BaseCollection<MediaSize> getCollection(List<MediaSize> list) {
        return new MediaSizeCollection(list);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public BaseTransformer<MediaSize> getTransformer() {
        return TransformerFactory.getInstance(MediaSizeTransformer.class);
    }

    /**
     * Returns all media sizes for list of medias.
     *
     * @param context   Application Context.
     * @param ids       Media Ids.
     * @return          Media Size Collection.
     */
    public MediaSizeCollection allForMedias(Context context, List<Integer> ids) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        String sql = "SELECT * FROM " + getSchema().getTableName() +
                        " WHERE " + MediaSizeSchema.COLUMN_MEDIA_ID +
                        " IN (" + TextUtils.join(",", ids) + ")";

        Cursor cursor = db.rawQuery(sql,null);

        return (MediaSizeCollection) getCollection(cursor);
    }
}
