package br.com.caramelo.neonteste.ext

import java.text.DecimalFormat
import java.util.*

/**
 * Created by lucascaramelo on 13/03/2018.
 */
fun Float.toCurrency(): String {
    val value = DecimalFormat.getCurrencyInstance(Locale("pt", "BR"))
            .format(this).replace("[^0-9.,]".toRegex(), "")
    return "R$ $value"
}