package com.chatbot.app;

import java.util.*;

import javax.servlet.http.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import result.MovieInfo;
import result.MovieTheater;

@RestController
public class MovieInfoController {
	HashMap<String, Object> resultJson = new HashMap<>();
    @RequestMapping(value = "/MovieInfo" , method= {RequestMethod.POST , RequestMethod.GET },headers = {"Accept=application/json"})
    public HashMap<String,Object> callAPI(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
    	String rtnStr = "정상";
        

        try{
        	resultJson.clear();

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(params);
            System.out.println(jsonInString);

            /* 발화 처리 부분 * */
            HashMap<String,Object> userRequest =  (HashMap<String,Object>)params.get("userRequest");
            String utter = userRequest.get("utterance").toString().replace("\n","").replaceAll(" ", "");
            
            
            if(utter.contains("찾아")) {
            	utter=utter.substring(0,utter.indexOf("찾아"));
            }
            if(utter.contains("검색")) {
            	utter=utter.substring(0,utter.indexOf("검색"));
            }
            
            System.out.println(utter);
            if(utter.contains("영화관")) {
            	
	            List<HashMap<String,Object>> contents = new ArrayList<>();
	            HashMap<String,Object> content=new HashMap<>();
            	MovieTheater mt=new MovieTheater(utter);
            	JSONArray result=mt.getArray();
            	
            	if (result==null||result.size()==0) {
            		errorMessage("영화관을 찾을 수 없습니다.");
            	}else {
            		 List<HashMap<String,Object>> cards = new ArrayList<>();
 		            for(int i=0;result.size()>i;i++) {
 		            	JSONObject job=(JSONObject)result.get(i);
 			        	HashMap<String,Object> cardItem=new HashMap<>();
 			        	HashMap<String,Object> data=new HashMap<>();
 			        	HashMap<String,Object> button=new HashMap<>();
 			        	List<HashMap<String,Object>> buttons=new ArrayList<HashMap<String,Object>>();
 			        	
 			        	data.put("url", "https://map.kakao.com/?itemId="+job.get("id"));
 			        	button.put("data", data);
 			        	button.put("type","url");
 			        	button.put("label", "길찾기");
 			        	buttons.add(button);
 			        	
 			        	cardItem.put("imageUrl","https://maps.googleapis.com/maps/api/staticmap?"
 			        			+ "size=300x150&maptype=roadmap&"
 			        			+ "markers=color:blue%7Clabel:M%7C"+job.get("y")+","+job.get("x")+"&key=AIzaSyChCBWyFwD3hvphvjRVVUEK1Avp0EslEE0");
 			        	cardItem.put("title",job.get("place_name"));
 			        	cardItem.put("description",job.get("road_address_name"));
 			        	cardItem.put("linkUrl",job.get("https://map.kakao.com/?itemId="+job.get("id")));
 			        	cardItem.put("buttons", buttons);
 			        	
 			        	cards.add(cardItem);
 			        	
 		            }
 		            content.put("type", "card.image");
 		        	content.put("cards", cards);
 		        	contents.add(content);
 		        	resultJson.put("contents", contents);
            		
            	}
            	
            	
            }else {
	
	            MovieInfo mi=new MovieInfo(utter);
	            List<HashMap<String,Object>> contents = new ArrayList<>();
	            HashMap<String,Object> content=new HashMap<>();
	            Map<Integer, HashMap> info=mi.getInfo();
	            if(info.size()!=0) {
	            	
		            List<HashMap<String,Object>> cards = new ArrayList<>();
		            for(int i=0;info.size()>i;i++) {
			        	HashMap<String,Object> cardItem=new HashMap<>();
			        	String description="감독:"+info.get(i).get("director")+"\t 장르:"+info.get(i).get("genre")+"\n"+info.get(i).get("plot")+"";
			        	cardItem.put("imageUrl",info.get(i).get("poster"));
			        	cardItem.put("title",info.get(i).get("title"));
			        	cardItem.put("description",description);
			        	cardItem.put("linkUrl",info.get(i).get("url")+"");
			        	cards.add(cardItem);
			        	
		            }
		            content.put("type", "card.image");
		        	content.put("cards", cards);
		        	contents.add(content);
		        	resultJson.put("contents", contents);
	            }else {
	            	errorMessage("검색결과가 없습니다.");
	            }
            }
        	
        	
        	
            

            

           

        }catch (Exception e){
        	errorMessage("처리도중 오류가 발생하였습니다.");
        	
        	e.printStackTrace();
        	

        }finally {
        	
        	
		}

        return resultJson;
    }
    
    public void errorMessage(String message) {
    	List<HashMap<String,Object>> outputs = new ArrayList<>();
        HashMap<String,Object> template = new HashMap<>();
        HashMap<String, Object> simpleText = new HashMap<>();
        HashMap<String, Object> text = new HashMap<>();
        text.put("text",message);
        simpleText.put("simpleText",text);
        outputs.add(simpleText);

        template.put("outputs",outputs);

        resultJson.put("version","2.0");
        resultJson.put("template",template);
    }

}

