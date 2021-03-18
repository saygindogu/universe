import com.saygindogu.universe.simulator.*
import kotlin.random.Random

fun main(args: Array<String>) {
    println("Running with \"${args[0]}_ ${args[1]}\"")
    val sim = initializeSim()
    val elapsed = sim.run(
        args[0].toInt(),
        "${args[0]}_ ${args[1]}",
        true
    ) { stats ->
       //println(stats.summary())
    }
    println("Elapsed Time: ${elapsed / 1e9} secs")
}

fun initializeSim(): Simulator {
    val space = Space(3, PlaceVector(mutableListOf(30,30,30)), mutableListOf())
    for (i in 1..10) {
        space.particles.add(
            Particle(
                i.toString(),
                space.randomPlace(),
                Random.nextDouble() * 3 + 5,
                mutableListOf(
                    Force(space.randomDirection(), Random.nextDouble() * 2),
                    Force(space.randomDirection(), Random.nextDouble() * 2)
                )
            )
        )
    }
    return Simulator(space, listOf(BiasedPropertyExchange()), listOf(Explode(), Implode(), Decay()))
}