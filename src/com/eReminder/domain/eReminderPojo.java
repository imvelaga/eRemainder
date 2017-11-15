package com.eReminder.domain;

import java.util.List;

public class eReminderPojo {

	private String mail;
	private String subject;
	private String message;
	private String recurrence;
	private String daily;
	private String monthly;
	private String yearly;
	private String month;
	private String dailyDays;
	private String hrs;
	private String min;
	private String CronExpression;
	private int serial;
	private List<String> weekCheckboxs;
	private String weekdays;
	private int period;
	private int monthlyTimes1;
	private int monthlyTimes2;

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public String getCronExpression() {
		return CronExpression;
	}

	public void setCronExpression(String cronExpression) {
		CronExpression = cronExpression;
	}

	public String getYearly() {
		return yearly;
	}

	public void setYearly(String yearly) {
		this.yearly = yearly;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getWeekdays() {
		return weekdays;
	}

	public void setWeekdays(String weekdays) {
		this.weekdays = weekdays;
	}

	public int getMonthlyTimes2() {
		return monthlyTimes2;
	}

	public void setMonthlyTimes2(int monthlyTimes2) {
		this.monthlyTimes2 = monthlyTimes2;
	}

	public int getMonthlyTimes1() {
		return monthlyTimes1;
	}

	public void setMonthlyTimes1(int monthlyTimes1) {
		this.monthlyTimes1 = monthlyTimes1;
	}

	public String getMonthly() {
		return monthly;
	}

	public void setMonthly(String monthly) {
		this.monthly = monthly;
	}

	public List<String> getWeekCheckboxs() {
		return weekCheckboxs;
	}

	public void setWeekCheckboxs(List<String> weekCheckboxs) {
		this.weekCheckboxs = weekCheckboxs;
	}

	public String getHrs() {
		return hrs;
	}

	public void setHrs(String hrs) {
		this.hrs = hrs;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMail() {
		return mail;
	}

	public String getRecurrence() {
		return recurrence;
	}

	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDaily() {
		return daily;
	}

	public void setDaily(String daily) {
		this.daily = daily;
	}

	public String getDailyDays() {
		return dailyDays;
	}

	public void setDailyDays(String dailyDays) {
		this.dailyDays = dailyDays;
	}

}
