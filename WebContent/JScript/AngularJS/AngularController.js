App
		.controller(
				'Controller',
				function($scope, AngularService) {

					$scope.disableTextBox = true;
					$scope.UserName=localStorage.getItem("UserName");

					$scope.daily = true;
					$scope.weekely = false;
					$scope.Monthly = false;
					$scope.Yearly = false;
					$scope.weekCheckboxs = false;

					$scope.hours = [];
					for (var i = 0; i <= 23; i++) {
						$scope.hours.push(i);
					}
					$scope.minutes = [];
					for (var i = 0; i <= 59; i++) {
						$scope.minutes.push(i);
					}
					$scope.remind = {
						hrs : "0",
						min : "0",
						recurrence : "Schedule your eReminder"
					}

					$scope.weekday = {

						MON : "Monday",
						TUE : "Tuesday",
						WED : "Wednesday",
						THU : "Thursday",
						FRI : "Friday",
						SAT : "Saturday",
						SUN : "Sunday"
					};

					$scope.months = {

						1 : "January",
						2 : "February",
						3 : "March",
						4 : "April",
						5 : "May",
						6 : "June",
						7 : "July",
						8 : "August",
						9 : "September",
						10 : "October",
						11 : "November",
						12 : "December"
					};

					$scope.selectWeekDays = [];
					$scope.seletDays = function(checked, WeekKeys) {

						if (!checked) {
							$scope.selectWeekDays.push(WeekKeys);
						} else {
							for (i = 0; i <= $scope.selectWeekDays.length; i++) {
								if ($scope.selectWeekDays[i] == WeekKeys) {
									$scope.selectWeekDays.splice(i, 1);
									break;
								}
							}
						}

					}

					$scope.dailyOperation = function() {
						$scope.daily = true;
						$scope.weekely = false;
						$scope.Monthly = false;
						$scope.Yearly = false;
						$scope.selectWeekDays = [];
						$scope.weekCheckboxs = false;
						$scope.alert = false;

					}
					$scope.weekelyOperation = function() {
						$scope.daily = false;
						$scope.weekely = true;
						$scope.Monthly = false;
						$scope.Yearly = false;
						$scope.weekCheckbox = false;
						$scope.alert = false;

					}
					$scope.monthlyOperation = function() {
						$scope.daily = false;
						$scope.weekely = false;
						$scope.Monthly = true;
						$scope.Yearly = false;
						$scope.alert = false;
						$scope.remind.dailyDays = '';

					}
					$scope.yearlyOperation = function() {
						$scope.daily = false;
						$scope.weekely = false;
						$scope.Monthly = false;
						$scope.Yearly = true;
						$scope.alert = false;
						$scope.remind.dailyDays = '';

					}

					$scope.setRecurrence = function(a) {

						$scope.selectWeekDays = [];
						$scope.weekCheckbox = false;

						$scope.disableTextBox = true;
						$scope.showRecurrenceBox = false;
					}

					$scope.saveReminder = function(reminders) {

						if ($scope.remind.recurrence == "Daily") {
							AngularService.SaveSchedule(reminders);
						} else if ($scope.remind.recurrence == "Weekely") {
							reminders['weekCheckboxs'] = $scope.selectWeekDays
							AngularService.SaveSchedule(reminders);
						} else if ($scope.remind.recurrence == "Monthly") {
							AngularService.SaveSchedule(reminders);
						} else if ($scope.remind.recurrence == "Yearly") {
							AngularService.SaveSchedule(reminders);
						} else {
							$('[data-toggle="modal"]').tooltip('show');
						}

						$scope.showRecurrenceBox = false;
						$scope.mainform.$setPristine();
					}

					$scope.alert = false;
					$scope.setOperation = function(span) {
						$('[data-toggle="modal"]').removeAttr(
								'data-original-title');
						$scope.alert = false;
						if (span.recurrence == 'Daily') {

							if (span.daily == "EveryWeekDays") {
								$scope.alert = false;
							} else if (span.daily == "Every") {
								if (span.dailyDays == undefined
										|| span.dailyDays == "") {
									$scope.alert = true;
								}
							} else {
								$scope.alert = true;
							}

						} else if (span.recurrence == 'Weekely') {
							if ($scope.selectWeekDays.length == 0) {
								$scope.alert = true;
							} else {
								$('.modal').modal('hide');
							}
						} else if (span.recurrence == 'Monthly') {

							if (span.monthly == "monthlyNoOfDays") {
								if ((span.dailyDays == undefined || span.dailyDays == "")
										|| (span.monthlyTimes1 == undefined || span.monthlyTimes1 == "")) {
									$scope.alert = true;
								}
							} else if (span.monthly == "monthlyDays") {

								if ((span.period == undefined || span.period == "")
										|| (span.weekdays == undefined || span.weekdays == "")
										|| (span.monthlyTimes2 == undefined || span.monthlyTimes2 == "")) {
									$scope.alert = true;
								}
							} else {
								$scope.alert = true;
							}

						}

						else if (span.recurrence == 'Yearly') {

							if (span.yearly == "EveryMonth") {
								if ((span.month == undefined || span.month == "")
										|| (span.dailyDays == undefined || span.dailyDays == "")) {
									$scope.alert = true;
								}
							} else if (span.yearly == "EveryPeriod") {

								if ((span.period == undefined || span.period == "")
										|| (span.weekdays == undefined || span.weekdays == "")
										|| (span.monthly == undefined || span.monthly == "")) {
									$scope.alert = true;
								}
							} else {
								$scope.alert = true;
							}

						}

						if (!$scope.alert) {
							$('.modal').modal('hide');
						}

						$scope.showRecurrenceBox = true;
					}
					$scope.everydayclick = function() {
						$scope.disableTextBox = false;

					}
					$scope.everyweekdayclick = function() {
						$scope.disableTextBox = true;
						$scope.remind.dailyDays = '';

					}

					$scope.disableMonthbox1 = true;
					$scope.disableMonthbox2 = true;
					$scope.monthlySelect = function() {
						$scope.remind.monthlyTimes2 = "";
						$scope.remind.period = "";
						$scope.remind.weekdays = "";
						$scope.disableMonthbox1 = false;
						$scope.disableMonthbox2 = true;
					}

					$scope.monthlySelectcheck = function() {
						$scope.disableMonthbox1 = true;
						$scope.disableMonthbox2 = false;
						$scope.remind.dailyDays = '';
						$scope.remind.monthlyTimes1 = '';
					}

					$("#foo").trigger("click");
					var count = 0;
					$scope.getRemiders = function() {
						AngularService.getReminders();
						$("#wrapper").toggleClass("toggled");

						var click = $(this).data('clicks');
						if (!click) {
							$('.count').each(function() {
								$(this).prop('Counter', 0).animate({
									Counter : $(this).text()
								}, {
									duration : 4000,
									easing : 'swing',
									step : function(now) {
										$(this).text(Math.ceil(now));
									}
								});
							});
						}

						$(this).data('clicks', !click);
					};

					$scope.deletee = function(id) {
						$scope.cnfrmDeleteId = id;
						$("#wrapper").toggleClass("toggled");
						$('#DeleteBox').modal('show');
					}

					$scope.yeralydisable1 = true;
					$scope.yeralydisable2 = true;
					$scope.yeralyselect = function() {
						$scope.yeralydisable1 = false;
						$scope.yeralydisable2 = true;
						$scope.remind.period = "";
						$scope.remind.weekdays = "";
						$scope.remind.monthly = "";
					}

					$scope.selectedyeraly = function() {
						$scope.yeralydisable2 = false;
						$scope.yeralydisable1 = true;
						$scope.remind.month = "";
						$scope.remind.dailyDays = "";
					}

					$scope.edit = function(serial) {
						if (serial != '') {
							$scope.remind.mail = serial.mail;
							$scope.remind.subject = serial.subject;
							$scope.remind.message = serial.message;
							$scope.remind.serial = serial.serial;
						}
						$("#wrapper").toggleClass("toggled");
					}

					$scope.cnfirmDelete = function() {
						AngularService.deleteReminder($scope.cnfrmDeleteId);
					}

					// --------------- Log In & Sign UP-----------------//

					$('#label-Remail').hide();
					$('#label-Rusername').hide();
					$('#label-Rphone').hide();
					$('#label-Rpassword').hide();
					$('#label-RCpassword').hide();
					$scope.shwLabelEmails = function() {
						$('#label-Remail').fadeIn(1000);
					}
					$scope.shwLabelUsername = function() {
						$('#label-Rusername').fadeIn(1000);
					}
					$scope.shwLabelphone = function() {
						$('#label-Rphone').fadeIn(1000);
					}
					$scope.shwLabelpwd = function() {
						$('#label-Rpassword').fadeIn(1000);
					}
					$scope.shwLabelCpwd = function() {
						$('#label-RCpassword').fadeIn(1000);
					}

					/* SignUp Here */

					$scope.registerMe = function(register) {
						AngularService.register(register);
					}

					/* LogIn Here */

					$scope.signIn = function(login) {
						AngularService.signIn(login);
						$scope.login.$setPristine();
					}
					$scope.accountAction = function(action) {						
						if (action == 'deleteAccount') {
							$('#DeleteAccount').modal('show');
						} else {
							AngularService.AccountAction(action);
						}

					}
					$scope.cnfirmDeleteAccount = function(action) {
						AngularService.AccountAction(action);
					}

				});