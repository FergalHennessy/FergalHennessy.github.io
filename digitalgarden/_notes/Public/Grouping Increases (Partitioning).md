---
title: Grouping Increases
feed: show
date: 2024-1-18
tags: hard greedy dp codeforces-1400
leetcode: codeforces
---
You are given an array `a` of size `n`. You will do the following process to calculate your penalty:
1. Split array $$a$$ into two (possibly empty) subsequences $$s$$ and $$t$$ such that every element of $$a$$ is either in $$s$$ or $$t$$.
2. For an array $$b$$ of size $$m$$, define the penalty $$p(b)$$ of an array $$b$$ as the number of indices $$i$$between 1 and $$m-1$$ where $$b_i < b_{i+1}$$
3. The total penalty you will receive is $$p(s) + p(t)$$.

If you perform the above process optimally, find the minimum possible penalty you will reveive.

a sequence a is a *subsequence* of a sequence b if a can be obtained from $$b$$ by the deletion of several (possibly, zero or all) elements.

### Example
Some valid ways to split array `a = [3, 1, 4, 1, 5]` into `(s, t)` <br>
`([3, 4, 1, 5], [1])`, <br>
`([1, 1],[3, 4, 5])`, <br>
`([],[3, 1, 4, 1, 5])` <br>
while some invalid ways to split `a` are <br>
`([3, 4, 5], [1])`,<br>
`([3, 1, 4, 1], [1, 5])`,<br>
`([1, 3, 4], [5, 1])`

### Input

Each input contains multiple test cases. the first line contains a single integer $$t: (1 \leq t \leq 10^4)$$ -- the number of test cases. The description of the test cases follows.

The first line of each test case contains a single integer $$n: (1\leq n \leq 2\cdot 10^5)$$ -- the size of the array $$a$$.

The second line contains $$n$$ integers $$a_1, a_2, ..., a_n$$ : the elements of the array $$n$$.

It is guaranteed that the sum of $$n$$ over all test cases does not exceed $$10^5$$.

### Output

For each test case, output a single integer representing the minimum possible penalty you will receive.

## Solution 1: 

Any increasing subsequences in these subsequences will result in a penalty. Note that for both arrays, we care only about the back element at any given time, as it is the only element that could actually impact the score. Let the two back elements be $$x$$ and $$y$$, and without loss of generality let $$x > y$$. In the case where the arrays are empty, let the back element $$= \infty$$. Let the current element from input be $$a$$

Because there are many binary choices, where one option is totally outclassed by another, we are inspired to use a greedy algorithm. Accordingly, we break the problem into three cases.

Case 1: $$x \geq y \geq a$$: In this case,  we may insert $$a$$ on top of $$x$$ or $$y$$, without incurring any penalty. Inserting on $$x$$ will leave two elements $$(a, y)$$, inserting on $$y$$ will leave two elements $$(x, a)$$. Given these two choices, it is always better to insert over $$y$$, as $$(x, a)$$ always gives us more freedom to insert more elements without penalty.

Case 2: $$a \geq x \geq y$$: In this case, we may insert $$a$$ on top of $$x$$ or $$y$$, incurring a penalty of 1 no matter what we do. Inserting on $$x$$ will leave two elements $$(a, y)$$, inserting on $$y$$ will leave two elements $$(x, a)$$. Given these two choices, it is always better to insert over $$y$$, as $$(x, a)$$ always gives us more freedom to insert more elements without penalty.

Case 3: $$x > a > y$$: In this case, we may insert $$a$$ on top of $$x$$ or $$y$$.<br>
We have the option of $$(x, a)$$ with penalty 1, or $$(a, y)$$ with penalty 0. Call the next item that is inserted into this pair that is not inserted over $$a$$ to be $$b$$. There are three subcases here:

(1) If the next item not inserted over a in this pair is $$b > x$$, choice 1 will have cost you    two penalty, choice 2 will have cost you 1 penalty, and the state of the arrays is now the same.

(2) If the next item not inserted over a in this pair is $$x > b > y$$, choice 1 will have cost you one penalty, choice 2 will have cost you one penalty, and the state of the arrays is now the same.

(3) If the next item not inserted over a in this pair is $$y > b$$, choice 1 will have cost you one penalty, choice 2 will have cost you 0 penalty, and the state of the arrays is now the same.

In all cases, it is at least as good to insert $$a$$ over the larger element $$x$$ for no penalty. This gives us a pretty simple set of decisions to make in any situation. Let's code up this solution:

```
#include "bits/stdc++.h"
using namespace std;

int t;
int main(){
	cin >> t;
	while(t--){
		int n;
		cin >> n;
		int bigger = INT_MAX;
		int smaller = INT_MAX;
		int current;
		int pen = 0;
		while(n--){
			cin>>current;	
			if(current <= smaller){
				smaller = current;
			}else if(current > bigger){
				smaller = current;
				pen++;
			}else{
				bigger = current;
			}
			if(bigger < smaller){
				std::swap(bigger, smaller);
			}
		}
		cout << pen << "\n";
	}
	return 0;
}
```

##### Runtime Analysis:
This function iterates through the list one time, doing a constant number of calculations, the runtime is $$O(n)$$.
