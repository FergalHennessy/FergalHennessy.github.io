---
title: Karen and Coffee
feed: show
date: 2024-01-31
tags: easy codeforces-1200 prefix-sum
leetcode: codeforces
---

You know n coffee recipes, and the *i*-th recipe suggests that coffee should be brewed between $l_i$ and $r_i$ degrees, inclusive, to achieve the optimal taste. Karen thinks that a temperature is *admissible* if at least **k** recipes recommend it. If Karen asks *q* questions, each one supposing she only wants to prepare coffee with a temperature between *a* and *b*, inclusive, can you tell her how many admissible integer temperatures fall within the range?

## Input

The first line of input contains three integers *n*, *k* $(1\leq k \leq n \leq 200000)$, and *q* $(\leq k \leq n \leq 200000)$, the number of recipes, the minimum number of recipes a certain temperature must be recommended  by to be admissible, and the number of questions Karen has, respectively.

The next *n* lines describe the recipes. Specifically, the *i*-th line among these contains two integers $l_i$ and $r_i$ $(1 \leq l_i \leq r_i \leq 200000)$, describing that the *i*-th recipe suggests that the coffee be brewed between $l_i$ and $r_i$ degrees, inclusive.

the next *q* lines describe the questions. Each of these lines contains *a* and *b*, $(1\leq a \leq b \leq 200000)$, describing that she wants to know the number of admissible integer temperatures between *a* and *b* degrees, inclusive.

## Output

For each question, output a single integer on a line by itself, the number of admissible integer temperatures between *a* and *b* degrees, inclusive.

##### Example 1:
Input <br>
3 2 4  <br>
91 94  <br>
92 97  <br>
97 99  <br>
92 94  <br>
93 97  <br>
95 96  <br>
90 100 <br>
\_\_\_\_\_\_\_\_\_\_\_\_<br>
Output <br>
3<br>3<br>0<br>4

##### Example 2:
Input<br>
2 1 1<br>
1 1 <br>
200000 200000<br>
90 100<br>
\_\_\_\_\_\_\_\_\_\_\_\_<br>
Output<br>
0

## Solution: Prefix Sum

To solve this problem, we use prefix sums. Because there are up to 200000 queries *q*, it is important that we do as much computation as possible beforehand. To this end, we construct a list of values `ps[200000]`, such that `ps[i]` represents the total number of temperatures \[1, i) that satisfy at least *k* of the conditions.

We can make two passes through `n` to construct this array, one that fills ps with the number of conditions that the temperature corresponding to each element fulfills, and one that sums over these values to create our prefix sum.

Afterwards, we can iterate through `ps`, subracting `ps[start]` from `ps[end]` to find the total number of temperatures in the range satisfying at least `k` of the conditions.

Here's the implementation of this logic:
```
int main(){
	int n, k, q;
	cin >> n >> k >> q;

	int ps[200001] = {0};
	//set boundaries where we start and end satisfying a condition
	while(n--){
		int tstart, tend;
		cin >> tstart >> tend;

		ps[tstart]++;
		ps[tend+1]--;
	}
	int curr = 0;
	//the number of valid temperatures (including the current)
	int totalSatisfying = 0;
	//fill out the prefix sum
	for(int i = 0; i < 200001; ++i){
		curr += ps[i];
		if(curr >= k){
			totalSatisfying++;
		}
		ps[i] = totalSatisfying;
	}
	while(q--){
		int tstart, tend;
		cin >> tstart >> tend;
		cout >> ps[tend] - ps[tstart-1] >> "\n";
	}
	return 0;
}
```

##### Runtime Analysis:
Setting boundaries: *O(n)* <br>
Filling out prefix sum: *O(200000)* <br>
Calculating answers: *O(q)*<br>
Our total runtime is *O(200000 + n + q)*, for a high fixed cost but well-scaling solution.

Space complexity is *O(200000)*, which is also high-inital but well scaled cost.

The main idea behind this problem, and most prefix sum problems in general, is to precompute as much as possible so the linear cost of computation is not passed onto the `q` queries that it is possible to get.