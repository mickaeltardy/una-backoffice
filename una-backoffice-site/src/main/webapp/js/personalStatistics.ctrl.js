function PersonalStatisticsCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = WorkoutsManagerCtrl($scope, $http, $routeParams,
			$rootScope);
	
	$scope.getMyWorkoutData();
	


	$scope.$on("workoutsLoaded", $scope.loadPersonalStatistics);
}