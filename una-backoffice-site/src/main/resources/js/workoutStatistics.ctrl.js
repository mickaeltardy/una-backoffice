function WorkoutStatisticsCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = WorkoutSupervisorCtrl($scope, $http, $routeParams,
			$rootScope);
	$rootScope.getAllWorkoutData();


	$scope.applyFilter = function() {
		if ($scope.filter) {
			lFilteredWorkouts = new Array();
			var lWorkouts = $scope.getWorkoutsByType(1);
			var lWorkoutsCnt = lWorkouts.length;
			for (i = 0; i < lWorkoutsCnt; i++) {
				if ($scope.workoutAcceptable(lWorkouts[i], $scope.filter))
					lFilteredWorkouts.push(lWorkouts[i]);
			}
			$scope.setFilteredWorkoutsByType(lFilteredWorkouts, 1)
		}
	}

	$scope.workoutAcceptable = function(pWorkout, pFilter) {
		var lResult = true;
		var lFilterFields = [ 'type', 'athleteCategory', 'athleteLevel',
				'athleteSex', 'athleteId', 'type', 'boat', 'class' ]

		for ( var i = 0; i < lFilterFields.length; i++) {
			if (pFilter[lFilterFields[i]]
					&& pWorkout[lFilterFields[i]] != pFilter[lFilterFields[i]]) {
				lResult = false;
				break;
			}

		}

		return lResult;
	}

	$scope.changeOrder = function(pOrder) {
		$scope.orderProp = pOrder;
	}

	$scope.orderProp = "date";
	$scope.filter = new Object();
	$scope.filter.type = 1;
	$scope.members = new Array();

	$scope.$on("sessionsLoaded", $scope.applyFilter);

	$scope.$on("sessionsLoaded", $scope.updateMembersList);
	
	$scope.$on("tasksLoaded", $scope.applyFilter);

	$scope.$on("tasksLoaded", $scope.updateMembersList);
}