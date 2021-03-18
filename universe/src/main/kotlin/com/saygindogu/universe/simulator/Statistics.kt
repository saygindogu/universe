package com.saygindogu.universe.simulator

data class Statistics(var interactionCount: Int) {
    var progress: Double = 0.0
    var particleCount: Int = 0
    var maxParticleCount: Int = 0

    fun summary(): String {
        return "progress: ${(progress * 100).toInt()} %" +
                "\nTotal Number of Interactions: ${interactionCount}" +
                "\nCurrent Particle Count: ${particleCount}" +
                "\nPeak Particle Count: ${maxParticleCount}"
    }
}