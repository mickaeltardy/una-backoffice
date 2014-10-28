function WorkoutsManagerCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = WorkoutCommonsCtrl($scope, $http, $routeParams, $rootScope);

	$http.get('../server/service/getCompactMembersList').success(
			function(data) {
				$scope.athletes = data;
			});

	$rootScope.getGoals = function(pDay) {
		var lTodo = "";

		var lWorkouts = $scope.getWorkoutsByType(0);
		for (var i = 0; i < lWorkouts.length; i++) {
			if ($scope.getDate(lWorkouts[i].date) - pDay.date === 0) {
				if (lTodo.length > 0)
					lTodo += "<br />";
				lTodo += $scope.getWorkoutLabel(lWorkouts[i]);
			}
		}
		return lTodo;
	}

	$rootScope.getFirstGoalOfTheDay = function(pDay) {
		var lGoal = new Object();

		var lFound = false;

		var lWorkouts = $scope.getWorkoutsByType(0);

		for (var i = 0; i < lWorkouts.length; i++) {
			if (lWorkouts[i] != null
					&& $scope.getDate(lWorkouts[i].date) - pDay.date === 0
					&& lFound == false) {
				lGoal = lWorkouts[i];
				lFound = true;
			}
		}
		return lGoal;
	}

	$scope.getDate = function(pDate) {
		var lDateRegexChrome = /^\d{4}-\d{2}-\d{2}$/;
		var lDateRegexFF = /^\d{2}\/\d{2}\/\d{4}$/;

		if (lDateRegexFF.test(pDate)) {
			var lDateSplit = pDate.split("/");
			return new Date(lDateSplit[2], lDateSplit[1], lDateSplit[0])
		} else if (lDateRegexChrome.test(pDate)) {
			var lDateSplit = pDate.split("-");
			return new Date(lDateSplit[0], lDateSplit[1] - 1, lDateSplit[2])
		}

	}

	$scope.getMembersList = function() {
		$http.get('../server/service/getMembersList').success(function(data) {
			$scope.members = data;
			if ($scope.members.length == 1) {
				$scope.setActiveMember($scope.members[0]);
			}
		});
	}

	$scope.setActiveMember = function(member) {
		if (!$scope.activeMember || member.id != $scope.activeMember.id) {
			$scope.activeMember = member;
			$rootScope.activeMember = member;
			$rootScope.$broadcast('memberSelected', $scope.activeMember);
		}
	}
	
	$scope.updateMyWorkouts = function(pType, pScope) {
		if ($rootScope.profile) {
			var lScope = new Object();
			var lDays = null;

			lScope.athleteId = $rootScope.profile.username;
			lScope.athleteLevel = $rootScope.profile.level;
			lScope.athleteCategory = $rootScope.profile.category;
			lScope.athleteSex = $rootScope.profile.sex;

			if (pScope && pScope.days) {
				lDays = pScope.days;
			} else if ($scope.days) {
				lDays = $scope.days;
			}

			if (lDays && lDays.length > 0) {
				lScope.dateFrom = $scope.getChromedDate(lDays[0].date);
				lScope.dateTo = $scope
						.getChromedDate(lDays[lDays.length - 1].date);
			}

			if (pType == "sessions")
				$scope.getSessionsDataByRequest(lScope);
			else if (pType == "tasks")
				$scope.getTasksDataByRequest(lScope);
		}
	}

	$scope.updateMySessions = function(pEvents, pScope) {
		$scope.updateMyWorkouts("sessions", pScope);
	}

	$scope.updateMyTasks = function(pEvents, pScope) {
		$scope.updateMyWorkouts("tasks", pScope);
	}

	$scope.submit = function() {

		var lResult = 0;
		var lData = new Array();

		lData.push($scope.currentWorkout);
		$http({
			method : 'POST',
			url : "app/sessions/create",
			data : lData
		}).success(
				function(data, status) {
					if (data.status == "success" && !data.error) {
						if (!$scope.currentWorkout.id) {
							$scope.currentWorkout.id = data.id / 1;
							$scope.sessions.push($scope.currentWorkout)
							$rootScope.$broadcast('sessionSaved',
									$scope.customWorkout);
						}
						$scope.currentWorkout = new Object();
						$scope.closeEditor();
					} else if (data.error) {
						alert(data.message);
					} else
						alert("Void");
				});
		return true;

	}

	$scope.reset = function() {

	}

	$rootScope.addResult = function(pDay) {
		if (!pDay) {
			pDay = new Object();
			lDate = new Date();
			pDay.date = new Date(lDate.getFullYear(), lDate.getMonth(), lDate
					.getDate());
		}
		$scope.operationName = "Ajouter un résultat";
		$scope.editWorkoutShow = true;
		$scope.currentWorkout = new Object();
		$scope.currentWorkout.date = $scope.getChromedDate(pDay.date);
		$scope.currentWorkout.itemType = 1;
		/* $scope.currentWorkout.state = 1; */
		$scope.currentWorkout.athleteId = $rootScope.profile.username;
		lWorkout = $rootScope.getFirstGoalOfTheDay(pDay);
		// Description is no more required
		$scope.currentWorkout.description = "";
		$scope.currentWorkout.type = lWorkout.type;
		$scope.currentWorkout.boat = lWorkout.boat;
		$scope.currentWorkout.category = lWorkout.category;
		$scope.currentWorkout.distance = lWorkout.distance;
		$scope.currentWorkout.duration = lWorkout.duration;
		$scope.currentWorkout.laps = lWorkout.laps;
		$scope.currentWorkout.athleteLevel = $rootScope.profile.level;
		$scope.currentWorkout.athleteSex = $rootScope.profile.sex;
		$scope.currentWorkout.athleteCategory = $rootScope.profile.category;
		/*
		 * var lMembers = (lWorkout.members) ? lWorkout.members : new Array();
		 * $scope.currentWorkout.members = lMembers;
		 * $scope.currentWorkout.members_dump = lWorkout.members_dump;
		 */
		$scope.scrollUp();

	}

	$scope.editWorkout = function(pWorkout) {
		$scope.currentWorkout = new Object();
		$scope.currentWorkout = pWorkout;

		$scope.currentWorkout.distance = pWorkout.distance / 1;
		$scope.currentWorkout.laps = pWorkout.laps / 1;

		if ($scope.currentWorkout.itemType == 0)
			$scope.operationName = "Corriger un objectif";
		else
			$scope.operationName = "Corriger un résultat";
		$scope.editWorkoutShow = true;

	}

	$rootScope.viewGoals = function(pDay) {
		$scope.operationName = "Voir les objectifs";
		$scope.showWorkoutsShow = true;
		$scope.selDay = pDay;
		$scope.selWorkoutType = 0;

		$scope.filteredWorkouts = $scope.filterWorkouts(pDay,
				$scope.selWorkoutType);
	}
	$rootScope.viewResults = function(pDay) {
		$scope.operationName = "Voir les resultats";
		$scope.showWorkoutsShow = true;
		$scope.selDay = pDay;
		$scope.selWorkoutType = 1;

		$scope.filteredWorkouts = $scope.filterWorkouts(pDay,
				$scope.selWorkoutType);
	}

	$scope.currentWorkout = new Object();
	$scope.alertMonthChanged = function(pArg0, pArg1) {
		debugger;
	}
	$scope.$on("monthChanged", $scope.alertMonthChanged);

	$scope.$on("memberSelected", $scope.getActiveMemberWorkoutsList);

	$scope.activeMember = new Object();
	$scope.sessions = new Array();
	$scope.filteredSessions = new Array();
	$scope.tasks = new Array();
	$scope.filteredSessions = new Array();

	$scope.changeDate = function(pDate) {
		if (pDate)
			$scope.currentWorkout.date = pDate;
		if ($scope.getDate($scope.currentWorkout.date) - new Date() > 0) {
			alert("Pas possible d'ajouter un résultat pour la date dans le futur");
			$scope.currentWorkout.date = $scope.getChromedDate(new Date());
		}
		if (!$scope.currentWorkout.id) {
			var lDay = new Object();
			lDay.date = $scope.getDate($scope.currentWorkout.date);
			$scope.addResult(lDay);
		}

	}

	$scope.getDateLabel = function(pDate) {
		if (pDate) {
			lDate = $scope.getDate(pDate);
			return lDate.getDate() + " "
					+ $scope.calendar.getMonthLabel(lDate.getMonth());
		} else
			return "";

	}

	$scope.calendar = new CalendarCtrl($scope, $http, $rootScope);

	$scope.scrollUp = function() {
		var lBrowser = navigator.userAgent;

		if (lBrowser.indexOf('iPhone') >= 0)
			$('html,body').scrollTop(0);
	}

	$scope.needDatePicker = function() {
		var lBrowser = navigator.userAgent;
		var lResult = true;
		if (lBrowser.indexOf('Chrome') >= 0 || lBrowser.indexOf('iPhone') >= 0)
			lResult = false;

		return lResult;
	}

	$scope.setDatePicker = function() {
		if ($scope.needDatePicker() == true)
			$("#workoutDate").datepicker({
				dateFormat : "yy-mm-dd",
				onSelect : function(pValue) {
					$scope.changeDate(pValue);
					$scope.$apply()
				},
				firstDay : 1
			});

	};

	$scope.getBrowser = (function() {
		var N = navigator.appName, ua = navigator.userAgent, tem;
		var M = ua
				.match(/(opera|chrome|safari|firefox|msie)\/?\s*(\.?\d+(\.\d+)*)/i);
		if (M && (tem = ua.match(/version\/([\.\d]+)/i)) != null)
			M[2] = tem[1];
		M = M ? [ M[1], M[2] ] : [ N, navigator.appVersion, '-?' ];
		return M;
	});

	$scope.displayInline = "inline";
	$scope.displayNone = "none";
	$scope.orderProp = "surname";
	$scope.promptPositionTop = "0px";
	$scope.promptPositionLeft = "0px";
	$scope.displayPrompt = false;
	$scope.promptFilter = "";
	$scope.promptedElement = null;

	// Members prompter
	$scope.getPromptStyle = function() {
		return {
			position : "absolute",
			top : $scope.promptPositionTop,
			left : $scope.promptPositionLeft
		}
	}
	/*
	 * $scope.promptMember = function(pElement, pDisplay, $event) {
	 * $scope.displayPrompt = pDisplay; $scope.promptFilter =
	 * pElement.member.name; $scope.promptedElement = pElement;
	 * $scope.promptPositionTop = event.srcElement.offsetTop + 20 + "px";
	 * $scope.promptPositionLeft = event.srcElement.offsetLeft + "px"; }
	 */
	$scope.promptMember = function(pElement, pDisplay, $event) {
		$scope.displayPrompt = pDisplay;
		// $scope.promptFilter = pElement.member.name;

		var lCaretPorition = $scope.getCaretPosition(event.srcElement);
		var lDump = $scope.currentWorkout.members_dump;
		var lItems = lDump.split(",");
		var lTmpLength = 0;
		var lCurrenItem = 0;
		if (lCaretPorition >= lDump.length - 1) {
			lCurrenItem = lItems.length - 1;
		} else {
			for (var i = 0; i < lItems.length; i++) {
				lTmpLength += (lItems[i].length) + 1;
				if (lTmpLength > lCaretPorition) {
					lCurrenItem = i;
					break;
				}
			}
		}
		// Get the name of the rower
		var lFilter = "";

		var lItemText = lItems[lCurrenItem].trim();
		var lItemTextSplitted = lItemText.split(":");
		if (lItemTextSplitted.length == 2)
			lFilter = lItemTextSplitted[1].trim();
		else
			lFilter = lItemTextSplitted[0].trim();

		if (lFilter.length < 2) {
			$scope.promptFilter = "";
		} else {
			$scope.promptedMemberItem = lCurrenItem;
			if (!$scope.currentWorkout.members)
				$scope.currentWorkout.members = new Array();

			$scope.currentWorkout.members[$scope.promptedMemberItem] = "";
			$scope.promptFilter = lFilter;
			$scope.promptedElement = event.srcElement;
			$scope.promptPositionTop = event.srcElement.offsetTop
					+ event.srcElement.offsetHeight + "px";
			$scope.promptPositionLeft = event.srcElement.offsetLeft + "px";
		}
		$scope.members_dumpBak = $scope.currentWorkout.members_dump;
	}
	$scope.addMember = function(pElement) {
		$scope.displayPrompt = false;

		if (!$scope.currentWorkout.members)
			$scope.currentWorkout.members = new Array();

		var lLabel = pElement.athlete.name + " " + pElement.athlete.surname;
		$scope.currentWorkout.members[$scope.promptedMemberItem] = pElement.athlete.id;

		var lDump = $scope.currentWorkout.members_dump;
		var lItems = lDump.split(",");
		var lItem = lItems[$scope.promptedMemberItem];
		var lItemSplitted = lItem.split(":");
		if (lItemSplitted.length == 2) {
			lItem = lItemSplitted[0] + ": " + lLabel;
		} else {
			if (lItems.length > 1)
				lItem = " " + lLabel;
			else
				lItem = lLabel;
		}

		lItems[$scope.promptedMemberItem] = lItem;
		$scope.currentWorkout.members_dump = lItems.join(",");
		if ($scope.promptedMemberItem == lItems.length - 1)
			$scope.currentWorkout.members_dump += ", ";

		$scope.promptedElement.focus();

	}

	$scope.getInputSize = function(pElement) {

		return (pElement.member && pElement.member.name) ? pElement.member.name.length
				: 15;
	}
	$scope.getCaretPosition = function(el) {

		var pos = 0;
		// IE Support
		if (document.selection) {
			el.focus();
			var Sel = document.selection.createRange();
			var SelLength = document.selection.createRange().text.length;
			Sel.moveStart('character', -el.value.length);
			pos = Sel.text.length - SelLength;
		}
		// Firefox support
		else if (el.selectionStart || el.selectionStart == '0')
			pos = el.selectionStart;

		return pos;

	}

	$scope.isDistanceEstimationRequired = function() {
		return ($scope.currentWorkout && ($scope.currentWorkout.category == "boat"));
	}

	$scope.isDistanceEstimationAvailable = function() {
		return ($scope.currentWorkout && ($scope.isDistanceEstimationRequired()
				|| $scope.currentWorkout.category == "ergo" || $scope.currentWorkout.category == "footing"));
	}

	$scope.isLapsEstimationRequired = function() {
		return ($scope.currentWorkout && ($scope.currentWorkout.category == "building"))
	}

	$scope.isTimeEstimationAvailable = function() {
		return ($scope.currentWorkout && $scope.currentWorkout.category)
	}

	$scope.isBoatTypeEstimationRequired = function() {
		return ($scope.currentWorkout && ($scope.currentWorkout.category == "boat"))
	}

	$scope.isTimeEstimationRequired = function() {
		return ($scope.currentWorkout.category == "ergo")
	}
	$scope.isWorkoutTypeEstimationRequired = function() {
		return ($scope.currentWorkout && ($scope.currentWorkout.category == "boat"
				|| $scope.currentWorkout.category == "building" || $scope.currentWorkout.category == "ergo"))
	}
	$scope.selectWorkoutClass = function(pClass) {
		if (!$scope.currentWorkout)
			$scope.currentWorkout = new Object();

		$scope.currentWorkout.category = pClass;
		$scope.selectWorkoutWorkoutType("");
		$scope.selectWorkoutBoatType("");
	}
	$scope.selectWorkoutWorkoutType = function(pType) {
		if (!$scope.currentWorkout)
			$scope.currentWorkout = new Object();

		$scope.currentWorkout.type = pType;
	}
	$scope.selectWorkoutBoatType = function(pBoat) {
		if (!$scope.currentWorkout)
			$scope.currentWorkout = new Object();

		$scope.currentWorkout.boat = pBoat;
	}

	$scope.getCurrentWorkoutClassBoatType = function() {
		if ($scope.currentWorkout && $scope.currentWorkout.category) {
			return $scope.workoutData.boatTypes;
		}
	}

	$scope.isCurrentWorkoutClassRowing = function() {
		return ($scope.currentWorkout && ($scope.currentWorkout.category == "boat" || $scope.currentWorkout.category == "ergo"))
	}

	$scope.getCurrentWorkoutClassWorkoutType = function() {
		if ($scope.currentWorkout && $scope.currentWorkout.category) {
			var lClass = $scope.currentWorkout.category;
			if ($scope.isCurrentWorkoutClassRowing())
				lClass = "rowing";
			return $scope.workoutData.workoutTypes[lClass];
		}
	}

	$scope.validateAndSubmit = function() {
		if ($scope.validate()) {
			return $scope.submit();
		}
	}

	$scope.validate = function() {
		var lResult = true;
		var lTemp = $scope.currentWorkout;
		if (lTemp) {
			if (lTemp.category) {
				if ($scope.isBoatTypeEstimationRequired() && !lTemp.boat)
					lResult = false;
				if ($scope.isTimeEstimationRequired()
						&& $scope.processEstimation(lTemp.duration) == 0)
					lResult = false;
				if ($scope.isWorkoutTypeEstimationRequired() && !lTemp.type)
					lResult = false;
				if ($scope.isDistanceEstimationRequired()
						&& $scope.processEstimation(lTemp.distance) == 0)
					lResult = false;
				if ($scope.isLapsEstimationRequired()
						&& $scope.processEstimation(lTemp.laps) == 0)
					lResult = false;
			} else {
				lResult = false;
			}
		} else {
			lResult = false;
		}
		return lResult;

	}

	$scope.getCountUnitLabel = function(pCount) {
		if (pCount == "distance")
			return "km";
		if (pCount == "duration")
			return "min";
		if (pCount == "laps")
			return "tour(s)";

	}

	$scope.toggleShowSpecific = function(pStatistics) {
		pStatistics.showSpecific = !pStatistics.showSpecific;
	}

	$scope.getCountUnit = function(pStatistics) {
		return pStatistics.count;
	}

	$scope.processEstimation = function(pEstimation) {
		var lSuffixes = [ "kms", "km", "mins", "min", "m", "'" ];
		if (pEstimation) {
			if (pEstimation / 1 > 0 || pEstimation / 1 < 0)
				return pEstimation / 1;

			pEstimation.replace(/ /g, "");

			var lMult = 1;
			var lValue = pEstimation;
			var lEval = 0;
			if (pEstimation.indexOf("x") >= 0) {
				var lTmp = pEstimation.split("x");
				lMult = lTmp[0];
				lValue = lTmp[1];
			}
			for (var i = 0; i < lSuffixes.length; i++) {
				lValue = lValue.replace(lSuffixes[i], "");
			}
			lValue = lValue.replace(",", ".");

			if (lValue.indexOf("h") >= 0) {
				var lTmp = lValue.split("h");
				lEval = lTmp[0] / 1 * 60
						+ lTmp[1].replace("min", "").replace("'", "") / 1
			} else if (lValue.indexOf(":") > 0) {
				var lTmp = lValue.split(":");
				if (lTmp.length == 3) {
					lValue = lTmp[0] * 60 + lTmp[1] * 1;
				} else if (lTmp.length == 3) {
					lValue = lTmp[0]
				}

			}
			if (lEval == 0) {

				lEval = lValue / 1;
				if (lEval > 1000)
					lEval = lEval / 1000;
			}

			if (lEval > 0)
				return lEval * lMult;
		}
		return 0;
	}

	$scope.getStatistics = function(pStatistics, pMember, pDateStart, pDateStop) {
		var lStat = new Object();
		var lEval = 0;
		var lGoal = 0;
		var pType = pStatistics.type;
		var lWorkouts = $scope.workouts;
		var pBoatType = pStatistics.boat;
		var lCountUnit = $scope.getCountUnit(pStatistics);
		if (!pDateStop)
			pDateStop = new Date();
		for (var i = 0; i < lWorkouts.length; i++) {
			if ($scope.getDate(lWorkouts[i].date) - pDateStop <= 0
					&& (!pDateStart || $scope.getDate(lWorkouts[i].date)
							- pDateStart >= 0)
					&& ((typeof pStatistics.category === 'string' && lWorkouts[i].category == pStatistics.category) || pStatistics.category
							.indexOf(lWorkouts[i].category) >= 0)
					&& (pType != "" && pType == lWorkouts[i].type || (pType == "" || !pType))
					&& (pBoatType
							&& ((typeof lWorkouts[i].boat === 'string' && $scope
									.arrayContains(pBoatType, lWorkouts[i].boat)) || typeof lWorkouts[i].boat !== 'string'
									&& $scope.arrayContained(pBoatType,
											lWorkouts[i].boat)) || (pBoatType == "" || !pBoatType))) {
				var lTmpEval = $scope
						.processEstimation(lWorkouts[i][lCountUnit]);

				if (lTmpEval > 0) {
					if (lWorkouts[i].itemType == 1
							&& pMember.username == lWorkouts[i].athleteId)
						lEval += lTmpEval;
					else if (lWorkouts[i].itemType == 0
							&& (!lWorkouts[i].athleteSex || $scope
									.arrayContains(lWorkouts[i].athleteSex,
											pMember.sex))
							&& (!lWorkouts[i].athleteLevel || $scope
									.arrayContains(lWorkouts[i].athleteLevel,
											pMember.level))
							&& (!lWorkouts[i].athleteCategory || $scope
									.arrayContains(
											lWorkouts[i].athleteCategory,
											pMember.category)))
						lGoal += lTmpEval;
				}
			}
		}
		lUnit = " " + $scope.getCountUnitLabel(lCountUnit);
		lStat.value = lEval + lUnit;
		lStat.goal = lGoal + lUnit;
		if (lGoal > 0)
			lStat.achievement = (lEval / lGoal * 100).toFixed(2) + " %";
		else
			lStat.achievement = "NA";
		lStat.type = pType;

		lStat.label = (pStatistics.label) ? pStatistics.label
				: pStatistics.category;
		lStat.label = $scope.messages.statistics.labels[lStat.label];
		lStat.showSpecific = false;
		return lStat;
	}

	$scope.getWorkoutType = function(pType) {
		var lResult = null;
		for (var i = 0; i < $scope.workoutData.workoutTypes.length; i++) {
			if ($scope.workoutData.workoutTypes[i]['code'] == pType) {
				lResult = $scope.workoutData.workoutType;
				break;
			}
		}
		return lResult;
	}
	$scope.getWorkoutTypeLabel = function(pType) {
		var lResult = null;
		lResult = ($scope.messages.statistics.labels[pType]) ? $scope.messages.statistics.labels[pType]
				: pType;
		return lResult;
	}

	$scope.getPersonalStatistics = function(pMember, pDateStart, pDateStop) {

		var lResults = new Array();
		var lStats = $scope.workoutData.statistics;

		for (var i = 0; i < lStats.length; i++) {
			var lTmpStat = new Object();
			lTmpStat.label = lStats[i].label;
			lTmpStat.category = lStats[i].category;
			lTmpStat.boat = lStats[i].boat;
			lTmpStat.count = lStats[i].count;
			var lStat = new Object();
			if (lStats[i].type) {
				var lSpecific = new Array();
				for (var j = 0; j < lStats[i].type.length; j++) {
					lTmpStat.type = lStats[i].type[j];
					if (lStats[i].type[j] == "") {
						lStat = $scope.getStatistics(lTmpStat, pMember,
								pDateStart, pDateStop);
					} else if ($scope.workoutData.workoutTypes[lStats[i].type[j]]) {
						for (var k = 0; k < $scope.workoutData.workoutTypes[lStats[i].type[j]].length; k++) {
							lTmpStat.type = $scope.workoutData.workoutTypes[lStats[i].type[j]][k].code
							lSpecific.push($scope.getStatistics(lTmpStat,
									pMember, pDateStart, pDateStop));
						}
					} else {
						lSpecific.push($scope.getStatistics(lStats[i], pMember,
								lStats[i].type[j], pDateStart, pDateStop));
					}
				}

				if (lSpecific.length == 1) {
					lStat = lSpecific[0];
				} else {
					lStat.specific = lSpecific;
				}
				lResults.push(lStat);
			} else {
				lTmpStat.type = "";
				lResults.push($scope.getStatistics(lTmpStat, pMember,
						pDateStart, pDateStop));
			}
		}

		return lResults;

	}

	$scope.loadPersonalStatistics = function() {
		var lNow = new Date();

		var lWeekStart = new Date(lNow.getFullYear(), lNow.getMonth(), lNow
				.getDate()
				- (lNow.getDay() + 6) % 7);
		var lMonthStart = new Date(lNow.getFullYear(), lNow.getMonth(), 1);
		var lWeekStats = new Object();
		lWeekStats.stats = $scope.getPersonalStatistics($rootScope.profile,
				lWeekStart, lNow);
		lWeekStats.label = "week";
		var lMonthStats = new Object();
		lMonthStats.stats = $scope.getPersonalStatistics($rootScope.profile,
				lMonthStart, lNow);
		lMonthStats.label = "month";
		/*
		 * var lFullStats = new Object(); lFullStats.stats =
		 * $scope.getPersonalStatistics(); lFullStats.label = "full";
		 * $scope.personalStatistics = [ lWeekStats, lMonthStats, lFullStats ];
		 */
		$scope.personalStatistics = [ lWeekStats, lMonthStats ];
	}

	$scope.$on("calendarChanged", $scope.updateMyTasks)
	$scope.$on("profileLoaded", $scope.updateMyTasks)

}
