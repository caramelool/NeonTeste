package br.com.caramelo.neonteste.ext

import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

/**
 * Created by lucascaramelo on 13/03/2018.
 */
fun ImageView.load(url: String): RequestCreator {
    val request = Picasso.get().load(url)
    request.into(this)
    return request
}