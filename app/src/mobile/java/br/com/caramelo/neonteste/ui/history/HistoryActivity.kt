package br.com.caramelo.neonteste.ui.history

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import br.com.caramelo.neonteste.R
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.data.NeonRepositoryFactory
import br.com.caramelo.neonteste.uibase.NeonItemDecoration
import br.com.caramelo.neonteste.ui.contactlist.ContactListAdapter

import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.neon_loading.*
import kotlinx.android.synthetic.mobile.content_send_history.*

class HistoryActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, NeonRepositoryFactory())
                .get(HistoryViewModel::class.java)
    }
    private val graphAdapter by lazy { HistoryGraphAdapter() }
    private val adapter by lazy { ContactListAdapter(true) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_history)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        graphRecyclerView.setHasFixedSize(true)
        graphRecyclerView.adapter = graphAdapter

        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(NeonItemDecoration(this))
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadingLiveData.observe(this, loadingObserver)
        viewModel.listLiveData?.observe(this, listObserver)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private val loadingObserver = Observer<Boolean> {
        TransitionManager.beginDelayedTransition(constraintLayout)
        if (it == true) {
            loading.visibility = View.VISIBLE
            graphRecyclerView.visibility = View.GONE
            recyclerView.visibility = View.GONE
        } else {
            loading.visibility = View.GONE
            graphRecyclerView.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private val listObserver = Observer<List<Contact>> { data ->
        adapter.data = data
        graphAdapter.data = data
    }

}
