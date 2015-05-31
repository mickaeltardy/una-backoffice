function SignupCtrl($scope, $http, $routeParams, $rootScope, $location) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope, $location) ;

	$scope.submit = function() {
		var lResult = true;
		var lMessage = ""
		var lErrors = new Array();
		if ($scope.validateForm()) {
			$scope.serverRequestOngoing(true);
			$http(
					{
						method : 'POST',
						url : "app/registration/internal",
						data : $scope.param($scope.member),
						headers : {
							'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
						}
					}).success(function(data, status) {
				$scope.serverRequestOngoing(false);
				if (data.result == "success") {
					$scope.completeForm = true;
					$scope.inProgress = false;
					$rootScope.requestRedirect = true;
					$rootScope.notifications.push({
						"cssClass" : "info",
						"text" : $scope.messages.notifications.accountCreated
					});
					$location.path("/login");
					$('html,body').scrollTop(0);
				} else
					alert($scope.messages.errors.serverError);
				$scope.validatingForm = false;
			}).error(function() {
				$scope.serverRequestOngoing(false)
			});
		}
		if (lMessage)
			alert(lMessage);
		return lResult;
	}

	$scope.checkEmailExistence = function() {
		var lData = new Object();
		lData.email = $scope.member.email;

		if ($scope.checkRequiredValue(lData.email)) {
			if ($scope.validateEmail(lData.email)) {
				$http(
						{
							method : 'POST',
							url : "app/registration/checkEmailExistence",
							data : $scope.param(lData),
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
							}
						})
						.success(
								function(data, status) {

									if (data == "false") {

										$scope.errors.email = false;
										$scope.errorMsgs.email = "";
									} else if (data == "true") {
										$scope.errors.email = true;
										$scope.errorMsgs.email = $scope.messages.notifications.emailExists;

									} else {
										$scope.errors.email = true;
										$scope.errorMsgs.email = $scope.messages.notifications.emailCanNotBeChecked;
									}
								})
						.error(
								function() {
									$scope.errors.email = true;
									$scope.errorMsgs.email = $scope.messages.notifications.emailCanNotBeChecked;
								});
			} else {

				$scope.errors.email = true;
				$scope.errorMsgs.email = $scope.messages.notifications.emailInvalid;
			}
		} else {
			$scope.errors.email = true;
			$scope.errorMsgs.email = $scope.messages.notifications.emailEmpty;
		}
		return true;
	}

	$scope.validateEmail = function(pEmail) {
		var lMailRegex = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		return (lMailRegex.test(pEmail));
	}

	$scope.checkEmailConfirmation = function() {

		var lEmail = $scope.member.email;
		var lEmailConfirmation = $scope.member.emailConfirmation;

		if (lEmail != lEmailConfirmation) {
			$scope.errors.emailsDiff = true;
		} else {
			$scope.errors.emailsDiff = false;
		}

	}

	$scope.checkPassword = function() {
		var lPassword = $scope.member.password;
		if (!lPassword || lPassword.length < 6) {
			$scope.errors.badPassword = true;
		} else {
			$scope.errors.badPassword = false;
		}
	}

	$scope.checkPasswordConfirmation = function() {
		var lPassword = $scope.member.password;
		var lPasswordConfirmation = $scope.member.passwordConfirmation;

		if (lPassword != lPasswordConfirmation) {
			$scope.errors.passwordsDiff = true;
		} else {
			$scope.errors.passwordsDiff = false;
		}

	}
	$scope.checkRequiredValue = function(pValue) {
		return (pValue && pValue.length > 0)
	}

	$scope.checkName = function() {

		$scope.errors.nameEmpty = !($scope
				.checkRequiredValue($scope.member.name));
	}

	$scope.checkSurname = function() {

		$scope.errors.surnameEmpty = !($scope
				.checkRequiredValue($scope.member.surname));
	}

	$scope.canFormBeValidated = function() {
		var lResult = true;
		if ($scope.errors) {
			for ( var lErr in $scope.errors) {
				if ($scope.errors.hasOwnProperty(lErr)) {
					if ($scope.errors[lErr] == true)
						lResult = false;
				}
			}
		}
		return lResult;
	}

	$scope.validateForm = function() {
		$scope.checkEmailExistence();
		$scope.checkEmailConfirmation();
		$scope.checkPassword();
		$scope.checkPasswordConfirmation();
		$scope.checkSurname();
		$scope.checkName();

		if ($scope.canFormBeValidated())
			return true;
		else
			return false;
	}

	$scope.errorMsgs = new Object();
	$scope.errors = new Object();
	$scope.member = new Object();
}