package br.com.caramelo.neonteste.ui.sendmoney


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.caramelo.neonteste.R
import br.com.caramelo.neonteste.data.model.Contact
import br.com.caramelo.neonteste.ext.hideKeyboard
import br.com.caramelo.neonteste.ext.toCurrency
import kotlinx.android.synthetic.main.fragment_send_money.*
import kotlinx.android.synthetic.main.neon_loading.*


/**
 * A simple [Fragment] subclass.
 */
class SendMoneyFragment : DialogFragment() {

    companion object {

        private const val EXTRA_CONTACT = "extra_contact"

        fun newInstance(contact: Contact): SendMoneyFragment {
            val fragment = SendMoneyFragment()
            fragment.arguments = Bundle()
            fragment.arguments!!.putSerializable(EXTRA_CONTACT, contact)
            return fragment
        }
    }

    private val viewModel by lazy {
        val contanct = arguments?.getSerializable(EXTRA_CONTACT) as Contact
        val factory = SendMoneyViewModelFactory(contanct)
        ViewModelProviders.of(this, factory)
                .get(SendMoneyViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_money, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeButton.setOnClickListener {
            if (isCancelable) {
                dismiss()
            }
        }

        moneyEditText.addTextChangedListener(MoneyTextWatcher())
        moneyEditText.setText("0")
        moneyEditText.setOnTouchListener { _, _ ->
            moneyEditText.setSelection(moneyEditText.text.length)
            false
        }

        sendButton.setOnClickListener {
            viewModel.sendMoney()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        configDialog()

        viewModel.contactLiveData?.observe(this, contactObserver)
        viewModel.progressLiveData.observe(this, progressObserver)
    }

    private fun configDialog() {
        dialog.window.attributes.windowAnimations = R.style.NeonDialogAnimation
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme)
    }

    private val contactObserver = Observer<Contact> {
        it?.let { contact ->
            contactImageView.load(contact)
            nameTextView.text = contact.name
            phoneTextView.text = contact.phone
        }
    }

    private val progressObserver = Observer<SendMoneyProgress> { progress ->
        TransitionManager.beginDelayedTransition(containerButton)
        when (progress) {
            SendMoneyProgress.Initial -> {
                isCancelable = true
                sendButton.setBackgroundResource(R.drawable.neon_button)
                sendButton.setText(R.string.send)
                loading.visibility = View.INVISIBLE
                checkAnimation.visibility = View.INVISIBLE
                sendButton.visibility = View.VISIBLE
            }
            SendMoneyProgress.Sending -> {
                isCancelable = false
                moneyEditText.hideKeyboard()
                loading.visibility = View.VISIBLE
                checkAnimation.visibility = View.INVISIBLE
                sendButton.visibility = View.INVISIBLE
            }
            SendMoneyProgress.Error -> bindError()
            SendMoneyProgress.Success -> bindSuccess()
        }
    }

    private fun bindError() {
        checkAnimation.visibility = View.VISIBLE
        loading.visibility = View.INVISIBLE
        checkAnimation.setAnimation(R.raw.sendmoney_fail)
        checkAnimation.playAnimation()
        view?.postDelayed({
            viewModel.progressLiveData.postValue(SendMoneyProgress.Initial)
        }, 3000)
    }

    private fun bindSuccess() {
        checkAnimation.visibility = View.VISIBLE
        loading.visibility = View.INVISIBLE
        checkAnimation.setAnimation(R.raw.sendmoney_success)
        checkAnimation.playAnimation()
        view?.postDelayed({
            dismiss()
        }, 3000)
    }

    inner class MoneyTextWatcher: TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {
            var money = s.replace("[^0-9]".toRegex(), "")
                    .toFloat()
            money /= 100.0f
            viewModel.money = money
            val currency = money.toCurrency()
            moneyEditText.removeTextChangedListener(this)
            moneyEditText.setText(currency)
            moneyEditText.setSelection(moneyEditText.text.length)
            moneyEditText.addTextChangedListener(this)
        }
    }

}// Required empty public constructor
