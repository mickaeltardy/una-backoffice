function AthletesStatisticsCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = WorkoutsManagerCtrl($scope, $http, $routeParams,
			$rootScope);

	$http.get('app/sessions/athletes').success(function(data) {
		$scope.athletes = data;
		$scope.filterAthletes();
	});

	$scope.filterAthletes = function() {
		$scope.members = new Array();
		var lTmp = new Array();
		if ($scope.athletes && $scope.workouts) {
			for (var j = 0; j < $scope.athletes.length; j++) {
				for (var i = 0; i < $scope.workouts.length; i++) {
					if (!$scope.arrayContains(lTmp,
							$scope.workouts[i].member_id)
							&& $scope.workouts[i].member_id == $scope.athletes[j].id) {
						$scope.members.push($scope.athletes[j]);
						lTmp.push($scope.athletes[j].id);
					}
				}
			}
		}
		return true;
	}

	$scope.getMembersOfCategory = function(pMembers, pCategory) {
		var lMembers = new Array();
		if (pMembers && pMembers.length > 0) {
			for (var i = 0; i < pMembers.length; i++) {
				if (pMembers[i].category == pCategory)
					lMembers.push(pMembers[i]);
			}
		}
		return lMembers;
	}

	$scope.setActiveMemberForStats = function(member) {
		if (!$scope.activeMember || member.id != $scope.activeMember.id) {
			$scope.activeMember = member;
			$rootScope.activeMember = member;
			var lScope = new Object();
			lScope.athleteId = member.username; 
			$rootScope.$broadcast('memberSelectedForStats', lScope);
		}
	}
	

	$scope.collectWorkouts = function() {
		if ($scope.sessionsLoaded && $scope.tasksLoaded) {
			$scope.workouts = $scope.sessions.concat($scope.tasks);
			$scope.$broadcast("allWorkoutsLoaded", $scope.workouts, $rootScope.activeMember);
			
		}
	}

	$scope.$on("allWorkoutsLoaded", $scope.loadPersonalStatistics);

	$scope.$on("sessionsLoaded", function() {
		$scope.sessionsLoaded = true;
		$scope.collectWorkouts();
	});
	$scope.$on("tasksLoaded", function() {
		$scope.tasksLoaded = true;
		$scope.collectWorkouts();
	});

	$scope.$on("memberSelectedForStats", $scope.updateMyTasks);
	$scope.$on("memberSelectedForStats", $scope.updateMySessions);

	$scope.members = new Array();

}