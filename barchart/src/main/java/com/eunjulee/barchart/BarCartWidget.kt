package com.eunjulee.barchart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer

data class GraphItem(val size: Int)

/**
 * @param gridLineSpacing sets the spacing between the horizontal lines drawn in the background of the chart.
 * @param gridLineStrokeWidth sets the thickness of the horizontal lines drawn in the background of the chart.
 * @param showYAxisUnit sets the visibility of the unit numbers on the Y-axis.
 * @param showBarValue sets the visibility of the value text displayed inside each bar.
 *  @param yMax sets the maximum value of the Y-axis. It is only available for Type B of the bar chart.
 * @param yUnit sets the unit of the Y-axis.
 * @param barWidth sets the width of the bars.
 * @param yTextStyle sets the text style for the Y-axis, including properties like text size, color, and font.
 * @param barTextStyle sets the text style for the text displayed on the bars, including properties like text size, color, and font.
 * It is only available for Type A of the bar chart.
 * @param color sets the color of the bars. If there are two or more colors, a gradient will be applied.
 * @param shape sets the corner shape of the top of the bars.
 * @param list sets the values that determine the height of the bars.
 *
 * **/
@Composable
fun BarCartWidget(
    modifier: Modifier = Modifier,
    gridLineSpacing: Dp = 30.dp,
    gridLineStrokeWidth: Dp = 1.dp,
    showYAxisUnit: Boolean = false,
    showBarValue: Boolean = false,
    yMax: Int = 25, // Y-axis maximum (Only need B type)
    yUnit: Int = 5, // Y-axis unit
    barWidth: Dp = 20.dp,
    yTextStyle: TextStyle = TextStyle(),
    barTextStyle: TextStyle = TextStyle(), //(Only need A type)
    color: List<Color> = listOf(Color.Yellow, Color.Cyan),
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    list: List<GraphItem>,
) {
    val chartHeight = gridLineSpacing * (yMax / yUnit)

    val density = LocalDensity.current

    // Calculate the width of the Y-axis text area
    val yAxisWidthDp = if (showYAxisUnit) {
        with(density) {
            val maxText = yMax.toString()
            val paint = android.graphics.Paint().apply {
                textSize = yTextStyle.fontSize.toPx()
            }
            paint.measureText(maxText).toDp() + 8.dp
        }
    } else 0.dp

    Layout(
        modifier = modifier.height(chartHeight),
        content = {

            GridLayer(
                yMax = yMax,
                yUnit = yUnit,
                gridLineSpacing = gridLineSpacing,
                gridLineStrokeWidth = gridLineStrokeWidth,
                showYAxisUnit = showYAxisUnit,
                yTextStyle = yTextStyle
            )

            BarsLayer(
                list = list,
                barWidth = barWidth,
                yUnit = yUnit,
                gridLineSpacing = gridLineSpacing,
                color = color,
                shape = shape,
                barTextStyle = barTextStyle,
                startPadding = yAxisWidthDp,
                showBarValue = showBarValue
            )
        }
    ) { measurables, constraints ->

        val placeables = measurables.map {
            it.measure(constraints)
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach {
                it.place(0, 0)
            }
        }
    }
}

@Composable
private fun GridLayer(
    yMax: Int,
    yUnit: Int,
    gridLineSpacing: Dp,
    gridLineStrokeWidth: Dp,
    showYAxisUnit: Boolean,
    yTextStyle: TextStyle
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(Modifier.fillMaxSize()) {

        val spacingPx = gridLineSpacing.toPx()
        val strokePx = gridLineStrokeWidth.toPx()
        val range = yMax / yUnit
        val dash = PathEffect.dashPathEffect(floatArrayOf(8f, 4f))

        repeat(range + 1) { i ->
            val y = size.height - i * spacingPx

            // dashed lines
            drawLine(
                color = if (i == 0) Color(0x33000000) else Color(0x1A000000),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokePx,
                pathEffect = if (i == 0) null else dash
            )

            // Y-axis text positioned just above the dashed lines
            if (showYAxisUnit) {
                val label = (i * yUnit).toString()

                val textLayout = textMeasurer.measure(
                    text = label,
                    style = yTextStyle
                )

                drawText(
                    textLayoutResult = textLayout,
                    topLeft = Offset(
                        x = 4.dp.toPx(),
                        y = y - textLayout.size.height
                    )
                )
            }
        }
    }
}

@Composable
private fun BarsLayer(
    list: List<GraphItem>,
    barWidth: Dp,
    yUnit: Int,
    gridLineSpacing: Dp,
    color: List<Color>,
    shape: RoundedCornerShape,
    barTextStyle: TextStyle,
    startPadding: Dp,
    showBarValue: Boolean
) {
    Layout(
        content = {
            list.forEach { item ->
                BarItem(
                    item,
                    barWidth,
                    yUnit,
                    gridLineSpacing,
                    color,
                    shape,
                    barTextStyle,
                    showBarValue
                )
            }
        }
    ) { measurables, constraints ->

        val startPaddingPx = startPadding.roundToPx()
        val barWidthPx = barWidth.roundToPx()
        val count = measurables.size

        val usableWidth = constraints.maxWidth - startPaddingPx
        val spacing = (usableWidth - barWidthPx * count) / (count + 1)

        val placeables = measurables.map {
            it.measure(Constraints.fixedWidth(barWidthPx))
        }

        val height = constraints.maxHeight

        layout(constraints.maxWidth, height) {
            var x = startPaddingPx + spacing
            placeables.forEach { p ->
                p.placeRelative(
                    x = x,
                    y = height - p.height
                )
                x += barWidthPx + spacing
            }
        }
    }
}

@Composable
private fun BarItem(
    item: GraphItem,
    width: Dp,
    unit: Int,
    lineSpacing: Dp,
    color: List<Color>,
    shape: RoundedCornerShape,
    barTextStyle: TextStyle,
    showBarValue: Boolean,
) {
    val barHeight = (item.size / unit) * lineSpacing + (item.size % unit) * 4.5.dp
    val bottomPadding = 6.dp

    Layout(
        content = {
            // Bar
            Box(
                Modifier
                    .height(barHeight)
                    .background(
                        Brush.verticalGradient(color),
                        shape
                    )
            )

            if (showBarValue) {
                Text(
                    text = item.size.toString(),
                    style = barTextStyle,
                    textAlign = TextAlign.Center
                )
            }
        }
    ) { measurables, _ ->

        val bar = measurables[0].measure(
            Constraints.fixedWidth(width.roundToPx())
        )

        val text = measurables.getOrNull(1)?.measure(
            Constraints(
                maxWidth = bar.width,
                maxHeight = bar.height
            )
        )

        val bottomPaddingPx = bottomPadding.roundToPx()

        layout(bar.width, bar.height) {

            // Bar background
            bar.placeRelative(0, 0)

            // Place the text inside the bar, aligned to the bottom
            text?.placeRelative(
                x = (bar.width - text.width) / 2,
                y = bar.height - text.height - bottomPaddingPx
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 220)
@Composable
fun PreviewBarChart() {
    BarCartWidget(
        showYAxisUnit = true,
        list = listOf(
            GraphItem(25),
            GraphItem(3),
            GraphItem(13),
            GraphItem(30),
            GraphItem(20)
        )
    )
}