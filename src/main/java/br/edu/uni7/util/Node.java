package br.edu.uni7.util;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {

	private T data = null;

	private List<Branch<T>> branchs = new ArrayList<>();

	private Branch<T> parentBranch = null;

	public Node(T data) {
		this.data = data;
	}
	
	public boolean isLeaf() {
		if (branchs == null || branchs.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public Branch<T> addBranch(Branch<T> branch) {
		branch.setParentNode(this);
		branchs.add(branch);
		return branch;
	}

	public List<Branch<T>> getBranchs() {
		return branchs;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setParent(Branch<T> parentBranch) {
		this.parentBranch = parentBranch;
	}

	public Branch<T> getParent() {
		return parentBranch;
	}
	
	@Override
	public String toString() {
		return "[" + data.toString() + "]";
	}
}