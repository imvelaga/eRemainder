package com.eReminder.dao;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.eReminder.domain.RegistrationDomain;
import com.eReminder.domain.eReminderPojo;


public interface eReminderDAO {

	List<com.eReminder.domain.eReminderPojo> newSchedule(eReminderPojo newSchedule, String cronExcepression, Object useriD);
	
	List<eReminderPojo> eReminder();

	List<eReminderPojo> details(int serial);

	boolean deleteSchedules(eReminderPojo deleteIdPojo);

	boolean Registration(RegistrationDomain newRegistration) throws NoSuchAlgorithmException;

	List<RegistrationDomain> Login(RegistrationDomain newRegistration);

	boolean action(Object iD);

	List<eReminderPojo> getAllSchedulesOfUser(int userID);


}
