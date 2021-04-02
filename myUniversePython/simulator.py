import timeit

class Simulator:

    def __init__(self, space, interractions):
        self.space = space
        self.interactions = interractions
        self.history_book = open('history.txt', 'w')
        self.interaction_count = 0



    def run(self, iterations=1000, ckpt=100):
        self.start = timeit.timeit()
        self._record()
        for i in range(iterations):
            if i % ckpt == 0:
                self._flush()
            self._tick()
        self._statictics()
        self._close()

    def _statictics(self):
        self.history_book.write(f'intercation count: {self.interaction_count}\n')
        self.history_book.write(f'time it took: {self.start - timeit.timeit()}\n')

    def _close(self):
        self.history_book.close()

    def _flush(self):
        self.history_book.flush()

    def _tick(self):
        pass
        # Move particles
        self._move()
        # Make them interract
        self._interract()
        # Record History
        self._record()

    def _move(self):
        for particle in self.space.particles:
            for force in particle.acting_forces:
                for index in range(len(particle.pos)):
                    movement = force.magnitude * force.direction[index]
                    particle.pos[index] += int(movement)
                    particle.pos[index] %= self.space.size[index]

    def _interract(self):
        for i in range(len(self.space.particles)):
            for j in range(i+1, len(self.space.particles)):
                if(self.space.particles[i].pos == self.space.particles[j].pos):
                    for interraction in self.interactions:
                        self.interaction_count += 1
                        interraction.apply(self.space.particles[i],self.space.particles[j])


    def _record(self):
        lines = []
        self.space.particles.sort(key=lambda x: x.val)
        for particle in self.space.particles:
            lines.append(f'{particle.name}\t{particle.pos}\t{particle.val}\n')
        lines.append('==\n')
        self.history_book.writelines(lines)