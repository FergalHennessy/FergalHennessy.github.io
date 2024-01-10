---
title: Longest Valid Parentheses
feed: show
date: 2024-01-09
tags: hard stack dp
leetcode: 32
---

## Longest Valid Parentheses

You are given a string s containing just the characters '(' and ')'.<br>
*return the length of the longest valid (well-formed) parentheses substring*
### Examples
Input =`" (()"` --> 2 <br>
Input = `")()())"` --> 4 <br>
Input = `""` --> 0

## Solution 1: 

Noting that computing the longest substring at any given point in the string requires memory of previous longest substrings, we decide to use dynamic programming. We can determine the longest substring at the ith index with the following recurrence relations:

1. If `s[i] == '('`  longest = 0
2. if `s[i] == ')'`  && `s[i-1] == '('`  longest = `dp[i-2] + 2`
3. if `s[i] == ')'`  &&  `s[i - s[i-1] -1] == '('`  longest = `dp[i-1] + dp[i-dp[i-1] - 2] + 2`

where equation 3 uses the previously computed longest strings to determine whether a large containing set of parentheses exists

Using these recurrence equations, dynamic programming, and some safeguards to out of bounds errors, we write the following code:

```
class Solution{
public:
	int longestValidParentheses(string s){
		int dp[s.size()];
		int res = 0;
		for(int i = 0; i < s.size(); ++i){
			if(s[i] == '(') dp[i] = 0;
			else if(s[i] == ')' && i > 0 && s[i-1] == '(') 
				dp[i] = (i-2 >= 0 ? dp[i-2] : 0) + 2;
			else if(s[i] == ')' && i > 0 && dp[i-1] + 1 <= i && 
					s[i-dp[i-1]-1] == '(')
			dp[i] = dp[i-1] + (i - dp[i-1] - 2 >= 0 ? dp[i-dp[i-1]-2] : 0) + 2;
			else dp[i] = 0;
			res = max(res, dp[i]);
		}
		return res;
	}
};
```

## Solution 2:

Noting that, generally speaking, we need to store only the indexes of most recent open parentheses and any orphan close parentheses that break up substrings, it looks like we can use a stack for this problem. In general, there are three cases:

1. If `str[i] == '('` push i to stack
2. if `str[i] == ')'`  && `s[stack.top] == '('` pop from stack
3. else push i to stack (`s[i]` is an orphan)

Afterwards, if the stack is not empty, we pop from stack and return the longest sequence.
Adding some edge cases and then typing this up, we obtain

```
class Solution{
public:
	int longestValidParentheses(string s){
		stack<int> st;
		int res = 0;
		for(int i = 0; i < s.size(); ++i){
			if(s[i] == '(') st.push(i);
			else if(s[i] == ')' && !st.empty() && s[st.top()] == '(') st.pop();
			else st.push(i);
		}
		if(st.empty()) return s.size();
		st.push(s.size());
		while(!st.empty()){
			int curr = st.top();
			st.pop();
			if(!st.empty()) res = max(res, curr - st.top() - 1);
			else{res = max(curr, res);}
		}
		return res;
	}
};
```