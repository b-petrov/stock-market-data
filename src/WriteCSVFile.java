import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//This class write Market Data into a CSV file.
public class WriteCSVFile {
	
	//Function to write Market Data into a CSV file
	public static void generateCSVFile(ArrayList<MarketData> lst_MarketData, String tck_Symbol)
	{
		//This Variable is used to format the current date&time.
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");  
		
        try
    	{
        	//File is named as "<Current Date&Time>_<Ticker Symbol>.csv"
    	    FileWriter writer = new FileWriter(df.format(new Date()) + "_" + tck_Symbol + ".csv");
    		
    	    //File is written in order.
    	    writer.append("Date,Open,High,Low,CLose,Volume\n");
    	    
    	    for (int i = 0; i < lst_MarketData.size(); i++)
    	    {
    	    	MarketData data = lst_MarketData.get(i);
    	    	
    	    	df = new SimpleDateFormat("dd-MMM-yyyy");
    	    	
    	    	writer.append(df.format(data.dat_Date) + ',');
    	    	writer.append(new Double(data.dbl_Opening).toString() + ',');
    	    	writer.append(new Double(data.dbl_High).toString() + ',');
    	    	writer.append(new Double(data.dbl_Low).toString() + ',');
    	    	writer.append(new Double(data.dbl_Closing).toString() + ',');
    	    	writer.append(new Long(data.lng_Volume).toString() + '\n');
    	    }
    			
    	    writer.flush();
    	    writer.close();
    	}
    	catch(IOException e)
    	{
    	     e.printStackTrace();
    	} 
	}
}
