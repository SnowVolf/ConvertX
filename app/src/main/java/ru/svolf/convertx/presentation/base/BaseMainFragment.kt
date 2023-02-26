package ru.svolf.convertx.presentation.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.svolf.convertx.App
import ru.svolf.convertx.R
import ru.svolf.convertx.data.dao.HistoryDao
import ru.svolf.convertx.databinding.FragmentMainBinding
import ru.svolf.convertx.utils.StringUtils.copyToClipboard
import ru.svolf.convertx.utils.SwipeEditText.OnSwipeListener
import ru.svolf.convertx.utils.clear


/**
 * Created by Snow Volf on 20.06.2017, 11:35
 */
open class BaseMainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    protected val binding get() = _binding!!
    private lateinit var databaseDao: HistoryDao
    private val viewModel by viewModels<MainFragmentViewModel>()

    /**
     * Specify item ID for database. Needs for updates in case of sequence decoding.
     * For example:
     * - Type: A long time ago
     * - Wait 2 sec
     * - Value decoded
     * - Saved to database
     * - Added 3 dots (A long time ago...)
     * - Wait 2 sec
     * - Value decoded
     * - Updates EXITING item instead of creating a new one
     * This key has been updated automatically in case of removing all text from EditText
     */
    protected var persistedId: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    protected fun getDao(): HistoryDao {
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
            if (s.isNullOrEmpty()) {
                persistedId = System.currentTimeMillis()
                Log.e("TAGS", "RESET VAL = $persistedId")
            }
            if (this::inputRunnable.isInitialized) {
                inputHandler.removeCallbacks(inputRunnable)
            }
            inputRunnable = Runnable {
                if (!s.isNullOrBlank()) {
                    convertInput(s.toString())
                    Log.e("TAGS", "CHECK VAL = $persistedId")
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
            if (s.isNullOrEmpty()) {
                persistedId = System.currentTimeMillis()
                Log.e("TAGS", "RESET VAL = $persistedId")
            }
            if (this::outputRunnable.isInitialized) {
                outputHandler.removeCallbacks(outputRunnable)
            }
            outputRunnable = Runnable {
                if (!s.isNullOrBlank()) {
                    convertOutput(s.toString())
                    Log.e("TAGS", "NEW VAL = $persistedId")
                }
            }
            // Ждем 2 секунды пока не закончится ввод текста, чтобы не нагружать зря систему
            outputHandler.postDelayed(outputRunnable, 2000)
        }

    }
}

