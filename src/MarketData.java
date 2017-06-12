import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

//This class is used to store one market data.
public class MarketData{
	
	//These variables are matched to market data fields.
	Date		dat_Date;
	long		lng_Volume;
	double		dbl_Opening;
	double		dbl_High;
	double		dbl_Low;
	double		dbl_Closing;
	
	//This is constructor and converts string variable to market data.
	public MarketData(String []datas) throws ParseException
	{
		dat_Date = new SimpleDateFormat("dd-MMM-yy").parse(datas[0]);
		
		dbl_Opening = Double.parseDouble(datas[1]);
		dbl_High = Double.parseDouble(datas[2]);
		dbl_Low = Double.parseDouble(datas[3]);
		dbl_Closing = Double.parseDouble(datas[4]);
		lng_Volume = Long.parseLong(datas[5]);
	}
	
	public String toString()
	{	
		return String.format("%s, %f, %f, %f, %f, %d", dat_Date.toString(), dbl_Opening, dbl_High, dbl_Low, dbl_Closing, lng_Volume);
	}
}