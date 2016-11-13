package np.edu.ku.kurc.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.HashMap;

import np.edu.ku.kurc.BuildConfig;
import np.edu.ku.kurc.utils.StringUtils;

public class FeaturedMedia {

    public int id;

    public Date date;

    @SerializedName("source_url")
    public String sourceUrl;

    public HashMap<String,Size> sizes;

    public Size getOptimalSize(int width, int height) {
        Size optimal = null;

        for(Size s: sizes.values()) {
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
        // For backward compatibility.
        String url = sourceUrl;

        if(sizes != null && (!sizes.isEmpty())) {
            Size s = getOptimalSize(width,height);

            if(s != null) {
                url = s.sourceUrl;
            }
        }

        if(BuildConfig.DEBUG) {
            return StringUtils.replaceLocalhost(url);
        }

        return url;
    }

    /**
     * Checks if this object has image data.
     *
     * @return  Flag to determine if the featured image has image data.
     */
    public boolean hasImageData() {
        return (sizes != null || sourceUrl != null);
    }

    public class Size {
        public int width;
        public int height;

        @SerializedName("source_url")
        public String sourceUrl;

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
    }
}
