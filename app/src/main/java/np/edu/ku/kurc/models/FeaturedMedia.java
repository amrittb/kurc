package np.edu.ku.kurc.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.HashMap;

import np.edu.ku.kurc.BuildConfig;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.utils.StringUtils;

public class FeaturedMedia {

    public int id;
    public Date date;

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
