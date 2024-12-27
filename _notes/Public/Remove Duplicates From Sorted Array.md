---
title: Remove Duplicates From Sorted Array
date: 2024-1-31
feed: show
tags: two-pointer medium
leetcode: 80
---

Given an integer array `nums`, sorted in **non-decreasing order**, remove some duplicates *in-place* such that each unique element appears **at most twice.** The relative order of the elements should be kept the same.

Place your answer list containing `k` elements in the first `k` elements of `nums`, and return `k`.

## Input/Output

##### Example 1 
Input:  `nums = [1, 1, 1, 2, 2, 3]` <br>
Output: 5, `nums = [1, 1, 2, 2, 3, _]`

##### Example 2
Input: `nums = [0, 0, 1, 1, 1, 1, 2, 3, 3]` <br>
Output: 7, `nums = [0, 0, 1, 1, 2, 3, 3, _, _]`

## Solution 1: Cache Previous Value

The path to solving this problem involves iterating through `nums` and overwriting some prior values at an offset that increments whenever we encounter a value that should be discarded. (We discard any indices who have two equal values prior). This way, we will only ever be writing to the first `n` values that correspond to the final value of `nums`.

However, this solution runs into problems if overwriting one of the previous values changes the evaluation of the next value.

To track the current index we are evaluating and the index that we will overwrite, we use two local variables, `left` and `right`. To make sure that we don't change the evaluation of a future index, we will pre-evaluate whether the next index should be kept or discarded before moving past the current value of `right`. In the end, we return `left` as the number of non-discarded elements in the array.

Here's one implementation the above logic:

```
class Solution{
	public:
	int removeDuplicates(vector<int>& nums) {
		if(nums.size() <= 2)return nums.size();
		//The position in unique_list we're inserting into
		int left = 2;
		//The position in raw_list we're evaluating
		int right = 2;
		//Do we include (right-1)? or not
		int include_prev = 1;
		if(nums[0] == nums[1] && nums[2] == nums[0]){
			include_prev = 0;
		}
		right++;
		while(right < nums.size()){
			//Is the current location unnecessary? if so, increment right but not left
			if(nums[right-2] == nums[right-1] && nums[right] == nums[right-2]){
				if(include_prev){
				nums[left] = nums[right-1];
				left++;
			}
			include_prev = 0;
			}else{
				if(include_prev){
					nums[left] = nums[right-1];
					left++;
				}
				include_prev = 1;
			}
		right++;
		}
		if(include_prev){
			nums[left] = nums[right-1];
			left++;
		}
		return left;
	}
};
```

## Solution 2: No Lookahead

Solution 1 actually does some unnecessary calculations. The reason for precomputing whether `right` should be kept or ignored was because this evaluation, `nums[right] == nums[right-1] || nums[right] == nums[right-2]`, where all indices have their original values, would evaluate to something different in the case we overwrote `nums[right-2]` with `nums[right-1]` in an earlier iteration.

A much better way to approach this solution is to look back from left, to see if we already have two of the values equal to `nums[right]` in the list to return that we are building. This avoids any caching of values and results in much simpler code. 

Implementing this solution, we get the following code:

```
class Solution {
	public:
	int removeDuplicates(vector<int>& nums){
		int n = nums.size();
		if(n < 3) return n;

		int left = 2;
		for(int right = 2; right < n; ++right){
			if(nums[right] != nums[left-2]){
				nums[left] = nums[right];
				left++;
			}
		}
		return left;
	}
};
```
