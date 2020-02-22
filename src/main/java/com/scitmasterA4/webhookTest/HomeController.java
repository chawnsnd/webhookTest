package com.scitmasterA4.webhookTest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

	@RequestMapping(value = "/papago", method = RequestMethod.GET)
	public String goPapago() {
		return "papago";
	}
	
	@RequestMapping(value = "/papago", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
	@ResponseBody
	public String papago(String token, String teamName, String roomName, String writerName, String text, String keyword,
			String createdAt, HttpServletResponse httpServletResponse) {
		
		String translateUrl = "https://openapi.naver.com/v1/papago/n2mt";
		String XNaverClientId = "Bh9GX6eaqwK4vmyWdvGV";
		String XNaverClientSecret = "E3JIZxnlHD";

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", XNaverClientId);
		headers.set("X-Naver-Client-Secret", XNaverClientSecret);		
		HashMap<String, String> body = new HashMap<String, String>();
		body.put("source", "ko");
		body.put("target", "ja");
		body.put("text", "안녕하세요");
		HttpEntity<HashMap> req = new HttpEntity<HashMap>(body, headers);
		
		Map<String, Map<String, Map<String, String>>> res = restTemplate.postForObject(translateUrl, req, Map.class);
		String srcLangType = res.get("message").get("result").get("srcLangType"); //번역할 원본 언어의 언어 코드
		String tarLangType = res.get("message").get("result").get("tarLangType"); //번역한 목적 언어의 언어 코드
		String translatedText = res.get("message").get("result").get("translatedText"); //번역된 텍스트
		
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("body", "파파고번역");
		data.put("connectColor", "#FAC11B");
//		data.put("body", "[핑퐁테스트] token: "+token+", teamName: "+teamName+", roomName: "+roomName+", writerName: "+writerName+", text: "+text+", keyword: "+keyword+", createdAt: "+createdAt);
		try {
			json = mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		httpServletResponse.addHeader("Accept", "application/vnd.tosslab.jandi-v2+json");
		httpServletResponse.addHeader("Content-Type", "application/json");
		return json;
	}
}
