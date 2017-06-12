import java.awt.event.ActionListener;
import java.text.ParseException;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;


//This class is the main GUI class.
public class MarketGUI {
	
	Date			dat_From_Date			=	new Date();			//Variable to store the start date of market data.
	Date			dat_To_Date				=	new Date();			//Variable to store the end date of market data.
	JFrame  		frm_Main				= 	new JFrame();		//Main frame that we meet first.
	DataViewGUI		frm_Data_View			=	new DataViewGUI();	//Frame that shows market data in a table.
	
	//These are widgets that appear in the main frame.
	JPanel			pnl_Settings			=	new JPanel();
	JPanel 			pnl_Tck_Symbol			= 	new JPanel();
	JPanel			pnl_To_Date				=	new JPanel();
	JPanel			pnl_From_Date			=	new JPanel();
	JPanel			pnl_Buttons				=	new JPanel();
	
	JLabel			lbl_To_Date				=	new JLabel("To:          ");
	JLabel			lbl_From_Date			=	new JLabel("From:     ");
	JLabel 			lbl_Tck_Symbol			= 	new JLabel("TickerSymbol:");
	JLabel			lbl_Empty1				=	new JLabel("                           ");
	JLabel			lbl_Empty2				=	new JLabel("                           ");
	JLabel			lbl_Empty3				=	new JLabel("                           ");
	
	JButton			btn_Retrieve_Data		=	new JButton("RetrieveData");
	JButton			btn_To_Date				=	new JButton("...");
	JButton			btn_From_Date			=	new JButton("...");
	
	//These two variables are used to show start and end date that is selected by the user.
	String			str_From_Date			=	"";
	String			str_To_Date				=	"";
	
	String[] 			str_Tck_Symbols		= 	{"Microsoft(MSFT)", "Apple(AAPL)"};
	JComboBox<String>  	cb_Tck_Symbol		= 	new JComboBox<String>(str_Tck_Symbols);
	
	JEditorPane		edt_To_Date				=	new JEditorPane();
	JEditorPane		edt_From_Date			=	new JEditorPane();
	
	ArrayList<MarketData> lst_MarketData 	= 	new ArrayList<MarketData>();
	
	public static void main(String[] args) {
		  
		new MarketGUI();
		
	}
	
	//This function generates url according to the setting.(for example "http://www.google.com/finance/historical?q=AAPL&histperiod=daily&startdate=Jan+1+2014&en
	//ddate=Dec+31+2014&output=csv
	
	public String GetURL()
	{
		Date current_Date = new Date();
		
		//If start date is not selected.
		if (str_From_Date.equals("") == true)
		{
			JOptionPane.showMessageDialog(null, "Please select start date.", "Notification", JOptionPane.INFORMATION_MESSAGE);
			return "";
		}
		
		//If end date is not selected.
		if (str_To_Date.equals("") == true)
		{
			JOptionPane.showMessageDialog(null, "Please select end date.", "Notification", JOptionPane.INFORMATION_MESSAGE);
			return "";
		}
		
		//If start date is later than end date.
		if (dat_From_Date.getTime() > dat_To_Date.getTime())
		{
			JOptionPane.showMessageDialog(null, "Please select start & end date correctly.", "Notification", JOptionPane.INFORMATION_MESSAGE);
			return "";
		}
		
		//If start date is later than current date.
		if (dat_From_Date.getTime() > current_Date.getTime())
		{
			JOptionPane.showMessageDialog(null, "Please select start date correctly.", "Notification", JOptionPane.INFORMATION_MESSAGE);
			return "";
		}
		
		//If end date is later than current date.
		if (dat_To_Date.getTime() > current_Date.getTime())
		{
			JOptionPane.showMessageDialog(null, "Please select end date correctly.", "Notification", JOptionPane.INFORMATION_MESSAGE);
			return "";
		}
		
		//Generate url from settings.
		String returnStr 	= "http://www.google.com/finance/historical?";
		String tck_Symbol 	= cb_Tck_Symbol.getSelectedItem().toString();
		
		tck_Symbol = tck_Symbol.substring(tck_Symbol.indexOf('(') + 1, tck_Symbol.indexOf(')'));
		
		returnStr += "q=" + tck_Symbol + "&histperiod=daily&startdate=" + str_From_Date + "&enddate=" + str_To_Date + "&output=CSV";
		
		return returnStr;
	}

	
	public MarketGUI()
	{
		//Following codes are used to layout.
		
		frm_Main.setSize(450,220);
		frm_Main.setTitle("MarketGUI");
		frm_Main.setLocationRelativeTo(null);
		frm_Main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
		pnl_Tck_Symbol.add(lbl_Tck_Symbol);
		pnl_Tck_Symbol.add(cb_Tck_Symbol);
		
		pnl_To_Date.add(lbl_To_Date);
		pnl_To_Date.add(edt_To_Date);
		pnl_To_Date.add(btn_To_Date);
		
		pnl_From_Date.add(lbl_From_Date);
		pnl_From_Date.add(edt_From_Date);
		pnl_From_Date.add(btn_From_Date);
		
		pnl_Settings.add(pnl_Tck_Symbol);
		pnl_Settings.add(lbl_Empty1);
		
		pnl_Settings.add(pnl_From_Date);
		pnl_Settings.add(lbl_Empty2);
		
		pnl_Settings.add(pnl_To_Date);
		pnl_Settings.add(lbl_Empty3);
		
		pnl_Buttons.setLayout(new GridLayout(6, 1));
		pnl_Buttons.add(btn_Retrieve_Data);
		
   
		//Following code are used to add listeners to each button.
		
		//This button shows date picker window and select the start date.
		btn_From_Date.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				DatePicker picker = new DatePicker(frm_Main);
				edt_From_Date.setText(picker.showDateFormat());
				str_From_Date = picker.googleDateFormat();
				
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(picker.getYear(), picker.getMonth(), picker.getDay());
				dat_From_Date = cal.getTime();
	        }
		});
		
		//This button shows date picker window and select the end date.
		btn_To_Date.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				DatePicker picker = new DatePicker(frm_Main);
				edt_To_Date.setText(picker.showDateFormat());
				str_To_Date = picker.googleDateFormat();
				
				java.util.Calendar cal = java.util.Calendar.getInstance();
				cal.set(picker.getYear(), picker.getMonth(), picker.getDay());
				dat_To_Date = cal.getTime();
	        }
		});
		
		//This button retrieve data from google site.
		btn_Retrieve_Data.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				lst_MarketData.clear();
				String url = GetURL();
				if (url.equals(""))
					return;
					
				ReceiveCSVData.Receive(url, lst_MarketData);
				
				/*try {
					new ReadCSVFile().readCSVFile("aapl.csv", lst_MarketData);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				if (lst_MarketData.isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Data Retrieval Failed.", "Notification", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				String selected = cb_Tck_Symbol.getSelectedItem().toString();
				WriteCSVFile.generateCSVFile(lst_MarketData, selected.substring(selected.indexOf('(') + 1, selected.indexOf(')')));
				
				
				JOptionPane.showMessageDialog(null, "Data Retrieved Successfully.", "Notification", JOptionPane.INFORMATION_MESSAGE);
				
				String[] columns = {"Date", "Open", "High", "Low", "Close", "Volume"};
				
				frm_Data_View = new DataViewGUI();
				frm_Data_View.setData(columns, lst_MarketData);
				frm_Data_View.setVisible(true);
	        }
		});
		
		//This button shows a window that contains table of market data.
		
		//This button shows a window that contains 4 graphs(except volume graph).
		
		
		frm_Main.setVisible(true);
		frm_Main.setLayout(new GridLayout(1, 2));
		
		frm_Main.add(pnl_Settings);
		frm_Main.add(pnl_Buttons);
	}
}