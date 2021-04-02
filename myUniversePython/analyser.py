# Import the necessary packages and modules
import matplotlib.pyplot as plt
import numpy as np


f = open('history.txt', 'r')

for line in f:
    if line.startswith('='):

    tokens = line.split('\t')

# Prepare the data
x = np.linspace(0, 10, 100)

# Plot the data
plt.plot(x, x, label='linear')

# Add a legend
plt.legend()

# Show the plot
plt.show()