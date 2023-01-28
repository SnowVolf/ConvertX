package ru.svolf.convertx.presentation.screens.regex

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.BackgroundColorSpan
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.svolf.convertx.R
import ru.svolf.convertx.presentation.screens.settings.Preferences
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException


/**
 * A placeholder fragment containing a simple view.
 */
class RegexValidatorFragment : Fragment() {
    lateinit var regexVal: EditText
    lateinit var sourceSoup: EditText
    private var counter: TextView? = null
    private var getCounterResult: TextView? = null
    private var currentFlag: TextView? = null
    private var rootView: View? = null
    private val flags = Flags()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_regex_dragon, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        regexVal = view.findViewById(R.id.regex_text) as EditText
        sourceSoup = view.findViewById(R.id.plain_text) as EditText
        counter = view.findViewById(R.id.regex_count) as TextView
        getCounterResult = view.findViewById(R.id.regex_result) as TextView
        currentFlag = view.findViewById(R.id.regex_flags) as TextView

        currentFlag!!.text = flags.flagString
        regexVal.textSize = Preferences.fontSize.toFloat()
        sourceSoup.textSize = Preferences.fontSize.toFloat()
        regexVal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!sourceSoup.text.toString().isEmpty()) {
                    testRegex()
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
        sourceSoup.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                /**
                 * Фикс бага htc 600
                 * Когда при пустом поле regex, все выражения были совпадающими
                 */
                testRegex()
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()

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

    @SuppressLint("SetTextI18n")
    fun testRegex() {
        val rV = regexVal.text.toString()
        if (rV == "") {
            counter?.text = "Совпадений: 0"
            getCounterResult!!.text = sourceSoup.text
            return
        }
        try {
            val m = Pattern.compile(rV, flags.flags).matcher(sourceSoup.text)
            val spannable = SpannableString(sourceSoup.text)
            var i = 0
            while (m.find()) {
                spannable.setSpan(
                    BackgroundColorSpan(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)),
                    m.start(),
                    m.end(),
                    33
                )
                i++
            }
            getCounterResult?.text = spannable
            if (i == 1) {
                counter!!.text = i.toString() + getString(R.string.one_match)
            } else
                counter!!.text = getString(R.string.more_matches) + i
        } catch (pse: PatternSyntaxException) {
            getCounterResult?.text = getString(R.string.illegal_syntax1) + pse.message + getString(R.string.illegal_syntax2)
        }

    }

    private fun showFlagsList() {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.regex_flags_title)
                .setMultiChoiceItems(flags.options, flags.selectedBooleans) { dialogInterface, zoe, isChecked ->
                    if (isChecked) {
                        flags.add(zoe)
                    } else {
                        flags.remove(zoe)
                    }
                }.setPositiveButton(R.string.ok) { _, _ ->
                currentFlag!!.text = flags.flagString
                /**
                 * Проверяем поля на пустоту, сделано для того чтобы не применял regex к пустому выражению "".
                 * Иначе получается, если в {@param sourceSoup} пусто, то возникает коллапс
                 * "" = ""  = 1 совпадение
                 */
                if (regexVal.text.isNotEmpty() && sourceSoup.text.isNotEmpty()) {
                    testRegex()
                }
        }.create().show()

    }

    private fun clearAllText() {
        sourceSoup.setText("")
        regexVal.setText("")
        counter!!.text = ""
        getCounterResult!!.text = ""
    }
}
