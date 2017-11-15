package com.eReminder.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.eReminder.dao.eReminderDAO;
import com.eReminder.domain.RegistrationDomain;
import com.eReminder.domain.eReminderPojo;

public class eReminderService {

	@Autowired
	private eReminderDAO dao;

	@Autowired
	private JavaMailSenderImpl mailSender;

	private static Logger logger = Logger.getLogger(eReminderService.class);

	public List<eReminderPojo> newSchedule(eReminderPojo newSchedule,
			String cronExcepression, Object useriD) {
		return dao.newSchedule(newSchedule, cronExcepression,useriD);
	}

	public List<eReminderPojo> getEmailService() {
		List<eReminderPojo> recipients = dao.eReminder();
		return recipients;
	}

	public void details(int serial) {
		List<eReminderPojo> mailDetails = dao.details(serial);

		for (int i = 0; i < mailDetails.size(); i++) {
			String recipientAddress = mailDetails.get(i).getMail();
			String subject = mailDetails.get(i).getSubject();
			String message = mailDetails.get(i).getMessage();
			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo(recipientAddress);
			email.setSubject(subject);
			email.setText(message);
			mailSender.send(email);

		}
	}

	public List<eReminderPojo> getAllSchedulesOfUser(int userID) {
		return dao.getAllSchedulesOfUser(userID);
	}

	public boolean deleteSchedules(eReminderPojo deleteIdPojo) {
		return dao.deleteSchedules(deleteIdPojo);
	}

	public boolean Registration(RegistrationDomain newRegistration) throws NoSuchAlgorithmException {
		return dao.Registration(newRegistration);
	}

	public List<RegistrationDomain> Login(RegistrationDomain newRegistration) {
		return dao.Login(newRegistration);
	}

	public boolean action(Object iD) {
		return dao.action(iD);
	}
}
