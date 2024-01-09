---

title:(LC1) Merge K Lists
feed: hide
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

### Intuition
Because merging two lists is simple, one intuitive approach could be to merge greedily, merging list 1 with list 2, the result with list 3, and so on. Writing it up quickly, we might get some code that looks like this:

```
Class Solution{
private:
	function MergeTwoLists()
}
```
