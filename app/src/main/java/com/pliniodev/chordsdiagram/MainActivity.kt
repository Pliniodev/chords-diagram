package com.pliniodev.chordsdiagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.pliniodev.chordsdiagram.ui.theme.ChordsDiagramTheme
import com.pliniodev.guitarview.ChordPosition
import com.pliniodev.guitarview.Fingering
import com.pliniodev.guitarview.GuitarFret
import com.pliniodev.guitarview.GuitarString
import com.pliniodev.chordsDiagram.ChordsDiagram
import com.pliniodev.guitarview.GuitarViewOptions
import com.pliniodev.guitarview.GuitarViewSize
import com.pliniodev.guitarview.OpenGuitarString

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChordsDiagramTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChordsDiagram(
                        modifier = Modifier.fillMaxWidth(),
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
                                ChordPosition(GuitarFret.Third, GuitarString.E4, Fingering.Third),
                            )
                        ),
                    )
                }
            }
        }
    }
}