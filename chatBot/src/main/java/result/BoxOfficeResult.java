package result;

import java.io.BufferedInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.*;
import org.json.simple.parser.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatbot.app.testController;

public class BoxOfficeResult {
	private static final Logger logger =  LoggerFactory.getLogger(testController.class);
	JSONParser jsonparser = new JSONParser();
	JSONObject jsonobject = (JSONObject) jsonparser.parse(readUrl());
	JSONObject json = (JSONObject) jsonobject.get("boxOfficeResult");
	JSONArray array = (JSONArray) json.get("dailyBoxOfficeList");
	Map list = new HashMap<String, String>();
	
	


	public BoxOfficeResult() throws Exception {
			String[] movies = new String[array.size()];
			for (int i = 0; i < array.size(); i++) {
			JSONObject entity = (JSONObject) array.get(i);
			movies[i] = (String) entity.get("movieNm");
			}
			System.out.println(jsonobject.toJSONString());
			for(int i=0;i<movies.length;i++) {
				list.put(i+1, movies[i]);
				System.out.println(list.get(i+1));
			}

	}
	
	public Map getList() {
		return list;
	}
	
	
	

	private static String readUrl() throws Exception {
		SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
		Calendar time = Calendar.getInstance();
		time.add(Calendar.DATE, -1);
		String dt = today.format(time.getTime());
		String key = "db5de7f822df67e104adca04fb5e2324";
		BufferedInputStream reader = null;
		try {
			URL url = new URL("http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/"
					+ "searchDailyBoxOfficeList.json" + "?key=" +key+"&targetDt="+dt);
			reader = new BufferedInputStream(url.openStream());
			StringBuffer buffer = new StringBuffer();
			int i;
			byte[] b = new byte[4096];
			while ((i = reader.read(b)) != -1) {
				buffer.append(new String(b, 0, i));
			}
			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}

}

