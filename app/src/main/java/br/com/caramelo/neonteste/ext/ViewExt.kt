package br.com.caramelo.neonteste.ext

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by lucascaramelo on 14/03/2018.
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}