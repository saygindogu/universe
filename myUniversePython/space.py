from particle import Particle
from random import randint, random
from decimal import Decimal

class Space:

    def __init__(self, dim: int = 1, size: list = [10]):
        self.dim = dim
        self.size = size
        self.particles = []

    def add_particle(self, particle: Particle):
        self.particles.append(particle)

    def random_place(self):
        pos = []
        for i in range(self.dim):
            pos.append(randint(0, self.size[i]))
        return pos

    def random_direction(self):
        pos = []
        for i in range(self.dim):
            pos.append(Decimal(random()))
        return pos
