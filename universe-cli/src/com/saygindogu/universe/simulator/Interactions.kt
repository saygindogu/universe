package com.saygindogu.universe.simulator

import kotlin.random.Random

interface Interaction {
    fun apply(particles: List<Particle>): List<Particle>
}

interface SelfInteraction {
    fun apply(particle: Particle): List<Particle>
}

class RandomPropertyExchange : Interaction {
    override fun apply(particles: List<Particle>): List<Particle> {
        val total = particles[0].fundementalValue + particles[1].fundementalValue
        val rand = Random.nextDouble()
        particles[0].fundementalValue = total * rand
        particles[1].fundementalValue = total * (1 - rand)
        return listOf()
    }
}

class BiasedPropertyExchange : Interaction {
    override fun apply(particles: List<Particle>): List<Particle> {
        val particle0 = particles[0]
        val particle1 = particles[1]
        val value0 = particle0.fundementalValue
        val value1 = particle1.fundementalValue

        val total = particle0.fundementalValue + particle1.fundementalValue

        if (particle0.fundementalValue > particle1.fundementalValue) {
            particle0.fundementalValue += value1 * 0.2
            particle1.fundementalValue = total - value0
        } else {
            particle1.fundementalValue += value0 * 0.2
            particle0.fundementalValue = total - value1
        }
        return listOf()
    }
}

class Explode : SelfInteraction {
    override fun apply(particle: Particle): List<Particle> {
        if (particle.fundementalValue > 10.0 && Random.nextDouble() < (particle.fundementalValue / 10.0) - 1) {
            val forces = particle.actingForces.map { generateNewForce(it) }
            particle.fundementalValue *= 0.5
            return listOf(
                Particle(
                    "e_" + particle.name,
                    particle.position.clone(),
                    particle.fundementalValue * 0.5,
                    particle.actingForces.map { generateNewForce(it) }
                )
            )
        }
        return listOf()
    }

    private fun generateNewForce(it: Force): Force {
        it.magnitude *= 0.5
        val originalDirection = it.direction.clone()
        val newDirection = originalDirection.value.map { it * (Random.nextDouble() * 2 - 1) }
        return Force(DirectionVector(newDirection.toMutableList()), it.magnitude * 0.5)
    }
}

class Implode : SelfInteraction {
    override fun apply(particle: Particle): List<Particle> {
        if (particle.fundementalValue < 0.5 && Random.nextDouble() < 0.01) {
            if (particle.fundementalValue < 0) {
                particle.fundementalValue *= -1
            }
            val forces = particle.actingForces.map { generateNewForce(it) }
            val forces2 = particle.actingForces.map { generateNewForce(it) }
            particle.fundementalValue += 5
            particle.actingForces = forces2.toMutableList()
            return listOf(
                Particle(
                    "i_" + particle.name,
                    particle.position.clone(),
                    (particle.fundementalValue + 3) * 2.2,
                    forces.toMutableList()
                )
            )
        }
        return listOf()
    }

    private fun generateNewForce(it: Force): Force {
        val originalDirection = it.direction.clone()
        val newDirection = originalDirection.value.map { it * (Random.nextDouble() * 2 - 1) }
        return Force(DirectionVector(newDirection.toMutableList()), it.magnitude)
    }
}

class Decay: SelfInteraction{
    override fun apply(particle: Particle): List<Particle> {
        if( Random.nextDouble() < 0.75){
            particle.fundementalValue *= (1 - (Random.nextDouble() * 0.07))
        }
        return listOf()
    }

}