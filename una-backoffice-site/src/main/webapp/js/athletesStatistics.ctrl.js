function AthletesStatisticsCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = WorkoutsManagerCtrl($scope, $http, $routeParams,
			$rootScope);

	$http.get('../server/service/getFullMembersList').success(function(data) {
		$scope.athletes = data;
		$scope.filterAthletes();
	});

	$scope.filterAthletes = function() {
		$scope.members = new Array();
		var lTmp = new Array();
		if ($scope.athletes && $scope.workouts) {
			for ( var j = 0; j < $scope.athletes.length; j++) {
				for ( var i = 0; i < $scope.workouts.length; i++) {
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
			for ( var i = 0; i < pMembers.length; i++) {
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
			$rootScope
					.$broadcast('memberSelectedForStats', $scope.activeMember);
		}
	}

	$scope.$on("workoutsLoaded", $scope.filterAthletes);

	$scope.$on("memberSelectedForStats", $scope.loadPersonalStatistics);

	$scope.members = new Array();

	$rootScope.getAllWorkoutData();

}