package ru.svolf.convertx.presentation.screens.regex

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.svolf.convertx.R
import ru.svolf.convertx.databinding.FragmentRegexDragonBinding
import ru.svolf.convertx.extension.clear
import ru.svolf.convertx.presentation.screens.settings.Preferences
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException


/**
 * A placeholder fragment containing a simple view.
 */
class RegexValidatorFragment : Fragment() {
    private var _binding: FragmentRegexDragonBinding? = null
    private val binding get() = _binding!!
    private val flags = Flags()

    private lateinit var pattern: Pattern
    private lateinit var matcher: Matcher
    private lateinit var spannable: SpannableString
    private var occursCount: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegexDragonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_regex, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId) {
                    R.id.item_clear -> clearAllText()
                    R.id.regex_flags -> showFlagsList()
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.regexFlags.text = flags.flagString
        binding.regexText.apply {
            textSize = Preferences.fontSize.toFloat()
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (binding.plainText.text.toString().isNotEmpty()) {
                        testRegex(binding.regexText.text.toString())
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
                    testRegex(binding.regexText.text.toString())
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


    private fun testRegex(regexString: String) {
        if (regexString.isEmpty()) {
            binding.regexResult.text = binding.plainText.text
            return
        }
        try {
            pattern = Pattern.compile(regexString, flags.flags)
            matcher = pattern.matcher(binding.plainText.text)
            spannable = SpannableString(binding.plainText.text)
            occursCount = 0
            while (matcher.find()) {
                spannable.setSpan(
                    BackgroundColorSpan(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)),
                    matcher.start(),
                    matcher.end(),
                    33
                )
                occursCount++
            }
            binding.regexResult.text = spannable
            binding.regexCount.text = resources.getQuantityString(R.plurals.regex_matches, occursCount, occursCount)
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
