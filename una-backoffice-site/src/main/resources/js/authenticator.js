function AuthenticationCtrl($scope, $http, $routeParams, $rootScope, $location) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope,
			$location);
	
	$scope.checkLogin = function(){
		debugger;
		alert($scope.auth.login)
	}
	

	$scope.authenticate = function(pUsername, pPassword) {
		$http(
				{
					method : "POST",
					data : $scope.param({
						"j_username" : pUsername,
						"j_password" : pPassword
					}),
					url : 'j_spring_security_check',
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
					}
				})
				.success(
						function(data) {
							$scope.serverRequestOngoing(false);
							if (data.status == "success") {

								$rootScope.loggedUser = data.username;

								var lRedirection = ($rootScope.redirection) ? $rootScope.redirection
										: $rootScope.defaultPath;

								$location.path(lRedirection);
							} else {

								$rootScope.notifications.push({
									"cssClass" : "error",
									"text" : $scope.messages.errors.invalidAuth
								});
							}
						});
	}

	$scope.attemptLogin = function() {
		$http({
			method : "POST",
			data : $scope.auth,
			url : '../server/service/authenticate'
		})
				.success(
						function(data) {
							if (data.status == "ok") {
								$rootScope.loggedUser = data.userName;
								var lRedirection = ($rootScope.redirection) ? $rootScope.redirection
										: $rootScope.defaultPath;
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
			if (data.status == "ok") {
				$scope.passwordForgotten = false;
			} else {
				alert("Fail");
			}
		});

	}

	$scope.checkAuth = function() {

		$http
				.get('app/auth/check')
				.success(
						function(data) {
							debugger;
							if (data.auth == "true") {
								$rootScope.loggedUser = data.username;
								var lRedirection = ($rootScope.redirection) ? $rootScope.redirection
										: $rootScope.defaultPath;

								$location.path(lRedirection);
							}
						});

	}

	$scope.logout = function() {
		$http.get(' j_spring_security_logout').success(function(data) {
			$location.path("");
		});
	}

	$scope.forgetPassword = function() {
		$scope.passwordForgotten = true;
	}

	$scope.closePasswordResetter = function() {
		$scope.passwordForgotten = false;
	}

	$rootScope.$on("logout", $scope.logout);

	$scope.auth = new Object();

	window.fbAsyncInit = function() {

		FB.init({
			appId : '1483521245220414',
			cookie : true, // enable cookies to allow the server to access
			// the session
			xfbml : true, // parse social plugins on this page
			version : 'v2.0' // use version 2.0
		});

		// Now that we've initialized the JavaScript SDK, we call
		// FB.getLoginStatus(). This function gets the state of the
		// person visiting this page and can return one of three states to
		// the callback you provide. They can be:
		//
		// 1. Logged into your app ('connected')
		// 2. Logged into Facebook, but not your app ('not_authorized')
		// 3. Not logged into Facebook and can't tell if they are logged into
		// your app or not.
		//
		// These three cases are handled in the callback function.

		FB.getLoginStatus(function(response) {
			statusChangeCallback(response);
		});

		parseFacebook();

	};

	// Load the SDK asynchronously
	(function(d, s, id) {

		var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id)) {
			fbAsyncInit();
			return;
		}

		js = d.createElement(s);
		js.id = id;
		js.src = "//connect.facebook.net/en_US/sdk.js";
		fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));

	(function() {
		var po = document.createElement('script');
		po.type = 'text/javascript';
		po.async = true;
		po.id = "googleScript";
		po.src = 'https://apis.google.com/js/client:plusone.js?onload=render';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(po, s);
	})();

}