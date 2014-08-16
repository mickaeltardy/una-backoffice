function SimplifiedWorkoutsManagerCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = WorkoutsManagerCtrl($scope, $http, $routeParams,
			$rootScope);

	$scope.getMyWorkoutData();

	$scope.filterWorkouts = function() {
		$scope.filteredWorkouts = new Array();
		var lWorkoutsCnt = $scope.workouts.length;
		for (i = 0; i < lWorkoutsCnt; i++) {
			if ($scope.workouts[i].type == 1)
				$scope.filteredWorkouts.push($scope.workouts[i]);
		}
	}

	$scope.orderProp = "date";

	$scope.$on("workoutsLoaded", $scope.filterWorkouts);

	$scope.removeWorkout = function(pWorkout) {
		var lResult = 0;
		var lData = new Array();
		if (!confirm("Etes-vous sur de vouloir supprimer cet entrainement ?"))
			return true;
		pWorkout.state = 0;

		$scope.currentWorkout = pWorkout;
		lData.push($scope.currentWorkout);
		$http({
			method : 'POST',
			url : "../server/service/saveWorkouts",
			data : lData
		}).success(function(data, status) {
			if (data.info) {
				var lTmpWorkouts = $scope.workouts;
				$scope.workouts = new Array();
				var lWorkoutsCnt = lTmpWorkouts.length;
				for (i = 0; i < lWorkoutsCnt; i++) {
					if (lTmpWorkouts[i].id != $scope.currentWorkout.id)
						$scope.workouts.push(lTmpWorkouts[i]);
				}

				$rootScope.$broadcast('workoutSubmit', $scope.customWorkout);

			} else if (data.error) {
				alert(data.message);
			} else
				alert("Void");
		});
		return true;
	}

	$scope.$on("workoutSubmit", $scope.filterWorkouts);




}
