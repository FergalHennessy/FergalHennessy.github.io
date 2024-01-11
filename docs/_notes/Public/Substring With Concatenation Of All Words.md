---
title: Substring With Concatenation Of All Words
feed: show
date: 2024/01/11
tags: hard hashmap sliding-window
leetcode: 30
---

## Substring With Concatenation Of All Words

You are given a string `s`, and an array of strings `words`. All the strings of words are **of the same length** 

A **concatenated substring** in `s` is a substring that contains all the strings of any permutation of `words` concatenated. Essentially, if all the words in `words` appear exactly once consecutively, then the substring is a concatenated substring.

*Return the starting indices of the concatenated substrings in* `s`. You can return the answer in **any order.**

## Examples

__Input: __ `s = "barfoothefoobarman", words = ["foo, bar"]` --> `[0, 9]`
**Input: ** `s = "wordgoodgoodgoodbestword", words = ["word", "good", "best", "word"]` --> `[]`

### Solution 1: Brute Force Hashmap Comparison

Let the size of `s` be `n`, let the number of elements in `words` be `m`, let the length of a word in `words` be `w`. It follows that every concatenated substring will have length `m * w`. We can brute force a solution as follows:
1. load all `words` into a hashmap `total`
2. For each length `m * w` substring of `s`, split into length `w` pieces, add the pieces to a new hashmap `prospect` and check `prospect == total`
3. Return a vector of all the indices where step 2 is true.
Putting these steps into code, we get this solution:

```
class Solution{
public:
	vector<int> findSubstring(string s, vector<string>& words){
		vector<int> res;
		if(words.size() == 0) return res;
		unordered_map<string, int> total;
		for(string& word: words){
			total[word]++;
		}
		int m = words.size();
		int w = words[0].size();
		for(int i = 0; i < s.size() - (m*w) + 1; ++i){
			unordered_map<string, int> prospect;
			int j = i;
			while(j < i + (m*w)){
				string word = s.substr(j, w);
				prospect[word] += 1;
				j+= w;
			}
			if(prospect == total) res.push_back(i);
		}
		return res;
	}
};
```

##### Runtime Analysis:
1. Building `total`: `O(1) * m = O(m)`
2. Building `prospect` and comparing with `total`: `O(m*w)`  (There are `(m*w)` characters in both maps)
3. Iterating through `s`: `O(n)`
Total Runtime: `O(m) + O(n * m * w) = O(n * m* w)`

##### Space Analysis:
1. `total`: `O(m*w)`
2. `prospect`: `O(m*w)` (`n` times)
Space complexity is also `O(m*n*w)`

Both space and time are cubic: can we do better? Looking at this solution, we do a lot of duplicate comparisons. if we create a map `prospect` and check all the internals against `total`, then discard it, we waste a lot of comparisons that we could have saved for future iterations...

### Solution 2: Sliding Window Hashmap Comparison

Being able to return the answer in any order turns out to be helpful as characters that are the same index mod w are processed together as potential start indices for the substring.

```
class Solution{
public:
	vector<int> findSubstring(string s, vector<string>& words){
		int n = s.size();
		int m = words.size(), w = words[0].size();
		unordered_map<string, int> total;
		
		for(string& word : words){
			total[word]++;
		}
		
		vector<int> res;
		for(int i = 0; i < w; i++){
			unordered_map<string, int> prospect;
			string t = "";
			for(int j = i, k = i; k < n; k++){
				t += s[k];
				if(t.size() == w){
					prospect[t]++;
					t = "";
				}
				if(k-j+1 == m*w){
					if(prospect == total)res.push_back(j);
					string remove = s.substr(j, w);
					if(prospect[remove] > 1) prospect[remove]--;
					else prospect.erase(remove);
					j+=w;
				}
			}
		}
		return res;
	}
};
```

