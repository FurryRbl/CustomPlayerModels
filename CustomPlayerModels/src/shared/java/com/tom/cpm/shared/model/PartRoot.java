package com.tom.cpm.shared.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PartRoot {
	private List<RootModelElement> elements;
	private RootModelElement mainRoot;

	public PartRoot(RootModelElement element) {
		elements = new ArrayList<>(1);
		mainRoot = element;
		elements.add(element);
	}

	public PartRoot() {
		elements = new ArrayList<>();
	}

	public void forEach(Consumer<? super RootModelElement> action) {
		elements.forEach(action);
	}

	public boolean add(RootModelElement e) {
		return elements.add(e);
	}

	public RootModelElement get() {
		return elements.get(0);
	}

	public RootModelElement getMainRoot() {
		return mainRoot;
	}

	public void setMainRoot(RootModelElement mainRoot) {
		this.mainRoot = mainRoot;
	}
}
