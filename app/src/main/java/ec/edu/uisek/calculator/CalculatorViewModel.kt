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
    data class Operator(val Operator: String) : CalculatorEvent()
    object Clear : CalculatorEvent()
    object AllClear : CalculatorEvent()
    object Calculate : CalculatorEvent()
    object Decimal : CalculatorEvent()
}

class CalculatorViewModel : ViewModel () {
    private var number1: String = ""
    private var number2: String = ""
    private var operator: String? = null

    var state by mutableStateOf(CalculatorState())
        private set

    fun onEvent(event:CalculatorEvent) {
        when (event) {
            is CalculatorEvent.Number -> enterNumber(event.number)
            is CalculatorEvent.Operator -> enterOperator(event.Operator)
            is CalculatorEvent.Decimal -> enterDecimal()
            is CalculatorEvent.Clear -> clearlast()
            is CalculatorEvent.AllClear -> clearAll()
            is CalculatorEvent.Calculate -> performCalculation()
        }
    }

    private fun performCalculation() {
        TODO("Not yet implemented")
    }

    private fun clearAll() {
        TODO("Not yet implemented")
    }

    private fun clearlast() {
        TODO("Not yet implemented")
    }

    private fun enterDecimal() {
        val currentNumber = if (operator == null) number1 else number2
        if (!currentNumber.contains(".")) {
            if (operator == null) {
                number1 += "."
                state = state.copy(display = number1)
            } else {
                number1 += "."
                state = state.copy(display = number1)
            }
        }
    }

    private fun enterOperator(operator: String) {
        if (number1.isNotBlank()) {
            this.operator = operator
        }
    }

    private fun enterNumber(number: String) {
        if (operator == null) {
            number1 += number
            state = state.copy(display = number1)
        } else {
            number1 += number
            state = state.copy(display = number2)
        }
    }
}