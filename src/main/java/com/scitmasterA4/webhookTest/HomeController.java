package com.scitmasterA4.webhookTest;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
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

	String translateUrl = "https://openapi.naver.com/v1/papago/n2mt";
	String XNaverClientId = "Bh9GX6eaqwK4vmyWdvGV";
	String XNaverClientSecret = "E3JIZxnlHD";

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
	
	@RequestMapping(value = "/papago", method = RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String papago(@RequestBody Map<String, Object> request, HttpServletResponse httpServletResponse) {
		
		String text = (String) request.get("text");
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", XNaverClientId);
		headers.set("X-Naver-Client-Secret", XNaverClientSecret);		
		HashMap<String, String> body1 = new HashMap<String, String>();
		body1.put("query", text);
		HttpEntity<HashMap> req1 = new HttpEntity<HashMap>(body1, headers);
		String detectUrl = "https://openapi.naver.com/v1/papago/detectLangs";
		Map<String, String> res1 = restTemplate.postForObject(detectUrl, req1, Map.class);

		String source = res1.get("langCode");
		String target = "";
		if(source.equals("ko")) {
			target = "ja";
		}else {
			target = "ko";
		}
		HashMap<String, String> body2 = new HashMap<String, String>();
		body2.put("source", source);
		body2.put("target", target);
		body2.put("text", text);
		HttpEntity<HashMap> req2 = new HttpEntity<HashMap>(body2, headers);
		
		Map<String, Map<String, Map<String, String>>> res2 = restTemplate.postForObject(translateUrl, req2, Map.class);
		String srcLangType = res2.get("message").get("result").get("srcLangType"); //번역할 원본 언어의 언어 코드
		String tarLangType = res2.get("message").get("result").get("tarLangType"); //번역한 목적 언어의 언어 코드
		String translatedText = res2.get("message").get("result").get("translatedText"); //번역된 텍스트

		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("body", "원본글: "+text+"("+source+"), 번역글"+text+"("+target+")");
		data.put("connectColor", "#FAC11B");
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
