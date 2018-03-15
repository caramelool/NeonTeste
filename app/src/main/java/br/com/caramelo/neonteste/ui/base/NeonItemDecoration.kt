package br.com.caramelo.neonteste.ui.base

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.util.TypedValue


/**
 * Created by lucascaramelo on 14/03/2018.
 */
class NeonItemDecoration(
        context: Context,
        private val divider: Drawable = ColorDrawable(0x4d00a7a8)
): RecyclerView.ItemDecoration() {

    private val mHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            1f, context.resources.displayMetrics).toInt()
    private val mPaddingLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            20f, context.resources.displayMetrics).toInt()
    private val mPaddingRight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            20f, context.resources.displayMetrics).toInt()

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft + mPaddingLeft
        val right = parent.width - parent.paddingRight - mPaddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin
            val bottom = top + mHeight

            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

}