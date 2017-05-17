package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.crypto.dsig.Transform;

public class Histogram extends JPanel
{
	protected static final int MIN_BAR_WIDTH = 20;
	private Map<String, Integer> data;
	public Histogram(Map<String, Integer> dataMap)
	{
		data = new LinkedHashMap<String, Integer>();
		List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String,Integer>>(dataMap.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>()
		{

			@Override
			public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2)
			{
				return o2.getValue().compareTo(o1.getValue());
			}
			
		});
		for(Map.Entry<String, Integer> entry : entries)
		{
			data.put(entry.getKey(), entry.getValue());
		}
		
		
		
		int width = data.size() * MIN_BAR_WIDTH + 10;
		Dimension minSize = new Dimension(width, 128);
		Dimension prefSize = new Dimension(width, 256);
		setMinimumSize(minSize);
		setPreferredSize(prefSize);
		
		
	}
	public static void drawRotate(Graphics2D g2d, double x, double y, int angle, String text) 
	{    
	    g2d.translate((float)x,(float)y);
	    g2d.rotate(Math.toRadians(angle));
	    g2d.drawString(text,0,0);
	    g2d.rotate(-Math.toRadians(angle));
	    g2d.translate(-(float)x,-(float)y);
	} 
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(data != null)
		{
			Graphics2D g2d = (Graphics2D) g.create();
			int maxValue = 0;
			String maxKey = null;
			for (String key : data.keySet()) 
			{
                int value = data.get(key);
                maxValue = Math.max(maxValue, value);
                if(maxValue == value)
                	maxKey = key;
            }
			int xOffset = 5;
			int yOffset = 10;
			int width = getWidth() - 1 - xOffset*2;
			int height = getHeight() - 1 - yOffset*2;
			g2d.setColor(Color.DARK_GRAY);
			g2d.drawRect(xOffset, yOffset, width, height);
			int barWidth = Math.max(MIN_BAR_WIDTH, (int)Math.floor((float)width/(float)data.size()));
			
			int xPos = xOffset;
			for (String key : data.keySet()) 
			{
                int value = data.get(key);
				
				int barHeight = Math.round((float)value/(float)maxValue*height);
				int yPos = height + yOffset - barHeight;
				int colorConstant = (int)(((float)value/(float)maxValue) * 255);
				g2d.setColor(new Color((int)((double)colorConstant), (int)((double)colorConstant), (int)((double)colorConstant)));
				Rectangle2D bar = new Rectangle2D.Float(xPos,yPos,barWidth,barHeight);
				g2d.fill(bar);
				g2d.setColor(Color.BLUE);
                g2d.draw(bar);
                xPos += barWidth+5;
                g2d.setColor(Color.RED);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
                int vertOffset = g2d.getFontMetrics().stringWidth(key+" "+value);
                if(barHeight >= vertOffset)
                	drawRotate(g2d, xPos-barWidth, yPos, 90, key+" "+value);
                else 
                	drawRotate(g2d, xPos-barWidth, yPos-vertOffset, 90, key+" "+value);
              //  g2d.drawString(key+" "+value, xPos-barWidth, yPos);
			}
		}
	}
}
