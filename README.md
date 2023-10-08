

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
    options = GuitarViewOptions(
        guitarViewSize = GuitarViewSize.Large,
        chordPositions = listOf(
            ChordPosition(
                guitarFret = GuitarFret.First,
                guitarString = GuitarString.E2,
                fingering = Fingering.First,
            ),
            ChordPosition(
                guitarFret = GuitarFret.Second,
                guitarString = GuitarString.G,
                fingering = Fingering.Second,
            ),
            ChordPosition(
                guitarFret = GuitarFret.Third,
                guitarString = GuitarString.E4,
                fingering = Fingering.Third,
            ),
        ),
        openStrings = listOf(
            OpenGuitarString.Fifth,
            OpenGuitarString.First,
            OpenGuitarString.Third,
        ),
    ),
)
```

Result

<img width="300" alt="image" src="https://github.com/Pliniodev/chords-diagram/assets/50078639/39da72f5-9568-4a85-925e-1800fb67f1a7">
