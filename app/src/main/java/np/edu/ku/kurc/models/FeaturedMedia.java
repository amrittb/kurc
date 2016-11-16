package np.edu.ku.kurc.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import np.edu.ku.kurc.database.schema.MediaSchema;
import np.edu.ku.kurc.database.schema.SchemaFactory;
import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.collection.MediaCollection;
import np.edu.ku.kurc.models.collection.MediaSizeCollection;
import np.edu.ku.kurc.models.exception.DatabaseErrorException;
import np.edu.ku.kurc.models.transformers.BaseTransformer;
import np.edu.ku.kurc.models.transformers.MediaTransformer;
import np.edu.ku.kurc.models.transformers.TransformerFactory;

public class FeaturedMedia extends BaseModel<FeaturedMedia,MediaSchema> {

    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("sizes")
    public HashMap<String,MediaSize> sizes;

    public MediaSize getOptimalSize(int width, int height) {
        MediaSize optimal = null;

        for(MediaSize s: sizes.values()) {
            if(width < s.width) {
                if(optimal == null) {
                    optimal = s;
                } else {
                    if(optimal.width > s.width) {
                        optimal = s;
                    }
                }
            }
        }

        if(optimal == null) {
            optimal = sizes.get("full");
        }

        return optimal;
    }

    /**
     * Returns optimal sized source url.
     *
     * @param width     Width of image view.
     * @param height    Height of image view.
     * @return          URL of optimal sized image.
     */
    public String getOptimalSourceUrl(int width, int height) {
        return getOptimalSize(width,height).getUrl();
    }

    /**
     * Checks if this object has image data.
     *
     * @return  Flag to determine if the featured image has image data.
     */
    public boolean hasImageData() {
        return (sizes != null && ! sizes.isEmpty());
    }

    @Override
    public MediaSchema getSchema() {
        return SchemaFactory.getInstance(MediaSchema.class);
    }

    @Override
    public BaseCollection<FeaturedMedia> getCollection(List<FeaturedMedia> list) {
        return new MediaCollection(list);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public BaseTransformer<FeaturedMedia> getTransformer() {
        return TransformerFactory.getInstance(MediaTransformer.class);
    }

    public void attachSizes(MediaSizeCollection list) {
        sizes = new HashMap<>();

        for(MediaSize size: list) {
            if(size.mediaId == id) {
                sizes.put(size.sizeName,size);
            }
        }
    }

    /**
     * Returns size collection instead of hash map.
     *
     * @return  MediaSizeCollection
     */
    public MediaSizeCollection getSizeCollection() {
        if(sizes != null) {
            MediaSizeCollection list = new MediaSizeCollection();

            for(String sizeName: sizes.keySet()) {
                MediaSize size = sizes.get(sizeName);

                size.mediaId = id;
                size.sizeName = sizeName;
                list.add(size);
            }

            return list;
        }

        return null;
    }

    @Override
    public long save(SQLiteDatabase db) throws DatabaseErrorException {
        long savedId = super.save(db);
        // Saving sizes after saving media.
        MediaSizeCollection sizesList = getSizeCollection();

        if(sizesList != null) {
            sizesList.saveAllSync(db);
        }

        return savedId;
    }

    @Override
    public BaseCollection<FeaturedMedia> allIn(Context context, List<Integer> ids) {

        BaseCollection<FeaturedMedia> list = super.allIn(context, ids);

        List<Integer> mediaIds = new ArrayList<>();

        for(FeaturedMedia media: list) {
            if( ! mediaIds.contains(media.id)) {
                mediaIds.add(media.id);
            }
        }

        MediaSizeCollection sizes = (new MediaSize()).allForMedias(context,mediaIds);

        for(FeaturedMedia media: list) {
            media.attachSizes(sizes);
        }

        return list;
    }
}
