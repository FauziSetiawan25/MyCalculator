package com.example.mycalculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mycalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var lastNumber = false
    private var stateError = false
    private var lastDot = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onEqual(){
        if (lastNumber && !stateError){
            val txt = binding.data.text.toString()

            expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                binding.result.visibility = View.VISIBLE
                binding.result.text = result.toString()
            }catch (ex : ArithmeticException){
                Log.e("evaluate error ", ex.toString())
                binding.result.text = "Error"
                stateError = true
                lastNumber = false
            }
        }
    }

    fun onDigitClick(view: View) {
        if (stateError){
            binding.data.text = (view as Button).text
            stateError = false
        }else{
            binding.data.append((view as Button).text)
        }
        lastNumber = true
        onEqual()
    }

    fun onOperatorClick(view: View) {
        if (!stateError && lastNumber){
            binding.data.append((view as Button).text)
            lastDot = false
            lastNumber = false
            onEqual()
        }
    }

    fun onClearClick(view: View) {
        binding.data.text = ""
        binding.result.text = ""
        stateError = false
        lastDot = false
        lastNumber = false
        binding.result.visibility = View.GONE
    }

    fun onBackClick(view: View) {
        binding.data.text = binding.data.text.toString().dropLast(1)
        try {
            val lastChar = binding.data.text.toString().last()
            if(lastChar.isDigit()){
                onEqual()
            }
        }catch (e: Exception){
            binding.result.text = ""
            binding.result.visibility = View.GONE
            Log.e("Last char error ", e.toString())
        }
    }

    fun onEqualClick(view: View) {
        if (binding.result.text.toString().last() == '0'){
            binding.result.text = binding.result.text.toString().dropLast(2)
        }
        binding.data.text = binding.result.text
        binding.result.visibility = View.GONE
    }
}