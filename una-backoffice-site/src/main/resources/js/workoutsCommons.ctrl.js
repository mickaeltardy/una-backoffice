function WorkoutCommonsCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope);

	$http.get('app/resources/workoutsData.datasource').success(function(data) {
		$scope.workoutData = data;
	});

	$scope.getSimpleMemberName = function(pName) {
		if (pName) {
			var lTmp = pName.split(" ");
			return lTmp[0];
		}
	}

	// TODO Get rid of this stuff

	$scope.updateMembersList = function() {
		$scope.members = new Array();
		for (i = 0; i < $scope.workouts.length; i++) {
			if (!$scope.arrayContains($scope.members,
					$scope.workouts[i].member_name))
				$scope.members.push($scope.workouts[i].member_name);
		}
		return true;
	}

	$scope.getTasksDataByRequest = function(pScope) {

		var lData = pScope;
		$http({
			method : 'POST',
			url : "app/tasks/retrieve",
			data : lData
		}).success(function(data, status) {
			if (data) {
				$scope.tasks = new Array();
				$scope.filteredTasks = new Array();
				for (i = 0; i < data.length; i++) {
					$scope.tasks.push(data[i]);
					$scope.filteredTasks.push(data[i]);
				}
				$rootScope.$broadcast("tasksLoaded", data);
			}
		});
	}
	$scope.getSessionsDataByRequest = function(pScope) {
		var lData = pScope;
		$http({
			method : 'POST',
			url : "app/sessions/retrieve",
			data : lData
		}).success(function(data, status) {
			if (data) {
				$scope.sessions = new Array();
				$scope.filteredSessions = new Array();
				for (i = 0; i < data.length; i++) {
					$scope.sessions.push(data[i]);
					$scope.filteredSessions.push(data[i]);
				}
				$rootScope.$broadcast("sessionsLoaded", data);
			}
		});

	}

	$scope.getWorkoutsDataByRequest = function(pUrl, pScope) {
		var lData = pScope;
		$http({
			method : 'POST',
			url : pUrl,
			data : lData
		}).success(function(data, status) {
			if (data) {
				$scope.workouts = new Array();
				$scope.filteredWorkouts = new Array();
				for (i = 0; i < data.length; i++) {
					$scope.workouts.push(data[i]);
					$scope.filteredWorkouts.push(data[i]);
				}
				$rootScope.$broadcast("workoutsLoaded", data);
			}
		});
	}
	// --------

	$scope.getChromedDate = function(pDate) {
		var lMonth = pDate.getMonth() + 1
		if (lMonth < 10)
			lMonth = "0" + lMonth;
		var lDay = pDate.getDate();
		if (lDay < 10)
			lDay = "0" + lDay;
		return pDate.getFullYear() + "-" + lMonth + "-" + lDay;
	}
	
	$scope.getWorkoutsByType = function(pType){
		var lWorkouts = new Array();
		if(pType == 0){
			lWorkouts = $scope.tasks;
		}else if (pType == 0){ 
			lWorkouts = $scope.sessions;
		}
		return lWorkouts;
	}

	$scope.setFilteredWorkoutsByType = function(pFilteredWorkouts, pType){
		
		if(pType == 0){
			$scope.filteredTasks = pFilteredWorkouts;
		}else if (pType == 0){ 
			$scope.filteredSessions = pFilteredWorkouts;
		}
	}
	
	$scope.filterWorkouts = function(pDay, pType) {
		if ($scope.selDay)
			;
		if ($scope.selWorkoutType)
			;
		var lWorkouts = $scope.getWorkoutsByType(pType);
		
		var filteredWorkouts = new Array();
		for (i = 0; i < lWorkouts.length; i++) {
			if ($scope.getDate(lWorkouts[i].date) - pDay.date === 0
					&& lWorkouts[i].itemType == pType)
				filteredWorkouts.push(lWorkouts[i]);
		}
		return filteredWorkouts;

	}

	$scope.setArrayValue = function(pValue, pGroup) {
		if (!$scope.currentWorkout[pGroup])
			$scope.currentWorkout[pGroup] = new Array();
		if ($scope.currentWorkout && $scope.currentWorkout[pGroup]
				&& $scope.currentWorkout[pGroup].indexOf(pValue) == -1)
			$scope.currentWorkout[pGroup].push(pValue);
		else if ($scope.currentWorkout && $scope.currentWorkout[pGroup]
				&& $scope.currentWorkout[pGroup].indexOf(pValue) !== -1)
			$scope.currentWorkout[pGroup].splice($scope.currentWorkout[pGroup]
					.indexOf(pValue), 1);
	};

	$rootScope.getGoalsCnt = function(pDay) {
		return " (" + $scope.getCnt(pDay, 0) + ")";

	}
	$rootScope.getResultsCnt = function(pDay) {
		return " (" + $scope.getCnt(pDay, 1) + ")";
	}

	$rootScope.isResultsNotNull = function(pDay) {
		return $scope.getCnt(pDay, 1) > 0;
	}

	$rootScope.isGoalsNotNull = function(pDay) {
		return $scope.getCnt(pDay, 0) > 0;
	}

	$scope.getCnt = function(pDay, pType) {
		var lGoalsCnt = 0;
		var lWorkouts = $scope.getWorkoutsByType(pType);
		
		for (i = 0; i < lWorkouts.length; i++) {
			if ($scope.getDate(lWorkouts[i].date) - pDay.date === 0)
				lGoalsCnt++;
		}
		return lGoalsCnt;
	}

	$scope.closeEditor = function() {
		$scope.editWorkoutShow = false;
	}

	$scope.closeViewer = function() {
		$scope.showWorkoutsShow = false;
	}

	$scope.getWorkoutLabel = function(pWorkout) {
		var lResult = "";

		if (pWorkout.description)
			lResult += pWorkout.description;
		else {
			if (pWorkout.category) {
				var lClassLabel = "";
				for (var i = 0; i < $scope.workoutData.workoutClasses.length; i++) {
					if ($scope.workoutData.workoutClasses[i]['code'] == pWorkout.category) {
						if (pWorkout.boat) {
							if ($scope.isShortBoat(pWorkout.boat))
								lClassLabel += $scope.messages.statistics.labels['shortBoat'];
							else if ($scope.isLongBoat(pWorkout.boat))
								lClassLabel += $scope.messages.statistics.labels['longBoat'];
						}
						if (!lClassLabel) {
							lClassLabel = $scope.workoutData.workoutClasses[i]['label'];
						}
					}
				}
				lResult += lClassLabel + " / ";
			}
			if (pWorkout.type)
				lResult += pWorkout.type + " / ";
			if (pWorkout.distance)
				lResult += pWorkout.distance + "km / ";
			if (pWorkout.duration)
				lResult += pWorkout.duration + " / ";
			if (pWorkout.laps)
				lResult += pWorkout.laps + " tours / ";
		}
		return lResult

	}

	$scope.isShortBoat = function(pBoat) {
		return $scope.isBoatOfType(pBoat, "short");
	};

	$scope.isLongBoat = function(pBoat) {
		return $scope.isBoatOfType(pBoat, "long");
	};

	$scope.isBoatOfType = function(pBoat, pType) {
		var lResult = false;
		var lType = null;
		var lBoat = null;
		if (pBoat && typeof pBoat === 'string') {
			lBoat = $scope.findItemByCode($scope.workoutData.boatTypes, pBoat);
			if (lBoat && lBoat.type)
				lType = lBoat.type
		} else if (pBoat && pBoat.length) {
			for (var i = 0; i < pBoat.length; i++) {
				var lTmp = $scope.findItemByCode($scope.workoutData.boatTypes,
						pBoat[i]);
				if (lTmp && lTmp.type) {
					if (!lType) {
						lBoat = lTmp;
						lType = lTmp.type;
					} else if (lType != lTmp.type) {
						lBoat = null;
						lType = null;
						break;
					}
				}

			}
		}

		if (lType == pType)
			lResult = true;
		return lResult;
	};

	$scope.storeDays = function(pScope) {
		if (pScope && pScope.days) {
			$scope.days = pScope.days;
		}
	}

	$scope.$on("calendarChanged", $scope.storeDays);

}