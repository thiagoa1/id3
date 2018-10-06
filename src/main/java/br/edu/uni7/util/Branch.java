package br.edu.uni7.util;

public class Branch<T> {
	
	private String name;
	
	private Node<T> node;
	
	private Node<T> parentNode;
	
	public Branch(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node<T> getNode() {
		return node;
	}

	public void setNode(Node<T> node) {
		this.node = node;
	}
	
	public void setParentNode(Node<T> parentNode) {
		this.parentNode = parentNode;
	}
	
	public Node<T> getParentNode() {
		return parentNode;
	}
}