from numpy import *
import random
from math import sqrt

# TODO: Naming rule is bad. Not very software engineering friendly.

# Parameter settings
Sample_Number = 10000
T = 20                          # trading in year
dT = 1 / 250                    # in year (250 trading day per year)
Step_Num = int(T / dT)
Inflation = 3.5 / 100           # inflation rate
Initial_Investment = 100000

mu_Aggressive = 9.4324 / 100
sigma_Aggressive = 15.675 / 100

mu_Conservative = 6.189 / 100
sigma_Conservative = 6.3438 / 100

median = int(Sample_Number * 0.5)
top10 = int(Sample_Number * (1 - 0.1))
bottom10 = int(Sample_Number * (1 -0.9))

# Construct matrix as sample paths
dZ_Aggressive = array([random.gauss(0, 1) for i in range(Sample_Number * Step_Num)]) # Gauss Distribution
dZ_Aggressive.shape = (Sample_Number, Step_Num)

aggressiveFlow = zeros((Sample_Number, Step_Num))
aggressiveFlow[:, 0] = Initial_Investment

for i in range(Step_Num - 1):
    aggressiveFlow[:, i + 1] = aggressiveFlow[:, i] * exp(
        (mu_Aggressive - 0.5 * sigma_Aggressive**2 - Inflation) * dT +
        sigma_Aggressive * sqrt(dT) * dZ_Aggressive[:, i]
    )                   # Weiner Process

# TODO: the generation of sample paths can be generalized. Refactor code is necessary in future.
dZ_Conservative = array([random.gauss(0, 1) for i in range(Sample_Number * Step_Num)]) # Gauss Distribution
dZ_Conservative.shape = (Sample_Number, Step_Num)

conservativeFlow = zeros((Sample_Number, Step_Num))
conservativeFlow[:, 0] = Initial_Investment

for i in range(Step_Num - 1):
    conservativeFlow[:, i + 1] = conservativeFlow[:, i] * exp(
        (mu_Conservative - 0.5 * sigma_Conservative**2 - Inflation) * dT +
        sigma_Conservative * sqrt(dT) * dZ_Conservative[:, i]
    )                   # Weiner Process


# Output result
resultAggressive = aggressiveFlow[:, Step_Num - 1]
resultAggressive.sort()
print("Aggressive 20 year median is: ", resultAggressive[median], end='\n')
print("Aggressive 20 year top 10% is: ", resultAggressive[top10], end='\n')
print("Aggressive 20 year bottom 10% is: ", resultAggressive[bottom10], end='\n')

resultConservative = conservativeFlow[:, Step_Num - 1]
resultConservative.sort()
print("Conservative 20 year median is: ", resultConservative[median], end='\n')
print("Conservative 20 year top 10% is: ", resultConservative[top10], end='\n')
print("Conservative 20 year bottom 10% is: ", resultConservative[bottom10], end='\n')
