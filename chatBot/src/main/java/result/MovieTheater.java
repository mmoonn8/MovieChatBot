package result;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chatbot.app.testController;

public class MovieTheater {
	private static final Logger logger =  LoggerFactory.getLogger(testController.class);
	JSONParser jsonparser = new JSONParser();
	JSONArray jarray=null;
	
	public MovieTheater(String keyword) throws ParseException, Exception {
		
			JSONParser jsonparser = new JSONParser();
			String result = readUrl(keyword);
			
			if(!result.contains("error")) {
			JSONObject jsonobject = (JSONObject) jsonparser.parse(result);
			jarray= (JSONArray) jsonobject.get("documents");
			System.out.println(jarray.get(0));
			}
	}
	
	public JSONArray getArray() {
		return jarray;
	}
	
	
	private static String readUrl(String keyword) throws Exception {
        String jsonString = new String();
        String buf;
		try {
 
            String addr = "https://dapi.kakao.com/v2/local/search/keyword.json";
            String apiKey = "KakaoAK 135f823323aa5ccafaa26493acf0efa5";
            keyword = URLEncoder.encode(keyword, "UTF-8");
            String query = "page=1&size=10&query=" + keyword;
            
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(addr);
            stringBuffer.append("?");
            stringBuffer.append(query);
            
            System.out.println("stringBuffer.toString() "+ stringBuffer.toString());
            
            URL url = new URL(stringBuffer.toString());
            
            URLConnection conn = url.openConnection();
            
            conn.setRequestProperty("Authorization", apiKey);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"));
            while ((buf = br.readLine()) != null) {
                jsonString += buf;
                
                System.out.println(jsonString);
            }
            return jsonString;
            
        }catch(Exception e) {
            e.printStackTrace();
            return "error";
        }
	}

}
