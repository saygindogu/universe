package com.saygindogu.universe

import com.saygindogu.universe.simulator.*
import kotlin.random.Random

class SimulationInitializer {

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
}