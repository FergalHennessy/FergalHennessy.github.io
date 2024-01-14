---
title: Candy (Peaks and Valleys)
feed: show
date: 2022-01-14
tags: hard two-pass
leetcode: 135
---
## Candy

There are `n` children standing in a line. Each child is assigned a rating value given in the integer array `ratings`.

You are giving candies to these children subjected to the following requirements:
1. Each child must have at least one candy.
2. Children with a higher rating get more candies than their neighbors.
Return *the minimum number of candies you need to have to distribute the candies to the children*

## Examples

**Input:** `ratings = [1, 0, 2]` --> `5`
**Explanation:** The first, second, and third child can be given 2, 1, and 2 candies respectively
**Input:** `ratings = [1, 2, 2]` --> `4`
**Explanation:** The first, second and third child can be given 1, 2, and 1 candies respectively as it satisfies the above conditions.

### Solution 1: Two Pass Array Construction

In this solution, we create an array `candies` with the same length as 



TODO:
is there a way to do right-aligned comments in code sections?
nicer lc style formatting for code blocks
make a real homepage
