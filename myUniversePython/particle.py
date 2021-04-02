from decimal import Decimal

class Particle:

    def __init__(self, name, pos: list=[], fundemental_value: Decimal = Decimal('1.0')):
        self.name = name
        self.pos = pos
        self.val = fundemental_value
        self.acting_forces = []