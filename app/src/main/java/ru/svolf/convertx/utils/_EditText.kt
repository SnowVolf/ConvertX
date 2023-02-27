 package ru.svolf.convertx.utils

 import android.util.Log
 import android.widget.TextView
 import androidx.appcompat.widget.AppCompatTextView
 import androidx.core.text.PrecomputedTextCompat
 import kotlinx.coroutines.CoroutineScope
 import kotlinx.coroutines.Dispatchers
 import kotlinx.coroutines.launch
 import kotlinx.coroutines.withContext

 fun TextView.clear() {
     setText("")
 }

 fun AppCompatTextView.setTextAsync(text: CharSequence) {
     val start = System.currentTimeMillis()
     val params = this.textMetricsParamsCompat
     CoroutineScope(Dispatchers.Default).launch {
         val pText = PrecomputedTextCompat.create(text, params)
         withContext(Dispatchers.Main) {
             setPrecomputedText(pText)
             val end = System.currentTimeMillis()
             Log.e("SUKA", "Set TEXT by ${end - start} ms")
         }
     }
 }