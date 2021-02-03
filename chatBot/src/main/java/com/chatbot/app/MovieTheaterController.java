package com.chatbot.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import result.BoxOfficeResult;
import result.MovieTheater;

@RestController
public class MovieTheaterController {
	
	private static final Logger logger =  LoggerFactory.getLogger(testController.class);
	
	@RequestMapping(value="/theater")
	public Map keyboard() throws ParseException, Exception {
		Map list = null;
		MovieTheater mt=new MovieTheater("안산시 영화관");
		List<HashMap<String,Object>> contents = new ArrayList<>();
        HashMap<String,Object> content=new HashMap<>();
    	JSONArray result=mt.getArray();
    	HashMap<String,Object> button=new HashMap<>();
    		 List<HashMap<String,Object>> cards = new ArrayList<>();
	            for(int i=0;result.size()>i;i++) {
	            	JSONObject job=(JSONObject)result.get(i);
		        	HashMap<String,Object> cardItem=new HashMap<>();
		        	List<HashMap<String,Object>> buttons=new ArrayList<HashMap<String,Object>>();
		        	
		        	button.put("webLink", "https://map.kakao.com/?itemId="+job.get("id"));
		        	button.put("action","weblink");
		        	button.put("label", "길찾기");
		        	buttons.add(button);
		        	
		        	cardItem.put("imageUrl","");
		        	cardItem.put("title",job.get("place_name"));
		        	cardItem.put("description",job.get("road_address_name"));
		        	cardItem.put("linkUrl",job.get("place_url"));
		        	cardItem.put("buttons", buttons);
		        	
		        	cards.add(cardItem);
		        	
	            }
	            content.put("type", "card.image");
	        	content.put("cards", cards);
	        	contents.add(content);
		return content;
	}

}
