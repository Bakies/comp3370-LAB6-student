package edu.wit.cs.comp3370;

import java.util.ArrayList;
import java.util.Collections;

import edu.wit.cs.comp3370.tests.LAB6TestCase;

/* Implements a trie data structure 
 * 
 * Wentworth Institute of Technology
 * COMP 3370
 * Lab Assignment 6 solution
 * 
 */

public class Trie extends Speller {
	private class TrieNode {
		private TrieNode parent;
		private TrieNode[] letters;
		private boolean isWord;
		private char c;

		private TrieNode(TrieNode parent, char c) {
			this.parent = parent;
			letters = new TrieNode[26];
			isWord = false;
			this.c = c;
		}

		private TrieNode() {
			parent = null;
			letters = new TrieNode[26];
		}

		private TrieNode getChild(char c) {
			int i = c - 'a';
			return letters[i];
		}

		private TrieNode getMakeChild(char c) {
			int i = c - 'a';
			if (letters[i] == null) {
				return (letters[i] = new TrieNode(this, c));
			} else {
				return letters[i];
			}
		}

		private ArrayList<TrieNode> getAllChildren() {
			ArrayList<TrieNode> allChildren = new ArrayList<>();
			for (TrieNode n : letters) {
				if (n != null) {
					allChildren.add(n);
				}
			}
			return allChildren;
		}
	}

	private TrieNode root;

	public Trie() {
		root = new TrieNode();
	}

	@Override
	public void insertWord(String s) {
		TrieNode node = root;
		for (char c : s.toCharArray()) {
			node = node.getMakeChild(c);
		}
		node.isWord = true;
	}

	@Override
	public boolean contains(String s) {
		TrieNode node = root;
		for (char c : s.toCharArray()) {
			node = node.getChild(c);
			if (node == null)
				return false;
		}
		return node.isWord;
	}

	@Override
	public String[] getSugg(String s) {
		ArrayList<String> suggs = new ArrayList<>();
		recurSugg(s, 0, 2, root, suggs);
		String[] words = new String[suggs.size()];
		Collections.sort(suggs);
		for (int x = 0; x < words.length; x++) {
			words[x] = suggs.get(x);
		}
		return words;
	}

	private void recurSugg(String target, final int depth, final int editDist, TrieNode currNode, ArrayList<String> words) {
		if (editDist < 0) {
			return;
		}
		if (currNode.isWord) {
			if (depth == target.length()) {
				StringBuilder sb = new StringBuilder();
				TrieNode w = currNode;
				while (w.parent != null) { // while not root
					sb.append(w.c);
					w = w.parent;
				}
				words.add(sb.reverse().toString());
			}
		}
		for (TrieNode n : currNode.getAllChildren()) {
			int x = editDist;
			if (depth >= target.length()) {
				x--;
			} else if (target.charAt(depth) != n.c) {
				x--;
			}
			recurSugg(target, depth + 1, x, n, words);
		}
	}

	public static void main(String... a) {
		Trie t = new Trie();
		LAB6TestCase.populateSpeller(t, "/home/jon/dict");
		String[] suggs = t.getSugg("test");
		System.out.println(t.contains("test"));
		for (String s : suggs) {
			System.out.println(s);
		}
		System.out.println();

		BinTree bt = new BinTree();
		LAB6TestCase.populateSpeller(bt, "/home/jon/dict");
		suggs = bt.getSugg("test");
		System.out.println(t.contains("test"));
		for (String s : suggs) {
			System.out.println(s);
		}
		System.out.println();

	}

}
