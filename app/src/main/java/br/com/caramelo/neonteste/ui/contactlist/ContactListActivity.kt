package br.com.caramelo.neonteste.ui.contactlist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import br.com.caramelo.neonteste.R
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.data.repository.NeonRepositoryFactory
import br.com.caramelo.neonteste.ui.base.NeonItemDecoration
import br.com.caramelo.neonteste.ui.sendmoney.SendMoneyFragment
import kotlinx.android.synthetic.main.appbar.*
import kotlinx.android.synthetic.main.content_contact_list.*
import kotlinx.android.synthetic.main.neon_loading.*

class ContactListActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, NeonRepositoryFactory())
                .get(ContactListViewModel::class.java)
    }
    private val adapter by lazy { ContactListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(NeonItemDecoration(this))
        recyclerView.adapter = adapter

        adapter.setOnContactClickListener { contact ->
            SendMoneyFragment.newInstance(contact)
                    .show(supportFragmentManager, "")
        }
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
            recyclerView.visibility = View.GONE
        } else {
            loading.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private val listObserver = Observer<List<Contact>> { data ->
        adapter.data = data
    }

}
