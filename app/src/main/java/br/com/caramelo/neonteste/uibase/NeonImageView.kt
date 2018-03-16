package br.com.caramelo.neonteste.uibase

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import br.com.caramelo.neonteste.R
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.data.model.Me
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.neon_image_view.view.*

/**
 * Created by lucascaramelo on 13/03/2018.
 */
class NeonImageView: FrameLayout {
    constructor(context: Context?) : super(context) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.neon_image_view, this)
    }

    fun load(contact: Contact, loadImage: Boolean = true) {
        roundImageView.setImageResource(R.drawable.neon_border_contact)
        bindName(contact.name)
        if (loadImage) {
            loadImage(contact.image)
        }
    }

    fun load(me: Me, loadImage: Boolean = true) {
        roundImageView.setImageResource(R.drawable.neon_border_me)
        bindName(me.name)
        if (loadImage) {
            loadImage(me.image)
        }
    }

    fun setTextSize(size: Float) {
        roundNameTextView.textSize = size
    }

    private fun bindName(name: String) {
        roundNameTextView.text = name
                .split(" ")
                .joinToString("") {
                    it.substring(0, 1)
                }
    }

    private fun loadImage(url: String?) {
        if (url.isNullOrEmpty()) {
            imageView.visibility = View.GONE
            return
        }
        imageView.visibility = View.VISIBLE
        Picasso.get().load(url)
                .transform(CircleTransform())
                .into(imageView)
    }

    private class CircleTransform: Transformation {
        override fun transform(source: Bitmap): Bitmap {
            val size = Math.min(source.width, source.height)

            val x = (source.width - size) / 2
            val y = (source.height - size) / 2

            val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
            if (squaredBitmap != source) {
                source.recycle()
            }

            val bitmap = Bitmap.createBitmap(size, size, source.config)

            val canvas = Canvas(bitmap)
            val paint = Paint()
            val shader = BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.shader = shader
            paint.isAntiAlias = true

            val r = size / 2f
            canvas.drawCircle(r, r, r, paint)

            squaredBitmap.recycle()
            return bitmap
        }

        override fun key(): String {
            return "circle"
        }
    }
}