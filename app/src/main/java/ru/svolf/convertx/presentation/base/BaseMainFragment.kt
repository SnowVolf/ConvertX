package ru.svolf.convertx.presentation.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.svolf.convertx.App
import ru.svolf.convertx.R
import ru.svolf.convertx.data.dao.HistoryDao
import ru.svolf.convertx.databinding.FragmentMainBinding
import ru.svolf.convertx.extension.clear
import ru.svolf.convertx.utils.StringUtils.copyToClipboard
import ru.svolf.convertx.utils.SwipeEditText.OnSwipeListener


/**
 * Created by Snow Volf on 20.06.2017, 11:35
 */
open class BaseMainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!
    private lateinit var databaseDao: HistoryDao
    private lateinit var viewModel: MainFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainFragmentViewModel::class.java]
        databaseDao = (context?.applicationContext as App).mainComponent.getDbManager().getDatabase().historyDao()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_clear, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_clear -> {
                        clearAllText()
                        return true
                    }
                }
                return false
            }
        }, viewLifecycleOwner)

        val inputWatcher = InputWatcher()
        val outputWatcher = OutputWatcher()
        binding.fieldInput.apply {
            onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    addTextChangedListener(inputWatcher)
                } else {
                    removeTextChangedListener(inputWatcher)
                }
            }
            setOnSwipeListener(object : OnSwipeListener {
                override fun onSwipeLeft() {
                    clear()
                }

                override fun onSwipeRight() {

                }
            })
            setOnCompoundClickListener {
                copyInput(this)
            }
        }

        binding.fieldOutput.apply {
            onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    addTextChangedListener(outputWatcher)
                } else {
                    removeTextChangedListener(outputWatcher)
                }
            }
            setOnSwipeListener(object : OnSwipeListener {
                override fun onSwipeLeft() {
                    clear()
                }

                override fun onSwipeRight() {

                }
            })
            setOnCompoundClickListener {
                copyInput(this)
            }
        }


        viewModel.textSize.observe(viewLifecycleOwner) {
            binding.fieldOutput.textSize = it.toFloat()
            binding.fieldInput.textSize = it.toFloat()
        }

        onViewsReady()

        binding.fieldInput.setText(arguments?.getString("input_string"))
        binding.fieldOutput.setText(arguments?.getString("output_string"))
        arguments?.getInt("decoder_switch")?.let { binding.spinnerMode.setSelection(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun convertInput(input: String) {}

    open fun convertOutput(output: String) {}

    open fun onViewsReady() {}

    private fun copyInput(v: EditText) {
        copyToClipboard(requireContext(), v.text.toString())
        Toast.makeText(requireContext(), getString(R.string.copied2clipboard), Toast.LENGTH_SHORT).show()
    }

    open fun clearAllText() {
        binding.fieldInput.clear()
        binding.fieldOutput.clear()
        Toast.makeText(requireContext(), getString(R.string.cleared), Toast.LENGTH_SHORT).show()
    }

    fun getDao(): HistoryDao {
        return databaseDao
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
            inputRunnable = Runnable {
                if (!s.isNullOrBlank()) {
                    convertInput(s.toString())
                }
            }
            // Ждем 2 секунды пока не закончится ввод текста, чтобы не нагружать зря систему
            inputHandler.postDelayed(inputRunnable, 2000)
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
            outputRunnable = Runnable {
                if (!s.isNullOrBlank()) {
                    convertOutput(s.toString())
                }
            }
            // Ждем 2 секунды пока не закончится ввод текста, чтобы не нагружать зря систему
            outputHandler.postDelayed(outputRunnable, 2000)
        }

    }
}

