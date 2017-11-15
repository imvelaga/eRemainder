App
		.service(
				'AngularService',
				function($window) {

					var scope = angular.element($("#body")).scope();

					return ({
						SaveSchedule : SaveSchedule,
						getReminders : getReminders,
						deleteReminder : deleteReminder,
						register : register,
						signIn : signIn,
						AccountAction : AccountAction
					});

					function SaveSchedule(data) {
						$
								.ajax({
									type : "POST",
									url : "/eReminder/newSchedule.htm",
									data : "data=" + JSON.stringify(data),
									datatype : "json",
									success : function(response) {
										scope
												.$apply(function() {
													scope.result = response.status

													if (scope.result == true) {
														scope.remind = {
															hrs : "0",
															min : "0",
															recurrence : "Schedule your eReminder"
														}
														scope.succusMessage = "You'r Reminder Saved Successfully ThankYou"
														$('#succussModel')
																.modal('show');
													} else {
														scope.succusMessage = "Operation Unsuccessfully Try Again ThankYou"
													}
													scope.daily = true;
													scope.weekely = false;
													scope.Monthly = false;
													scope.Yearly = false;
													scope.yeralydisable2 = true;
													scope.yeralydisable1 = true;
													scope.disableMonthbox1 = true;
													scope.disableMonthbox2 = true;
												});
									}
								});
					}

					function getReminders() {
						$.ajax({
							type : "POST",
							url : "/eReminder/getSchedules.htm",
							datatype : "json",
							success : function(response) {
								scope.$apply(function() {
									scope.result = response.reminders
								});
							}
						});
					}

					function deleteReminder(cnfrmDeleteId) {
						$
								.ajax({
									type : "POST",
									url : "/eReminder/deleteSchedules.htm",
									data : "data=" + cnfrmDeleteId,
									datatype : "json",
									success : function(response) {
										scope
												.$apply(function() {
													scope.status = response.idDeleted
													if (scope.status == true) {
														scope.succusMessage = "eReminder Schedule Deleted Successfully"
														scope.remind = {
															hrs : "0",
															min : "0",
															recurrence : "Schedule your eReminder"
														}
													} else {
														scope.succusMessage = "Operation Unsuccessfully Try Again ThankYou"
													}
													$('#succussModel').modal(
															'show');
												});
									}
								});
					}

					function register(register) {
						$
								.ajax({
									type : "POST",
									url : "/eReminder/Register.htm",
									data : "data=" + JSON.stringify(register),
									datatype : "json",
									success : function(response) {
										scope
												.$apply(function() {
													if (response.isRegistered) {
														scope.registrationMessage = "Registration Successful... Now You Can Schedule You'r Events";
														scope.register = '';
														scope.signup
																.$setPristine();
													} else {
														scope.registrationMessage = "Registration UnSuccessful Please Try Again";
													}
													$('#registrationStatus')
															.modal('show');
												});
									}
								});
					}

					function signIn(login) {
						$
								.ajax({
									type : "POST",
									url : "/eReminder/Login.htm",
									data : "data=" + JSON.stringify(login),
									datatype : "json",
									success : function(response) {
										scope.$apply(function() {
													scope.UserId = response.isLoginSuccusful[0].userId;
													scope.UserName = $.trim(response.isLoginSuccusful[0].user);
													localStorage.setItem("UserName", scope.UserName);
													if (response.isLoginSuccusful[0].userId != 0) {
														window.location
																.replace("/eReminder/home.htm");
													} else {
														$('#registrationStatus')
																.modal('show');
														scope.registrationMessage = "Please Check You'r Credentials And Try Again";
													}
												});
									}
								});
					}

					function AccountAction(action) {
						$.ajax({
							type : "POST",
							url : "/eReminder/Action.htm",
							data : "data=" + action,
							datatype : "json",
							success : function(response) {
								scope.$apply(function() {
									scope.UserId = response.isActionSuccusful;
									if (scope.UserId) {
										localStorage.setItem("UserName", "");
										window.location.replace("/eReminder");
										$('#registrationStatus')
									}
								});
							}
						});
					}

				});