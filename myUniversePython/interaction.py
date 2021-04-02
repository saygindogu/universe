from abc import ABC
from particle import Particle
from random import random
from decimal import Decimal

class Interaction(ABC):
    def apply(self, a: Particle, b:Particle):
        pass


class Gravity(Interaction):

    def __init__(self):
        pass

    def apply(self, a: Particle, b: Particle):
        pass


class RandomFundementalPropertyExchange(Interaction):

    def apply(self, a: Particle, b: Particle):
        tot = a.val + b.val
        rand = Decimal(random())
        a.val = tot * rand
        b.val = tot * (1-rand)


