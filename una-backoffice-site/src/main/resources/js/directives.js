/*
 * TODO 
 * Add validation and prompt if error
 * Also need to be able to link somehow all of the fields togethe in order to define the overall form validity
 */
function promptedFormField() {

	return {
		restrict : 'A',
		templateUrl : 'templates/utils/promptedFormField.tpl.html',
		scope : {
			value : '=',
			label : '@',
			name : '@',
			type : '@',
			placeholder : '@',
			cssClass : '@'

		},
		link : function(scope, elem, attrs) {
			if (!scope.cssClass)
				scope.cssClass = "field";

			scope.$watch('value', function(oldVal, newVal) {
				if (newVal != oldVal) {
				}
			});
		}
	}
}

function uploader() {

	return {
		restrict : 'E',
		scope : {
			action : '@'
		},
		controller : [ '$scope', '$http', '$rootScope', ClassicUploaderCtrl ],
		link : function(scope, elem, attrs, ctrl) {
			elem.find('.fakeUploader').click(function() {
				elem.find('input[type="file"]').click();
			});
		},
		replace : false,
		templateUrl : 'templates/classic.uploader.html'
	};

}

function dndUploader() {

	return {
		restrict : 'E',
		scope : {
			action : '@'
		},
		controller : [ '$scope', '$http', '$rootScope',
				DragAndDropUploaderCtrl ],
		link : function(scope, http, elem, attrs, ctrl) {
			return true;
		},
		replace : false,
		templateUrl : 'templates/dragndrop.uploader.html'
	};

}


function CalendarCtrl($scope, $http, $rootScope) {

	$scope.dayNames = [ 'Lu', 'Ma', 'Me', 'Je', 'Ve', 'Sa', 'Di' ];
	$scope.monthLabels = [ 'Janvier', 'Février', 'Mars', 'Avril', 'Mai',
			'Juin', 'Juillet', 'Aout', 'Septembre', 'Octobre', 'Novembre',
			'Décembre' ];

	$scope.switchCalendarView = function() {
		$scope.calendarViewStyle = ($scope.calendarViewStyle == "calendarTable") ? "calendarList"
				: "calendarTable";
	}

	$scope.prevMonth = function() {
		if ($scope.month == 0) {
			$scope.month = 11;
			$scope.year--;
		} else {
			$scope.month--;
		}

		$scope.days = $scope.getDaysOfMonth($scope.month, $scope.year, true);
	}

	$scope.nextMonth = function() {
		if ($scope.month == 11) {
			$scope.month = 0;
			$scope.year++;
		} else {
			$scope.month++;
		}
		$scope.days = $scope.getDaysOfMonth($scope.month, $scope.year, true);
	}

	$scope.daysInMonth = function(pMonth, pYear) {
		return new Date(pYear, pMonth + 1, 0).getDate();
	}

	$scope.convertDay = function(pDay) {
		return (pDay + 6) % 7;
	}

	$scope.getDay = function(pDate, pInside) {
		var lDay = new Object();
		var lToday = new Date(new Date().getFullYear(), new Date().getMonth(),
				new Date().getDate());
		lDay.date = pDate;
		lDay.month = pDate.getMonth();
		lDay.dow = pDate.getDay();
		lDay.label = pDate.getDate() + " " + $scope.getMonthLabel(lDay.month);
		lDay.today = (pDate - lToday === 0) ? 0 : (pDate - lToday < 0) ? -1 : 1;

		lDay.inside = pInside;
		return lDay;
	}

	$scope.weeksInMonth = function(pMonth, pYear) {
		return Math
				.ceil(($scope.daysInMonth(pMonth, pYear) + convertDate(new Date(
						pYear, pMonth, 1).getDay())) / 7);
	}

	$scope.getDaysOfMonth = function(pMonth, pYear, pCallback) {
		var lDays = new Array();
		
		
		var lDaysCnt = $scope.daysInMonth(pMonth, pYear);
		
		
		var lFirstDay = $scope.convertDay(new Date(pYear, pMonth, 1).getDay());
		var lLastDay = $scope.convertDay(new Date(pYear, pMonth + 1, 0)
				.getDay());
		var lInside = false;

		for (i = -lFirstDay + 1; i < lDaysCnt + 7 - lLastDay; i++) {
			lInside = (i > 0 && i <= lDaysCnt) ? true : false;
			var lDay = $scope.getDay(new Date(pYear, pMonth, i), lInside);

			lDays.push(lDay);
		}
		
		if(pCallback && $scope.dataRetreiveMethod && $rootScope[$scope.dataRetreiveMethod])
			$rootScope[$scope.dataRetreiveMethod]($scope);
		
		return lDays;
	}

	$scope.getMonthLabel = function(pMonthId) {
		return $scope.monthLabels[pMonthId];
	}
	
	
	

	$scope.call = function(pFn, pDay) {
		if (pFn && $scope[pFn])
			return $scope[pFn](pDay);
		else if (pFn && $rootScope[pFn])
			return $rootScope[pFn](pDay);
		else
			return ""
	}

	$scope.refreshCalendarData = function (){
		$scope.days = $scope.getDaysOfMonth($scope.month, $scope.year, false);
	}
	
	$scope.days = new Array();
	$scope.month = new Date().getMonth();
	$scope.year = new Date().getFullYear();
	$scope.days = $scope.getDaysOfMonth($scope.month, $scope.year, true);
	$scope.calendarViewStyle = "calendarTable";
	
	this.getMonthLabel = $scope.getMonthLabel;
	

}
function calendarView() {

	return {
		restrict : 'A',
		templateUrl : 'templates/utils/calendarView.tpl.html',
		scope : {
			controlsList : '@',
			dataRefreshMethod : '@',
			dataRetreiveMethod : '@',
			dayContentMethod : '@'
		},
		controller : [ '$scope', '$http', '$rootScope', CalendarCtrl ],
		link : function(scope, elem, attrs) {
			scope.$on("workoutsLoaded", scope.refreshCalendarData);
			scope.controls = scope.$eval(scope.controlsList);
		}
	}
}