var lData;
function BackofficeCtrl($scope, $http, $routeParams, $rootScope, $location) {

	$http.get('app/resources/messages.datasource').success(function(data) {
		$rootScope.messages = data;
		$scope.messages = data;
		$rootScope.$broadcast("messagesLoaded", 1);
	});

	$scope.checkAuth = function() {

		$http.get('app/auth/check').success(function(data) {

			if (data.auth == "true") {
				$rootScope.loggedUser = data.username;
				$rootScope.$broadcast("auth.success", data);
			} else {
				$rootScope.$broadcast("auth.failed", data);
			}
		});

	}
	$scope.loadPublicProfile = function() {
		$http.get('app/profile/' + $rootScope.loggedUser + '/public').success(
				function(data) {
					$rootScope.profile = data;
					if (data.id) {

						$scope.profile = data;
						$rootScope = $scope.profile;
						$scope.$broadcast("profileLoaded", $scope.profile);
					}
				});
	}

	$rootScope.validateExternalAccount = function(pRequest) {
		$scope.serverRequestOngoing(true);
		$http(
				{
					method : "POST",
					data : $scope.param(pRequest),
					url : 'app/registration/external',
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
					}
				}).success(function(data) {
			if (data.result == "success") {
				$scope.authenticate(data.username, data.openid);
			} else {
				$scope.serverRequestOngoing(false);
				$rootScope.notifications.push({
					"cssClass" : "error",
					"text" : $scope.messages.errors.invalidAuth
				});
			}
		});
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

	
	
	$scope.param = function(obj) {
		var query = '', name, value, fullSubName, subName, subValue, innerObj, i;

		for (name in obj) {
			value = obj[name];

			if (value instanceof Array) {
				for (i = 0; i < value.length; ++i) {
					subValue = value[i];
					fullSubName = name + '[' + i + ']';
					innerObj = {};
					innerObj[fullSubName] = subValue;
					query += param(innerObj) + '&';
				}
			} else if (value instanceof Object) {
				for (subName in value) {
					subValue = value[subName];
					fullSubName = name + '[' + subName + ']';
					innerObj = {};
					innerObj[fullSubName] = subValue;
					query += param(innerObj) + '&';
				}
			} else if (value !== undefined && value !== null)
				query += encodeURIComponent(name) + '='
						+ encodeURIComponent(value) + '&';
		}

		return query.length ? query.substr(0, query.length - 1) : query;
	};

	$scope.serverRequestOngoing = function(pStatus) {
		var lPreloader = jQuery("#preloaderContainer");
		if (pStatus && !lPreloader.is(":visible"))
			lPreloader.fadeIn();
		else if (!pStatus && lPreloader.is(":visible"))
			lPreloader.fadeOut();
	}

	$scope.isArrayValid = function(pArray) {
		return (pArray && typeof pContainer !== 'string' && pArray.length);
	}

	$scope.getToolLabel = function(pTool) {
		if ($scope.tools) {
			var lToolsCnt = $scope.tools.length;
			for (i = 0; i < lToolsCnt; i++) {
				if ($scope.tools[i] && $scope.tools[i].code == pTool)
					return $scope.tools[i].label;

			}
		}
		return "";
	};

	$scope.arrayContains = function(pContainer, pElement) {
		if ($scope.isArrayValid(pContainer) && (pElement === "" || pElement)) {
			for (var i = 0; i < pContainer.length; i++) {
				if (pContainer[i] === pElement) {
					return true;
				}
			}
		}
		return false;
	};

	$scope.arrayContained = function(pContainer, pArray) {
		var lResult = false;
		if ($scope.isArrayValid(pContainer) && $scope.isArrayValid(pArray)) {
			for (var i = 0; i < pArray.length; i++) {
				var lContains = false;
				for (var j = 0; j < pContainer.length; j++) {
					if (pArray[i] === pContainer[j]) {
						lContains = true;
						lResult = true;
						break;
					}
				}
				if (!lContains) {
					lResult = false;
					break;
				}

			}
		}
		return lResult;
	};

	$scope.findItemByCode = function(pArray, pCode) {
		var lResult = null;

		if (pArray && pArray.length && pCode) {
			for (var i = 0; i < pArray.length; i++) {
				if (pArray[i]['code'] == pCode) {
					lResult = pArray[i];
					break;
				}
			}
		}

		return lResult;
	}

	$scope.tool = $routeParams.tool;

	$scope.tools = $rootScope.tools;

	$scope.messages = $rootScope.messages;

	$scope.loggedUser = $rootScope.loggedUser;

	$scope.checkAuth();

	$scope.$on("auth.success", $scope.loadPublicProfile)

}

function FileMgrCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope, $location);

}
function CrewCompositorCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope, $location);
}
function StatsCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope, $location);
}

var CommonCtrl = function($rootScope, $http, $route) {
	$rootScope.tmpFlag = "check";

	if (!$rootScope.notifications || !$rootScope.requestRedirect)
		$rootScope.notifications = new Array();

	$rootScope.resetNotifications = function() {
		$rootScope.notifications = new Array();
	}

	$rootScope.requestRedirect = false;

	$rootScope.tool = ($route.current.params.tool) ? $route.current.params.tool
			: 'login';

	$rootScope.getModules = function() {
		$http.get('app/modules').success(function(data) {
			$rootScope.tools = data;
		});
	}
	$rootScope.getModules();

	$rootScope.menuUrl = "app/resources/templates-menu.backoffice.html";
	if ($route.current.params.tool) {
		$rootScope.redirection = $route.current.params.tool + "/";

		$rootScope.toolUrl = "app/resources/templates-tools-"
				+ $route.current.params.tool + ".tpl.html";
	}
	$rootScope.workoutsEditor = "app/resources/templates-utils-newWorkoutsEditor.tpl.html";

	$rootScope.workoutsStatistics = "app/resources/templates-utils-workoutPersonalStats.tpl.html";
	jQuery("#nav-button").off("click")

	jQuery("#nav-button").click(function() {
		jQuery("#nav-panel").slideToggle();
	})

};

var lBackOfficeApp = angular.module('unaBackOffice',
		[ 'ngRoute', 'ngSanitize' ]);

lBackOfficeApp
		.config([
				'$routeProvider',
				function($routeProvider) {
					$routeProvider
							.when(
									"/login",
									{
										module : "login",
										templateUrl : 'app/resources/templates-login.gui.html',
										controller : AuthenticationCtrl,
										resolve : {
											common : [ '$rootScope', '$http',
													'$route', CommonCtrl ]
										}

									})
							.when(
									'/:tool/:params?',
									{
										templateUrl : 'app/resources/templates-backoffice.gui.html',
										controller : function($route, $scope,
												$http, $routeParams,
												$rootScope, $location) {
											lCtrlName = $route.current.params.tool
													+ "Ctrl";
											lCtrlName = lCtrlName.charAt(0)
													.toUpperCase()
													+ lCtrlName.slice(1);
											console.log(lCtrlName)
											if (!window[lCtrlName])
												lCtrlName = "BackofficeCtrl";

											this.prototype = ((window[lCtrlName])) ? window[lCtrlName]
													($scope, $http,
															$routeParams,
															$rootScope,
															$location)
													: null;

										},
										resolve : {
											common : [ '$rootScope', '$http',
													'$route', '$location',
													CommonCtrl ]
										}
									}).otherwise({
								redirectTo : '/login'
							});
				} ]);

lBackOfficeApp.run(function($rootScope, $http, $location) {
	// $rootScope.loggedUser = "yyy";

	$rootScope.defaultPath = "simplifiedWorkoutsManager";

	$rootScope.checkModuleAccess = function(pModule) {
		console.log(pModule);
		return true;
	}

	$rootScope.logout = function() {
		$http.get(' j_spring_security_logout').success(function(data) {
			$location.path("");
		});
	}

	// register listener to watch route changes
	$rootScope.$on("$routeChangeStart", function(event, next, current) {
		if ($rootScope.checkModuleAccess(next.params.tool)) {
			console.log("Module available")
		}
		/*
		 * if ($rootScope.loggedUser == null) { // no logged user, we should
		 * begoing to // #login if (next && next.templateUrl ==
		 * "app/resources/templates-login.gui.html") { // already going to
		 * #login, no redirect // needed } else { // not going to #login, we //
		 * should redirect now $location.path("/login"); } }
		 */
		/*
		 * if ($rootScope.loggedUser == null) { // no logged user, we should be
		 * going to #login if (next.templateUrl == "templates/login.gui.html") { //
		 * already going to #login, no redirect needed } else { // not going to
		 * #login, we should redirect now $location.path("/login"); } }
		 */
	});
});

lBackOfficeApp.directive('promptedFormField', promptedFormField);

lBackOfficeApp.directive('uploader', uploader);
lBackOfficeApp.directive('dnduploader', dndUploader);

lBackOfficeApp.directive('calendarView', calendarView);
