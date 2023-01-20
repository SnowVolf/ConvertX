package ru.svolf.convertx.presentation.fragments.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.FragmentMainBinding
import ru.svolf.convertx.extension.clear
import ru.svolf.convertx.settings.Preferences
import ru.svolf.convertx.utils.StringUtils.copyToClipboard
import ru.svolf.convertx.utils.StringUtils.readFromClipboard
import ru.svolf.convertx.utils.Toasty


/**
 * Created by Snow Volf on 20.06.2017, 11:35
 */
open class BaseMainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inputWatcher = InputWatcher()
        val outputWatcher = OutputWatcher()

        binding.fieldInput.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.fieldInput.addTextChangedListener(inputWatcher)
            } else {
                binding.fieldInput.removeTextChangedListener(inputWatcher)
            }
        }

        binding.fieldOutput.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.fieldOutput.addTextChangedListener(outputWatcher)
            } else {
                binding.fieldOutput.removeTextChangedListener(outputWatcher)
            }
        }
        binding.btnCopyInput.setOnClickListener { v: View? -> copyInput(binding.fieldInput) }
        binding.btnCopyOutput.setOnClickListener { v: View? -> copyOutput(binding.fieldOutput) }
        binding.btnClearInput.setOnClickListener { v: View? -> clearOutput(binding.fieldInput) }
        binding.btnPasteOutput.setOnClickListener { v: View? -> pasteInput(binding.fieldOutput) }

        onViewsReady()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
            try {
                binding.fieldOutput.textSize = Preferences.fontSize.toFloat()
                binding.fieldInput.textSize = Preferences.fontSize.toFloat()
            } catch (ex: Exception) {
                ex.printStackTrace()
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

    open fun convertInput(input: String) {}

    open fun convertOutput(output: String) {}

    open fun onViewsReady() {}

    fun copyInput(v: EditText) {
        copyToClipboard(requireContext(), v.text.toString())
        Toasty.info(requireContext(), getString(R.string.copied2clipboard)).show()
    }

    fun copyOutput(v: EditText) {
        copyToClipboard(requireContext(), v.text.toString())
        Toasty.info(requireContext(), getString(R.string.copied2clipboard)).show()
    }

    fun clearOutput(edit: EditText) {
        edit.setText("")
    }

    fun pasteInput(edit: EditText) {
        edit.setText(readFromClipboard())
    }

    open fun clearAllText() {
        binding.fieldInput.clear()
        binding.fieldOutput.setText("")
        Toasty.success(requireContext(), getString(R.string.cleared), Toast.LENGTH_SHORT, true).show()
    }

    inner class InputWatcher : TextWatcher {
        private lateinit var inputRunnable: Runnable
        private val inputHandler = Handler(Looper.getMainLooper())

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (this::inputRunnable.isInitialized) {
                inputHandler.removeCallbacks(inputRunnable)
            }
            inputRunnable = Runnable { convertInput(s.toString()) }
            // Ждем 1,5 секунды пока не закончится ввод текста, чтобы не нагружать зря систему
            inputHandler.postDelayed(inputRunnable, 1500)
        }
    }

    inner class OutputWatcher : TextWatcher {
        private lateinit var outputRunnable: Runnable
        private val outputHandler = Handler(Looper.getMainLooper())
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (this::outputRunnable.isInitialized) {
                outputHandler.removeCallbacks(outputRunnable)
            }
            outputRunnable = Runnable { convertOutput(s.toString()) }
            // Ждем 1,5 секунды пока не закончится ввод текста, чтобы не нагружать зря систему
            outputHandler.postDelayed(outputRunnable, 1500)
        }

    }
}

