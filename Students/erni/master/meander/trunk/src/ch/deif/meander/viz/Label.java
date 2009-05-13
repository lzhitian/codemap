package ch.deif.meander.viz;

import java.awt.Rectangle;

import processing.core.PGraphics;

/**
 * Displays some text.
 */
public class Label implements Drawable, Comparable<Label> {

	// TODO extract parts of this class into a figure superclass, eg positioning and bounding box

	public static boolean DRAFT = false;

	private String text;
	public float size = 12;
	public float x0, y0, x, y;
	public float width;
	private Position pos = Position.TOPLEFT;
	boolean hidden;

	public Label(String text) {
		assert text != null;
		this.text = text;
	}

	public void initializeWidth(PGraphics pg) {
		pg.textSize(size);
		width = pg.textWidth(text);
	}

	@Override
	public void draw(PGraphics pg) {
		if (DRAFT) {
			pg.stroke(0);
			pg.noFill();
			pg.rect(x, y, width, -(size * 0.6f));
			pg.ellipse(x0, y0, 5, 5);
		}
		if (hidden) return;
		pg.fill(0);
		pg.textSize(size);
		pg.text(text, x, y);
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) (y - (size)), (int) width, (int) (size));
	}

	@Override
	public int compareTo(Label other) {
		boolean thisTest = this.text.endsWith("Test");
		boolean otherTest = other.text.endsWith("Test");
		if (thisTest != otherTest) return thisTest ? 1 : -1;
		return (int) (other.size - this.size);
	}

	public boolean hasNextPosition() {
		return pos.ordinal() < 3;
	}

	public void nextPosition() {
		pos = Position.values()[pos.ordinal() + 1];
		y = y0 + ((pos == Position.BOTTOMLEFT || pos == Position.BOTTOMRIGHT) ? (size * 0.6f) : 0);
		x = x0 - ((pos == Position.BOTTOMLEFT || pos == Position.TOPLEFT) ? width : 0);
	}

	private enum Position {

		TOPRIGHT, TOPLEFT, BOTTOMLEFT, BOTTOMRIGHT;

	}

}