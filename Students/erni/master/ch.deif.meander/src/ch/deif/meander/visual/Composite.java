package ch.deif.meander.visual;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import ch.deif.meander.MapInstance;
import ch.deif.meander.ui.MeanderApplet;

public class Composite<E extends Layer> extends Layer implements Iterable<E> {

	private List<E> children;
	
	public Composite() {
		this.children = new ArrayList<E>();
	}
	
	public void drawFigure(MapInstance map, PGraphics pg, PApplet pa) {
		// by default, do nothing
	}

	public void draw(MapInstance map, PGraphics pg, PApplet pa) {
		draw(map, pg, pa);
	}

	public void draw(MapInstance map, PGraphics pg, MeanderApplet pa) {
		this.drawFigure(map, pg, pa);
		this.drawChildren(map, pg, pa);
	}

	public void drawChildren(MapInstance map, PGraphics pg, PApplet pa) {
		for (Layer each: children) {
			each.draw(map, pg, pa);
		}
	}

	public static final <T extends Layer> Composite<T> newInstance() {
		return new Composite<T>();
	}

	public static final <T extends Layer> Composite<T> of(T... elements) {
		Composite<T> composite = new Composite<T>();
		for (T each: elements) composite.append(each);
		return composite;
	}

	public void append(E element) {
		children.add(element);
	}

	@Override
	public Iterator<E> iterator() {
		return children.iterator();
	}
	
}
