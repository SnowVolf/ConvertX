package ru.svolf.convertx.presentation.screens.regex

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.FragmentRegexDragonBinding
import ru.svolf.convertx.extension.clear
import ru.svolf.convertx.presentation.screens.settings.Preferences
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException


/**
 * A placeholder fragment containing a simple view.
 */
class RegexValidatorFragment : Fragment() {
    private var _binding: FragmentRegexDragonBinding? = null
    private val binding get() = _binding!!
    private val flags = Flags()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegexDragonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.regexFlags.text = flags.flagString
        binding.regexText.apply {
            textSize = Preferences.fontSize.toFloat()
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (binding.plainText.text.toString().isNotEmpty()) {
                        testRegex(text.toString())
                    }
                }

                override fun afterTextChanged(editable: Editable) {

                }
            })
        }

        binding.plainText.apply {
            textSize = Preferences.fontSize.toFloat()
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    testRegex(text.toString())
                }

                override fun afterTextChanged(editable: Editable) {

                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        menu.add(R.string.clear_all)
            .setIcon(R.drawable.ic_menu_clear_all)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            .setOnMenuItemClickListener { menuItem ->
                clearAllText()
                true
            }
        menu.add(R.string.flags)
            .setIcon(R.drawable.ic_menu_regex_flags)
            .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
            .setOnMenuItemClickListener { menuItem ->
                showFlagsList()
                true
            }
    }


    fun testRegex(regexString: String) {
        if (regexString.isEmpty()) {
            binding.regexCount.text = "Совпадений: 0"
            binding.regexResult.text = binding.plainText.text
            return
        }
        try {
            val m = Pattern.compile(regexString, flags.flags).matcher(binding.plainText.text)
            val spannable = SpannableString(binding.plainText.text)
            var i = 0
            while (m.find()) {
                spannable.setSpan(
                    BackgroundColorSpan(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)),
                    m.start(),
                    m.end(),
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                i++
            }
            binding.regexResult.text = spannable
            if (i == 1) {
                binding.regexCount.text = i.toString() + getString(R.string.one_match)
            } else
                binding.regexCount.text = getString(R.string.more_matches) + i
        } catch (pse: PatternSyntaxException) {
            binding.regexResult.text = pse.message
        }
    }

    private fun showFlagsList() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.regex_flags_title)
            .setMultiChoiceItems(flags.options, flags.selectedBooleans) { _, zoe, isChecked ->
                if (isChecked) {
                    flags.add(zoe)
                } else {
                    flags.remove(zoe)
                }
            }.setPositiveButton(R.string.ok) { _, _ ->
                binding.regexFlags.text = flags.flagString
                /**
                 * Проверяем поля на пустоту, сделано для того чтобы не применял regex к пустому выражению "".
                 * Иначе получается, если в {@param binding.plainText} пусто, то возникает коллапс
                 * "" = ""  = 1 совпадение
                 */
                if (binding.regexText.text.isNotEmpty() && binding.plainText.text.isNotEmpty()) {
                    testRegex(binding.regexText.text.toString())
                }
        }.create().show()

    }

    private fun clearAllText() {
        binding.plainText.clear()
        binding.regexText.clear()
        binding.regexCount.text = ""
        binding.regexResult.text = ""
    }
}
