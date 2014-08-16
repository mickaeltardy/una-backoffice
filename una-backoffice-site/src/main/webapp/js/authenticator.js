function AuthenticationCtrl($scope, $http, $location, $rootScope) {

	$http.get('../shared/data/messages.json').success(function(data) {
		$rootScope.messages = data;
	});

	$scope.attemptLogin = function() {
		$http({
			method : "POST",
			data : $scope.auth,
			url : '../server/service/authenticate'
		}).success(function(data) {
			if (data.status=="ok") {
				$rootScope.loggedUser = data.userName;
				var lRedirection = ($rootScope.redirection) ? $rootScope.redirection: $rootScope.defaultPath;
				$location.path(lRedirection);
			} else {
				$scope.auth.error = $scope.messages.errors.invalidAuth;
			}
		});

	}
	
	$scope.resetPassword = function() {
		$http({
			method : "POST",
			data : $scope.auth,
			url : '../server/service/resetPassword'
		}).success(function(data) {
			if (data.status=="ok") {
				$scope.passwordForgotten = false;
			} else {
				alert("Fail");
			}
		});

	}

	$scope.checkAuth = function() {
		
		$http.get('../server/service/authenticate').success(function(data) {
			if (data.status=="ok") {
				$rootScope.loggedUser = data.userName;
				var lRedirection = ($rootScope.redirection) ? $rootScope.redirection: $rootScope.defaultPath;
					
				$location.path(lRedirection);
			}
		});
		

	}

	$scope.logout = function(){
		$http.get('../server/service/logout').success(function(data) {
			if (data.status == "ok") {
				$rootScope.loggedUser = "";
				$location.path("");
			}
		});
	}
	
	$scope.forgetPassword = function(){
		$scope.passwordForgotten = true;
	}
	
	$scope.closePasswordResetter = function(){
		$scope.passwordForgotten = false;
	}
	
	$rootScope.$on("logout", $scope.logout);
	
	$scope.auth = new Object();
}