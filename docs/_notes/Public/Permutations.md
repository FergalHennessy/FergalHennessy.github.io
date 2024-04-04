---
title: Permutations
feed: show
date: 2024-04-3
tags: hard hashmap sliding-window
leetcode: 46
---

## Permutations

Given an array `nums` of distinct integers, return *all the possible permutations.* You can return the answer in any order.

### Example:  

\\\( \[1, 2, 3\] \to \[\[1, 2, 3], \[1, 3, 2], \[2, 1, 3\], \[2, 3, 1\], \[3, 1, 2\], \[3, 2, 1\]\]\\\)

## Solution 1: Naive

We can do a DFS and at each node, pass the current state of the list down to all children by value. This will create a copy of the list local to that child only, which can be modified and then passed to the children's children.

Code:
```
class Solution {
public:
    vector<vector<int>> permute(vector<int>& nums) {
        unordered_set<int> seen;
        vector<int> currentList;
        return permuteWithSet(nums, currentList, seen);
    }

    vector<vector<int>> permuteWithSet(vector<int>& nums,
                                       vector<int> currentList,
                                       unordered_set<int>& seen) {
        vector<vector<int>> res;
        if (currentList.size() == nums.size()) {
            res.push_back(currentList);
            return res;
        }
        for (int i : nums) {
            if (seen.find(i) != seen.end())
                continue;
            seen.insert(i);
            currentList.push_back(i);
            for (vector<int> j : permuteWithSet(nums, currentList, seen)) {
                res.push_back(j);
            }
            seen.erase(i);
            currentList.erase(currentList.end() - 1);
        }
        return res;
    }
};
```

Time Complexity: O(n!) calls, and because we pass by value, each of the n! calls has to copy and destroy n spaces in memory

Space Complexity: O(n!) space, and because we pass by value, there's additional overhead in the function frames.

## Solution 2: Pass by Reference

In this solution, we take a different approach. For each array in nums, insert it into all the positions it could possibly go in the current array we have built. Then, return all the possible lists created at the end. We pass by reference, and return only on the base case. Here is the code:

```
class Solution {
public:
    vector<vector<int>> permute(vector<int>& nums) {
        vector<vector<int>> res;
        vector<int> curr;
        DFS(res, nums, 0, curr);
        return res;
    }

    void DFS(vector<vector<int>>& res, vector<int>& nums, int index, vector<int>& curr){
        if(index == nums.size()){
            res.push_back(curr);
            return;
        }
        for(int i = 0; i <= curr.size(); i++){
            curr.insert(curr.begin() + i, nums[index]);
            DFS(res, nums, index+1, curr);
            curr.erase(curr.begin() + i);
        }
        return;
    }
};
```
Time Complexity: O(n!) calls but save operations in the call stack

Space Complexity: O(n!) space, but save a lot of space in the call stacks.

