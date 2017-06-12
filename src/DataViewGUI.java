import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

//This class shows market datas in a table.
public class DataViewGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JTable	tbl_Data;
	
	ArrayList<MarketData>	lst_MarketData  =	new ArrayList<MarketData>();
	
	JButton 		btn_Show_Other 			= 	new JButton("OtherGraphs");
	JButton 		btn_Show_Volume 		= 	new JButton("VolumeGraph");
	
	JPanel			pnl_Buttons				=	new JPanel();
	
	OtherGraphs		frm_Other_Graphs		=	new OtherGraphs();	//Frame that shows 4 graphs except volume graph.
	VolumeGraph		frm_Volume_Graph		=	new VolumeGraph();	//Frame that shows volume graph.
	
	public DataViewGUI()
	{
		frm_Other_Graphs.setSize(830,300);
		frm_Other_Graphs.setTitle("OtherGraphs");
		frm_Other_Graphs.setLocationRelativeTo(null);
		
		frm_Volume_Graph.setSize(830,300);
		frm_Volume_Graph.setTitle("VolumeGraph");
		frm_Volume_Graph.setLocationRelativeTo(null);
		
		btn_Show_Other.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (lst_MarketData.isEmpty())
					return;
				
				frm_Other_Graphs.setMarketData(lst_MarketData);
				frm_Other_Graphs.setVisible(true);
	        }
		});
		
		//This button shows a window that contains volume graph.
		btn_Show_Volume.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				if (lst_MarketData.isEmpty())
					return;
				
				frm_Volume_Graph.setMarketData(lst_MarketData);
				frm_Volume_Graph.setVisible(true);
	        }
		});
	}
	
	//Function that converts date to string.
	public String dateToString(Date date)
	{
		String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//Conversion format is "MMM-dd-yy"
		return String.format("%s-%d-%d", months[calendar.get(Calendar.MONTH)], calendar.get(Calendar.DATE), calendar.get(Calendar.YEAR));
	}
	
	//Function that sets data to the table.
	public void setData(String[] col_names, ArrayList<MarketData> datas)
	{
		String[][] dat = new String[datas.size()][6];
		for (int i = 0; i < datas.size(); i++)
		{
			dat[i][0] = dateToString(datas.get(i).dat_Date);
			dat[i][1] = new Double(datas.get(i).dbl_Opening).toString();
			dat[i][2] = new Double(datas.get(i).dbl_High).toString();
			dat[i][3] = new Double(datas.get(i).dbl_Low).toString();
			dat[i][4] = new Double(datas.get(i).dbl_Closing).toString();
			dat[i][5] = new Long(datas.get(i).lng_Volume).toString();
		}
		tbl_Data = new JTable(dat, col_names);
		
		lst_MarketData = datas;
		
		this.setResizable(false);
		this.setLayout(new FlowLayout());
		this.add(new JScrollPane(tbl_Data));
		this.setTitle("Market Data");
		
		pnl_Buttons.setLayout(new GridLayout(2, 1));
		pnl_Buttons.add(btn_Show_Other);
		pnl_Buttons.add(btn_Show_Volume);
		
		this.add(pnl_Buttons);
		
		//this.add(btn_Show_Other);
		//btn_Show_Volume.setLocation(new Point((int)btn_Show_Other.getLocation().getX(), (int)btn_Show_Other.getLocation().getY() + 20));
		//this.add(btn_Show_Volume);
		
		this.pack();
	}

}
