import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

//This class will never used in actual use. I made this class because I couldn't connect to google.
//So I made this class to retrieve data from file(not from google) to develop faster.
//After finished testing with data from file I sent this code to my friend to verify this code.

public class ReadCSVFile {
	
	public void readCSVFile(String csvFile, ArrayList<MarketData> lst_MarketData) throws ParseException
	{
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int	lineCount = 0;

		try {

			br = new BufferedReader(new FileReader(csvFile));
			
			while ((line = br.readLine()) != null) {
				
				if (lineCount++ == 0) continue;
				String[] datas = line.split(cvsSplitBy);
				lst_MarketData.add(new MarketData(datas));
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if (br != null) {
				
				try {
					br.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
