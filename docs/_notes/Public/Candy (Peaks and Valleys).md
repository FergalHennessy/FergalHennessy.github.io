---
title: Candy (Peaks and Valleys)
feed: show
date: 2024-01-14
tags: hard two-pass greedy
leetcode: 135
---
## Candy

There are `n` children standing in a line. Each child is assigned a rating value given in the integer array `ratings`.

You are giving candies to these children subjected to the following requirements:
1. Each child must have at least one candy.
2. Children with a higher rating get more candies than their neighbors.
Return *the minimum number of candies you need to have to distribute the candies to the children*

## Examples

**Input:** `ratings = [1, 0, 2]` --> `5` <br>
**Explanation:** The first, second, and third child can be given 2, 1, and 2 candies respectively. <br>
**Input:** `ratings = [1, 2, 2]` --> `4` <br>
**Explanation:** The first, second and third child can be given 1, 2, and 1 candies respectively as it satisfies the above conditions.

### Solution 1: Two Pass Array Construction
There are two conditions on each of the childrens' candy amounts: One from the left, and one from the right. To solve the problem, we use two passes: one to ensure that the candy distribution satisfies the minimum demand from the left, and one to ensure that the candy distribution satisfies the minimum demand from the right (while also satisfying the left side's requirements). The algorithm has three steps:
1. Create a new array `candies` with the same length as `ratings` and set `candies[0]=1`
2. Iterate through `ratings` and `candies` from the left: if `ratings[i] > ratings[i-1]` then `candies[i] = candies[i-1] + 1`
3. Iterate through `ratings` and `candies` from the right: if `ratings[i] > ratings[i+1]` then `candies[i] = std::max(candies[i], candies[i+1]+1)`
Coding this up, we get the following solution:
```
class Solution{
public:
	int candy(std::vector<int>& ratings) {
        int n = ratings.size();
        int candies[n];
        candies[0] = 1;
	    for(int i = 1; i < n; i++){
		    if(ratings[i-1] < ratings[i]) candies[i] = candies[i-1]+1;
		    else candies[i] = 1;
	    }
	    int sum = candies[n-1];
	    for(int i = n-2; i >=0; i--){
		    if(ratings[i] > ratings[i+1]) 
			    candies[i] = std::max(candies[i], candies[i+1] + 1);
			sum += candies[i];
	    }
	    return sum;
    }
};
```

##### Runtime Analysis:
- In this function, there are two loops, both of which perform `n` linear operations. The time complexity is `O(n)`.
- There is one array created with size `n`. The space complexity is `O(n)`. 

## Solution 2: One Pass Greedy Algorithm

We have a solution that uses linear space, but can we do better? 



```
class Solution {
public:
    int candy(vector<int>& ratings) {
		int n = ratings.size();
		int candy = n, i=1;
		while(i<n){
			if(ratings[i] == ratings[i-1]){
				i++;
				continue;
			}
			//For increasing slope
			int peak = 0;
			while(ratings[i] > ratings [i-1]){
				peak++;
				candy += peak;
				i++;
				if(i == n) return candy;
			}
			//For decreasing slope
			int valley = 0;
			while(i<n && ratings[i] < ratings[i-1]){
				valley++;
				candy += valley;
				i++;
			}
			candy -= min(peak, valley); //Keep only the higher peak
		}
		return candy;
	}
};

```