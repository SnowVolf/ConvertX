package ru.svolf.convertx.presentation.fragments.main

import android.text.method.DigitsKeyListener
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.svolf.convertx.R
import ru.svolf.convertx.algorhitms.Decoder
import ru.svolf.convertx.data.entity.HistoryItem
import ru.svolf.convertx.presentation.fragments.base.BaseMainFragment
import ru.svolf.convertx.settings.Preferences

/**
 * Created by Snow Volf on 28.01.2017.
 */
class HexFragment : BaseMainFragment() {
    private var isInt = false

    override fun onViewsReady() {
        super.onViewsReady()
        binding.fieldInput.hint = getString(R.string.hint_string)
        binding.fieldOutput.apply {
            setHint(R.string.hint_hex)
            keyListener = DigitsKeyListener.getInstance("1234567890ABCDEFabcdef")
        }
        val hexAdapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(requireActivity(), R.array.hexMode, android.R.layout.simple_spinner_item)
        hexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMode.apply {
            visibility = View.VISIBLE
            adapter = hexAdapter
            setSelection(Preferences.getSpinnerPosition("spinner.hex"))
            onItemSelectedListener = object: OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    Preferences.setSpinnerPosition("spinner.hex", position)
                    addListener(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Preferences.setSpinnerPosition("spinner.hex", 0)
                    addListener(0)
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        //добавляем пункты меню
        menu.add(R.string.clear_all)
            .setIcon(R.drawable.ic_menu_clear_all)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                clearAllText()
                true
            }
    }

    override fun convertInput(input: String) {
        var string = ""
        if (isInt){
            try {
                string = Decoder.intToHex(input.toInt())
            } catch (ex: java.lang.NumberFormatException){
                Toast.makeText(requireContext(), ex.message!!, Toast.LENGTH_SHORT).show()
            }
        } else {
            try {
                string = Decoder.toHexString(input)
            } catch (ex: java.lang.NumberFormatException) {
                Toast.makeText(requireContext(), ex.message!!, Toast.LENGTH_SHORT).show()
            }
        }
        binding.fieldOutput.setText(string)
        CoroutineScope(Dispatchers.IO).launch {
            val hist = HistoryItem().apply {
                this.id = System.currentTimeMillis()
                this.input = input
                this.output = string
                this.decoder = 2
                this.spinnerPosition = Preferences.getSpinnerPosition("spinner.hex")
            }
            getDao().insert(hist)
        }
    }

    override fun convertOutput(output: String) {
        var string = ""
        if (isInt){
            try {
                string = Decoder.hexToInt(output)
            } catch (ex: java.lang.NumberFormatException){
                Toast.makeText(requireContext(), ex.message!!, Toast.LENGTH_SHORT).show()
            }
        } else {
            try {
                val string2 = output.replaceFirst("0x", "")
                string = Decoder.decodeHexString(string2)
            } catch (ex: Exception) {
                Toast.makeText(requireContext(), ex.message!!, Toast.LENGTH_SHORT).show()
            }
        }
        binding.fieldInput.setText(string)
        CoroutineScope(Dispatchers.IO).launch {
            val hist = HistoryItem().apply {
                this.id = System.currentTimeMillis()
                this.input = string
                this.output = output
                this.decoder = 2
                this.spinnerPosition = Preferences.getSpinnerPosition("spinner.hex")
            }
            getDao().insert(hist)
        }
    }


    fun addListener(mode: Int){
        when (mode){
            0 -> isInt = false
            1 -> isInt = true
        }
    }
}