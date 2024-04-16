package com.example.constraintlayout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtNumeroPessoas: EditText
    private lateinit var edtValorConta: EditText
    private lateinit var txtResultado: TextView
    private lateinit var btnCompartilhar: ImageButton
    private lateinit var btnFalar: ImageButton

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)

        edtNumeroPessoas = findViewById(R.id.edtNumeroPessoas)
        edtValorConta = findViewById(R.id.edtValorConta)
        txtResultado = findViewById(R.id.txtResultado)
        btnCompartilhar = findViewById(R.id.btnCompartilhar)
        btnFalar = findViewById(R.id.btnFalar)

        // Adiciona ouvinte de texto para detectar mudanças nas EditTexts
        edtNumeroPessoas.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                atualizarResultado()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        edtValorConta.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                atualizarResultado()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnFalar.setOnClickListener {
            try {
                val valorConta = edtValorConta.text.toString()
                val numPessoas = edtNumeroPessoas.text.toString()

                if (valorConta.isNotEmpty() && numPessoas.isNotEmpty()) {
                    val valor: Double = valorConta.toDouble()
                    val pessoas: Int = numPessoas.toInt()

                    val valorPorPessoa = valor / pessoas
                    val valorTratado = String.format("%.2f", valorPorPessoa)

                    txtResultado.text = "Valor por pessoa: $valorTratado"
                    txtResultado.visibility = View.VISIBLE

                    if (tts.isLanguageAvailable(Locale.getDefault()) != TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts.speak(
                            "O valor por pessoa é de $valorTratado reais.",
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null
                        )
                    } else {
                        Toast.makeText(
                            this,
                            "Idioma não suportado para Text-to-Speech",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Preencha o valor da conta e o número de pessoas.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show()
            }
        }

        btnCompartilhar.setOnClickListener {
            try {
                val valorConta = edtValorConta.text.toString()
                val numPessoas = edtNumeroPessoas.text.toString()

                if (valorConta.isNotEmpty() && numPessoas.isNotEmpty()) {
                    val valor: Double = valorConta.toDouble()
                    val pessoas: Int = numPessoas.toInt()

                    val valorPorPessoa = valor / pessoas
                    val valorTratado = String.format("%.2f", valorPorPessoa)

                    val message = "O valor por pessoa é de $valorTratado"
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, message)
                        type = "text/plain"
                    }
                    startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))
                } else {
                    Toast.makeText(
                        this,
                        "Preencha o valor da conta e o número de pessoas.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onDestroy() {
        if (tts.isSpeaking) {
            tts.stop()
        }
        tts.shutdown()
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.getDefault()
        } else {
            Toast.makeText(this, "Erro ao inicializar Text-to-Speech", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun atualizarResultado() {
        val valorConta = edtValorConta.text.toString()
        val numPessoas = edtNumeroPessoas.text.toString()

        if (valorConta.isNotEmpty() && numPessoas.isNotEmpty()) {
            val valor: Double = valorConta.toDouble()
            val pessoas: Int = numPessoas.toInt()

            val valorPorPessoa = valor / pessoas
            val valorTratado = String.format("%.2f", valorPorPessoa)

            txtResultado.text = "Valor por pessoa: $valorTratado"
            txtResultado.visibility = View.VISIBLE
        }
    }
}
