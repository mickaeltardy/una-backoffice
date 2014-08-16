function CrewsCompositorCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope);
	$http.get('../server/service/getFullAthletesList').success(function(data) {
		$scope.athletes = data;
	});

	$http.get('../shared/data/categories.json').success(function(data) {
		$scope.categories = data;
	});

	$scope.displayInline = "inline";
	$scope.displayNone = "none";
	// TODO add local storage support
	
	$scope.currentComposition = new Object();
	$scope.currentComposition.teams = [];
	$scope.orderProp = "surname";
	$scope.promptPositionTop = "0px";
	$scope.promptPositionLeft = "0px";
	$scope.displayTeamsSelector = false;
	$scope.displayPrompt = false;
	$scope.promptFilter = "";
	$scope.promptedElement = null;
	$scope.selectTeam = function() {
		$scope.displayTeamsSelector = ($scope.displayTeamsSelector == false) ? true
				: false;
	}

	$scope.getSavedCompositions = function (){
		$http.get('../server/service/getCrewsCompositions').success(function(data) {
			$scope.compositions = data;
		});
	}
	
	$scope.addTeam = function(pCategory) {
		var lAthletes = [];

		for (i = 0; i < pCategory.size; i++) {
			lAthletes.push({});
		}

		var lTeam = {
			label : pCategory.label,
			size : pCategory.size,
			athletes : lAthletes
		}
		$scope.currentComposition.teams.push(lTeam);
		$scope.displayTeamsSelector = false;
	}

	$scope.promptAthlete = function(pElement, pDisplay, $event) {
		$scope.displayPrompt = pDisplay;
		$scope.promptFilter = pElement.athlete.name;
		$scope.promptedElement = pElement;
		if($event && $event.target){
			$scope.promptPositionTop = $($event.target).position().top + 30 + "px";
			$scope.promptPositionLeft = $($event.target).position().left + "px";
		}
	}

	$scope.getPromptStyle = function() {
		return {
			position : "absolute",
			top : $scope.promptPositionTop,
			left : $scope.promptPositionLeft
		}
	}

	$scope.selectAthlete = function(pElement) {
		$scope.displayPrompt = false;

		$scope.promptedElement.athlete.name = pElement.athlete.surname + " "
				+ pElement.athlete.name + ";" + pElement.athlete.licence;
	}
	$scope.saveComposition = function(pNew) {
		var lData = $scope.currentComposition;
		if(pNew) lData.id = 0; 
		$http({
			method : 'POST',
			url : "../server/service/saveCrewsComposition",
			data : lData
		}).success(function(data, status) {
			$rootScope.$broadcast("compositionSaved", data);
		});
	}

	$scope.validateTeams = function() {
		// $http.post("http://localhost/angularjs-fun/csvGen.php", {"data" :
		// $scope.teams});
		/*
		 * $.fileDownload("http://localhost/angularjs-fun/csvGen.php", {
		 * preparingMessageHtml: "We are preparing your report, please wait...",
		 * failMessageHtml: "There was a problem generating your report, please
		 * try again.", httpMethod: "POST", data: {data: $scope.teams } });
		 */

		$http({
			method : 'POST',
			url : "../server/service/getCrewsCompositionCsv",
			data : $scope.currentComposition
		}).success(function(data, status) {
			$iframe = $("<iframe>").hide().attr("src", data).appendTo("body");
		});

	}
	
	$scope.selectComposition = function (pComposition){
		if(pComposition){
			$scope.currentComposition = new Object();
			$scope.currentComposition.id = pComposition.id;
			$scope.currentComposition.label = pComposition.label;
			$scope.currentComposition.state = pComposition.state;
			$scope.currentComposition.insert_date = pComposition.insert_date;
			$scope.currentComposition.teams = pComposition.teams;
		
		}
		
	}
	

	$scope.removeComposition = function(pComposition) {
		var lResult = 0;
		var lData = new Array();
		if (!confirm("Etes-vous sur de vouloir supprimer cette composition ?"))
			return true;

		$scope.currentComposition = pComposition;
		$scope.currentComposition.state = 0;
		$scope.saveComposition(false);
		$scope.currentComposition = new Object();
		return true;
	}	

	$scope.removeTeam = function(pTeamIndex) {
		var lResult = 0;
		var lData = new Array();
		if (!confirm("Etes-vous sur de vouloir supprimer cet équipage ?"))
			return true;
		$scope.currentComposition.teams.splice(pTeamIndex,1);

		return true;
	}

	
	$scope.compositions = new Array();
	$scope.getSavedCompositions();
	
	$scope.$on("compositionSaved", $scope.getSavedCompositions)

}