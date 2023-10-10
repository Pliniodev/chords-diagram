package com.pliniodev.chordsdiagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pliniodev.chordsDiagram.ChordDiagramOptions
import com.pliniodev.chordsDiagram.ChordDiagramSize
import com.pliniodev.chordsDiagram.ChordPosition
import com.pliniodev.chordsDiagram.ChordsDiagram
import com.pliniodev.chordsDiagram.FingerPosition
import com.pliniodev.chordsDiagram.Fingering
import com.pliniodev.chordsDiagram.GuitarFret
import com.pliniodev.chordsDiagram.GuitarString
import com.pliniodev.chordsDiagram.OpenGuitarString
import com.pliniodev.chordsdiagram.ui.theme.ChordsDiagramTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChordsDiagramTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChordsDiagram(
                        modifier = Modifier.fillMaxWidth(),
                        options = ChordDiagramOptions(
                            chordDiagramSize = ChordDiagramSize.XSmall,
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
                                )
                            )
                        )
                    )
                }
            }
        }
    }
}
