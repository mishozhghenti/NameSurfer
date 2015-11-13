/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;

import acm.graphics.GCanvas;
import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.util.MediaTools;
import acm.util.RandomGenerator;

public class NameSurferGraph extends GCanvas implements NameSurferConstants,
		ComponentListener {

	/**
	 * Creates a new NameSurferGraph object that displays the data.
	 */
	public NameSurferGraph() {
		addComponentListener(this);
		list = new ArrayList<NameSurferEntry>();
		colorList = new HashMap<NameSurferEntry, Color>();
		r.setSeed(10);
	}

	/**
	 * Clears the list of name surfer entries stored inside this class.
	 */
	public void clear() {
		list.clear();
		delete.play();
	}

	/* Method: addEntry(entry) */
	/**
	 * Adds a new NameSurferEntry to the list of entries on the display. Note
	 * that this method does not actually draw the graph, but simply stores the
	 * entry; the graph is drawn by calling update.
	 */

	/* adds new item(entry) */
	public void addEntry(NameSurferEntry entry) {
		list.add(entry);
		colorList.put(entry, r.nextColor());
		add.play();
	}

	/* removes (remove) item */
	public void removeEntry(NameSurferEntry remove) {
		list.remove(remove);
		colorList.remove(remove);
		delete.play();
	}

	/**
	 * Updates the display image by deleting all the graphical objects from the
	 * canvas and then reassembling the display according to the list of
	 * entries. Your application must call update after calling either clear or
	 * addEntry; update is also called whenever the size of the canvas changes.
	 */
	public void update() {
		removeAll();
		X = getWidth() / 11;
		Y = getHeight();
		drawVerticals();
		drawHorizontal(getHeight() - GRAPH_MARGIN_SIZE);
		drawHorizontal(GRAPH_MARGIN_SIZE);
		drawRanks();
	}

	/* this method draws vertical lines */
	private void drawVerticals() {
		for (int i = 0; i < NDECADES; i++) {
			drawEachLine(i);
			writeYears(i);
		}
	}

	/* this method draws each vertical lines */
	private void drawEachLine(int i) {
		GLine line = new GLine(i * X, 0, i * X, Y);
		add(line);
	}

	/* draws horizontal lines (top and bottom) */
	private void drawHorizontal(double y) {
		GLine line = new GLine(0, y, getWidth(), y);
		add(line);
	}

	/* writes years */
	private void writeYears(int i) {
		int year = START_DECADE + i * 10;
		String text = "" + year;
		GLabel label = new GLabel(text);
		label.setLocation(i * X, Y);
		add(label);
	}

	/* makes (col) appropriate value */
	private void makeNextColor(NameSurferEntry name) {
		col = colorList.get(name);
	}

	/* draws ranks */
	private void drawRanks() {
		for (int i = 0; i < list.size(); i++) {
			makeNextColor(list.get(i));
			for (int j = 0; j < NDECADES; j++) {
				int rank = list.get(i).getRank(j);
				if (j == 0) {
					x = getXCoordinate(j);
					if (rank != 0) {
						y = getYCoordinate(rank);
					} else {
						y = rankZeroCoordiante();
					}
					ranks[0] = rank;
					points[0][0] = x;
					points[0][1] = y;
				} else {
					x1 = getXCoordinate(j);
					if (rank != 0) {
						y1 = getYCoordinate(rank);
					} else {
						y1 = rankZeroCoordiante();
					}
					ranks[j] = rank;
					points[j][0] = x1;
					points[j][1] = y1;
					drawLine(i);
				}
			}
			name(i);
		}
	}

	/*
	 * this method helps us to draw names on graph correctly 
	 * (not to cross line graph)
	 */
	private void name(int j) {
		for (int i = 0; i < NDECADES; i++) {
			if (i == 0) {
				if (points[1][1] >= points[i][1]) {
					drawNameOnGraph(i, j, true);
				} else {
					drawNameOnGraph(i, j, false);
				}
			}
			if (i == NDECADES - 1) {
				drawNameOnGraph(i, j, true);
			}
			if (i != 0 && i != NDECADES - 1) {
				if (points[i - 1][1] == points[i][1]
						&& points[i + 1][1] == points[i][1]) {
					drawNameOnGraph(i, j, true);
				}else if (points[i - 1][1] < points[i][1]
						&& points[i + 1][1] < points[i][1]) {
					drawNameOnGraph(i, j, false);
				} else if (points[i - 1][1] <= points[i][1]
						&& points[i + 1][1] >= points[i][1]) {
					drawNameOnGraph(i, j, true);
				} else if (points[i - 1][1] >= points[i][1]
						&& points[i + 1][1] >= points[i][1]) {
					drawNameOnGraph(i, j, true);
				} else if (points[i - 1][1] >= points[i][1]
						&& points[i + 1][1] <= points[i][1]) {
					drawNameOnGraph(i, j, false);
				}
			}
		}
	}

	/* draws rank's each line */
	private void drawLine(int i) {
		GLine line = new GLine(x, y, x1, y1);
		x = x1;
		y = y1;
		line.setColor(col);
		add(line);
	}

	/* draws names and rank of graph */
	private void drawNameOnGraph(int i, int j, boolean direction) {
		String rank;
		if (ranks[i] == 0) {
			rank = "*";
		} else {
			rank = Integer.toString(ranks[i]);
		}
		GLabel label = new GLabel(list.get(j).getName() + " " + rank);
		if (direction == true) {
			label.setLocation(points[i][0], points[i][1]);
		} else {
			label.setLocation(points[i][0], points[i][1] + label.getAscent());
		}
		label.setColor(col);
		add(label);
	}

	/* calculates X coordinate */
	private double getXCoordinate(int j) {
		return j * getWidth() / 11;
	}

	/* calculates Y coordinate */
	private double getYCoordinate(int rank) {
		return rank * (getHeight() - 2 * GRAPH_MARGIN_SIZE) / MAX_RANK
				+ GRAPH_MARGIN_SIZE;
	}

	/* rank coordinate when it is 0 */
	private double rankZeroCoordiante() {
		return getHeight() - GRAPH_MARGIN_SIZE;
	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentResized(ComponentEvent e) {update();}
	public void componentShown(ComponentEvent e) {}

	/* Private instance variables */
	private double X;
	private double Y;
	private ArrayList<NameSurferEntry> list;
	private double x;
	private double y;
	private double x1;
	private double y1;
	private int[] ranks = new int[NDECADES];
	private double[][] points = new double[NDECADES][2];
	private RandomGenerator r = new RandomGenerator();
	private HashMap<NameSurferEntry, Color> colorList;
	private Color col;
	private AudioClip add = MediaTools.loadAudioClip("add.au");
	private AudioClip delete = MediaTools.loadAudioClip("delete.au");
}
