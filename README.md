# universe
A Silly Particle Simulation Software That Doesn't Make Any Sense

## Usage

You'll see 2 directories in this repo. Both share the same main simulation logic (and yes it's duplicated for convenience) `universe` is the main one. It features SpringBoot and JavaFX at the same time. `universe-cli` is the lightweight command line tool (not a tool actually, more of a `command line runner`). You'll need JDK to compile and run both of them.

### universe

run `./gradlew build` (or `gradle.bat build` if you're building in Windows) and wait for the gradle make it's magic for you. Run the jar file with `java -jar <path-to-jar>`

### universe-cli

CLI is harder to compile compared to the Gradle version. The JavaFX libraries doesn't work for the Apple M1 native JDK microsoft provides, hence this is there. 

Please check [Kotlin command line tool](https://kotlinlang.org/docs/command-line.html)
or [IDEA](https://www.jetbrains.com/idea/download) and follow their instructions for compilations.
