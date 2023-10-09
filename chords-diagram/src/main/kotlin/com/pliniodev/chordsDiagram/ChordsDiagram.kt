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

private const val QUANTITY_OF_STRINGS = 6f
private const val QUANTITY_OF_STRINGS_FACTOR = QUANTITY_OF_STRINGS + 1f
private const val QUANTITY_OF_FRETS_FACTOR = 5f
private const val LETTER_X_CENTRALIZER = 12f
private const val LAST_STRING = 6

@Composable
fun ChordsDiagram(
    options: ChordDiagramOptions,
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
                diagramWidth = options.chordDiagramSize.diagramSize.width,
                stringNoteHeight = options.chordDiagramSize.stringNoteSize.height,
                textNoteSize = options.chordDiagramSize.stringNoteSize.textNoteSize,
                textMeasurer = textMeasurer,
            )
        }
        Spacer(modifier = Modifier.height(options.chordDiagramSize.diagramSize.spacer))
        Guitar(
            diagramWidth = options.chordDiagramSize.diagramSize.width,
            diagramHeight = options.chordDiagramSize.diagramSize.height,
            fingeringTextSize = options.chordDiagramSize.fingeringSize.textSize,
            fingeringRadius = options.chordDiagramSize.fingeringSize.fingeringRadius,
            chordPositions = options.chordPositions,
            fretStroke = options.chordDiagramSize.diagramSize.fretStroke,
            nutStroke = options.chordDiagramSize.diagramSize.nutStroke,
            stringStroke = options.chordDiagramSize.diagramSize.stringStroke,
            barChordStroke = options.chordDiagramSize.fingeringSize.barChordStroke,
            textMeasurer = textMeasurer,
        )
        Spacer(modifier = Modifier.height(options.chordDiagramSize.diagramSize.spacer))
        if (options.showStringOpenOrClose) {
            GuitarStringState(
                diagramWidth = options.chordDiagramSize.diagramSize.width,
                stringStateSize = options.chordDiagramSize.stringStateSize,
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
    barChordStroke: Dp,
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
            barChordStroke = barChordStroke,
            fretBoardCentralizer = (diagramHeight.toPx() / (QUANTITY_OF_FRETS_FACTOR * 2)),
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
                    x = diagramWidth.toPx() * ((position) / QUANTITY_OF_STRINGS_FACTOR),
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
    barChordStroke: Dp,
    chordPositions: List<ChordPosition>,
    textMeasurer: TextMeasurer,
    textStyle: TextStyle,
    fretBoardCentralizer: Float,
) {
    chordPositions.forEach { chordPosition ->
        chordPosition.fingerPosition?.let { position ->
            drawFingerCircle(
                fingering = position.fingering,
                center = Offset(
                    x = diagramWidth.toPx() * (position.guitarString.position / QUANTITY_OF_STRINGS_FACTOR),
                    y = (diagramHeight.toPx() * (chordPosition.guitarFret.position / QUANTITY_OF_FRETS_FACTOR))
                            - fretBoardCentralizer,
                ),
                radius = fingeringRadius,
                textStyle = textStyle,
                textMeasurer = textMeasurer,
                textLayoutResult = textMeasurer.measure(position.fingering.number, textStyle)
            )
        }
        chordPosition.barChord?.let { barChord ->
            drawBarChord(
                diagramWidth = diagramWidth,
                diagramHeight = diagramHeight,
                fretBoardCentralizer = fretBoardCentralizer,
                strokeWidth = barChordStroke,
                barChordStringStart = barChord.first,
                barChordStringEnd = barChord.last,
                barChordFretNumber = chordPosition.guitarFret.position,
            )
        }
    }
}

private fun DrawScope.drawBarChord(
    diagramWidth: Dp,
    diagramHeight: Dp,
    strokeWidth: Dp,
    barChordStringStart: Int,
    barChordFretNumber: Int,
    fretBoardCentralizer: Float,
    barChordStringEnd: Int = LAST_STRING,
) {
    drawLine(
        color = Color.Black,
        start = Offset(
            x = diagramWidth.toPx() * (barChordStringStart / QUANTITY_OF_STRINGS_FACTOR),
            y = diagramHeight.toPx() * (barChordFretNumber / QUANTITY_OF_FRETS_FACTOR)
                    - fretBoardCentralizer
        ),
        end = Offset(
            x = diagramWidth.toPx() * (barChordStringEnd / QUANTITY_OF_STRINGS_FACTOR),
            y = diagramHeight.toPx() * (barChordFretNumber / QUANTITY_OF_FRETS_FACTOR)
                    - fretBoardCentralizer
        ),
        strokeWidth = strokeWidth.toPx(),
        cap = StrokeCap.Round
    )
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
            x = diagramWidth.toPx() * (xLinePosition / QUANTITY_OF_STRINGS_FACTOR),
            y = 0f
        ),
        end = Offset(
            x = diagramWidth.toPx() * (xLinePosition / QUANTITY_OF_STRINGS_FACTOR),
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
            x = diagramWidth.toPx() / QUANTITY_OF_STRINGS_FACTOR,
            y = diagramHeight.toPx() * (yLinePosition / QUANTITY_OF_FRETS_FACTOR)
        ),
        end = Offset(
            x = diagramWidth.toPx() - diagramWidth.toPx() / QUANTITY_OF_STRINGS_FACTOR,
            y = diagramHeight.toPx() * (yLinePosition / QUANTITY_OF_FRETS_FACTOR)
        ),
        strokeWidth = strokeWidth.toPx(),
        cap = StrokeCap.Round
    )
    // shadow frets
    drawLine(
        color = Color.DarkGray,
        start = Offset(
            x = diagramWidth.toPx() / 7,
            y = diagramHeight.toPx() * (yLinePosition / QUANTITY_OF_FRETS_FACTOR) - 3f
        ),
        end = Offset(
            x = diagramWidth.toPx() - diagramWidth.toPx() / QUANTITY_OF_STRINGS_FACTOR,
            y = diagramHeight.toPx() * (yLinePosition / QUANTITY_OF_FRETS_FACTOR) - 3f
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
            x = (diagramWidth.toPx() / QUANTITY_OF_STRINGS_FACTOR) - 5f,
            y = diagramHeight.toPx() * (0 / QUANTITY_OF_FRETS_FACTOR) + 3f
        ),
        end = Offset(
            x = diagramWidth.toPx() - (diagramWidth.toPx() / QUANTITY_OF_STRINGS_FACTOR) + 5f,
            y = diagramHeight.toPx() * (0 / QUANTITY_OF_FRETS_FACTOR) + 3f
        ),
        strokeWidth = strokeWidth.toPx(),
        cap = StrokeCap.Butt
    )
    drawLine(
        brush = brush,
        start = Offset(
            x = (diagramWidth.toPx() / QUANTITY_OF_STRINGS_FACTOR) - 5f,
            y = diagramHeight.toPx() * (0 / QUANTITY_OF_FRETS_FACTOR)
        ),
        end = Offset(
            x = diagramWidth.toPx() - (diagramWidth.toPx() / QUANTITY_OF_STRINGS_FACTOR) + 5f,
            y = diagramHeight.toPx() * (0 / QUANTITY_OF_FRETS_FACTOR)
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
        diagramWidth.toPx() - ((diagramWidth.toPx() / QUANTITY_OF_STRINGS_FACTOR) * 2)
    val rectHeight = diagramHeight.toPx()
    drawRect(
        brush = brush,
        size = Size(
            width = rectWidth,
            height = rectHeight
        ),
        topLeft = Offset(
            x = (diagramWidth.toPx() / QUANTITY_OF_STRINGS_FACTOR),
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
            x = diagramWidth.toPx() * (position / QUANTITY_OF_STRINGS_FACTOR) - LETTER_X_CENTRALIZER,
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
            x = diagramWidth.toPx() * (position / QUANTITY_OF_STRINGS_FACTOR),
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
            options = ChordDiagramOptions(
                chordDiagramSize = ChordDiagramSize.Small,
                openStrings = listOf(
                    OpenGuitarString.Fifth,
                    OpenGuitarString.First,
                    OpenGuitarString.Third,
                ),
                chordPositions = listOf(
                    ChordPosition(
                        guitarFret = GuitarFret.First,
                        barChord = 2..6,
                    ),
                    ChordPosition(
                        guitarFret = GuitarFret.Second,
                        fingerPosition = FingerPosition(
                            guitarString = GuitarString.G,
                            fingering = Fingering.Second,
                        )
                    ),
                    ChordPosition(
                        guitarFret = GuitarFret.Third,
                        fingerPosition = FingerPosition(
                            guitarString = GuitarString.D,
                            fingering = Fingering.Third,
                        )
                    ),
                )
            ),
        )
        Spacer(modifier = Modifier.size(20.dp))
    }
}