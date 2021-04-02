from god import God
if __name__ == '__main__':
    print('Hello Universe...')

    sim = God.create_universe()

    print('Running the sim')
    sim.run(1000)

    print('Done.')