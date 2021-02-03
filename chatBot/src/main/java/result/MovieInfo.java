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
public class MovieInfo {
	private static final Logger logger =  LoggerFactory.getLogger(testController.class);
	Map<Integer, HashMap> movies=new HashMap<Integer, HashMap>();

	public MovieInfo(String utter) throws ParseException, Exception {
		JSONParser jsonparser = new JSONParser();
		JSONObject jsonobject = (JSONObject) jsonparser.parse(readUrl(utter));
		JSONObject json = (JSONObject)((JSONArray)jsonobject.get("Data")).get(0);
		int totalCount=Integer.parseInt(""+json.get("TotalCount"));
		
		if(totalCount>5) totalCount=5;
		for(int i=0;totalCount>i;i++) {
			JSONObject result=(JSONObject)((JSONArray)json.get("Result")).get(i);
			HashMap<String,Object> info = new HashMap<String,Object>();
			JSONArray directors=(JSONArray)((JSONObject)result.get("directors")).get("director");
			String director="";
			String posters=(result.get("posters")+"").substring(0,(result.get("posters")+"").indexOf("|")+1);
			
			if(posters.length()!=0) {
				posters=posters.substring(0,posters.length()-1);
			}else {
				posters=result.get("posters")+"";
			}
			if(posters.length()==0) {
				posters="https://3.bp.blogspot.com/-ZKBbW7TmQD4/U6P_DTbE2MI/AAAAAAAADjg/wdhBRyLv5e8/s1600/noimg.gif";
			}
			
			for(int j=0;directors.size()>j;j++){
				JSONObject thisPD=(JSONObject)directors.get(j);
				if(j==0) {
					director=director+thisPD.get("directorNm");
				}else {
					director=director+","+thisPD.get("directorNm");
				}
			}
			
			
			info.put("director", director.replaceAll(" !HS ", "").replaceAll(" !HE ",""));
			JSONObject plot=(JSONObject)((JSONArray)((JSONObject)result.get("plots")).get("plot")).get(0);
			String plotText=plot.get("plotText")+"";
			if(plotText.length()>100) {
				plotText=plotText.substring(0,100)+"...";
			}
			info.put("plot",plotText.replaceAll(" !HS ", "").replaceAll(" !HE ",""));
			info.put("genre",(result.get("genre")+"").replaceAll(" !HS ", "").replaceAll(" !HE ",""));
			info.put("url", result.get("kmdbUrl"));
			info.put("poster", posters);
			info.put("title",(result.get("title")+"("+result.get("prodYear")+")").replaceAll(" !HS ", "").replaceAll(" !HE ",""));
			movies.put(i, info);
			
		}
		System.out.println(movies);

		
		
	}
	public Map getInfo(){
		return movies;
	}
	
	private static String readUrl(String keyword) throws Exception {

		
		String key = "P7B6843472999U350X8O";
		String searchKey="title";
		if(keyword.contains("감독")) {
			searchKey="director";
			keyword=keyword.replaceAll("감독", "");
		}else if(keyword.contains("줄거리")) {
			searchKey="plot";
			keyword=keyword.replaceAll("줄거리", "");
		}else if(keyword.contains("배우")) {
			searchKey="actor";
			keyword=keyword.replaceAll("배우", "");
		}else if(keyword.contains("제목")) {
			keyword=keyword.replaceAll("제목", "");
		}else if(keyword.contains("장르")) {
			searchKey="genre";
			keyword=keyword.replaceAll("장르", "");
		}
		
		BufferedInputStream reader = null;
		try {
			URL url = new URL("http://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?"
					+ "collection=kmdb_new2&detail=Y"
					+ "&"+searchKey+"="+keyword+"&sort=prodYear,1&ServiceKey="+key+"&listCount=5");
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
