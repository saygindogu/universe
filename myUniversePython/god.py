from decimal import Decimal

from force import Force
from random import random
from interaction import RandomFundementalPropertyExchange
from space import Space
from particle import Particle
from simulator import Simulator

# Tovbe
class God:

    @staticmethod
    def create_universe() -> Simulator:
        space = Space(3, [10,10,10])

        for i in range(40):
            p = Particle(i, space.random_place(), Decimal(random()))
            for j in range(2):
                p.acting_forces.append(Force(Decimal(random()*3), space.random_direction()))
            space.add_particle(p)
        simulator = Simulator(space, [RandomFundementalPropertyExchange()])
        return simulator