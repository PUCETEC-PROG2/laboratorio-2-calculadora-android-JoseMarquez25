package ec.edu.uisek.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel()
) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = state.display,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            fontSize = 56.sp,
            textAlign = TextAlign.End, // Corrige el error de sintaxis
            color = Color.White,
        )

        // Llamamos al grid de botones
        CalculatorGrid(onButtonClick = { label ->
            viewModel.onEvent(mapButtonToEvent(label))
        })
    }
}

@Composable
fun CalculatorGrid(onButtonClick: (String) -> Unit) {
    val buttons = listOf(
        "7", "8", "9", "÷",
        "4", "5", "6", "×",
        "1", "2", "3", "−",
        "0", ".", "=", "+"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(buttons.size) { index ->
            val label = buttons[index]
            CalculatorButton(label = label) {
                onButtonClick(label)
            }
        }
        item(span = { GridItemSpan(currentLineSpan = 2) }) {
            CalculatorButton(label = "AC") {
                onButtonClick("AC")
            }
        }
        item {
            CalculatorButton(label = "C") {
                onButtonClick("C")
            }
        }
    }
}

@Composable
fun CalculatorButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(if (label == "AC") 2f else 1f)
            .fillMaxSize()
            .clip(CircleShape)
            .background(
                when (label) {
                    in listOf("÷", "×", "−", "+", "=", ".") -> Color.Gray // Aquí puedes cambiar los colores a tus preferencias
                    in listOf("AC", "C") -> Color.Red
                    else -> Color.Cyan
                }
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

fun mapButtonToEvent(label: String): CalculatorEvent {
    return when (label) {
        in "0".."9" -> CalculatorEvent.Number(label)
        in listOf("÷", "×", "−", "+") -> CalculatorEvent.Operator(label)
        "=" -> CalculatorEvent.Calculate
        "." -> CalculatorEvent.Decimal
        "AC" -> CalculatorEvent.AllClear
        "C" -> CalculatorEvent.Clear
        else -> throw IllegalArgumentException("Unknown label: $label")
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    CalculatorScreen()
}
