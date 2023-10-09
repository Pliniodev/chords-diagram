

<div align="center">
    <h3>
      chords-diagram  
    </h3>
<img width="150" alt="image" src="https://github.com/Pliniodev/chords-diagram/assets/50078639/0891c59e-aa5f-4743-a79f-6f886c75a158">

A simple Composable Guitar Chords Diagram 

<br>



[![](https://jitpack.io/v/Pliniodev/chords-diagram.svg)](https://jitpack.io/#Pliniodev/chords-diagram)
    
</div>





quick start

```
maven { url 'https://jitpack.io' }
```
```
dependencies {
    implementation 'com.github.Pliniodev:chords-diagram:1.0.1'
}
```

<h3>Example of use</h3>

```
ChordsDiagram(
    modifier = Modifier.fillMaxWidth(),
    options = ChordDiagramOptions(
        chordDiagramSize = ChordDiagramSize.Medium,
        variant = ChordDiagramVariant.Full,
        openStrings = listOf(
            OpenGuitarString.First,
            OpenGuitarString.Second,
            OpenGuitarString.Third,
            OpenGuitarString.Fifth,
        ),
        chordPositions = listOf(
            ChordPosition(
                guitarFret = GuitarFret.First,
                barChord = 1..6,
            ),
            ChordPosition(
                guitarFret = GuitarFret.Second,
                fingerPosition = FingerPosition(
                    guitarString = GuitarString.B,
                    fingering = Fingering.Second,
                )
            ),
            ChordPosition(
                guitarFret = GuitarFret.Third,
                fingerPosition = FingerPosition(
                    guitarString = GuitarString.D,
                    fingering = Fingering.Fourth,
                )
            ),
            ChordPosition(
                guitarFret = GuitarFret.Third,
                fingerPosition = FingerPosition(
                    guitarString = GuitarString.A,
                    fingering = Fingering.Third,
                )
            ),
        )
    ),
)
```

Result

<img width="300" alt="image" src="https://github.com/Pliniodev/chords-diagram/assets/50078639/78e714ab-7488-40a4-a612-7989521ca063">
