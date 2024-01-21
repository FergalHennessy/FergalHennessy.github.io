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


## Solution

TBH i didn't code this but roughly the solution goes like this:

Given an array of these bottom leaves, note that we can always replace the highest value leaf and a neighbor that has value one less with just the neighbor. This is equivalent to taking two leaves and replacing them with their parent to form a new valid complete binary tree. Having done this, we need to somehow radiate outward, replacing children with their parents until we are left with just one node in O(n) time. I'm not sure how to accomplish this, but the below code apparently does it.

```
#include<bits/stdc++.h>
using namespace std;

const int MAXN = 200005;

int n;
int a[MAXN];
int prv[MAXN],nxt[MAXN];
bool in[MAXN];

bool good(int i) {
    if (i < 1 || i > n) {
        return 0;
    }
    return a[prv[i]] == a[i] - 1 || a[nxt[i]] == a[i] - 1;
}
int main(){
    ios::sync_with_stdio(0), cin.tie(0);
    int t; cin >> t;
    while (t--) {
        cin >> n;
        priority_queue<pair<int, int>> pq;
        for (int i = 1; i <= n; i++) {
            prv[i] = i - 1;
            nxt[i] = i + 1;
            in[i] = 0;
            cin >> a[i];
        }
        a[n + 1] = a[0] = -2;
        for (int i = 1; i <= n; i++) {
            if (good(i)) {
                in[i] = 1;
                pq.push({a[i], i});
            }
        }
        while (!pq.empty()) {
            auto [_, i] = pq.top(); pq.pop();
            nxt[prv[i]] = nxt[i];
            prv[nxt[i]] = prv[i];
            if (!in[prv[i]] && good(prv[i])) {
                in[prv[i]]=1;
                pq.push({a[prv[i]], prv[i]});
            }
            if (!in[nxt[i]] && good(nxt[i])) {
                in[nxt[i]]=1;
                pq.push({a[nxt[i]], nxt[i]});
            }
        }
        int mn = n, bad = 0;
        for (int i = 1; i <= n; i++) {
            bad += !in[i];
            mn = min(a[i], mn);
        }
        if (bad == 1 && mn == 0) {
            cout << "YES\n";
        } else {
            cout << "NO\n";
        }
    }
}
```

Runtime analysis: O(n log n)