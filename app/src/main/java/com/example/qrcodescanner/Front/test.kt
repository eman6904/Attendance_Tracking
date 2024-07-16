import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.sin

@Composable
fun WavyRow(
    modifier: Modifier = Modifier,
    color: Color = Color.Blue,
    amplitude: Float = 10f,
    frequency: Float = 0.5f,
    content: @Composable RowScope.() -> Unit
) {
    Box(modifier = modifier.background(Color.Magenta)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val path = Path().apply {
                moveTo(0f, height / 2)
                for (x in 0..width.toInt()) {
                    val y = height / 2 + amplitude * sin((x / width.toFloat()) * frequency * 2 * Math.PI).toFloat()
                    lineTo(x.toFloat(), y)
                }
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(
                path = path,
                color = color,
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun WavyRowPreview() {
    WavyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        color = Color.Yellow,
        amplitude = 90f,
        frequency = 0.5f
    ) {
        Text(text = "Hello Wave!", color = Color.White, modifier = Modifier.padding(8.dp))
        Text(text = "This is a wavy row", color = Color.White, modifier = Modifier.padding(8.dp))
    }
}
