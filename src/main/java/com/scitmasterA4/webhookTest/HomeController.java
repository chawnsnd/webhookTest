package com.scitmasterA4.webhookTest;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private RestTemplate restTemplate;
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}

	@RequestMapping(value = "/papago", method = RequestMethod.POST)
	@ResponseBody
	public String papago(String token, String teamName, String roomName, String writerName, String text, String keyword,
			String createdAt) {
		
		String translateUrl = "https://openapi.naver.com/v1/papago/n2mt";
//		
//		source	String	Y	원본 언어(source language)의 언어 코드
//		target	String	Y	목적 언어(target language)의 언어 코드
//		text	String	Y	번역할 텍스트. 1회 호출 시 최대 5,000자까지 번역할 수 있습니다.
//		
		HashMap<String, String> req = new HashMap<String, String>();
		req.put("source", "ko");
		req.put("target", "ja");
		req.put("text", "안녕하세요");
		Map<String, String> res = restTemplate.postForObject(translateUrl, req, Map.class);
		String trnaslatedText = res.get("translatedText");
			
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("body", "안녕하세요 -> "+trnaslatedText);
//		data.put("body", "[핑퐁테스트] token: "+token+", teamName: "+teamName+", roomName: "+roomName+", writerName: "+writerName+", text: "+text+", keyword: "+keyword+", createdAt: "+createdAt);
		try {
			json = mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}
