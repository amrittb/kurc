package np.edu.ku.kurc.views.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class PreCachingLinearLayoutManager extends LinearLayoutManager{

    private static final int DEFAULT_EXTRA_LAYOUT_SPACE = 1200;
    private int extraLayoutSpace = -1;

    public PreCachingLinearLayoutManager(Context context) {
        super(context);
    }

    public PreCachingLinearLayoutManager(Context context, int extraLayoutSpace) {
        super(context);
        this.extraLayoutSpace = extraLayoutSpace;
    }

    public PreCachingLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public PreCachingLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setExtraLayoutSpace(int extraLayoutSpace) {
        this.extraLayoutSpace = extraLayoutSpace;
    }

    /**
     * Returns extra layout space.
     *
     * @param state RecyclerView State
     * @return      Extra layout space.
     */
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        if(extraLayoutSpace > 0) {
            return extraLayoutSpace;
        }

        return DEFAULT_EXTRA_LAYOUT_SPACE;
    }
}
