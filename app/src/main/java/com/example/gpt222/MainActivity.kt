package com.example.gpt222

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import com.example.gpt222.ml.GPT2Client

class MainActivity : ComponentActivity() {
    private val gpt2: GPT2Client by viewModels()
    private var prompt = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.autocomplete_button).setOnClickListener {
            gpt2.launchAutocomplete()
        }
        findViewById<Button>(R.id.shuffle_button).setOnClickListener {
            gpt2.refreshPrompt()
        }
        gpt2.prompt.observe(this) { viewModelPrompt ->
            prompt = viewModelPrompt
        }
        gpt2.completion.observe(this) { completion ->
            findViewById<TextView>(R.id.prompt).text = when {
                completion.isEmpty() -> prompt
                else -> {
                    val str = SpannableStringBuilder(prompt + completion)
                    val bgCompletionColor = ResourcesCompat.getColor(resources, R.color.colorPrimary, theme)
                    str.setSpan(android.text.style.BackgroundColorSpan(bgCompletionColor), prompt.length, str.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    str
                }
            }
        }
    }
}
