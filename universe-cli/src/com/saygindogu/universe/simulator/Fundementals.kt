package com.saygindogu.universe.simulator

import kotlin.random.Random

data class Particle(
    val name: String,
    var position: PlaceVector,
    var fundementalValue: Double,
    var actingForces: Collection<Force>
) {
    fun summary(): String {
        return "name: ${name}\tvalue: ${fundementalValue}\tpos: ${position.value}"
    }

    fun clone(): Particle {
        return Particle("unnamedClone", position.clone(), fundementalValue, listOf())
    }
}

data class Force(
    var direction: DirectionVector,
    var magnitude: Double
)

class Space(
    val dim: Int,
    val size: PlaceVector,
    var particles: MutableList<Particle>
) {
    fun randomDirection(): DirectionVector {
        val vec = mutableListOf<Double>()
        for (i in 0..dim - 1) {
            vec.add(Random.nextDouble() * 2 - 1)
        }
        return DirectionVector(vec)
    }

    fun randomPlace(): PlaceVector {
        val vec = mutableListOf<Int>()
        for (i in 0..dim - 1) {
            vec.add(Random.nextInt(size.value[i]))
        }
        return PlaceVector(vec)
    }

    fun summary(): String {
        return particles.map { it.summary() }.joinToString("\n")
    }

    fun particlesSnapshot(): List<Particle>{
        return particles.map { it.clone() }
    }
}

class PlaceVector(val value: List<Int>) {

    fun clone(): PlaceVector {
        val newList = value.map { it }
        return PlaceVector(newList)
    }

}

class DirectionVector(var value: List<Double>) {

    fun clone(): DirectionVector {
        val newList = value.map { it }
        return DirectionVector(newList)
    }

}