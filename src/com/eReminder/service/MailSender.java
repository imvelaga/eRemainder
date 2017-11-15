package com.eReminder.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MailSender implements Job {

	@Override
	public void execute(JobExecutionContext Job) throws JobExecutionException {

		int Serial = Job.getTrigger().getPriority();
		String email = Job.getTrigger().getGroup();
		eReminderService servc = ApplicationContextProvider
				.getApplicationContext().getBean("service",
						eReminderService.class);
		servc.details(Serial);
		System.out.println("mail sent to : " + email);

	}

}
