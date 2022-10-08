package ru.svolf.convertx.ui.fragments.extended

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.svolf.convertx.R
import ru.svolf.convertx.algorhitms.Decoder
import ru.svolf.convertx.settings.Preferences
import ru.svolf.convertx.ui.Toasty

/**
 * Created by Snow Volf on 26.02.2017, 20:44
 */

class TimestampFragment : Fragment() {
    lateinit var Timestamp: EditText
    lateinit var normalDate: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timestamp, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timestamp = view.findViewById(R.id.Timestamp) as EditText
        Timestamp.hint = getString(R.string.hint_timestamp)
        Timestamp.isFocusable = true
        normalDate = view.findViewById(R.id.humanDate) as TextView
        normalDate.hint = "dd.MM.yyyy HH:mm:ss"

        Timestamp.textSize = Preferences.fontSize.toFloat()
        Timestamp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                if (!Timestamp.text.toString().isEmpty()) {
                    try {
                        val source = Timestamp.text.toString()
                        val timestamp = java.lang.Long.parseLong(source)
                        val human = Decoder.getNormalDate(timestamp)
                        normalDate.text = human
                    } catch (nfe: NumberFormatException) {
                        nfe.printStackTrace()
                    }

                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        //добавляем пункты меню
        menu.add(R.string.cur_timestamp)
            .setIcon(R.drawable.ic_menu_insert_text)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            .setOnMenuItemClickListener { menuItem ->
                InsertCurrentTimestamp()
                true
            }
        menu.add(R.string.clear_all)
                .setIcon(R.drawable.ic_menu_clear_all)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener { menuItem ->
                    clearAllText()
                    true
                }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == android.R.id.home)
            requireActivity().finish()
        return true
    }

    fun clearAllText() {
        Timestamp.setText("")
        normalDate.text = ""
        Toasty.success(requireContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show()
    }

    fun InsertCurrentTimestamp() {
        val epoch = System.currentTimeMillis()
        Timestamp.setText(epoch.toString())
        Timestamp.setSelection(Timestamp.text.length)
    }

    /*public void ConvertDate2Timestamp(){
        if (!normalDate.getText().toString().isEmpty() && Timestamp.getText().toString().isEmpty()) {
            String srcDate = normalDate.getText().toString();
            Date local;
            try {
                local = new SimpleDateFormat("dd.MM.YYYY HH:mm:ss", Locale.getDefault()).parse(srcDate);
                Timestamp.setText(""+local.getTime());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }*/
}
