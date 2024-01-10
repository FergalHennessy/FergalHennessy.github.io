---
title: Reverse Nodes In K-Group
feed: show
date: 2024-01-10
tags: hard linked-list recursion
leetcode: 0
---



## Reverse Nodes In K-Group
Given the head of a linked list, reverse the nodes of the list `k` at a time, without altering the value of any nodes in the list. If the number of nodes is not a multiple of `k` then left-out nodes, in the end, should remain as it is.
*Return the modified list.*
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

## Examples
Input: {`head = [1, 2, 3, 4, 5], k = 2`} --> `[2, 1, 3, 4, 5]` <br>
Input: {`head = [1, 2, 3, 4, 5], k = 3`} --> `[3, 2, 1, 4, 5]` <br>
Input: {`head = [1, 2], k= 2`} --> `[2, 1]`
### Solution 1: Recursion
To quickly tackle this problem, recursion makes a lot of sense, as the back of the list can be transformed independently of earlier items. To implement a recursion solution takes three steps:

1. Look Ahead: define a pointer `cursor` which looks ahead to see if we have enough space to do a reversal (if we have k or more nodes in the current list)

2. List Reversal: similar to normal linked-list reversal, but limited to k items. Use 3 `ListNode*` variables: `curr, nxt, and prev`.

3. Recursive Call: set the next item in the list to the result of a new reversal call, and return the head of the current list

Coding this up, we find the following function:

```
ListNode* reverseKGroup(ListNode* head, int k){
	ListNode* cursor = head;
	for(int i = 0; i<k; ++i){
		if(cursor == nullptr) return head;
		cursor = cursor->next;
	}
	ListNode* curr = head;
	ListNode* prev = nullptr;
	ListNode* nxt = nullptr;
	for(int i = 0; i < k; ++i){
		nxt = curr->next;
		curr->next = prev;
		prev = curr;
		curr = nxt;
	}
	head->next = reverseKGroup(curr, k);
	return prev;
}
```

Runtime Analysis:
The runtime of this function is O(n), as each node is traversed at most twice.
The space complexity of this function is O(n) due to call stacks and its recursive nature.
This function is easy to understand as is, but to reduce the space complexity we can rewrite it to be iterative.

### Solution 2: Iteration

```
ListNode* reverseKGroup(ListNode* head, int k) {
        ListNode* dummy = new ListNode(0);
        dummy->next = head;
        ListNode* before = dummy;
        ListNode* after = head;
        ListNode* curr = nullptr;
        ListNode* prev = nullptr;
        ListNode* nxt = nullptr;
        while(true){
            ListNode* cursor = after;
            for(int i = 0; i < k; i++){
                if(cursor == nullptr) return dummy->next;
                cursor = cursor->next;
            }
            curr = after;
            prev = before;
            for(int i = 0; i < k; i++){
                nxt = curr->next;
                curr->next = prev;
                prev = curr;
                curr = nxt;
            }
            after->next = curr;
            before->next = prev;
            before = after;
            after = curr;
        }
    }
```