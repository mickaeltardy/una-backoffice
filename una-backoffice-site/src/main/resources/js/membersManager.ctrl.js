function MembersManagerCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope);

	$http.get('app/resources/workoutsData.datasource').success(function(data) {
		$scope.workoutData = data;
	});
	
	$http.get('app/supervisor/profiles').success(function(data) {

		if (data) {
			for (i = 0; i < data.length; i++) {
				if (data[i].dump)
					data[i].dump.docsToProvide = "";
				if(data[i].invalid == 0 || !data[i].invalid){
					$scope.members.push(data[i]);
					$scope.filteredMembers.push(data[i]);
				}
			}

		}
	});

	$http.get('app/resources/data.datasource').success(function(data) {
		$scope.categories = data.categeries;
		$scope.data = data;
	});

	$scope.orderProp = "surname";

	$scope.filterCategory = ""

	$scope.getMembers = function() {
		lMembers = new Array();
		if ($scope.members) {
			for (i = 0; i < $scope.members.length; i++) {
				if ((($scope.filterCategory && $scope.members[i].dump.category == $scope.filterCategory) || !$scope.filterCategory)
						&& (($scope.filterState && $scope.members[i].state == $scope.filterState) || !$scope.filterState))
					lMembers.push($scope.members[i]);
			}
		}
		return lMembers;
	}

	$scope.getMemberState = function(pState) {
		return (pState == 1) ? "validé" : "en attente";
	}

	$scope.changeMemberCategory = function(pMember) {
		$scope.activeMember = pMember;
		$scope.changeCategory = true;
	
		$("#categoryModifierDiv").css( {position:"absolute", top:event.pageY, left: event.pageX});
	}
	$scope.updateMemberCategory = function(pMember, pCategory) {
		$scope.changeCategory = false;
		pMember.category = pCategory;
		$scope.postMemberData(pMember.username, pMember.category, 'category');
	}
	$scope.updateMemberAutonomy = function(pMember) {

		pMember.autonomous = (pMember.autonomous == 1) ? 0 : 1;
		$scope.postMemberData(pMember.username, pMember.autonomous, 'autonomous');
		

	}
	$scope.updateMemberLevel = function(pMember) {
		
		pMember.level = (pMember.level == "advanced") ? "newbie" : "advanced";
		$scope.postMemberData(pMember.username, pMember.level, 'level');
		

	}
	$scope.postMemberData = function(pMemberId, pData, pFieldName) {
		var lResult = 0;
		var lData = new Object();

		lData.username = pMemberId;

		lData[pFieldName] = pData;

		$http({
			method : 'PUT',
			url : "app/supervisor/profiles/"+lData.username,
			data : lData
		}).success(function(data, status) {
			if (data == "OK") {

			}
		});
		return true;
	}

	$scope.getMessage = function(text, group) {
		if ($rootScope.messages)
			return $rootScope.messages[group][text];
		else
			return "";
	};

	$scope.getCsv = function() {

		$iframe = $("<iframe>").hide().attr("src",
				"../server/service/getFullMembersList?format=csv").appendTo(
				"body");

	};

	$scope.filterMembersByCategory = function(pData) {
		$scope.filterCategory = pData.filterCategory
		$scope.filteredMembers = $scope.getMembers();

	}

	$scope.filterMembersByState = function(pData) {
		if (pData)
			$scope.filterState = pData.filterState
		$scope.filteredMembers = $scope.getMembers();

	}

	$scope.activeMember = new Array();
	$scope.members = new Array();
	$scope.filteredMembers = new Array();
	$scope.changeCategory = false;

}