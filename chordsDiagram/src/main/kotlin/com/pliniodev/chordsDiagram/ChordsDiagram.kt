@file:OptIn(ExperimentalTextApi::class)

package com.pliniodev.chordsDiagram

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.pliniodev.guitarview.ChordPosition
import com.pliniodev.guitarview.Fingering
import com.pliniodev.guitarview.GuitarFret
import com.pliniodev.guitarview.GuitarString
import com.pliniodev.guitarview.GuitarViewOptions
import com.pliniodev.guitarview.GuitarViewSize
import com.pliniodev.guitarview.OpenGuitarString
import com.pliniodev.guitarview.StringStateSize

private const val quantityOfStringsFactor = 7f
private const val quantityOfFretsFactor = 5f

private const val LetterXCentralize = 12f

@Composable
fun ChordsDiagram(
    options: GuitarViewOptions,
    modifier: Modifier = Modifier,
) {
    val textMeasurer = rememberTextMeasurer()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        if (options.showStringNotes) {
            GuitarStringNotes(
                diagramWidth = options.guitarViewSize.diagramSize.width,
                stringNoteHeight = options.guitarViewSize.stringNoteSize.height,
                textNoteSize = options.guitarViewSize.stringNoteSize.textNoteSize,
                textMeasurer = textMeasurer,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Guitar(
            diagramWidth = options.guitarViewSize.diagramSize.width,
            diagramHeight = options.guitarViewSize.diagramSize.height,
            fingeringTextSize = options.guitarViewSize.fingeringSize.textSize,
            fingeringRadius = options.guitarViewSize.fingeringSize.fingeringRadius,
            chordPositions = options.chordPositions,
            fretStroke = options.guitarViewSize.diagramSize.fretStroke,
            nutStroke = options.guitarViewSize.diagramSize.nutStroke,
            stringStroke = options.guitarViewSize.diagramSize.stringStroke,
            textMeasurer = textMeasurer,
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (options.showStringOpenOrClose) {
            GuitarStringState(
                diagramWidth = options.guitarViewSize.diagramSize.width,
                stringStateSize = options.guitarViewSize.stringStateSize,
                openStrings = options.openStrings
            )
        }
    }
}

@Composable
private fun Guitar(
    diagramWidth: Dp,
    diagramHeight: Dp,
    fingeringTextSize: TextUnit,
    fingeringRadius: Dp,
    fretStroke: Dp,
    nutStroke: Dp,
    stringStroke: Dp,
    chordPositions: List<ChordPosition>,
    textMeasurer: TextMeasurer
) {
    val style = remember {
        TextStyle(
            color = Color.White,
            fontSize = fingeringTextSize,
        )
    }
    Canvas(
        modifier = Modifier
            .size(
                width = diagramWidth,
                height = diagramHeight,
            )
    ) {
        drawGuitarBackground(
            diagramWidth = diagramWidth,
            diagramHeight = diagramHeight,
        )
        drawGuitar(
            diagramWidth = diagramWidth,
            diagramHeight = diagramHeight,
            strokeWidth = fretStroke,
            nutStrokeWidth = nutStroke,
            stringStroke = stringStroke,
        )
        drawChordAndFingering(
            diagramWidth = diagramWidth,
            diagramHeight = diagramHeight,
            fingeringRadius = fingeringRadius,
            chordPositions = chordPositions,
            textMeasurer = textMeasurer,
            textStyle = style,
        )
    }
}

@Composable
private fun GuitarStringNotes(
    diagramWidth: Dp,
    stringNoteHeight: Dp,
    textNoteSize: TextUnit,
    textMeasurer: TextMeasurer
) {
    Canvas(
        modifier = Modifier
            .size(
                width = diagramWidth,
                height = stringNoteHeight,
            )
    ) { drawLineHeader(textMeasurer, diagramWidth, textNoteSize) }
}

@Composable
private fun GuitarStringState(
    diagramWidth: Dp,
    openStrings: List<OpenGuitarString>,
    stringStateSize: StringStateSize
) {
    Canvas(
        modifier = Modifier
            .size(
                width = diagramWidth,
                height = stringStateSize.height,
            )
    ) {
        drawStringState(
            openStrings = openStrings,
            openRadius = stringStateSize.openRadius,
            stateStroke = stringStateSize.stroke,
            mutedSize = stringStateSize.mutedSize,
            diagramWidth = diagramWidth,
        )
    }
}

private fun DrawScope.drawLineHeader(
    textMeasurer: TextMeasurer,
    diagramWidth: Dp,
    textNoteSize: TextUnit,
) {
    GuitarString.values().forEachIndexed { index, guitarString ->
        drawLineNote(
            text = guitarString.stringName,
            position = index + 1,
            diagramWidth = diagramWidth,
            stringNoteSize = textNoteSize,
            textMeasurer = textMeasurer,
        )
    }
}

private fun DrawScope.drawStringState(
    openStrings: List<OpenGuitarString>,
    openRadius: Dp,
    mutedSize: Dp,
    diagramWidth: Dp,
    stateStroke: Dp,
) {
    val quantityOfStrings = GuitarString.values().size
    val stringsRange = 1..quantityOfStrings

    openStrings.map { opened ->
        drawOpenString(
            position = opened.position,
            openRadius = openRadius,
            diagramWidth = diagramWidth,
            stroke = stateStroke,
        )
    }
    stringsRange.filterNot { guitarString -> openStrings.any { guitarString == it.position } }
        .map { position ->
            drawMutedString(
                drawMutedSize = mutedSize,
                stringMutedStroke = stateStroke,
                center = Offset(
                    x = diagramWidth.toPx() * ((position) / quantityOfStringsFactor),
                    y = center.y,
                )
            )
        }
}

private fun DrawScope.drawGuitar(
    diagramWidth: Dp,
    diagramHeight: Dp,
    strokeWidth: Dp,
    stringStroke: Dp,
    nutStrokeWidth: Dp,
) {
    val quantityOfStrings = GuitarString.values().size
    val quantityOfFrets = GuitarFret.values().size
    (1..quantityOfFrets).map { yLinePosition ->
        drawGuitarFret(
            diagramWidth = diagramWidth,
            diagramHeight = diagramHeight,
            strokeWidth = strokeWidth,
            yLinePosition = yLinePosition,
        )
    }
    (1..quantityOfStrings).map { xLinePosition ->
        drawGuitarString(
            diagramWidth = diagramWidth,
            diagramHeight = diagramHeight,
            strokeWidth = stringStroke,
            xLinePosition = xLinePosition,
        )
    }
    drawGuitarNut(
        diagramWidth = diagramWidth,
        diagramHeight = diagramHeight,
        strokeWidth = nutStrokeWidth,
    )
}

private fun DrawScope.drawChordAndFingering(
    diagramWidth: Dp,
    diagramHeight: Dp,
    fingeringRadius: Dp,
    chordPositions: List<ChordPosition>,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
) {
    chordPositions.forEach { chordPosition ->
        drawFingerCircle(
            fingering = chordPosition.fingering,
            center = Offset(
                x = diagramWidth.toPx() * (chordPosition.guitarString.position / quantityOfStringsFactor),
                y = (diagramHeight.toPx() * (chordPosition.guitarFret.position / quantityOfFretsFactor))
                        - (diagramHeight.toPx() / (quantityOfFretsFactor * 2)),
            ),
            radius = fingeringRadius,
            textStyle = textStyle,
            textMeasurer = textMeasurer,
            textLayoutResult = textMeasurer.measure(chordPosition.fingering.number, textStyle)
        )
    }
}

private fun DrawScope.drawGuitarString(
    diagramWidth: Dp,
    diagramHeight: Dp,
    strokeWidth: Dp,
    xLinePosition: Int,
) {
    drawLine(
        color = Color.Black,
        start = Offset(
            x = diagramWidth.toPx() * (xLinePosition / quantityOfStringsFactor),
            y = 0f
        ),
        end = Offset(
            x = diagramWidth.toPx() * (xLinePosition / quantityOfStringsFactor),
            y = diagramHeight.toPx()
        ),
        strokeWidth = strokeWidth.toPx(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawGuitarFret(
    diagramWidth: Dp,
    diagramHeight: Dp,
    yLinePosition: Int,
    strokeWidth: Dp,
) {
    drawLine(
        color = Color.Gray,
        start = Offset(
            x = diagramWidth.toPx() / quantityOfStringsFactor,
            y = diagramHeight.toPx() * (yLinePosition / quantityOfFretsFactor)
        ),
        end = Offset(
            x = diagramWidth.toPx() - diagramWidth.toPx() / quantityOfStringsFactor,
            y = diagramHeight.toPx() * (yLinePosition / quantityOfFretsFactor)
        ),
        strokeWidth = strokeWidth.toPx(),
        cap = StrokeCap.Round
    )
    // shadow frets
    drawLine(
        color = Color.DarkGray,
        start = Offset(
            x = diagramWidth.toPx() / 7,
            y = diagramHeight.toPx() * (yLinePosition / quantityOfFretsFactor) - 3f
        ),
        end = Offset(
            x = diagramWidth.toPx() - diagramWidth.toPx() / quantityOfStringsFactor,
            y = diagramHeight.toPx() * (yLinePosition / quantityOfFretsFactor) - 3f
        ),
        strokeWidth = strokeWidth.toPx(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawGuitarNut(
    diagramWidth: Dp,
    diagramHeight: Dp,
    strokeWidth: Dp,
) {
    val brush = Brush.linearGradient(
        colors = listOf(Color.Gray, Color.White)
    )
    drawLine(
        color = Color.LightGray,
        start = Offset(
            x = (diagramWidth.toPx() / quantityOfStringsFactor) - 5f,
            y = diagramHeight.toPx() * (0 / quantityOfFretsFactor) + 3f
        ),
        end = Offset(
            x = diagramWidth.toPx() - (diagramWidth.toPx() / quantityOfStringsFactor) + 5f,
            y = diagramHeight.toPx() * (0 / quantityOfFretsFactor) + 3f
        ),
        strokeWidth = strokeWidth.toPx(),
        cap = StrokeCap.Butt
    )
    drawLine(
        brush = brush,
        start = Offset(
            x = (diagramWidth.toPx() / quantityOfStringsFactor) - 5f,
            y = diagramHeight.toPx() * (0 / quantityOfFretsFactor)
        ),
        end = Offset(
            x = diagramWidth.toPx() - (diagramWidth.toPx() / quantityOfStringsFactor) + 5f,
            y = diagramHeight.toPx() * (0 / quantityOfFretsFactor)
        ),
        strokeWidth = strokeWidth.toPx(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawGuitarBackground(
    diagramWidth: Dp,
    diagramHeight: Dp,
) {
    val brush = Brush.horizontalGradient(listOf(Color.White, Color.LightGray))
    val rectWidth =
        diagramWidth.toPx() - ((diagramWidth.toPx() / quantityOfStringsFactor) * 2)
    val rectHeight = diagramHeight.toPx()
    drawRect(
        brush = brush,
        size = Size(
            width = rectWidth,
            height = rectHeight
        ),
        topLeft = Offset(
            x = (diagramWidth.toPx() / quantityOfStringsFactor),
            y = 0f
        ),
    )
}

private fun DrawScope.drawFingerCircle(
    radius: Dp,
    fingering: Fingering,
    center: Offset,
    textMeasurer: TextMeasurer,
    textLayoutResult: TextLayoutResult,
    textStyle: TextStyle,
) {
    drawCircle(
        color = Color.Black,
        radius = radius.toPx(),
        center = Offset(
            x = center.x,
            y = center.y
        )
    )
    drawText(
        text = fingering.number,
        style = textStyle,
        textMeasurer = textMeasurer,
        topLeft = Offset(
            x = center.x - (textLayoutResult.size.width / 2f),
            y = center.y - (textLayoutResult.size.height / 2f),
        )
    )
}

private fun DrawScope.drawLineNote(
    diagramWidth: Dp,
    stringNoteSize: TextUnit,
    text: String,
    position: Int,
    textMeasurer: TextMeasurer
) {
    drawText(
        text = text,
        style = TextStyle(fontSize = stringNoteSize),
        textMeasurer = textMeasurer,
        topLeft = Offset(
            x = diagramWidth.toPx() * (position / quantityOfStringsFactor) - LetterXCentralize,
            y = 0f
        )
    )
}

private fun DrawScope.drawOpenString(
    position: Int,
    diagramWidth: Dp,
    openRadius: Dp,
    stroke: Dp,
) {
    drawCircle(
        color = Color.Black,
        radius = openRadius.toPx(),
        style = Stroke(width = stroke.toPx(), cap = StrokeCap.Round),
        center = Offset(
            x = diagramWidth.toPx() * (position / quantityOfStringsFactor),
            y = center.y
        )
    )
}

private fun DrawScope.drawMutedString(
    stringMutedStroke: Dp,
    drawMutedSize: Dp,
    center: Offset,
) {
    val size = Size(drawMutedSize.toPx(), drawMutedSize.toPx())
    drawLine(
        color = Color.Black,
        start = Offset(
            x = center.x - size.width / 2f,
            y = center.y - size.height / 2f,
        ),
        end = Offset(
            x = center.x + size.width / 2f,
            y = center.y + size.height / 2f,
        ),
        strokeWidth = stringMutedStroke.toPx(),
        cap = StrokeCap.Round,
    )
    drawLine(
        color = Color.Black,
        start = Offset(
            x = center.x - size.width / 2f,
            y = center.y + size.height / 2f,
        ),
        end = Offset(
            x = center.x + size.width / 2f,
            y = center.y - size.height / 2f,
        ),
        strokeWidth = stringMutedStroke.toPx(),
        cap = StrokeCap.Round,
    )
}

@Preview(showBackground = true)
@Composable
fun GuitarViewPreview() {
    Column {
        ChordsDiagram(
            options = GuitarViewOptions(
                guitarViewSize = GuitarViewSize.Large,
                openStrings = listOf(
                    OpenGuitarString.Fifth,
                    OpenGuitarString.First,
                    OpenGuitarString.Third,
                ),
                chordPositions = listOf(
                    ChordPosition(GuitarFret.First, GuitarString.E2, Fingering.First),
                    ChordPosition(GuitarFret.Second, GuitarString.G, Fingering.Second),
                    ChordPosition(GuitarFret.Fifth, GuitarString.E4, Fingering.Third),
                )
            ),
        )
        Spacer(modifier = Modifier.size(20.dp))
    }
}