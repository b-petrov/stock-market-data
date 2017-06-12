import java.awt.BasicStroke;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

//This class draws graphs of Opening, High, Low and Closing.
public class OtherGraphs extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//I made checkboxes to determine whether each graph is shown or not. And these variables are used to determination.
	int			n_Graph_Flg 	= 0x08;
	int			n_Opening		= 0x01;
	int			n_High			= 0x02;
	int			n_Low			= 0x04;
	int			n_Closing		= 0x08;
	
	//Max and Min price.
	int			n_Max_Price		= 0;
	int			n_Min_Price		= 10000;
	
	//Interval between y-axis unit.
	int			n_Interval		= 50;
	
	//Color of the graph of opening, high, low and closing.
	Color		clr_Opening 	= Color.red;
	Color		clr_High		= Color.blue;
	Color		clr_Low			= Color.green;
	Color		clr_Closing 	= Color.GRAY;
	
	//Panel that contains checkboxes.
	JPanel		pnl_ChkBox		= new JPanel();
	
	//Checkbox variables to determine whether graphs are shown are not.
	Checkbox	chk_Opening 	= new Checkbox("Opening");
	Checkbox	chk_High 		= new Checkbox("High");
	Checkbox	chk_Low 		= new Checkbox("Low");
	Checkbox	chk_Closing 	= new Checkbox("Closing");
	
	//Main market data.
	ArrayList<MarketData> lst_MarketData 	= 	new ArrayList<MarketData>();
	
	//Constructor.
	public OtherGraphs()
	{
		
		pnl_ChkBox.add(chk_Opening);
		pnl_ChkBox.add(chk_High);
		pnl_ChkBox.add(chk_Low);
		pnl_ChkBox.add(chk_Closing);
		
		//Initially closing checkbox is checked so it means that closing graph is shown by default.
		chk_Closing.setState(true);
		
		//Checkbox listeners to change the graph show state.
		chk_Opening.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				n_Graph_Flg ^= n_Opening;
				repaint();
			}
			
		});
		
		chk_High.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				n_Graph_Flg ^= n_High;
				repaint();
			}
			
		});
		
		chk_Low.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				n_Graph_Flg ^= n_Low;
				repaint();
			}
			
		});
		
		chk_Closing.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				n_Graph_Flg ^= n_Closing;
				repaint();
			}
			
		});
		
		add(pnl_ChkBox);
	}
	
	//Function to retrieve data from main MarketGUI class.
	public void setMarketData(ArrayList<MarketData> lst_MarketData)
	{
		this.lst_MarketData = lst_MarketData;
		for (int i = 0; i < lst_MarketData.size(); i++)
		{
			if (n_Min_Price > lst_MarketData.get(i).dbl_Opening)
				n_Min_Price = (int)lst_MarketData.get(i).dbl_Opening;
			if (n_Min_Price > lst_MarketData.get(i).dbl_High)
				n_Min_Price = (int)lst_MarketData.get(i).dbl_High;
			if (n_Min_Price > lst_MarketData.get(i).dbl_Low)
				n_Min_Price = (int)lst_MarketData.get(i).dbl_Low;
			if (n_Min_Price > lst_MarketData.get(i).dbl_Closing)
				n_Min_Price = (int)lst_MarketData.get(i).dbl_Closing;
			
			if (n_Max_Price < lst_MarketData.get(i).dbl_Opening)
				n_Max_Price = (int)lst_MarketData.get(i).dbl_Opening;
			if (n_Max_Price < lst_MarketData.get(i).dbl_High)
				n_Max_Price = (int)lst_MarketData.get(i).dbl_High;
			if (n_Max_Price < lst_MarketData.get(i).dbl_Low)
				n_Max_Price = (int)lst_MarketData.get(i).dbl_Low;
			if (n_Max_Price < lst_MarketData.get(i).dbl_Closing)
				n_Max_Price = (int)lst_MarketData.get(i).dbl_Closing;
		}
		
		n_Min_Price = n_Min_Price >= 10 ? n_Min_Price - 10 : 0;
		n_Max_Price += 10;
	}
	
	//System drawing function.
	@Override
	public void paint(Graphics g)
	{
		Color 		clr_Org 	= 	g.getColor();
		Dimension 	dim 		= 	getSize();
		
		g.setColor(new Color(0xEE, 0xEE, 0xEE));
		g.fillRect(0, 0, dim.width, dim.height);
	
		drawGraph(g, dim);
		drawRoadMap(g, dim);
		
		g.setColor(clr_Org);
	}
	
	
	//Function that draws 4 graphs.
	public void drawGraph(Graphics g, Dimension dim)
	{	
		//This variables are used to fit the graph to window size.
		int n_End_Date 		= (int)(lst_MarketData.get(0).dat_Date.getTime() / (24 * 3600 * 1000));
		int n_Start_Date 	= (int)(lst_MarketData.get(lst_MarketData.size() - 1).dat_Date.getTime() / (24 * 3600 * 1000));
		
		//Variables that store the x and y pos of graph point.
		int	x_pos[] 		= new int[lst_MarketData.size()];
		int	y_Low[] 		= new int[lst_MarketData.size()];
		int	y_High[] 		= new int[lst_MarketData.size()];
		int	y_Opening[] 	= new int[lst_MarketData.size()];
		int	y_Closing[] 	= new int[lst_MarketData.size()];
		
		g.setColor(Color.black);
		drawXYAxis(g, dim);
		
		//Get the x and y position of graphs. X position is the same and y position are calculated respectively.
		for (int i = 0; i < lst_MarketData.size(); i++)
		{
			x_pos[i] = (int)(lst_MarketData.get(i).dat_Date.getTime() / (24 * 3600 * 1000)) - n_Start_Date;
			x_pos[i] = x_pos[i] * (dim.width - 100) / (n_End_Date - n_Start_Date);
			x_pos[i] += 50;
			
			y_Low[i] 		= dim.height - 50 - (int)(lst_MarketData.get(i).dbl_Low - n_Min_Price) * (dim.height - 100) / (n_Max_Price - n_Min_Price);
			y_High[i] 		= dim.height - 50 - (int)(lst_MarketData.get(i).dbl_High - n_Min_Price) * (dim.height - 100) / (n_Max_Price - n_Min_Price);
			y_Opening[i] 	= dim.height - 50 - (int)(lst_MarketData.get(i).dbl_Opening - n_Min_Price) * (dim.height - 100) / (n_Max_Price - n_Min_Price);
			y_Closing[i] 	= dim.height - 50 - (int)(lst_MarketData.get(i).dbl_Closing - n_Min_Price) * (dim.height - 100) / (n_Max_Price - n_Min_Price);
		}
		
		//Draw each graph according to the show state.
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(2f));
		
		if ((n_Graph_Flg & n_Opening) != 0)
		{	
			g.setColor(clr_Opening);
			g.drawPolyline(x_pos, y_Opening, lst_MarketData.size());
		}
		if ((n_Graph_Flg & n_High) != 0)
		{	
			g.setColor(clr_High);
			g.drawPolyline(x_pos, y_High, lst_MarketData.size());
		}
		if ((n_Graph_Flg & n_Low) != 0)
		{	
			g.setColor(clr_Low);
			g.drawPolyline(x_pos, y_Low, lst_MarketData.size());
		}
		if ((n_Graph_Flg & n_Closing) != 0)
		{	
			g.setColor(clr_Closing);
			g.drawPolyline(x_pos, y_Closing, lst_MarketData.size());
		}
			
	}
	
	public void drawXYAxis(Graphics g, Dimension dim)
	{	
		g.setColor(Color.black);
		//Draw x & y axis;
		g.drawLine(50, 50, 50, dim.height - 50);
		g.drawLine(50, dim.height - 50, dim.width - 50, dim.height - 50);
		
		n_Interval = 50;
		
		FontMetrics fm 		= g.getFontMetrics();
		
		//Draw y axis string.
		for (int i = dim.height - 50; i >= 50; i -= n_Interval)
		{
			int value = (dim.height - 50 - i) * (n_Max_Price - n_Min_Price) / (dim.height - 100) + n_Min_Price;
			Rectangle2D rect 	= fm.getStringBounds(new Integer(value).toString(), g);
			g.drawString(new Long(value).toString(), 50 - (int)rect.getWidth(), i);
			g.drawLine(50, i, 55, i);
		}
		
		n_Interval = 100;
		
		//Draw x axis string.
		for (int i = 50; i < dim.getWidth() - 50; i += n_Interval)
		{
			int 		index 		= 	(int) ((i - 50) * lst_MarketData.size() / (this.getSize().getWidth() - 100));
			String 		date 		= 	dateToString(lst_MarketData.get(lst_MarketData.size() - 1 - index).dat_Date);
			Rectangle2D rect 	= 	fm.getStringBounds(date, g);
			
			g.drawString(date, (int) (i - rect.getWidth() / 2), dim.height - 30);
			g.drawLine(i, dim.height - 55, i, dim.height - 50);
		}
		
		g.setColor(new Color(200, 200, 200));
		for (int i = dim.height - 50; i >= 50; i -= 50)
		{
			for (int j = 50; j < dim.getWidth() - 50; j += 100)
			{
				g.drawLine(50, i, dim.width - 50, i);
				g.drawLine(j, 50, j, dim.height - 50);
			}
		}
	}
	
	//Function that converts date to string.
	public String dateToString(Date date)
	{
		String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return String.format("%s-%d-%d", months[calendar.get(Calendar.MONTH)], calendar.get(Calendar.DATE), calendar.get(Calendar.YEAR));
	}
	
	//Function that draws roadmap.
	public void drawRoadMap(Graphics g, Dimension dim)
	{
		g.setColor(Color.black);
		
		g.drawString("Opening:", dim.width - 100, 50);
		g.drawString("High:", dim.width - 100, 70);
		g.drawString("Low:", dim.width - 100, 90);
		g.drawString("Closing:", dim.width - 100, 110);
		
		g.setColor(clr_Opening);
		g.drawLine(dim.width - 40, 45, dim.width, 45);
		
		g.setColor(clr_High);
		g.drawLine(dim.width - 40, 65, dim.width, 65);
		
		g.setColor(clr_Low);
		g.drawLine(dim.width - 40, 85, dim.width, 85);
		
		g.setColor(clr_Closing);
		g.drawLine(dim.width - 40, 105, dim.width, 105);
		
	}
}
