var lData;
function RegistrationCtrl($scope, $http) {

	var mInsurancePrice = 10.65;
	var mAdultDiscount = 20;
	var mCategoryDetails = [ 'ufr', 'establishment', 'school', 'class',
			'family' ];

	$http.get('../shared/data/data.json').success(function(data) {
		$scope.categories = data.categeries;
		$scope.data = data;
	});
	$http.get('../shared/data/messages.json').success(function(data) {
		$scope.messages = data;
	});
	$http.get('../shared/data/personalData.json').success(function(data) {
		$scope.personalData = data;
	});

	$scope.token = function(text) {
		return text;
	};

	$scope.reset = function() {
		$scope.member = new Object();
	}
	$scope.submit = function() {
		var lResult = true;
		var lMessage = ""
		var lErrors = new Array();
		if (!validate(lErrors)) {
			lResult = false;
			lMessage = $scope.messages.errors.invalidForm;
			$scope.errors = lErrors;
		} else {
			$scope.errors = new Array();
			$scope.validatingForm = true;

			$http({
				method : 'POST',
				url : "../server/service/saveForm",
				data : $scope.member
			}).success(function(data, status) {
				if (data == "OK") {
					$scope.completeForm = true;
					$scope.inProgress = false;
					$('html,body').scrollTop(0);
				} else
					alert($scope.messages.errors.serverError);
				$scope.validatingForm = false;
			});
		}
		if (lMessage)
			alert(lMessage);
		return lResult;
	}

	$scope.checkEmail = function() {
		var lResult = 0;
		var lData = new Object();

		lData.email = $scope.member.email;

		$http({
			method : 'POST',
			url : "../server/service/checkEmail",
			data : lData
		}).success(
				function(data, status) {
					if (data == "free") {
						$scope.emailExists = false;
						$scope.updateAlert("account", "validation", "")
					} else if (data == "taken") {
						$scope.emailExists = true;
						$scope.member.emailConfirmation = $scope.member.email;
						$scope.updateAlert("account", "validation",
								$scope.messages.info.existentEmail)
					}
				});
		return true;
	}

	$scope.checkPassword = function() {
		var lResult = 0;
		var lPassword = MD5($scope.member.password)
		var lData = new Object();

		lData.email = $scope.member.email;
		lData.password = lPassword;
		lData.encrypt = true;
		$http({
			method : 'POST',
			url : "../server/service/checkPassword",
			data : lData
		})
				.success(
						function(data, status) {
							if (data == "OK") {
								$scope.passwordValid = true;
								$scope.member.passwordConfirmation = $scope.member.password;
								$scope.updateAlert("account", "validation", "")
							} else if (data == "NOK") {
								$scope.passwordValid = false;
								$scope.updateAlert("account", "validation",
										$scope.messages.errors.invalidPassword)
							}
						});
		return true;
	}

	$scope.updateAlert = function(pGroup, pLabel, pMessage) {
		if (!$scope.alerts[pGroup])
			$scope.alerts[pGroup] = new Object();

		$scope.alerts[pGroup][pLabel] = pMessage;

		return true;
	}

	$scope.getMessage = function(text, group) {
		if ($scope.messages)
			return $scope.messages[group][text];
		else
			return "";
	};

	$scope.isCategory = function(pCategory) {
		return ($scope.member) && $scope.member.category
				&& ($scope.member.category.code.indexOf(pCategory) !== -1)
	};

	$scope.isStudent = function() {
		return $scope.isCategory("student");
	};
	$scope.isStudentOther = function() {
		return $scope.isCategory("studentOther");
	};
	$scope.isUniversity = function() {
		return $scope.isCategory("Univ");
	};

	$scope.isScolar = function() {
		return $scope.isCategory("school");
	};
	$scope.isAdult = function() {
		return $scope.isCategory("adult");
	};

	$scope.isAdultUniv = function() {
		return $scope.isCategory("adultUniv");
	};

	$scope.isScolar = function() {
		return $scope.isCategory("school");
	};
	
	$scope.isMinor = function(){
		if(!isFieldEmpty($scope.member.birthdate)){
			var lBirthdate = new Date($scope.formatDate($scope.member.birthdate));
			var lToday = new Date();
			if (lToday >= new Date(lBirthdate.getFullYear() + 18, lBirthdate.getMonth(), lBirthdate.getDate())) {
				return false;
			}
			return true;
		}
	}

	$scope.formatDate = function(pDate){
		var lDateRegexChrome = /^\d{4}-\d{2}-\d{2}$/;
		var lDateRegexFF = /^\d{2}\/\d{2}\/\d{4}$/;
		if(!isFieldEmpty(pDate)){
			if(lDateRegexFF.test(pDate)) {
				var lDateSplit = pDate.split("/");
				return lDateSplit[1]+"/"+lDateSplit[0]+"/"+lDateSplit[2]
			}else if(lDateRegexChrome.test(pDate)){
				return pDate;
			}
		}		
		


	}
	
	$scope.getDocsToProvide = function() {
		var lDocs = new Array();
		if ($scope.messages && $scope.messages.docs) {
			lDocs.push($scope.messages.docs.signedForm);
			lDocs.push($scope.messages.docs.certificate);
			lDocs.push($scope.messages.docs.photo);
			lDocs.push($scope.messages.docs.payment);
			if (isStudent())
				lDocs.push($scope.messages.docs.studentCard);
			if (isAdultUniv())
				lDocs.push($scope.messages.docs.employeeCard);
			if (isMinor())
				lDocs.push($scope.messages.docs.swimmingCertificate);

			$scope.member.docsToProvide = lDocs;
		}
		return lDocs;

	}

	$scope.getCorrectedCategoryPrice = function(pCategory, pCorrection) {
		var lResult = "";
		if (pCategory) {
			lResult = pCategory.price;

			if ($scope.member.entryType == 'old' && pCategory.code == "adult")
				lResult = lResult - mAdultDiscount;

			if (lResult > 0) {
				lResult = lResult - pCorrection;
				if ($scope.messages && $scope.messages.info)
					lResult = "(" + lResult + $scope.messages.info.priceSuffix
							+ ")"
			}
		}
		return lResult;
	}

	$scope.getRegistrationPrice = function() {
		var lResult = "";
		if ($scope.member && $scope.member.category) {
			lResult = $scope.member.category.price;
			if ($scope.member.entryType == 'old'
					&& $scope.member.category.code == "adult")
				lResult = lResult - mAdultDiscount;
			if ($scope.member.family) {
				lResult = lResult / 1 - $scope.member.family.price;
			}
			if (lResult > 0)
				$scope.member.entryPrice = lResult;

			if (!isFieldEmpty($scope.member.insurance) && $scope.member.insurance != "0") {
				$scope.member.insurancePrice = mInsurancePrice;
				lResult = lResult / 1 + mInsurancePrice;
			}

			if (lResult > 0) {
				$scope.member.totalPrice = lResult;
				lResult = $scope.messages.info.priceToPay + " " + lResult
						+ $scope.messages.info.priceSuffix;
			}
		}
		if (lResult == "" && $scope.messages && $scope.messages.info)
			lResult = $scope.messages.info.noPrice;

		return lResult;
	}

	$scope.setMemberCategory = function(pValue) {
		var lLength = mCategoryDetails.length;
		for ( var i = 0; i < lLength; i++) {
			$scope.member[mCategoryDetails[i]] = "";
		}

		$scope.member.category = pValue;
		if (isStudent()) {
			alert($scope.messages.info.localAddressAlert);
		}
	}
	$scope.setArrayValue = function(pValue, pGroup) {
		if (!$scope.member[pGroup])
			$scope.member[pGroup] = new Array();
		if ($scope.member && $scope.member[pGroup]
				&& $scope.member[pGroup].indexOf(pValue) == -1)
			$scope.member[pGroup].push(pValue);
		else if ($scope.member && $scope.member[pGroup]
				&& $scope.member[pGroup].indexOf(pValue) !== -1)
			$scope.member[pGroup].pop(pValue);
	};

	$scope.isFieldEmpty = function(pField) {
		return !pField || pField == ""
				|| (typeof pField === 'string' && pField.trim() == "");
	};

	$scope.validate = function(pErrors) {
		var lBool = true;

		if (pErrors == undefined || !pErrors)
			pErrors = new Array();

		if (!$scope.member)
			lBool = false;

		if (lBool) {
			if (isFieldEmpty($scope.member.entryType)) {
				lBool = false;
				pErrors.push("entryTypeEmpty");
			} else {
				/*
				 * if($scope.member.entryType == "old" &&
				 * (isFieldEmpty($scope.member.licenceNo))){ lBool = false;
				 * pErrors.push("licenceNoEmpty"); }
				 * 
				 * if($scope.member.entryType == "migration" &&
				 * (isFieldEmpty($scope.member.licenceNo))){ lBool = false;
				 * pErrors.push("licenceNoEmpty"); }
				 */
				if (($scope.member.entryType == "migration" || $scope.member.entryType == "old")
						&& (!isFieldEmpty($scope.member.licenceNo))) {
					var lLicenceRegex = /^[ADU]{0,1}\d{3,7}$/i;
					if (!lLicenceRegex.test($scope.member.licenceNo)) {
						lBool = false;
						pErrors.push("licenceNoInvalid");
					}
				}
				if ($scope.member.entryType == "migration"
						&& (isFieldEmpty($scope.member.previousClub))) {
					lBool = false;
					pErrors.push("previousClubEmpty");
				}
			}

			if (!$scope.member.category
					|| isFieldEmpty($scope.member.category.code)) {
				lBool = false;
				pErrors.push("categoryEmpty");
			} else {
				if ($scope.member.entryType == "studentUniv"
						&& (isFieldEmpty($scope.member.ufr))) {
					lBool = false;
					pErrors.push("ufrEmpty");
				}
				if ($scope.member.entryType == "studentOther"
						&& (isFieldEmpty($scope.member.school))) {
					lBool = false;
					pErrors.push("schoolEmpty");
				}
				if ($scope.member.entryType == "school"
						&& (isFieldEmpty($scope.member.school))) {
					lBool = false;
					pErrors.push("schoolEmpty");
				}
				/*
				 * if($scope.member.entryType == "school" &&
				 * (isFieldEmpty($scope.member.class))){ lBool = false;
				 * pErrors.push("classEmpty"); }
				 */

			}
			if (isFieldEmpty($scope.member.surname)) {
				lBool = false;
				pErrors.push("surnameEmpty");
			}
			if (isFieldEmpty($scope.member.name)) {
				lBool = false;
				pErrors.push("nameEmpty");
			}
			if (isFieldEmpty($scope.member.sex)) {
				lBool = false;
				pErrors.push("sexEmpty");
			}
			if (isFieldEmpty($scope.member.birthdate)) {
				lBool = false;
				pErrors.push("birthdateEmpty");
			} else {
				var lDateRegexChrome = /^\d{4}-\d{2}-\d{2}$/;
				var lDateRegexFF = /^\d{2}\/\d{2}\/\d{4}$/;
				if (!lDateRegexChrome.test($scope.member.birthdate)
						&& !lDateRegexFF.test($scope.member.birthdate)) {
					lBool = false;
					pErrors.push("birthdateInvalid");
				}
			}
			if (isFieldEmpty($scope.member.nationality)) {
				lBool = false;
				pErrors.push("nationalityEmpty");
			}
			if (isFieldEmpty($scope.member.address)) {
				lBool = false;
				pErrors.push("addressEmpty");
			}
			if (isFieldEmpty($scope.member.zipcode)) {
				lBool = false;
				pErrors.push("zipEmpty");
			} else {
				var lZipRegex = /^\d{5}$/;
				if (!lZipRegex.test($scope.member.zipcode)) {
					lBool = false;
					pErrors.push("zipInvalid");
				}
			}
			if (isFieldEmpty($scope.member.city)) {
				lBool = false;
				pErrors.push("cityEmpty");
			}
			if (isFieldEmpty($scope.member.telephone)) {
				lBool = false;
				pErrors.push("telephoneEmpty");
			} else {
				var lTelRegex = /^\+{0,1}\d{8,15}$/;
				if (!lTelRegex.test($scope.member.telephone)) {
					lBool = false;
					pErrors.push("telephoneInvalid");
				}
			}
			if (isFieldEmpty($scope.member.email)) {
				lBool = false;
				pErrors.push("emailEmpty");
			} else {
				var lMailRegex = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
				if (!lMailRegex.test($scope.member.email)) {
					lBool = false;
					pErrors.push("emailInvalid");
				}
			}
			if (!isFieldEmpty($scope.member.emailConfirmation)
					&& $scope.member.emailConfirmation != $scope.member.email) {
				lBool = false;
				pErrors.push("emailConfirmationIncorrect");
			}

			if (isFieldEmpty($scope.member.password)) {
				lBool = false;
				pErrors.push("passwordEmpty");
			}
			if ($scope.member.password != $scope.member.passwordConfirmation) {
				lBool = false;
				pErrors.push("passwordConfirmationIncorrect");
			}

			if (!isFieldEmpty($scope.member.email)) {
				if ($scope.emailExists && !$scope.passwordValid) {
					lBool = false;
					pErrors.push("invalidPassword");
				}

			}
			
			if (isFieldEmpty($scope.member.insurance)) {
				lBool = false;
				pErrors.push("confirmInsuranceEmpty");
			}
			
			if ($scope.member.donation && $scope.member.donation.fixedAmmount == "-1" && isFieldEmpty($scope.member.donation.ammount)) {
				lBool = false;
				pErrors.push("donationEmpty");
			}



			if (isFieldEmpty($scope.member.confirmSwim)) {
				lBool = false;
				pErrors.push("confirmSwimEmpty");
			}

			if (isFieldEmpty($scope.member.confirmConditions)) {
				lBool = false;
				pErrors.push("confirmConditionsEmpty");
			}

		}
		return lBool;
	};
	var validate = $scope.validate;
	var isFieldEmpty = $scope.isFieldEmpty;
	var isScolar = $scope.isScolar;
	var isStudent = $scope.isStudent;
	var isAdultUniv = $scope.isAdultUniv;
	var isMinor = $scope.isMinor;
	$scope.member = new Object();
	$scope.validatingForm = false;
	$scope.inProgress = true;
	$scope.completeForm = false;
	$scope.emailExists = false;
	$scope.alerts = new Object();

}

var myApp = angular.module('myApp', []);

myApp.directive('autoFillableField', function() {
	return {
		restrict : "A",
		require : "?ngModel",
		link : function(scope, element, attrs, ngModel) {
			setInterval(function() {
				if (!(element.val() == '' && ngModel.$pristine)) {
					scope.$apply(function() {
						ngModel.$setViewValue(element.val());
					});
				}
			}, 300);
		}
	};
});