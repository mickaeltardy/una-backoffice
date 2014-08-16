function WorkoutStatisticsCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = WorkoutSupervisorCtrl($scope, $http, $routeParams,
			$rootScope);
	$rootScope.getAllWorkoutData();


	$scope.applyFilter = function() {
		if ($scope.filter) {
			$scope.filteredWorkouts = new Array();
			var lWorkoutsCnt = $scope.workouts.length;
			for (i = 0; i < lWorkoutsCnt; i++) {
				if ($scope.workoutAcceptable($scope.workouts[i], $scope.filter))
					$scope.filteredWorkouts.push($scope.workouts[i]);
			}
		}
	}

	$scope.workoutAcceptable = function(pWorkout, pFilter) {
		var lResult = true;
		var lFilterFields = [ 'type', 'athlete_category', 'athlete_level',
				'athlete_sex', 'member_name', 'workout_type', 'workout_boat', 'workout_class' ]

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

	$scope.$on("workoutsLoaded", $scope.applyFilter);

	$scope.$on("workoutsLoaded", $scope.updateMembersList);
}