package ec.edu.uisek.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class CalculatorState(
    val display: String = "0"
)

sealed class CalculatorEvent {
    data class Number(val number: String) : CalculatorEvent()
    data class Operator(val operator: String) : CalculatorEvent()
    object Clear : CalculatorEvent()
    object AllClear : CalculatorEvent()
    object Calculate : CalculatorEvent()
    object Decimal : CalculatorEvent()
}

class CalculatorViewModel : ViewModel() {

    private var number1: String = ""
    private var number2: String = ""
    private var operator: String? = null
    private var justCalculated: Boolean = false // Flag para saber si el cálculo ya se hizo

    var state by mutableStateOf(CalculatorState())
        private set

    fun onEvent(event: CalculatorEvent) {
        when (event) {
            is CalculatorEvent.Number -> enterNumber(event.number)
            is CalculatorEvent.Operator -> enterOperator(event.operator)
            is CalculatorEvent.Decimal -> enterDecimal()
            is CalculatorEvent.AllClear -> clearAll()
            is CalculatorEvent.Clear -> clearLast()
            is CalculatorEvent.Calculate -> performCalculation()
        }
    }

    private fun enterNumber(number: String) {
        // Si se acaba de hacer un cálculo, reiniciamos el display
        if (justCalculated) {
            clearAll()
        }

        // Agregamos el número al display
        if (operator == null) {
            number1 += number
            state = state.copy(display = number1)
        } else {
            number2 += number
            state = state.copy(display = number1 + operator!! + number2)
        }
    }

    private fun enterOperator(op: String) {
        // Evitamos operadores consecutivos
        if (number1.isNotEmpty() && number2.isEmpty() && operator == null) {
            operator = op
            state = state.copy(display = number1 + operator!!)
        } else if (number2.isNotEmpty() && operator != null) {
            // Si ya tenemos un número 2 y un operador, realizamos el cálculo
            performCalculation()
            operator = op
            state = state.copy(display = number1 + operator!!)
        } else if (number2.isEmpty() && operator != null) {
            // Si ya hay un operador y no hemos ingresado el segundo número, no agregamos un nuevo operador
            return
        }
    }

    private fun enterDecimal() {
        val currentNumber = if (operator == null) number1 else number2
        if (!currentNumber.contains(".")) {
            if (operator == null) {
                number1 += "."
                state = state.copy(display = number1)
            } else {
                number2 += "."
                state = state.copy(display = number1 + operator + number2)
            }
        }
    }

    private fun clearAll() {
        number1 = ""
        number2 = ""
        operator = null
        justCalculated = false
        state = state.copy(display = "0")
    }

    private fun clearLast() {
        if (justCalculated) return // Si ya calculamos, no podemos borrar más

        if (operator == null) {
            number1 = if (number1.isNotEmpty()) number1.dropLast(1) else ""
            state = state.copy(display = if (number1.isBlank()) "0" else number1)
        } else {
            number2 = if (number2.isNotEmpty()) number2.dropLast(1) else ""
            state = state.copy(display = number1 + operator + number2)
        }
    }

    private fun performCalculation() {
        if (number1.isBlank() || number2.isBlank() || operator == null) return

        val num1 = number1.toDoubleOrNull()
        val num2 = number2.toDoubleOrNull()

        if (num1 == null || num2 == null) {
            state = state.copy(display = "Error")
            return
        }

        val result = when (operator) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "×" -> num1 * num2
            "÷" -> if (num2 != 0.0) num1 / num2 else Double.NaN
            else -> Double.NaN
        }

        val resultString = if (result.isNaN()) "Error" else result.toString().removeSuffix(".0")

        number1 = resultString
        number2 = ""
        operator = null
        justCalculated = true // Marca que ya se calculó
        state = state.copy(display = resultString)
    }
}
