package br.com.caramelo.neonteste.ui.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import br.com.caramelo.neonteste.R
import br.com.caramelo.neonteste.data.model.Me
import br.com.caramelo.neonteste.data.NeonRepositoryFactory
import br.com.caramelo.neonteste.ui.contactlist.ContactListActivity
import br.com.caramelo.neonteste.ui.history.HistoryActivity
import kotlinx.android.synthetic.main.neon_loading.*
import kotlinx.android.synthetic.mobile.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, NeonRepositoryFactory())
                .get(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sendMoneyButton.setOnClickListener {
            val intent = Intent(this, ContactListActivity::class.java)
            startActivity(intent)
        }

        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.errorLiveData.observe(this, errorObserver)
        viewModel.loadingLiveData.observe(this, loadingObserver)
        viewModel.meLiveData?.observe(this, meObserver)
    }

    private val loadingObserver = Observer<Boolean> {
        TransitionManager.beginDelayedTransition(constraintLayout)
        retryContainer.visibility = View.GONE
        if (it == true) {
            loading.visibility = View.VISIBLE
            sendMoneyButton.visibility = View.GONE
            historyButton.visibility = View.GONE
            meImageView.visibility = View.GONE
            nameTextView.visibility = View.GONE
            emailTextView.visibility = View.GONE
        } else {
            loading.visibility = View.GONE
            sendMoneyButton.visibility = View.VISIBLE
            historyButton.visibility = View.VISIBLE
            meImageView.visibility = View.VISIBLE
            nameTextView.visibility = View.VISIBLE
            emailTextView.visibility = View.VISIBLE
        }
    }

    private val errorObserver = Observer<Throwable> {
        TransitionManager.beginDelayedTransition(constraintLayout)
        loading.visibility = View.GONE
        retryContainer.visibility = View.VISIBLE
        retryButton.setOnClickListener {
            viewModel.requestMe()
        }
    }

    private val meObserver = Observer<Me> {
        it?.let { me ->
            nameTextView.text = me.name
            emailTextView.text = me.email
            meImageView.load(me)
        }
    }
}
