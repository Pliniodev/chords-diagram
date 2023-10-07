package com.pliniodev.guitarview

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GuitarViewOptions(
    val guitarViewSize: GuitarViewSize = GuitarViewSize.Medium,
    val chordPositions: List<ChordPosition> = emptyList(),
    val openStrings: List<OpenGuitarString> = emptyList(),
    val showStringNotes: Boolean = true,
    val showStringOpenOrClose: Boolean = true,
)

data class ChordPosition(
    val guitarFret: GuitarFret,
    val guitarString: GuitarString,
    val fingering: Fingering,
)

enum class GuitarViewSize(
    val diagramSize: DiagramSize,
    val fingeringSize: FingeringSize,
    val stringStateSize: StringStateSize,
    val stringNoteSize: StringNotesSize,
) {
    Medium(
        diagramSize = DiagramSize.Medium,
        stringStateSize = StringStateSize.Medium,
        stringNoteSize = StringNotesSize.Medium,
        fingeringSize = FingeringSize.Medium,
    ),
    Large(
        diagramSize = DiagramSize.Large,
        stringStateSize = StringStateSize.Large,
        stringNoteSize = StringNotesSize.Large,
        fingeringSize = FingeringSize.Large,
    )
}

enum class DiagramSize(
    val width: Dp,
    val height: Dp,
    val fretStroke: Dp,
    val nutStroke: Dp,
    val stringStroke: Dp
) {
    Medium(
        width = 300.dp,
        height = 300.dp,
        fretStroke = 2.dp,
        nutStroke = 4.dp,
        stringStroke = 2.dp
    ),
    Large(width = 400.dp, height = 400.dp, fretStroke = 3.dp, nutStroke = 6.dp, stringStroke = 4.dp)
}

enum class StringNotesSize(val height: Dp, val textNoteSize: TextUnit) {
    Medium(height = 20.dp, textNoteSize = 16.sp),
    Large(height = 30.dp, textNoteSize = 24.sp),
}

enum class StringStateSize(val height: Dp, val stroke: Dp, val mutedSize: Dp, val openRadius: Dp) {
    Medium(height = 20.dp, stroke = 2.dp, mutedSize = 16.dp, openRadius = 12.dp),
    Large(height = 30.dp, stroke = 3.dp, mutedSize = 20.dp, openRadius = 16.dp),
}

enum class FingeringSize(val textSize: TextUnit, val fingeringRadius: Dp) {
    Medium(textSize = 24.sp, fingeringRadius = 16.dp),
    Large(textSize = 40.sp, fingeringRadius = 24.dp),
}

enum class OpenGuitarString(val position: Int) {
    First(position = 1),
    Second(position = 2),
    Third(position = 3),
    Fourth(position = 4),
    Fifth(position = 5),
    Sixth(position = 6),
}

enum class GuitarString(val position: Int, val stringName: String) {
    E4(position = 1, stringName = "E"),
    A(position = 2, stringName = "A"),
    D(position = 3, stringName = "D"),
    G(position = 4, stringName = "G"),
    B(position = 5, stringName = "B"),
    E2(position = 6, stringName = "E"),
}

enum class GuitarFret(val position: Int) {
    First(position = 1),
    Second(position = 2),
    Third(position = 3),
    Fourth(position = 4),
    Fifth(position = 5),
}

enum class Fingering(val number: String) {
    First(number = "1"),
    Second(number = "2"),
    Third(number = "3"),
    Fourth(number = "4"),
}