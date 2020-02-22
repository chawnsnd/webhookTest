package com.scitmasterA4.webhookTest;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

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
		logger.debug(token);
		logger.debug(teamName);
		logger.debug(roomName);
		logger.debug(writerName);
		logger.debug(text);
		logger.debug(keyword);
		logger.debug(createdAt);
		
//		{
//			"body" : "[[PizzaHouse]](http://url_to_text) You have a new Pizza order.",
//			"connectColor" : "#FAC11B",
//			"connectInfo" : [{
//			"title" : "Topping",
//			"description" : "Pepperoni"
//			},
//			{
//			"title": "Location",
//			"description": "Empire State Building, 5th Ave, New York",
//			}]
//			}
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("body", "[핑퐁테스트]"+text);
		try {
			json = mapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}
