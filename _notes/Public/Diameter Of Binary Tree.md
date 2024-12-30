---
title: Diameter Of Binary Tree
feed: show
date: 11-09-2024
tags:
  - easy
  - tree
  - dfs
  - long-test-tag
leetcode: medium
---


### Diameter Of a Binary Tree

Given the `root` of a binary tree, return *the length of the **diameter** of the tree.*

The **diameter** of a binary tree is the **length** of the longes path between any two nodes in a tree. This path may or may not pass through the `root`.

#### Examples:

Example 1:

Input: `root = [1, 2, 3, 4, 5]`

Output: `3`

Example 2:

Input: `root = [1, 2]`

Output: `1`

here trees are represented in array in a bfs order where all members of layer n are listed before all members of layer n+1

### Solution 1: Naive Recursive

We implement a recursive solution where the only parameter is the root node. Note that in our main funciton, we need to track the longest diameter of the left child and the longest diameter of the right child.

To help with our solution, we implement two helper functions which are also recursive.

Implementation:
```
class Solution{
private:
	int longestLeftPath(TreeNode* root){
		if(root == nullptr || root->left == nullptr){
			return 0;
		}
		return 1 + max(longestLeftPath(root->left), longestRightPath(root->right));
	}
	int longestRightPath(TreeNode* root){
		if(root == nullptr || root->right == nullptr){
			return 0;
		}
		return 1 + max(longestLeftPath(root->left), longestRightPath(root->right));
	}
public:
	int diameterOfBinaryTree(TreeNode* root){
		if(root == nullptr) return 0;
		return max(longestLeftPath(root) + longestRightPath(root),
			max(diameterOfBinaryTree(root->left), diameterOfBinarytree(root->right))
		);
	}
}
```

##### Complexity analysis:

Time Complexity: 

The time complexity of `longestLeftPath` and `longestRightPath` is $$O(n)$$ because we do $$o(1)$$ work per call and have a max of n calls.

The time complexity of `diameterOfBinaryTree` is $$O(n)$$ time for the `longestLeftPath` and `longestRightPath` calls and because of recursion, these calls can happen a maximum of $$n$$ times.

So the overall time complexity is $$O(n^2)$$ due to our helper functions.

The overall space complexity is $$O(n^2)$$ because recursive calls happen a maximum of $$n^2$$ times in total.

### Solution 2: Updating value while recursing

To save runtime, we combine our previous functions into one recursive function. However, we need some form of memory, because it may be the case that the left height + the right height is less than the diameter of either the left subtree or the right subtree alone. Therefore, we will carry our best previous diameter within a pointer `diameter` and update it whenever we have a new greatest diameter.

Implementation:

```
class Solution{
private:
	//height measures the distance from the root to a leaf
	int heightWithDia(TreeNode* root, int& diameter){
		if(root=nullptr) return 0;
		int lh = heightWithDia(TreeNode* root->left,  diameter);
		int rh = heightWithDia(TreeNode* root->right, diameter);
		//crucial to have a max here, because in some cases lh will be 0 or 1 while lh or rh of the children is high.
		diameter = max(diameter, lh + rh);
		return max(lh + 1, rh + 1);
	}
public:
	int diameterOfBinaryTree(TreeNode* root){
		int dia = 0;
		heightWithDia(root, dia);
		return dia;
	}
}
```

##### Complexity Analysis

Time Complexity:

The time complexity of `heightWithDia` is $$O(n)$$. We do $$O(1)$$ work a maximum of $$n$$ times.

The overall time and space complexity is $$O(n)$$ because all we have to do is initialize and return a variable `dia`.