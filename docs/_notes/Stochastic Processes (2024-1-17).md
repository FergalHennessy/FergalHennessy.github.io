---
title: Stochastic Processes (2024-1-17)
feed: show
date: 2024-1-17
---

Instructor: Benson Au <br>
website: <a href="https://www.stat.berkeley.edu/~bensonau/s24.150">stat.berkeley.edu/~bensonau/s24.150</a> <br>
bcourses, Edstem, Gradescope <br>
Optional discussion sections, see poll for times<br>
Hw0 is Gradescope review, HW1 is due Friday 2024-1-26

## Today

Probability Space, Random Variable

Probability Space: $$(\Omega, TF, P)$$ (sample space, algebra of sets, function that assigns likelihoods), where the probability function $$P$$ assigns probability on the space and sums to 1.

Random Variable: some $$X \in \Omega$$

We study the *distribution* of X: $$M_X(A) = P(X\in A)$$.

For "nice" sets $$A \subseteq \mathbb{R}$$. If X is discrete (countably infinite, by the way), we can work with the probability mass function $$P_X(x) = P(X=x)$$ for $$x \in \mathbb{R}$$

if $$X$$ is continuous, work with the *probability density function* of X: $$f_X(x)$$, such that $$\int_Af_X(x) dx = P(X\in A)$$

What about in general? CDF: $$F_X(x) = P(X\in x)$$

### Expectation

If $$X$$ is discrete, $$E[g(X)] = \sum_x g(x)p_X(x)$$ or $$E[g(x)] = \int g(x) f_X(x) dx$$

In general, if $$X \geq 0, E[X] = \int_0^\infty P(X>x)dx = \int_0^\infty P(X\geq x) dx$$

### Stochastic Process

What is a stochastic process? $$X: \Omega -> \mathbb{R}$$

$$X: \Omega \times T -> \mathbb{R}$$, where Time is a function that can be discrete or continuous. $$\omega, t -> X_t(w)$$

Let's say you have a time series $$x_1, x_2, x_3, x_4... $$

Basic question. What do the trajectories look like?

Distribution $$(Y_n)_{n=1}^\infty $$ i.i.d mean $$\mu$$ and variance 1

$$X_n = \frac{Y_1 + ... + Y_n}{n}, (X_n)_{n=1}\infty$$

by LLN, all the trajectories here approach the mean $$\mu$$. The trajectory has a horizontal asymptote at the mean. What about a new trajectory (sequence)?

$$\sqrt{n}(x_n-\mu) \overset{d}{\to} N(0, 1)$$ 

The difference from the moving average to the mean, when scaled by the square root of the number of proceses, approaches the normal distribution. (assuming that the starting variables have variance 1)

If $$X_1... X_n, \quad T = \{1, n\}$$

understand the joint distribution 

$$u_{X_1...X_n}(A_1, ... A_n) = P(X_1\in A_1, X_n \in A_n)$$

in the case of independence, joint probabilities is the same as multiplying the individual probabilities.

Remember: $$P(A \Cap B) = P(A)P(B$$ given $$ A)$$


Note: $$P_{X given Y}(x given y) = P(X=x given Y = y) = \frac{P_{x, y}(x, y)}{P_Y(y)}