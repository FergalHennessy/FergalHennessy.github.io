---

title:(LC1) Merge K Lists
feed: show
date: 1/08/2024

---


## Merge K Lists
You are given an array of k linked-lists `lists`, where each linked list is sorted in ascending order.
*merge all the linked-lists into one sorted linked-list and return it.*

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
lists = \[\[1, 4, 5\], \[1, 3, 4\], \[2, 6\]\] -->  \[1, 1, 2, 3, 4, 4, 5, 6\]
lists = \[\] --> \[\]
lists = \[\[\]\]

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

Runtime Analysis: Assuming *n* lists with an average size of *k*, we note that the first merger has *2k* comparisons, the second *3k*, and so on until the final list has *(n+1)k* comparisons for  a total O(n^2\*k) runtime

It's possible to do a little better with this pairs strategy by using a queue: we can merge lists of the same size and push the resulting bigger list to the back of a queue. Using this strategy, our runtime is O(n\*k\*log(n)).

But can we do better?

### Solution 2 : MinHeap
