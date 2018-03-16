package br.com.caramelo.neonteste.ui.contactlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import br.com.caramelo.neonteste.R
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.ext.toCurrency
import br.com.caramelo.neonteste.uibase.NeonImageView

/**
 * Created by lucascaramelo on 13/03/2018.
 */
class ContactListAdapter(val showTransfer: Boolean = false): RecyclerView.Adapter<ContactListAdapter.SendMoneyHolder>() {

    var data: List<Contact>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
        get() {
            if (field == null) {
                field = listOf()
            }
            return field
        }

    private var onContactClick: (Contact) -> Unit = {}

    fun setOnContactClickListener(l: (Contact) -> Unit) {
        onContactClick = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendMoneyHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_contact_list, parent, false)
        return SendMoneyHolder(view)
    }

    override fun onBindViewHolder(holder: SendMoneyHolder, position: Int) {
        val contact = data!![position]
        holder.bind(contact)
        holder.itemView.setOnClickListener {
            onContactClick(contact)
        }
    }

    override fun getItemCount(): Int = data!!.size

    inner class SendMoneyHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var contactImageView: NeonImageView = itemView.findViewById(R.id.contactImageView)
        private var nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private var phoneTextView: TextView = itemView.findViewById(R.id.phoneTextView)
        private var transferTextView: TextView = itemView.findViewById(R.id.transferTextView)

        fun bind(contact: Contact) {
            contactImageView.load(contact)
            nameTextView.text = contact.name
            phoneTextView.text = contact.phone
            transferTextView.text = contact.transfer.toCurrency()

            transferTextView.visibility = if (showTransfer) View.VISIBLE else View.GONE
        }
    }
}