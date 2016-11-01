package np.edu.ku.kurc.views.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HDImageView extends ImageView {
    public HDImageView(Context context) {
        super(context);
    }

    public HDImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(),(int) (9.0/16.0 * getMeasuredWidth()));
    }
}
