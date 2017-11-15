package com.eReminder.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eReminder.domain.JsonView;
import com.eReminder.domain.RegistrationDomain;
import com.eReminder.domain.eReminderPojo;
import com.eReminder.service.ApplicationContextProvider;
import com.eReminder.service.eReminderCron;
import com.eReminder.service.eReminderService;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class eReminderController {

	@Autowired
	private eReminderService service;

	private eReminderCron report;

	public eReminderCron getReport() {
		return report;
	}

	public void setReport(eReminderCron report) {
		this.report = report;
	}

	public eReminderService getService() {
		return service;
	}

	public void setService(eReminderService service) {
		this.service = service;
	}

	@SuppressWarnings("rawtypes")
	HashMap result = new HashMap();
	JsonView view = new JsonView();
	ModelMap map = new ModelMap();
	ObjectMapper mapper = new ObjectMapper();
	String jsonString;

	private static Logger logger = Logger.getLogger(eReminderController.class);

	@RequestMapping(value = "/welcome.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView SignIn() {
		return new ModelAndView("/VIEW/eReminderLogin.html");
	}

	@RequestMapping(value = "/home.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView login() {
		return new ModelAndView("/VIEW/eReminder.html");

	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@RequestMapping(value = "/newSchedule.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView GetGRPUsersList(HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException, ParseException, java.text.ParseException {

		eReminderCron cron = new eReminderCron();

		eReminderPojo newScheduleData = new eReminderPojo();
		String cronExcepression = null;
		mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		newScheduleData = mapper.readValue(request.getParameter("data"), eReminderPojo.class);
		if (newScheduleData.getRecurrence().equals("Daily")) {
			if (newScheduleData.getDaily().equals("Every")) {
				String days = newScheduleData.getDailyDays();
				String hrs = newScheduleData.getHrs();
				String min = newScheduleData.getMin();
				cronExcepression = "0 " + min + " " + hrs + " 1/" + days + " * ? *";
			} else if (newScheduleData.getDaily().equals("EveryWeekDays")) {
				String hrs = newScheduleData.getHrs();
				String min = newScheduleData.getMin();
				cronExcepression = "0 " + min + " " + hrs + " ? * MON-FRI *";
			}
		}

		else if (newScheduleData.getRecurrence().equals("Weekely")) {
			String hrs = newScheduleData.getHrs();
			String min = newScheduleData.getMin();
			String wdays = "";
			List<String> weekList = newScheduleData.getWeekCheckboxs();
			for (int i = 1; i <= weekList.size(); i++) {
				int a = i - 1;
				if (i < weekList.size()) {
					wdays = wdays + weekList.get(a) + ",";
				}
				if (i == weekList.size()) {
					wdays = wdays + weekList.get(a);
				}

			}
			cronExcepression = "0 " + min + " " + hrs + " ? * " + wdays + " *";
		}

		else if (newScheduleData.getRecurrence().equals("Monthly")) {
			if (newScheduleData.getMonthly().equals("monthlyNoOfDays")) {

				String days = newScheduleData.getDailyDays();
				int monthlyTimes1 = newScheduleData.getMonthlyTimes1();
				String hrs = newScheduleData.getHrs();
				String min = newScheduleData.getMin();

				cronExcepression = "0 " + min + " " + hrs + " " + days + " 1/" + monthlyTimes1 + " ? *";
			} else if (newScheduleData.getMonthly().equals("monthlyDays")) {

				String weekday = newScheduleData.getWeekdays();
				int monthlyTimes2 = newScheduleData.getMonthlyTimes2();
				int period = newScheduleData.getPeriod();
				String hrs = newScheduleData.getHrs();
				String min = newScheduleData.getMin();

				cronExcepression = "0 " + min + " " + hrs + " ?" + " 1/" + monthlyTimes2 + " " + weekday + "#" + period
						+ " *";
			}
		}

		else if (newScheduleData.getRecurrence().equals("Yearly")) {
			if (newScheduleData.getYearly().equals("EveryMonth")) {

				String days = newScheduleData.getDailyDays();
				String month = newScheduleData.getMonth();
				String hrs = newScheduleData.getHrs();
				String min = newScheduleData.getMin();

				cronExcepression = "0 " + min + " " + hrs + " " + days + " " + month + " ? *";
			} else if (newScheduleData.getYearly().equals("EveryPeriod")) {

				int period = newScheduleData.getPeriod();
				String weekday = newScheduleData.getWeekdays();
				String monthly = newScheduleData.getMonthly();
				String hrs = newScheduleData.getHrs();
				String min = newScheduleData.getMin();

				cronExcepression = "0 " + min + " " + hrs + " ? " + monthly + " " + weekday + "#" + period + " *";
			}

		}
		try {
			HttpSession session = request.getSession();
			Object UseriD = session.getAttribute("UserId");
			List<eReminderPojo> messages = service.newSchedule(newScheduleData, cronExcepression, UseriD);
			eReminderCron rept = ApplicationContextProvider.getApplicationContext().getBean("report",
					eReminderCron.class);
			newScheduleData.setSerial(messages.get(0).getSerial());

			boolean resultData = rept.updateJob(newScheduleData, cronExcepression);
			result.put("status", resultData);
			jsonString = mapper.writeValueAsString(result);

		} catch (Exception e) {
			logger.error("Error in controller" + e);
		}
		map.addObject("jsonString", jsonString);
		return new ModelAndView(view, map);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@RequestMapping(value = "/getSchedules.htm", method = RequestMethod.POST)
	public ModelAndView getAllSchedules(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Object id = session.getAttribute("UserId");
		int UserID = (Integer) id;
		List<eReminderPojo> messages = service.getAllSchedulesOfUser(UserID);

		try {
			result.put("reminders", messages);
			jsonString = mapper.writeValueAsString(result);
		} catch (Exception e) {
			logger.error("Error in controller" + e);
		}
		map.addObject("jsonString", jsonString);
		return new ModelAndView(view, map);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@RequestMapping(value = "/deleteSchedules.htm", method = RequestMethod.POST)
	public ModelAndView deleteSchedules(HttpServletRequest request, HttpServletResponse response) {

		eReminderPojo deleteIdPojo = new eReminderPojo();
		String deleteId = request.getParameter("data");
		deleteIdPojo.setSerial(Integer.parseInt(deleteId));
		String deleteThisJob = "jobId" + deleteId;

		boolean messages = service.deleteSchedules(deleteIdPojo);
		try {
			if (messages) {
				Scheduler scheduler = new StdSchedulerFactory().getScheduler();
				scheduler.start();
				scheduler.deleteJob(deleteThisJob, "DEFAULT");
				System.out.println("eReminder Deleted with Serail Number " + deleteId);
			}
			result.put("idDeleted", messages);
			jsonString = mapper.writeValueAsString(result);
		} catch (Exception e) {
			logger.error("Error in controller" + e);
		}
		map.addObject("jsonString", jsonString);
		return new ModelAndView(view, map);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@RequestMapping(value = "/Register.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView registration(HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException, NoSuchAlgorithmException {
		RegistrationDomain newRegistration = new RegistrationDomain();
		mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		newRegistration = mapper.readValue(request.getParameter("data"), RegistrationDomain.class);

		try {
			boolean status = service.Registration(newRegistration);
			result.put("isRegistered", status);
			jsonString = mapper.writeValueAsString(result);
		} catch (Exception e) {
			logger.error("Error in Registration controller" + e);
		}
		map.addObject("jsonString", jsonString);
		return new ModelAndView(view, map);

	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@RequestMapping(value = "/Login.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView Login(HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException, NoSuchAlgorithmException {
		RegistrationDomain newRegistration = new RegistrationDomain();
		mapper.configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		newRegistration = mapper.readValue(request.getParameter("data"), RegistrationDomain.class);

		try {
			List<RegistrationDomain> status = service.Login(newRegistration);
			HttpSession session = request.getSession();
			session.setAttribute("UserId", status.get(0).getUserId());
			result.put("isLoginSuccusful", status);
			jsonString = mapper.writeValueAsString(result);
		} catch (Exception e) {
			logger.error("Error in Registration controller" + e);
		}
		map.addObject("jsonString", jsonString);
		return new ModelAndView(view, map);

	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@RequestMapping(value = "/Action.htm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView Action(HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException, NoSuchAlgorithmException {
		boolean status = false;

		try {
			String action = request.getParameter("data");
			if (action.equals("logout")) {
				HttpSession session = request.getSession();
				session.removeAttribute("UserId");
				status = true;
			} else if (action.equals("deleteAccount")) {
				HttpSession session = request.getSession();
				Object iD = session.getAttribute("UserId");
				status = service.action(iD);
			}

			result.put("isActionSuccusful", status);
			jsonString = mapper.writeValueAsString(result);
		} catch (Exception e) {
			logger.error("Error in Registration controller" + e);
		}
		map.addObject("jsonString", jsonString);
		return new ModelAndView(view, map);

	}
}
