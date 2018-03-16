package br.com.caramelo.neonteste.ui

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View
import br.com.caramelo.neonteste.R
import br.com.caramelo.neonteste.component
import br.com.caramelo.neonteste.data.model.Me
import br.com.caramelo.neonteste.data.repository.NeonRepository
import kotlinx.android.synthetic.main.neon_loading.*
import kotlinx.android.synthetic.wear.activity_main.*
import javax.inject.Inject

class MainActivity : WearableActivity() {

    @Inject
    lateinit var neonRepository: NeonRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        component.inject(this)

        // Enables Always-on
        setAmbientEnabled()

        showLoading()
        neonRepository.me().observer {
            it?.let {
                bindMe(it)
            }
        }
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun bindMe(me: Me) {
        meImageView.load(me, false)
        loading.visibility = View.INVISIBLE
    }
}
