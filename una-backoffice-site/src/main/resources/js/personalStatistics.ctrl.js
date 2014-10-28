function PersonalStatisticsCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = WorkoutsManagerCtrl($scope, $http, $routeParams,
			$rootScope);
	$scope.sessionsLoaded = false;
	$scope.tasksLoaded = false;

	$scope.collectWorkouts = function() {
		if ($scope.sessionsLoaded && $scope.tasksLoaded) {
			$scope.workouts = $scope.sessions.concat($scope.tasks);
			
			
			$scope.$broadcast("allWorkoutsLoaded", $scope.workouts);
			
		}
	}

	$scope.$on("profileLoaded", $scope.updateMySessions)

	$scope.$on("allWorkoutsLoaded", $scope.loadPersonalStatistics);

	$scope.$on("sessionsLoaded", function() {
		$scope.sessionsLoaded = true;
		$scope.collectWorkouts();
	});
	$scope.$on("tasksLoaded", function() {
		$scope.tasksLoaded = true;
		$scope.collectWorkouts();
	});
}