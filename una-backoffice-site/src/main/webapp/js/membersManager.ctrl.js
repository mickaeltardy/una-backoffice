function MembersManagerCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope);

	$http.get('../shared/data/workoutsData.json').success(function(data) {
		$scope.workoutData = data;
	});
	
	$http.get('../server/service/getFullMembersList').success(function(data) {

		if (data) {
			for (i = 0; i < data.length; i++) {
				if (data[i].dump)
					data[i].dump.docsToProvide = "";
				if(data[i].invalid == 0){
					$scope.members.push(data[i]);
					$scope.filteredMembers.push(data[i]);
				}
			}

		}
	});

	$http.get('../shared/data/data.json').success(function(data) {
		$scope.categories = data.categeries;
		$scope.data = data;
	});

	$scope.orderProp = "surname";

	$scope.filterCategory = ""

	$scope.getMembers = function() {
		lMembers = new Array();
		if ($scope.members) {
			for (i = 0; i < $scope.members.length; i++) {
				if ((($scope.filterCategory && $scope.members[i].dump.category.code == $scope.filterCategory) || !$scope.filterCategory)
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
		$scope.postMemberData(pMember.id, pMember.category, 'category');
	}
	$scope.updateMemberAutonomy = function(pMember) {

		pMember.autonomous = (pMember.autonomous == 1) ? 0 : 1;
		$scope.postMemberData(pMember.id, pMember.autonomous, 'autonomous');
		

	}
	$scope.updateMemberLevel = function(pMember) {
		
		pMember.level = (pMember.level == "advanced") ? "newbie" : "advanced";
		$scope.postMemberData(pMember.id, pMember.level, 'level');
		

	}
	$scope.postMemberData = function(pMemberId, pData, pFieldName) {
		var lResult = 0;
		var lData = new Object();

		lData.memberId = pMemberId;

		lData[pFieldName] = pData;

		$http({
			method : 'POST',
			url : "../server/service/membersUpdate",
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