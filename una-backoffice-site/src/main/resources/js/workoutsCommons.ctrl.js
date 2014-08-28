function WorkoutCommonsCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope);

	$http.get('../shared/data/workoutsData.json').success(function(data) {
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
	$rootScope.getAllWorkoutData = function(pCallback) {
		$scope.workouts = new Array();
		$scope.filteredWorkouts = new Array();
		$http.get('../server/service/getWorkouts').success(function(data) {

			if (data) {
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

	$scope.filterWorkouts = function(pDay, pType) {
		if ($scope.selDay)
			;
		if ($scope.selWorkoutType)
			;

		var filteredWorkouts = new Array();
		for (i = 0; i < $scope.workouts.length; i++) {
			if ($scope.getDate($scope.workouts[i].date) - pDay.date === 0
					&& $scope.workouts[i].type == pType)
				filteredWorkouts.push($scope.workouts[i]);
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
		for (i = 0; i < $scope.workouts.length; i++) {
			if ($scope.getDate($scope.workouts[i].date) - pDay.date === 0
					&& pType == $scope.workouts[i].type)
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
			if (pWorkout.workout_class) {
				var lClassLabel = "";
				for ( var i = 0; i < $scope.workoutData.workoutClasses.length; i++) {
					if ($scope.workoutData.workoutClasses[i]['code'] == pWorkout.workout_class) {
						if (pWorkout.workout_boat) {
							if ($scope.isShortBoat(pWorkout.workout_boat))
								lClassLabel += $scope.messages.statistics.labels['shortBoat'];
							else if ($scope.isLongBoat(pWorkout.workout_boat))
								lClassLabel += $scope.messages.statistics.labels['longBoat'];
						}
						if (!lClassLabel) {
							lClassLabel = $scope.workoutData.workoutClasses[i]['label'];
						}
					}
				}
				lResult += lClassLabel + " / ";
			}
			if (pWorkout.workout_type)
				lResult += pWorkout.workout_type + " / ";
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
			if(lBoat && lBoat.type)
				lType = lBoat.type
		} else if (pBoat && pBoat.length) {
			for ( var i = 0; i < pBoat.length; i++) {
				var lTmp = $scope.findItemByCode($scope.workoutData.boatTypes,
						pBoat[i]);
				if(lTmp && lTmp.type){
					if (!lType){
						lBoat = lTmp;
						lType = lTmp.type;
					}
					else if (lType != lTmp.type) {
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

}