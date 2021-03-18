package com.saygindogu.universe.simulator

import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.random.Random
import kotlin.system.measureNanoTime


class Simulator(val space: Space, val interactions: List<Interaction>, val selfInteractions: List<SelfInteraction>) {

    val statistics = Statistics(0)

    lateinit var executor: ExecutorService

    lateinit var file: FileOutputStream
    var maxParticleCount = 0

    fun recordedRun(iterations: Int, callback: (progress: Statistics) -> Unit) {
        for (i in 1..iterations) {
            if (Thread.interrupted()) {
                println("I'm interrupted!!")
                break
            }
            tick(i, iterations)
            record()
            if (i % 10 == 0) {
                callback(statistics)
            }
            if (i % 100 == 0) {
                file.flush()
            }
        }
    }

    private fun dryRun(iterations: Int, callback: (progress: Statistics) -> Unit) {
        for (i in 1..iterations) {
            if (Thread.interrupted()) {
                println("I'm interrupted!!")
                break
            }
            tick(i, iterations)
            if (i % 100 == 0) {
                callback(statistics)
            }
        }
    }

    fun run(
        iterations: Int = 1000, fileName: String, benchmarking: Boolean, callback: (progress: Statistics) -> Unit
    ) {
        executor = Executors.newFixedThreadPool(64)
        file = FileOutputStream(File(fileName))
        val elapsed = measureNanoTime {
            if (benchmarking) dryRun(iterations, callback) else recordedRun(iterations, callback)
        }
        file.write("Max Particle Count: ${statistics.maxParticleCount}".toByteArray())
        file.write("Elapsed Time: ${elapsed / 1e9} secs".toByteArray())
        file.close()
        executor.shutdown()
        executor.awaitTermination(3, TimeUnit.SECONDS)
    }

    private fun tick(i: Int, iterations: Int) {
        val snapshot = space.particlesSnapshot()
        move()
        interact(snapshot)
        selfInterract()
        noteStatistics(i, iterations)
        cleanup()
    }


    private fun cleanup() {
        if( space.particles.size > 150 ){
            space.particles = space.particles.filter { Random.nextBoolean() }.toMutableList()
        }
        space.particles = space.particles.filter { it.fundementalValue > 1e-2 }.toMutableList()
    }

    private fun noteStatistics(i: Int, iterations: Int) {
        statistics.progress = i.toDouble() / iterations.toDouble()
        statistics.maxParticleCount = if (statistics.maxParticleCount < space.particles.size) space.particles.size else statistics.maxParticleCount
        statistics.particleCount = space.particles.size
    }

    private fun record() {
        file.write(space.summary().toByteArray())
        file.write("\n".toByteArray())
        file.write(statistics.summary().toByteArray())
        file.write("\n==\n".toByteArray())
    }

    private fun move() {
        val taskList = mutableListOf<Callable<Unit>>()
        for (particle in space.particles) {
            taskList.add(Callable {
                for (force in particle.actingForces) {
                    particle.position = PlaceVector(particle.position.value.mapIndexed { index, it ->
                        val movement = force.magnitude * force.direction.value[index]
                        (it + movement.toInt()) % space.size.value[index]
                    })

                }
            })
        }
        executor.invokeAll(taskList).map { it.get() }
    }

    private fun normalizedDirection(particle: Particle, particle1: Particle): DirectionVector {
        val vector = particle1.position.value.mapIndexed { index, value ->
            particle.position.value[index] - value
        }
        val absVector = vector.map { abs(it) }
        val maximumElement = absVector.max()!!.toDouble()
        val minimumElement = absVector.min()!!.toDouble()
        val normalizedVector = vector.map { it.toDouble() / (maximumElement - minimumElement) }
        return DirectionVector(normalizedVector.toMutableList())
    }

    private fun eucledianDistSquare(particle: Particle, particle1: Particle): Double {
        return particle1.position.value.mapIndexed { index, value ->
            (particle.position.value[index] - value) * (particle.position.value[index] - value)
        }.reduce { i, j -> i + j }.toDouble()
    }

    private fun interact(snapshot: List<Particle>) {
        val taskList = mutableListOf<Callable<List<Particle>>>()
        for (i in 0..snapshot.size - 1) {
            for (j in i + 1..snapshot.size - 1) {
                if (snapshot[i].position.value == snapshot[j].position.value) {
                    for (interraction in interactions) {
                        statistics.interactionCount += 1
                        taskList.add(Callable { interraction.apply(listOf(space.particles[i], snapshot[j])) })
                    }
                }
            }
        }
        space.particles.addAll(executor.invokeAll(taskList).flatMap { it.get() })
    }

    private fun selfInterract() {
        val taskList = mutableListOf<Callable<List<Particle>>>()
        space.particles.forEach {
            for (selfInterraction in selfInteractions) {
                taskList.add(Callable { selfInterraction.apply(it) })
            }
        }
        space.particles.addAll(executor.invokeAll(taskList).flatMap { it.get() })
    }
}