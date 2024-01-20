---
title: 01 Tree
feed: show
date: 2024-01-19
tags: hard tree greedy leetcode-2100
---

There is an edge-weighted complete binary tree with *n* leaves. A complete binary tree is defined as a tree where every non-leaf vertex has exactly 2 children. For each non-leaf vertex, we label one of its children as the left child, and the other as the right child.

The binary tree has a very strange property. For every non-leaf vertex, one of the edges to its children has weight 0 while the other edge has weight 1. Note that the edge with weight 0 can be connected to either its left or right child.

You forgot what the tree looks like, but luckily, you still remember some information about the leaves in the form of an array *a* of size *n*. For each *i* from 1 to *n*, $$a_i$$ represents the distance from the root to the *i*-th leaf in dfs order. Determine whether there exists a complete binary tree which satisfies array *a.*

The **dfs order** of the leaves traverses them in left to right order recursively.

The **distance** from vertex *u* to vertex *v* is defined as the sum of weights of the edges on the path from vertex *u* to vertex *v*.

### Example:

#### Input:
2 <br>
5 <br>
2 1 0 1 1 <br>
5 <br>
1 0 2 1 3 <br>

#### Output:
YES<br>
NO

In the second test case, it can be proven that there is no complete binary tree that satisfies the array.
