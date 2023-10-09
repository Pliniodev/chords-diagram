package com.pliniodev.chordsDiagram

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChordDiagramOptions(
    val chordDiagramSize: ChordDiagramSize = ChordDiagramSize.Medium,
    val chordPositions: List<ChordPosition> = emptyList(),
    val openStrings: List<OpenGuitarString> = emptyList(),
    val variant: ChordDiagramVariant = ChordDiagramVariant.Full,
)

data class ChordPosition(
    val guitarFret: GuitarFret,
    val fingerPosition: FingerPosition? = null,
    val barChord: IntRange? = null,
) {
    init {
        barChord?.let {
            require(it.first in 1..6) { "Bar chord range must be between 1 and 6" }
            require(it.last in 1..6) { "Bar chord range must be between 1 and 6" }
        }
    }
}

data class FingerPosition(
    val guitarString: GuitarString,
    val fingering: Fingering,
)

enum class ChordDiagramSize(
    val diagramSize: DiagramSize,
    val fingeringSize: FingeringSize,
    val stringStateSize: StringStateSize,
    val stringNoteSize: StringNotesSize,
) {
    XSmall(
        diagramSize = DiagramSize.XSmall,
        stringStateSize = StringStateSize.XSmall,
        stringNoteSize = StringNotesSize.XSmall,
        fingeringSize = FingeringSize.XSmall,
    ),
    Small(
        diagramSize = DiagramSize.Small,
        stringStateSize = StringStateSize.Small,
        stringNoteSize = StringNotesSize.Small,
        fingeringSize = FingeringSize.Small,
    ),
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
    val spacer: Dp,
    val fretStroke: Dp,
    val nutStroke: Dp,
    val stringStroke: Dp,
) {
    XSmall(
        width = 100.dp,
        height = 100.dp,
        spacer = 4.dp,
        fretStroke = 1.dp,
        nutStroke = 2.dp,
        stringStroke = 1.dp
    ),
    Small(
        width = 200.dp,
        height = 200.dp,
        spacer = 8.dp,
        fretStroke = 1.dp,
        nutStroke = 2.dp,
        stringStroke = 1.dp
    ),
    Medium(
        width = 300.dp,
        height = 300.dp,
        spacer = 10.dp,
        fretStroke = 2.dp,
        nutStroke = 4.dp,
        stringStroke = 2.dp
    ),
    Large(
        width = 360.dp,
        height = 360.dp,
        spacer = 12.dp,
        fretStroke = 3.dp,
        nutStroke = 6.dp,
        stringStroke = 4.dp,
    )
}

enum class StringNotesSize(val height: Dp, val textNoteSize: TextUnit) {
    XSmall(height = 14.dp, textNoteSize = 12.sp),
    Small(height = 16.dp, textNoteSize = 14.sp),
    Medium(height = 20.dp, textNoteSize = 16.sp),
    Large(height = 30.dp, textNoteSize = 24.sp),
}

enum class StringStateSize(val height: Dp, val stroke: Dp, val mutedSize: Dp, val openRadius: Dp) {
    XSmall(height = 12.dp, stroke = 1.dp, mutedSize = 8.dp, openRadius = 5.dp),
    Small(height = 16.dp, stroke = 1.5.dp, mutedSize = 12.dp, openRadius = 8.dp),
    Medium(height = 20.dp, stroke = 2.dp, mutedSize = 16.dp, openRadius = 12.dp),
    Large(height = 30.dp, stroke = 3.dp, mutedSize = 20.dp, openRadius = 16.dp),
}

enum class FingeringSize(val textSize: TextUnit, val fingeringRadius: Dp, val barChordStroke: Dp) {
    XSmall(textSize = 14.sp, fingeringRadius = 8.dp, barChordStroke = 8.dp),
    Small(textSize = 20.sp, fingeringRadius = 14.dp, barChordStroke = 16.dp),
    Medium(textSize = 24.sp, fingeringRadius = 16.dp, barChordStroke = 24.dp),
    Large(textSize = 40.sp, fingeringRadius = 24.dp, barChordStroke = 36.dp),
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

sealed class ChordDiagramVariant {
    object Full : ChordDiagramVariant()
    data class JustStringState(val inverted: Boolean) : ChordDiagramVariant()
    object Simple : ChordDiagramVariant()
}