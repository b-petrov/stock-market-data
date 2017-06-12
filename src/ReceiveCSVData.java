import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

//This class retrieves market data from google site. This is the sample code in bing.

public class ReceiveCSVData {
	
	public static void Receive(String urlpath, ArrayList<MarketData> lst_MarketData) {
		
		int count = 0;
		
        try {
        	
            URL url = new URL(urlpath);
            URLConnection urlConn = url.openConnection();
            
            urlConn.setConnectTimeout(60 * 1000);
            urlConn.setReadTimeout(60 * 1000);
            
            InputStreamReader inputStreamReader = new InputStreamReader(urlConn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            
            while ((line = bufferedReader.readLine()) != null) {
            	
            	if (count ++ == 0) continue;
            	
            	String[] datas = line.split(",");
            	
    			lst_MarketData.add(new MarketData(datas));
            }
            
            bufferedReader.close();
            inputStreamReader.close();
            
        } catch (Exception e) {
        	
            e.printStackTrace();
            
        }
    }
}
