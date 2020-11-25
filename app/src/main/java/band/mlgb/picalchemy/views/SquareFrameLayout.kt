package band.mlgb.picalchemy.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Used to make ItemView in a RecyclerView.GridLayoutManager a square.
 * GridLayoutManager will create View.MeasureSpec.UNSPECIFICED mode for width if orientation is
 * horizontal or height if orientation is horizontal. Use the none 0 spec to the other.
 */
class SquareFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (widthMeasureSpec == 0) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        }
    }
}