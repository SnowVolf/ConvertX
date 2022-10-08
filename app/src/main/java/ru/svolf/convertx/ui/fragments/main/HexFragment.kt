package ru.svolf.convertx.ui.fragments.main

import ru.svolf.convertx.ui.fragments.base.BaseMainFragment
import android.os.Bundle
import android.text.method.DigitsKeyListener
import ru.svolf.convertx.R
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import ru.svolf.convertx.algorhitms.Decoder
import ru.svolf.convertx.settings.Preferences
import ru.svolf.convertx.ui.Toasty

/**
 * Created by Snow Volf on 28.01.2017.
 */
class HexFragment : BaseMainFragment() {
    private var isInt = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                Toasty.error(requireContext(), ex.message!!).show()
            }
        } else {
            try {
                string = Decoder.toHexString(input)
            } catch (ex: java.lang.NumberFormatException){
                Toasty.error(requireContext(), ex.message!!).show()
            }
        }
        binding.fieldOutput.setText(string)
    }

    override fun convertOutput(output: String) {
        var string = ""
        if (isInt){
            try {
                string = Decoder.hexToInt(output)
            } catch (ex: java.lang.NumberFormatException){
                Toasty.error(requireContext(), ex.message!!).show()
            }
        } else {
            try {
                val string2 = output.replaceFirst("0x", "")
                string = Decoder.decodeHexString(string2)
            } catch (ex: Exception){
                Toasty.error(requireContext(), ex.message!!).show()
            }
        }
        binding.fieldInput.setText(string)
    }


    fun addListener(mode: Int){
        when (mode){
            0 -> isInt = false
            1 -> isInt = true
        }
    }
}