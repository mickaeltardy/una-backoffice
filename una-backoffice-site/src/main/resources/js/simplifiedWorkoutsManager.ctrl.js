function SimplifiedWorkoutsManagerCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = WorkoutsManagerCtrl($scope, $http, $routeParams,
			$rootScope);
	
	$scope.$on("profileLoaded", $scope.updateMySessions)

	
	$scope.filterWorkouts = function() {
		var lFilteredWorkouts = new Array();
		var lWorkouts = $scope.getWorkoutsByType(1);
		var lWorkoutsCnt = lWorkouts.length;
		
		for (i = 0; i < lWorkoutsCnt; i++) {
				lFilteredWorkouts.push(lWorkouts[i]);
		}
		$scope.setFilteredWorkoutsByType(lFilteredWorkouts, 1)
	}

	$scope.orderProp = "date";

	$scope.$on("workoutsLoaded", $scope.filterWorkouts);

	$scope.removeWorkout = function(pWorkout) {
		var lResult = 0;
		var lData = new Array();
		if (!confirm("Etes-vous sur de vouloir supprimer cet entrainement ?"))
			return true;

		$scope.currentWorkout = pWorkout;
		lData.push($scope.currentWorkout);
		$http({
			method : 'POST',
			url : "app/sessions/remove",
			data : lData
		}).success(function(data, status) {
			if (data.status == "success" && !data.error) {
				var lTmpWorkouts = $scope.sessions;
				$scope.sessions = new Array();
				var lWorkoutsCnt = lTmpWorkouts.length;
				for (i = 0; i < lWorkoutsCnt; i++) {
					if (lTmpWorkouts[i].id != $scope.currentWorkout.id)
						$scope.sessions.push(lTmpWorkouts[i]);
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
	$scope.$on("sessionSaved", $scope.updateMySessions);
	


}
