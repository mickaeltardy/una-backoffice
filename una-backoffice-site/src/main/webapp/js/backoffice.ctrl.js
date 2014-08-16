var lData;
function BackofficeCtrl($scope, $http, $routeParams, $rootScope) {

	$scope.logout = function() {

		$rootScope.$broadcast('logout', 1);
	};

	$scope.isArrayValid = function(pArray) {
		return (pArray && typeof pContainer !== 'string' && pArray.length);
	}

	$scope.getToolLabel = function(pTool) {
		if ($rootScope.tools) {
			var lToolsCnt = $rootScope.tools.length;
			for (i = 0; i < lToolsCnt; i++) {
				if ($rootScope.tools[i] && $rootScope.tools[i].code == pTool)
					return $rootScope.tools[i].label;

			}
		}
		return "";
	};

	$scope.arrayContains = function(pContainer, pElement) {
		if ($scope.isArrayValid(pContainer) && (pElement === "" || pElement)) {
			for ( var i = 0; i < pContainer.length; i++) {
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
			for ( var i = 0; i < pArray.length; i++) {
				var lContains = false;
				for ( var j = 0; j < pContainer.length; j++) {
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
			for ( var i = 0; i < pArray.length; i++) {
				if (pArray[i]['code'] == pCode) {
					lResult = pArray[i];
					break;
				}
			}
		}

		return lResult;
	}

	$scope.tool = $routeParams.tool;

}

function FileMgrCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope);

}
function CrewCompositorCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope);
}
function StatsCtrl($scope, $http, $routeParams, $rootScope) {
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope);
}

var CommonCtrl = function($rootScope, $http, $route) {
	$rootScope.tmpFlag = "check";
	$http.get('../shared/data/messages.json').success(function(data) {
		$rootScope.messages = data;
	});
	$http.get('../server/service/getModulesList').success(function(data) {
		$rootScope.tools = data;
	});
	$rootScope.menuUrl = "templates/menu.backoffice.html";
	if ($route.current.params.tool) {
		$rootScope.redirection = "backoffice/" + $route.current.params.tool
				+ "/";
		$rootScope.toolUrl = "templates/tools/" + $route.current.params.tool
				+ ".tpl.html";
	}
	$rootScope.workoutsEditor = "templates/utils/newWorkoutsEditor.tpl.html";

	$rootScope.workoutsStatistics = "templates/utils/workoutPersonalStats.tpl.html";
};

var lBackOfficeApp = angular.module('unaBackOffice', []);

lBackOfficeApp.config([
		'$routeProvider',
		function($routeProvider) {
			$routeProvider.when('/login', {
				templateUrl : 'templates/login.gui.html',
				controller : AuthenticationCtrl

			}).when('/backoffice', {

				templateUrl : 'templates/backoffice.gui.html',
				controller : BackofficeCtrl,

				resolve : {
					common : CommonCtrl,

				}
			}).when(
					'/backoffice/:tool/*params',
					{
						templateUrl : 'templates/backoffice.gui.html',
						controller : function($route, $scope, $http,
								$routeParams, $rootScope) {
							lCtrlName = $route.current.params.tool + "Ctrl";
							lCtrlName = lCtrlName.charAt(0).toUpperCase()
									+ lCtrlName.slice(1);
							this.prototype = window[lCtrlName]($scope, $http,
									$routeParams, $rootScope);

						},
						resolve : {
							common : [ '$rootScope', '$http', '$route',
									CommonCtrl ]
						}
					}).otherwise({
				redirectTo : '/login'
			});
		} ]);

lBackOfficeApp.run(function($rootScope, $http, $location, $rootScope) {
	// $rootScope.loggedUser = "xxx";
	$rootScope.defaultPath = "backoffice";

	// register listener to watch route changes
	$rootScope.$on("$routeChangeStart", function(event, next, current) {
		if ($rootScope.loggedUser == null) {
			// no logged user, we should be going to #login
			if (next.templateUrl == "templates/login.gui.html") {
				// already going to #login, no redirect needed
			} else {
				// not going to #login, we should redirect now
				$location.path("/login");
			}
		}
	});
});

lBackOfficeApp.directive('promptedFormField', promptedFormField);

lBackOfficeApp.directive('calendarView', calendarView);

lBackOfficeApp.directive('uploader', uploader);
lBackOfficeApp.directive('dnduploader', dndUploader);

lBackOfficeApp.directive('calendarView', calendarView);