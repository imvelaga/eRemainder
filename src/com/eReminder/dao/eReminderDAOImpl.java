package com.eReminder.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.eReminder.domain.RegistrationDomain;
import com.eReminder.domain.eReminderPojo;

public class eReminderDAOImpl extends JdbcDaoSupport implements eReminderDAO {

	boolean exist = false;

	@SuppressWarnings("unchecked")
	@Override
	public List<eReminderPojo> newSchedule(eReminderPojo newSchedule, String cronExcepression, Object useriD) {
		List<eReminderPojo> messages = new ArrayList<eReminderPojo>();

		try {

			if (newSchedule.getSerial() == 0) {
				String sql = "EXEC SPCI_New_Job ?,?,?,?,?,?";
				Object obj[] = new Object[] { useriD, newSchedule.getMail(), newSchedule.getSubject(),
						newSchedule.getMessage(), cronExcepression, newSchedule.getSerial() };
				int typ[] = new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.INTEGER };
				messages = getJdbcTemplate().query(sql, obj, typ, new dataMapper());
				exist = true;
			} else if (newSchedule.getSerial() != 0) {

				String sql = "EXEC SPCI_New_Job ?,?,?,?,?,?";
				Object obj[] = new Object[] { useriD, newSchedule.getMail(), newSchedule.getSubject(),
						newSchedule.getMessage(), cronExcepression, newSchedule.getSerial() };
				int typ[] = new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.INTEGER };
				messages = getJdbcTemplate().query(sql, obj, typ, new dataMapper());
				exist = true;

			}

		} catch (Exception e) {
			logger.error("Error in dao" + e);
		}
		return messages;
	}

	@SuppressWarnings("rawtypes")
	private final static class dataMapper implements RowMapper {
		@Override
		public Object mapRow(ResultSet rs, int arg1) throws SQLException {

			eReminderPojo resultEmails = new eReminderPojo();
			resultEmails.setSerial(Integer.parseInt(rs.getString("Serial")));
			return resultEmails;
		}
	}

	@SuppressWarnings("rawtypes")
	private final static class Results implements RowMapper {
		@Override
		public Object mapRow(ResultSet rs, int arg1) throws SQLException {

			eReminderPojo resultEmails = new eReminderPojo();
			resultEmails.setMail(rs.getString("Email"));
			resultEmails.setSubject(rs.getString("Subject"));
			resultEmails.setMessage(rs.getString("Message"));
			resultEmails.setCronExpression(rs.getString("CronJob"));
			resultEmails.setSerial(Integer.parseInt(rs.getString("Serial")));
			return resultEmails;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<eReminderPojo> eReminder() {
		String sql = "EXEC SPCR_All_Recipients";

		List<eReminderPojo> messages = getJdbcTemplate().query(sql, new Results());
		return messages;
	}

	@SuppressWarnings("rawtypes")
	private final static class mailsDetails implements RowMapper {
		@Override
		public Object mapRow(ResultSet rs, int arg1) throws SQLException {

			eReminderPojo resultEmails = new eReminderPojo();
			resultEmails.setMail(rs.getString("Email"));
			resultEmails.setSubject(rs.getString("Subject"));
			resultEmails.setMessage(rs.getString("Message"));
			// resultEmails.setSerial(Integer.parseInt(rs.getString("CRTS")));
			return resultEmails;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<eReminderPojo> details(int serial) {
		String sql = "EXEC  SPCR_Mail_Details ?";
		List<eReminderPojo> mailDetails = null;
		try {
			Object obj[] = new Object[] { serial };
			int typ[] = new int[] { Types.INTEGER };

			mailDetails = getJdbcTemplate().query(sql, obj, typ, new mailsDetails());

		} catch (Exception e) {
			logger.error("Error in dao" + e);
		}
		return mailDetails;
	}

	@Override
	public boolean deleteSchedules(eReminderPojo deleteIdPojo) {
		String sql = "EXEC SPCU_Delete_eReminder ?";
		int id = deleteIdPojo.getSerial();
		boolean operation = false;

		try {
			Object obj[] = new Object[] { id };
			int typ[] = new int[] { Types.INTEGER };
			getJdbcTemplate().update(sql, obj, typ);
			operation = true;
		} catch (Exception e) {
			logger.error("Error in dao" + e);
			operation = false;
		}
		return operation;
	}

	@Override
	public boolean Registration(RegistrationDomain newRegistration) throws NoSuchAlgorithmException {
		String sql = "EXEC SPCI_New_Registration ?,?,?,?";
		boolean status = false;
		/* Password Encrypt */
		String passwordEncrypted = PasswordEncrypt.encrypt(newRegistration.getPassword());

		try {
			Object obj[] = new Object[] { newRegistration.getEmail(), newRegistration.getUser(),
					newRegistration.getPhone(), passwordEncrypted };
			int typ[] = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
			getJdbcTemplate().update(sql, obj, typ);

			status = true;
		} catch (Exception e) {
			logger.error("Error in dao" + e);
			status = false;
		}
		return status;
	}

	@SuppressWarnings("rawtypes")
	private final static class UserId implements RowMapper {
		@Override
		public Object mapRow(ResultSet rs, int arg1) throws SQLException {

			RegistrationDomain userDetails = new RegistrationDomain();
			userDetails.setUserId((rs.getInt("UserID")));
			userDetails.setUser(rs.getString("Username"));
			return userDetails;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RegistrationDomain> Login(RegistrationDomain newRegistration) {

		String sql = "EXEC SPCF_Login_Details ?,?";
		List<RegistrationDomain> UserId = new ArrayList<RegistrationDomain>();

		/* Password Encrypt */
		String passwordEncrypted = PasswordEncrypt.encrypt(newRegistration.getPassword());

		try {
			Object obj[] = new Object[] { newRegistration.getEmail(), passwordEncrypted };
			int typ[] = new int[] { Types.VARCHAR, Types.VARCHAR };
			UserId = getJdbcTemplate().query(sql, obj, typ, new UserId());
		} catch (Exception e) {
			logger.error("Error in dao" + e);
		}

		return UserId;
	}

	@Override
	public boolean action(Object iD) {
		String sql = "Exec SPCU_Delete_Account ?";
		boolean operation;
		try {
			Object obj[] = new Object[] { iD };
			int typ[] = new int[] { Types.INTEGER };
			getJdbcTemplate().update(sql, obj, typ);
			operation = true;
		} catch (Exception e) {
			logger.error("Error in dao" + e);
			operation = false;
		}
		return operation;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<eReminderPojo> getAllSchedulesOfUser(int userID) {

		{

			String sql = "EXEC SPCR_All_eReminder_Of_User ?";
			Object obj[] = new Object[] { userID };
			int typ[] = new int[] { Types.INTEGER };
			List<eReminderPojo> Allmessages = new ArrayList<eReminderPojo>();
			Allmessages = getJdbcTemplate().query(sql, obj, typ, new eReminderResults());
			return Allmessages;
		}
	}

	@SuppressWarnings("rawtypes")
	private final static class eReminderResults implements RowMapper {
		@Override
		public Object mapRow(ResultSet rs, int arg1) throws SQLException {

			eReminderPojo resultEmails = new eReminderPojo();
			resultEmails.setMail(rs.getString("Email"));
			resultEmails.setSubject(rs.getString("Subject"));
			resultEmails.setMessage(rs.getString("Message"));
			resultEmails.setCronExpression(rs.getString("CronJob"));
			resultEmails.setSerial(Integer.parseInt(rs.getString("Serial")));
			return resultEmails;
		}
	}

}