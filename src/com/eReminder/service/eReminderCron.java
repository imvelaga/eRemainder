package com.eReminder.service;

import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.eReminder.domain.eReminderPojo;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class eReminderCron {

	private eReminderService service;

	public eReminderService getService() {
		return service;
	}

	public void setService(eReminderService service) {
		this.service = service;
	}

	@SuppressWarnings("unused")
	private void executeInternal() throws ParseException,
			java.text.ParseException {

		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			List<eReminderPojo> data = service.getEmailService();

			for (eReminderPojo exptes : data) {
				String name = "jobId" + exptes.getSerial();
				JobDetail job = new JobDetail();
				job.setName(name);
				job.setJobClass(MailSender.class);
				scheduler.scheduleJob(job, getTrigger(exptes));
			}

		} catch (SchedulerException e) {
			System.out.println(e);
		}

	}

	private Trigger getTrigger(eReminderPojo exptes) throws ParseException,
			java.text.ParseException {

		CronTrigger trigger = new CronTrigger();
		String name = "jobId" + exptes.getSerial();
		trigger.setName(name);
		trigger.setGroup(exptes.getMail());
		trigger.setPriority(exptes.getSerial());
		trigger.setCronExpression(exptes.getCronExpression());
		return trigger;
	}

	public boolean updateJob(eReminderPojo newScheduleData,
			String cronExcepression) throws java.text.ParseException {
		boolean result = true;
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			JobDetail job = new JobDetail();
			String name = "jobId" + newScheduleData.getSerial();
			job.setName(name);
			job.setJobClass(MailSender.class);
			CronTrigger trigger = new CronTrigger();
			trigger.setName(name);
			trigger.setGroup(newScheduleData.getMail());
			trigger.setPriority(newScheduleData.getSerial());
			trigger.setCronExpression(cronExcepression);
			scheduler.deleteJob(name, "DEFAULT");
			//scheduler.unscheduleJob(name, name);
			scheduler.scheduleJob(job, trigger);

		} catch (SchedulerException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return result;
	}
}
