package com.marginallyclever.convenience.swing;

import com.marginallyclever.convenience.helpers.StringHelper;
import com.marginallyclever.convenience.log.Log;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple line graph.
 *
 * @author Dan Royer
 * @since 2.5.0
 */
public class LineGraph extends JPanel {
	private final TreeMap<Double, Double> data = new TreeMap<>();
	private double yMin, yMax, xMin, xMax;
	private Color majorLineColor = new Color(0.8f,0.8f,0.8f);
	private Color minorLineColor = new Color(0.9f,0.9f,0.9f);
	private int gridSpacingX = 10;
	private int gridSpacingY = 10;

	public LineGraph() {
		super();
		setBackground(Color.WHITE);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				updateToolTip(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setToolTipText("");
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				updateToolTip(e);
			}
		});
	}

	public void updateToolTip(MouseEvent event) {
		double mx = event.getX();
		double scaledX = mx / getWidth();
		double x = xMin + (xMax - xMin) * scaledX;
		setToolTipText( x+"="+ StringHelper.formatDouble(getYatX(x)) );
	}

	private double getYatX(double x) {
		// Find the two keys k1 and k2 such that k1 <= x <= k2
		Double k1 = data.floorKey(x);
		Double k2 = data.ceilingKey(x);

		// If x matches a key exactly, return the corresponding value
		if (k1.equals(k2)) {
			return data.get(k1);
		}

		// Perform linear interpolation
		double y1 = data.get(k1);
		double y2 = data.get(k2);
		return y1 + (x - k1) * (y2 - y1) / (k2 - k1);
	}

	public void addValue(double x,double y) {
		data.put(x,y);
	}

	public void removeValue(double x) {
		data.remove(x);
	}

	public void clear() {
		data.clear();
	}

	public void setBounds(double xMin,double xMax,double yMin,double yMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
	}
	public void setYMin(double yMin) {
		this.yMin = yMin;
	}
	public void setYMax(double yMax) {
		this.yMax = yMax;
	}
	public void setXMin(double xMin) {
		this.xMin = xMin;
	}
	public void setXMax(double xMax) {
		this.xMax = xMax;
	}

	public void setBoundsToData() {
		if(data.isEmpty()) return;

		double [] bounds = getDataBounds();

		xMin = bounds[0];
		xMax = bounds[1];
		yMin = bounds[2];
		yMax = bounds[3];
	}

	/**
	 *
	 * @return minx,maxx,miny,maxy
	 */
	public double [] getDataBounds() {
		if(data.isEmpty()) return new double[] {0,0,0,0};

		List<Double> x = new ArrayList<>(data.keySet());
		List<Double> y = new ArrayList<>(data.values());
		double minX = x.get(0);
		double maxX = x.get(0);
		double minY = y.get(0);
		double maxY = y.get(0);
		for(int i=1;i<x.size();++i) {
			if(x.get(i)<minX) minX = x.get(i);
			if(x.get(i)>maxX) maxX = x.get(i);
			if(y.get(i)<minY) minY = y.get(i);
			if(y.get(i)>maxY) maxY = y.get(i);
		}

		return new double[] {minX,maxX,minY,maxY};
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		super.paintComponent(g2d);
		drawGrid(g2d);
		drawGraphLine(g2d);
	}

	private void drawGrid(Graphics g) {
		g.setColor(minorLineColor);

		int width = getWidth();
		int height = getHeight();

		double [] bounds = getDataBounds();
		double minX = bounds[0];
		double maxX = bounds[1];
		double minY = bounds[2];
		double maxY = bounds[3];

		// draw vertical lines
		double left = Math.floor(minX / gridSpacingX) * gridSpacingX;
		for (double x = left; x <= maxX; x += gridSpacingX) {
			int x1 = transformX(x);
			g.drawLine(x1, 0, x1, height);
		}

		// draw horizontal lines
		double bottom = Math.floor(minY / gridSpacingY) * gridSpacingY;
		for (double y = bottom; y <= maxY; y += gridSpacingY) {
			int y1 = transformY(y);
			g.drawLine(0, y1, width, y1);
		}
	}

	private void drawGraphLine(Graphics g) {
		if(data.isEmpty()) return;

		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(getForeground());
		int height = getHeight();

		Map.Entry<Double, Double> prevEntry = data.firstEntry();
		int prevX = transformX(prevEntry.getKey());
		int prevY = transformY(prevEntry.getValue());
		for (Map.Entry<Double, Double> entry : data.entrySet()) {
			int currentX = transformX(entry.getKey());
			int currentY = transformY(entry.getValue());
			g.drawLine(prevX, height - prevY, currentX, height - currentY);
			prevX = currentX;
			prevY = currentY;
		}
	}

	private int transformX(double x) {
		return (int) (((x - xMin) / (xMax - xMin)) * getWidth());
	}

	private int transformY(double y) {
		return (int) (((y - yMin) / (yMax - yMin)) * getHeight());
	}

	// TEST 
	
	public static void main(String[] args) {
		Log.start();

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {}

		LineGraph graph = new LineGraph();
		for(int i=0;i<250;++i) {
			graph.addValue(i,Math.random()*500);
		}
		graph.setBoundsToData();
		graph.setBorder(new BevelBorder(BevelBorder.LOWERED));

		JFrame frame = new JFrame("LineGraph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800,400));
		frame.setContentPane(graph);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public Color getMajorLineColor() {
		return majorLineColor;
	}

	public void setMajorLineColor(Color majorLineColor) {
		this.majorLineColor = majorLineColor;
	}

	public Color getMinorLineColor() {
		return minorLineColor;
	}

	public void setMinorLineColor(Color minorLineColor) {
		this.minorLineColor = minorLineColor;
	}

	public int getGridSpacingX() {
		return gridSpacingX;
	}

	public void setGridSpacingX(int gridSpacingX) {
		this.gridSpacingX = gridSpacingX;
	}

	public int getGridSpacingY() {
		return gridSpacingY;
	}

	public void setGridSpacingY(int gridSpacingY) {
		this.gridSpacingY = gridSpacingY;
	}
}
