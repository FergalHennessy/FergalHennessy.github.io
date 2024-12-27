## Reverse Linked List

A linked list is one of the most basic data structures in computer science. It consists of a series of nodes with two properties: a value, and a pointer to the next node in the sequence. Because there is no return address to find the previous node, performing operations on linked lists can be complicated. Here we will reverse a linked list.

### Problem: 

Given the `head` of a singly linked list, reverse the list, and *return the reversed list*

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

### Examples:

`head = [1, 2, 3, 4, 5]` --> `[5, 4, 3, 2, 1]` <br>
`head = [1, 2]` --> `[2, 1]`

### Solution 1: Iteration

We define three `ListNode*` objects, `curr = head`, `prev = nullptr`,` nxt = nullptr`. Each iteration consists of three steps:
1. save the next node (the node after curr) in the list to nxt
2. extend the list pointed to by `prev` to include curr
3. advance curr by setting it to nxt
when curr eventually runs out of items to advance to, we will have a full list pointed to by `prev`. Translating this solution to code gives the following:

```
ListNode revLinkedList(ListNode* head){
	ListNode* curr = head;
	ListNode* prev = nullptr;
	ListNode* nxt = nullptr;
	while(curr != nullptr){
		nxt = curr->next;
		curr->next = prev;
		prev = curr;
		curr = nxt; 
	}
	return prev;
}
```

Runtime Complexity: as we visit each node in the list exactly once, the time complexity of this solution is O(n), and the space complexity is O(1), as the only data structures used are pointers.

