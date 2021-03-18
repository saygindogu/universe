# Experiment Setup

Nothing fancy. Not so much controlled environment. I don't know much about JDK optimizations
hence I might be doing something nonsensical. I just compile and run the programs.

Also the programs itself utilizes highly random behavior. But multiple runs yielded similar
results so I did not care much about the seeds etc for fixing the random behavior.

## Machines

### 2019 MBP 16'' Intel i9 32 GB

#### Default JDK

Only default JDK is experimented. You can find the results of this machine in the files with suffix `intex_i9`

### 2020 MBA 13'' Apple M1 32 GB Default

#### Default JDK ( Rosetta )

Default JDK comes with IDEA.
You can find the results of this machine in the files with suffix `rosetta`

#### Microsoft aarch64 JDK for M1 chips ( native )

Jar is created with the default JDK. Run the jar with the native JDK in the tests.
You can find the results of this machine in the files with suffix `rosetta`