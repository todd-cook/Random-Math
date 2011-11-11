Random-Math
============

Random-Math is a packaged collection of math functions and random number generators for
seminumerical methods and statistical analysis.

Features
--------

Random-Math provides the following functions:

- [Error function](http://en.wikipedia.org/wiki/Error_function) (max relative error: 2.6607 E-7, max absolute error: 1.3849 E-7)
- [Phi (standard normal CDF)](http://en.wikipedia.org/wiki/Cumulative_distribution_function) (max relative error: 5.1299 E-5, max absolute error: 6.9249 E-8)
- Phi inverse (max relative error: 0.0022, max absolute error: 4.4338 E-4)
- [Gamma](http://en.wikipedia.org/wiki/Gamma_function) (max relative error: 6.4249 E-7, max absolute error: 7062.0)
- Log Gamma (max relative error: 2.3083 E-12, max absolute error: 4.9155 E-11)
- exp(x) - 1 (for small x) (max relative error: 1.6633 E-11, max absolute error: 3.3306 E-16)
- log(n!) Log Factorial (max relative error: 1.6633 E-11, max absolute error: 3.3306 E-16)
- Chi Square Table generator
- (see unit tests for sample data and expected results)

Utility Classes:

- Frequency Map (counting categories made easy)
- RunningStats (calculating mean, standard deviation, variance, harmonic mean, geometric mean)

Random Number Generation:

- Multiply With Carry
- Mersenne Twister
- Linear Congruential

Probability Distributions:

- Uniform
- Normal
- Exponential
- Gamma
- Chi Square
- Inverse Gamma
- Weibull
- Cauchy
- Student T
- Laplace
- Log Normal
- Beta

License
-------

BSD License unless previously licensed source indicates otherwise.

Usage
-----

See the unit test source code for examples of usage and the output for statistics on accuracy.
