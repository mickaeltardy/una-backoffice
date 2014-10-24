function WorkoutSupervisorCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = WorkoutCommonsCtrl($scope, $http, $routeParams, $rootScope);

	$scope.getAllTasksData = function(pScope) {
		var lData = new Object();

		if (pScope != null) {
			if (pScope.days && pScope.days.length > 0) {
				lData.dateFrom = pScope.days[0];
				lData.dateTo = pScope.days[pScope.days.length - 1];
			}
		}
		debugger;
		$http({
			method : 'POST',
			url : "app/tasks/retrieve",
			data : lData
		}).success(function(data, status) {
			if (data) {
				for (i = 0; i < data.length; i++) {
					$scope.workouts.push(data[i]);
					$scope.filteredWorkouts.push(data[i]);
				}
				$rootScope.$broadcast("workoutsLoaded", data);
			}
		});
	}

	$scope.updateWorkouts = function(pData) {
		var lScope = new Object();
		if ($scope.days) {
			lScope.days = $scope.days;
		}
		$scope.getAllTasksData(lScope);
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

	$scope.clone = function(obj) {
		if (null == obj || "object" != typeof obj)
			return obj;
		var copy = obj.constructor();
		for ( var attr in obj) {
			if (obj.hasOwnProperty(attr))
				copy[attr] = obj[attr];
		}
		return copy;
	}

	$scope.submit = function() {
		debugger;
		var lResult = 0;
		var lData = new Array();

		lData = $scope.getTasks($scope.currentWorkout);

		$http({
			method : 'POST',
			url : "app/tasks/create",
			data : lData
		}).success(function(data, status) {
			if (data.info) {
				if (!$scope.currentWorkout.id) {
					$scope.currentWorkout.id = data.id / 1;
					$scope.workouts.push($scope.currentWorkout)
				}
				$scope.currentWorkout = new Object();
				$scope.closeEditor();
				$rootScope.$broadcast("workoutsSaved", data);
			} else if (data.error) {
				alert(data.message);
			} else
				alert("Void");
		});
		return true;

	}

	$scope.getTasks = function(pData) {
		var lDateStr = $scope.currentWorkout.date;
		var lDates = lDateStr.split(", ");
		var lTasks = new Array();
		if (lDates.length > 0) {
			for (var i = 0; i < lDates.length; i++) {
				var lTask = $scope.clone($scope.currentWorkout);
				lTask.date = lDates[i];
				lTasks.push(lTask);
			}
		}
		return lTasks;

	}

	$scope.reset = function() {

	}

	$rootScope.addGoal = function(pDay) {
		$scope.operationName = "Ajouter un objectif";
		$scope.editWorkoutShow = true;
		$scope.currentWorkout = new Object();
		$scope.currentWorkout.date = $scope.getChromedDate(pDay.date);
		$scope.currentWorkout.type = 0;
		$scope.currentWorkout.state = 1;
	}

	$scope.editWorkout = function(pWorkout) {
		$scope.currentWorkout = new Object();
		$scope.currentWorkout = pWorkout;
		if ($scope.currentWorkout.type == 0)
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

	// TODO Refactoring
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
	// END REFACTORING

	$scope.currentWorkout = new Object();

	$scope.workouts = new Array();
	$scope.filteredWorkouts = new Array();
	$scope.changeDate = function() {

	}

	$scope.$on("calendarChanged", $scope.getAllTasksData);

	$scope.$on("workoutsSaved", $scope.updateWorkouts);

}
