import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

import javax.swing.JFrame;

//This class is used to draw graph about volume data.
public class VolumeGraph extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long					n_Max_Volume 	= 	0;								//Variable to store the maximum value of volume data.
	long					n_Min_Volume 	= 	Long.MAX_VALUE;					//Variable to store the minimum value of volume data.
	ArrayList<MarketData> 	lst_MarketData 	= 	new ArrayList<MarketData>();	//This is the main market data structure.
	
	//Function to retrieve market data from main MarketGUI class.
	public void setMarketData(ArrayList<MarketData> lst_MarketData)
	{
		this.lst_MarketData = lst_MarketData;
		
		//Set the maximum and minimum value of volume data.
		for (int i = 0; i < lst_MarketData.size(); i++)
		{
			if (n_Min_Volume > lst_MarketData.get(i).lng_Volume)
			{
				n_Min_Volume = lst_MarketData.get(i).lng_Volume;
			}
			if (n_Max_Volume < lst_MarketData.get(i).lng_Volume)
			{
				n_Max_Volume = lst_MarketData.get(i).lng_Volume;
			}
		}
	}
	
	//Function to convert date to String.
	public String dateToString(Date date)
	{
		String 		months[] 	= 	{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		Calendar 	calendar 	= 	Calendar.getInstance();
		
		calendar.setTime(date);
		
		return String.format("%s-%d-%d", months[calendar.get(Calendar.MONTH)], calendar.get(Calendar.DATE), calendar.get(Calendar.YEAR));
	}
	
	//System drawing Function.
	@Override
	public void paint(Graphics g)
	{
		Color 		clr_Org	 	= g.getColor();
		Dimension 	dim 		= getSize();
		
		g.setColor(new Color(0xEE, 0xEE, 0xEE));
		g.fillRect(0, 0, dim.width, dim.height);
		
		drawGraph(g, dim);
		
		g.setColor(clr_Org);
	}
	
	public void drawXYAxis(Graphics g, Dimension dim)
	{	
		g.setColor(Color.black);
		
		//Draw x & y axis;
		g.drawLine(100, 50, 100, dim.height - 50);
		g.drawLine(100, dim.height - 50, dim.width - 50, dim.height - 50);
		
		int n_Interval = 50;
		
		FontMetrics fm 		= g.getFontMetrics();
		
		//Draw y axis string.
		for (int i = dim.height - 50; i >= 50; i -= n_Interval)
		{
			int value = (int) ((dim.height - 50 - i) * (n_Max_Volume - n_Min_Volume) / (dim.height - 100) + n_Min_Volume);
			Rectangle2D rect 	= fm.getStringBounds(new Integer(value).toString(), g);
			g.drawString(new Long(value).toString(), 100 - (int)rect.getWidth(), i);
			g.drawLine(100, i, 105, i);
		}
		
		n_Interval = 100;
		
		//Draw x axis string.
		for (int i = 100; i < dim.getWidth() - 50; i += n_Interval)
		{
			int 		index 		= 	(int) ((i - 100) * lst_MarketData.size() / (this.getSize().getWidth() - 100));
			String 		date 		= 	dateToString(lst_MarketData.get(lst_MarketData.size() - 1 - index).dat_Date);
			Rectangle2D rect 		= 	fm.getStringBounds(date, g);
			
			g.drawString(date, (int) (i - rect.getWidth() / 2), dim.height - 30);
			g.drawLine(i, dim.height - 55, i, dim.height - 50);
		}
		
		g.setColor(new Color(200, 200, 200));
		for (int i = dim.height - 50; i >= 50; i -= 50)
		{
			for (int j = 100; j < dim.getWidth() - 50; j += 100)
			{
				g.drawLine(100, i, dim.width - 50, i);
				g.drawLine(j, 50, j, dim.height - 50);
			}
		}
	}
	
	//Function that draw volume data graph.
	public void drawGraph(Graphics g, Dimension dim)
	{
		SimpleDateFormat sdf 	= new SimpleDateFormat();
		
		sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
		sdf.applyPattern("dd MMM yyyy HH:mm:ss");
		
		
		int n_End_Date 		= (int)(lst_MarketData.get(0).dat_Date.getTime() / (24 * 3600 * 1000));
		int n_Start_Date 	= (int)(lst_MarketData.get(lst_MarketData.size() - 1).dat_Date.getTime() / (24 * 3600 * 1000));
		
		int	x_pos[] 		= new int[lst_MarketData.size()];
		int	y_pos[] 		= new int[lst_MarketData.size()];
		
		g.setColor(Color.black);
		
		
		drawXYAxis(g, dim);
		
		//Get the x & y position of volume data graph points.
		for (int i = 0; i < lst_MarketData.size(); i++)
		{
			x_pos[i] = (int)(lst_MarketData.get(i).dat_Date.getTime() / (24 * 3600 * 1000)) - n_Start_Date;
			x_pos[i] = x_pos[i] * (dim.width - 150) / (n_End_Date - n_Start_Date);
			x_pos[i] += 100;
			
			y_pos[i] = (int)(dim.height - 50 - (lst_MarketData.get(i).lng_Volume - n_Min_Volume) * (dim.height - 100) / (n_Max_Volume - n_Min_Volume));
		}
		
		//Color of the graph is cyan.
		g.setColor(Color.cyan);
		
		//Draw volume data graphs
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(2f));
		g.drawPolyline(x_pos, y_pos, lst_MarketData.size());
		
	}
}
