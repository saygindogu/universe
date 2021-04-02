from decimal import Decimal


class Force:
    def __init__(self, magnitude: Decimal, direction: list):
        self.magnitude = magnitude
        self.direction = direction