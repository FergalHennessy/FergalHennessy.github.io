---
title: Gas Station
feed: show
Date: 2024-16-1
tags: medium array greedy
---

There are `n` gas stations along a circular route, where the amount of gas at the `i'th` station is `gas[i]`.

You have a car with an unlimited gas tank, and it costs `cost[i]` to travel from the `ith` station to its next `i+1`th station. You begin the journey with an empty tank, at one of the gas stations.

Given two integer arrays `gas` and `cost`, return *the starting gas station's index if you can travel around the circuit once in the clockwise direction (stopping at every station). Otherwise, return -1.* (If there exists a solution, it is guaranteed to be unique.)

## Examples

**Input:** `gas = [1, 2, 3, 4, 5]`, `cost = [3, 4, 5, 1, 2]` <br>
**Output:** 3 <br>
**Explanation:** Beginning at index 3, we will have the following amounts of gas in the tank after arriving at each station: `[3, 6, 4, 2, 0]`. Because we always have a non-negative amount of gas, index 3 satisfies our conditions.

**Input:** `gas = [2, 3, 4]`, `cost = [3, 4, 3]` <br>
**Output:** -1 <br>
**Explanation:** Because it costs more to travel to every station than it is possible to obtain gas, no starting index will satisfy the conditions.

## Solution 1: Brute Force

It is possible to brute force this problem. We could try beginning our trip at each of the `n` stations, and do `n` calculations to determine whether the starting index is valid. This would take $$O(n^2)$$ time.

## Solution 2: Stay Positive

This is a trick question. Any solution must involve a non-negative amount of gas in the tank at every single gas station the car visits. The key insight to this question is that any non-solution will have a negative amount of gas in the tank at some station along the way.

Therefore, if we find that starting at `a` is a non-solution because we will have negative gas in the tank after leaving station `b`, then no starting index in the range `[a, b]` can be a solution, as starting there will provide equivalent or less gas when you eventually leave station `b`. By not considering these indices, we eliminate many redundant calculations.

Putting our solution into code, we get something like this:

```
class Solution{
public:
	int canCompleteCircuit(vector<int>& gas, vector<int>& cost){
		int total;
		int n = gas.size();
		for(int i = 0; i < n;){
			total = 0;
			for(int j = 0; j < n; ++j){
				total += gas[(i+j) % n] - cost[(i+j) % n];
				if(total < 0){
					i += j+1;
					break;
				}
				if(j == (n-1)) return i;
			}
		}
		return -1;
	}
}
```

##### Runtime Analysis:
- Using a greedy algorithm, we consider each starting index exactly once. Our solution has $$O(n)$$ time complexity.
- Space Complexity: $$O(1)$$