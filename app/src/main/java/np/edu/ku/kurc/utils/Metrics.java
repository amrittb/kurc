package np.edu.ku.kurc.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public final class Metrics {

    /**
     * Converts Dp to Pixels
     * @param context   Application context.
     * @param dipValue  Dip Value to be converted.
     * @return          Dip value converted to pixels.
     */
    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
