package br.com.caramelo.neonteste.ui.history

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.view.doOnPreDraw
import br.com.caramelo.neonteste.R
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.ext.toCurrency
import br.com.caramelo.neonteste.ui.base.NeonImageView

/**
 * Created by lucascaramelo on 14/03/2018.
 */
class HistoryGraphAdapter : RecyclerView.Adapter<HistoryGraphAdapter.SendMoneyHolder>() {

    var data: List<Contact>? = null
        set(value) {
            val comparable = Comparator<Contact>({a, b -> b.transfer.compareTo(a.transfer)})
            field = value?.toMutableList()?.sortedWith(comparable)
            dataGraph.clear()
            field?.forEach {
                dataGraph.add(GraphItem(it))
            }
            notifyDataSetChanged()
        }
        get() {
            if (field == null) {
                field = listOf()
                dataGraph.clear()
            }
            return field
        }

    private val dataGraph = mutableListOf<GraphItem>()
    private val maxTransfer: Float
        get() = data?.maxBy { it.transfer }?.transfer ?: 0f

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SendMoneyHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_graph_history, parent, false)
        return SendMoneyHolder(view)
    }

    override fun onBindViewHolder(holder: SendMoneyHolder, position: Int) {
        val item = dataGraph[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = dataGraph.size

    inner class GraphItem(val item: Contact) {
        var percent: Float = 1f
            get() {
                field = ((item.transfer * 50) / maxTransfer)
                if (field < 1f)
                    field = 1f
                return field
            }
    }

    inner class SendMoneyHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var contactImageView: NeonImageView = itemView.findViewById(R.id.contactImageView)
        private var transferTextView: TextView = itemView.findViewById(R.id.transferTextView)
        private var line: View = itemView.findViewById(R.id.graphLine)
        private var containerLine: LinearLayout = itemView.findViewById(R.id.containerLine)

        fun bind(graphItem: GraphItem) {
            val contact = graphItem.item
            contactImageView.load(contact)
            contactImageView.setTextSize(16f)
            transferTextView.text = contact.transfer.toCurrency()
                    .replace("[^0-9.,]".toRegex(), "")

            containerLine.doOnPreDraw {
                var height = 0f
                if (contact.transfer > 0f) {
                    height = itemView.height * graphItem.percent / 100
                }
                line.layoutParams.height = height.toInt()
                line.requestLayout()
            }
        }
    }
}