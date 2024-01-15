---
title: Merge K Lists
feed: show
date: 2024-01-08
tags: hard heap queue divConq
leetcode: 23
---

## Merge K Lists
You are given an array of k linked-lists `lists`, where each linked list is sorted in ascending order.
*merge all the linked-lists into one sorted linked-list and return it*

```
/**
* Definition for singly-linked list.
* struct ListNode {
*     int val;
*     ListNode *next;
*     ListNode() : val(0), next(nullptr) {}
*     ListNode(int x) : val(x), next(nullptr) {}
*     ListNode(int x, ListNode *next) : val(x), next(next) {}
* };
*/
```

#### Examples:
lists = `\[[1, 4, 5], [1, 3, 4], [2, 6]\]` -->  `[1, 1, 2, 3, 4, 4, 5, 6]`
lists = `[]` --> `[]`
lists = `[[]]` --> `[]`

### Solution 1 : Pairs
Because merging two lists is simple, one intuitive approach could be to merge greedily, merging list 1 with list 2, the result with list 3, and so on. Writing it up quickly, we might get some code that looks like this:

```
class Solution{
private:
	ListNode* mergeTwoLists(ListNode* l1, ListNode* l2){
		if(l1 == nullptr) return l2;
		if(l2 == nullptr) return l1;
		if(l1->val < l2->val){
			l1->next = mergeTwoLists(l1->next, l2);
			return l1;
		}else{
			l2->next = mergeTwoLists(l1, l2->next);
			return l2;
		}
	}
public:
	ListNode* mergeKLists(vector<ListNode*>& lists){
		if(lists.empty()) return nullptr;
		for(int i = 0; i < lists.size() - 1; ++i){
			lists[i+1] = mergeTwoLists(lists[i], lists[i+1]);
		}
		return lists[lists.size() - 1];
	}
};
```

Runtime Analysis: Assuming *k* lists with an average size of *n*, we note that the first merger has *2n* comparisons, the second *3n*, and so on until the final list has *(k+1)n* comparisons for  a total $$O(k^2\*n)$$ runtime

It's possible to do better with this pairs strategy by using a queue: we can merge lists of the same size and push the resulting bigger list to the back of a queue. Using this strategy, our runtime is *O(n\*k\*log(k))*.

```
class Solution{
private:
	ListNode* mergeTwoLists(ListNode* l1, ListNode* l2){
		if(l1 == nullptr) return l2;
		if(l2 == nullptr) return l1;
		if(l1->val < l2->val){
			l1->next = mergeTwoLists(l1->next, l2);
			l1 = l1->next;
		}else{
			l2->next = mergeTwoLists(l1, l2->next)l;
			l2 = l2->next;
		}
	}
public:
	ListNode* mergeKLists(vector<ListNode*>& lists){
		if(lists.size() == 0) return nullptr;
		for(int i = 0; i + 1 < lists.size(); i+=2){
			lsits.push_back(mergeTwoLists(lists[i], lists[i+1]));
		}
		return lists[i];
	}
}
```

### Solution 2 : MinHeap

A different approach to this problem, that ends up having the same asymptotic runtime, uses priority queues instead of simple queues to order the data. The idea behind this solution is placing all the linked lists in a big minHeap, sorted by the first element of each linked list. This way, the linked list node that we are interested in will always be at the top of the heap. We can build a new linked list by popping the least element from the top of the minHeap in a loop, then push the rest of the loop back to the minHeap.

```
class Solution{
	ListNode* mergeKLists(vector<ListNode*> lists){
		auto compare = [](ListNode* a, ListNode* b){return a->val > b->val;};
		priority_queue<ListNode*, vector<ListNode*>, decltype(compare)> minHeap(compare);
		
		for(list : lists){
			if(list)minHeap.push(list);
		}
		ListNode dummy = ListNode(0);
		ListNode* tail = &dummy;
		while(!minHeap.empty){
			//add the least element to tail
			tail->next = minHeap.top();
			minHeap.pop();
			tail = tail->next;
			//push the remaining list back to the heap
			if(tail->next) minHeap.push(tail->next);
		}
		return dummy.next;
	}
}
```

Runtime Analysis:
As above, assuming *k* lists with *n* members per list, the initial building of the minHeap takes *O(k\* log(k))* time. When building our solution linked list, we have n\*k insertions, each of which takes *O(log(k))* time, giving us an asymptotic runtime of *O(n \* k \* log(k))*, the same as above.