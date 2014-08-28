var lData;
function UploaderCtrl($scope, $http, $routeParams, $rootScope) {
	
	this.prototype = BackofficeCtrl($scope, $http, $routeParams, $rootScope);
	
	
	$scope.logout = function(){
		$rootScope.$broadcast('logout', 1);
	}
	
	$scope.getDocumentStatus = function(pStatus) {
		if (pStatus == 1)
			return $scope.messages.labels.fileApprouved;
		else if (pStatus == 0)
			return $scope.messages.labels.fileWaitingApprouval;
	}

	$scope.getMembersList = function() {
		$http.get('../server/service/getMembersList').success(function(data) {
			$scope.members = data;
			if ($scope.members.length == 1) {
				$scope.setActiveMember($scope.members[0]);
			}
		});
	}

	$scope.setActiveMember = function(member) {
		if (!$scope.activeMember || member.id != $scope.activeMember.id) {
			$scope.activeMember = member;
			$rootScope.activeMember = member;
			$rootScope.$broadcast('memberSelected', $scope.activeMember);
		}
	}

	$scope.getCssClass = function(member) {
		if ($scope.activeMember && $scope.activeMember.id == member.id) {
			return "selected";
		} else
			return "";
	}
	$scope.resetNotification = function(){

		$scope.fileUpdate.notification = "";
		$scope.fileUpdate.cssClass = "";

		$scope.fileUpload.notification = "";
		$scope.fileUpload.cssClass = "";
		
	}
	$scope.fileUploadNotification = function(event, data) {
		$rootScope.fileUploadOngoing = false;
		$scope.resetNotification();
		if (data.info) {
			$scope.fileUpload.notification = $scope.messages.labels.file + " "
					+ data.filename + " : " + $scope.messages.info[data.info];
			$scope.fileUpload.cssClass = "notification"
		} else if (data.error) {
			$scope.fileUpload.notification = $scope.messages.labels.file + " "
					+ data.filename + " : "
					+ $scope.messages.errors[data.error];
			$scope.fileUpload.cssClass = "error";
		}
	}

	$scope.fileUpdateNotification = function(event, data) {
		
		$scope.resetNotification();
		if (data.info) {
			$scope.fileUpdate.notification = $scope.messages.labels.file + " "
					+ data.filename + " : " + $scope.messages.info[data.info];
			$scope.fileUpdate.cssClass = "notification";
		} else if (data.error) {
			$scope.fileUpdate.notification = $scope.messages.errors[data.error];
			$scope.fileUpdate.cssClass = "error";
		}
	}

	$scope.startUploadNotification = function(event, data) {
		$scope.fileUpload.ongoing = true;
	}

	$scope.getDocumentsList = function(event, data) {

		$http(
				{
					method : 'GET',
					url : "../server/service/getDocumentsList?memberId="
							+ $scope.activeMember.id,

				}).success(function(data) {
			$scope.activeMember.documents = data;
		});
	}

	$scope.deleteDocument = function(pDocument) {

		if (confirm($scope.messages.info.confirmDocRemoval)) {
			$http(
					{
						method : 'GET',
						url : "../server/service/deleteDocument?documentId="
								+ pDocument.id,

					}).success(function(data) {

				$rootScope.$broadcast("fileRemoved", data);
			});
		}

	}

	$scope.$on("fileUploaded", $scope.fileUploadNotification);
	$scope.$on("fileUploaded", $scope.getDocumentsList);
	$scope.$on("fileRemoved", $scope.getDocumentsList);
	$scope.$on("fileRemoved", $scope.fileUpdateNotification);
	$scope.$on("memberSelected", $scope.getDocumentsList);
	$scope.fileUpload = new Object();

	$scope.fileUpdate = new Object();
}

function ClassicUploaderCtrl($scope, $http, $rootScope) {


	$scope.progress = 0;
	$scope.avatar = '';

	$scope.sendFile = function(el) {
		$rootScope.fileUploadOngoing = true;
		var lAction; 
		if ($rootScope && $rootScope.activeMember)
			lAction = $scope.action + "?memberId="
					+ $rootScope.activeMember.id;
		else
			lAction = $scope.action;
		var $form = $(el).parents('form');
		if ($(el).val() == '') {
			return false;
		}

		$form.attr('action', lAction);

		$scope.$apply(function() {
			$scope.progress = 0;
		});

		$form.ajaxSubmit({
			type : 'POST',
			uploadProgress : function(event, position, total, percentComplete) {

				$scope.$apply(function() {
					// upload the progress bar during the upload
					$scope.progress = percentComplete;
				});

			},
			error : function(event, statusText, responseText, form) {

				// remove the action attribute from the form
				$form.removeAttr('action');

				/*
				 * handle the error ...
				 */

			},
			success : function(data, statusText, xhr, form) {

				var ar = $(el).val().split('\\'), filename = ar[ar.length - 1];

				// remove the action attribute from the form
				$form.removeAttr('action');

				$scope.$apply(function() {
					$rootScope.$broadcast('fileUploaded', data);
				});

			},
		});

	}

}

function DragAndDropUploaderCtrl($scope, $http, $rootScope) {

	// Upload image files
	var place = "drop";
	var status = "status";
	var show = "list";
	var lMemberId = "";

	$scope.upload = function(file) {
		if ($rootScope && $rootScope.activeMember)
			lMemberId = $rootScope.activeMember.id;

		$rootScope.fileUploadOngoing = true;

		// Firefox 3.6, Chrome 6, WebKit
		if (window.FileReader) {

			// Once the process of reading file
			this.loadEnd = function(event, data) {
				bin = reader.result;
				xhr = new XMLHttpRequest();
				xhr.open('POST', $scope.action + '?up=true&memberId='
						+ lMemberId, true);
				var boundary = 'xxxxxxxxx';
				var body = '--' + boundary + "\r\n";
				body += "Content-Disposition: form-data; name='upload'; filename='"
						+ file.name + "'\r\n";
				body += "Content-Type: application/octet-stream\r\n\r\n";
				body += bin + "\r\n";
				body += '--' + boundary + '--';
				xhr.setRequestHeader('content-type',
						'multipart/form-data; boundary=' + boundary);
				// Firefox 3.6 provides a feature sendAsBinary ()
				if (xhr.sendAsBinary != null) {
					xhr.sendAsBinary(body);
					// Chrome 7 sends data but you must use the base64_decode on
					// the PHP side
				} else {
					xhr.open('POST', $scope.action
							+ '?up=true&base64=true&memberId=' + lMemberId,
							true);
					xhr.setRequestHeader('UP-FILENAME', file.name);
					xhr.setRequestHeader('UP-SIZE', file.size);
					xhr.setRequestHeader('UP-TYPE', file.type);
					xhr.send(window.btoa(bin));
				}
				/*
				 * if (show) { var newFile = document.createElement('div');
				 * newFile.innerHTML = 'Loaded : ' + file.name + ' size ' +
				 * file.size + ' B';
				 * document.getElementById(show).appendChild(newFile); } if
				 * (status) { document.getElementById(status).innerHTML =
				 * 'Loaded : 100%<br/>Next file ...'; }
				 */
				xhr.onreadystatechange = function() {
					if (xhr.readyState == 4) {
						data = JSON.parse(xhr.response);
						$rootScope.$broadcast('fileUploaded', data);

					}
				}

			}

			// Loading errors
			this.loadError = function(event) {
				switch (event.target.error.code) {
				case event.target.error.NOT_FOUND_ERR:
					document.getElementById(status).innerHTML = 'File not found!';
					break;
				case event.target.error.NOT_READABLE_ERR:
					document.getElementById(status).innerHTML = 'File not readable!';
					break;
				case event.target.error.ABORT_ERR:
					break;
				default:
					document.getElementById(status).innerHTML = 'Read error.';
				}
			}

			// Reading Progress
			this.loadProgress = function(event) {
				if (event.lengthComputable) {
					var percentage = Math.round((event.loaded * 100)
							/ event.total);
					/*
					 * document.getElementById(status).innerHTML = 'Loaded : ' +
					 * percentage + '%';
					 */
				}
			}

			// Preview images
			this.previewNow = function(event) {
				bin = preview.result;
				var img = document.createElement("img");
				img.className = 'addedIMG';
				img.file = file;
				img.src = bin;
				document.getElementById(show).appendChild(img);
			}

			reader = new FileReader();
			// Firefox 3.6, WebKit
			if (reader.addEventListener) {
				reader.addEventListener('loadend', this.loadEnd, false);
				if (status != null) {
					reader.addEventListener('error', this.loadError, false);
					reader.addEventListener('progress', this.loadProgress,
							false);
				}

				// Chrome 7
			} else {
				reader.onloadend = this.loadEnd;
				if (status != null) {
					reader.onerror = this.loadError;
					reader.onprogress = this.loadProgress;
				}
			}

			var preview = new FileReader();
			// Firefox 3.6, WebKit
			/*
			 * if (preview.addEventListener) {
			 * preview.addEventListener('loadend', this.previewNow, false); //
			 * Chrome 7 } else { preview.onloadend = this.previewNow; }
			 */
			// The function that starts reading the file as a binary string
			reader.readAsBinaryString(file);
			/*
			 * // Preview uploaded files if (show) {
			 * preview.readAsDataURL(file); }
			 */
			// Safari 5 does not support FileReader
		} else {
			xhr = new XMLHttpRequest();
			xhr.open('POST', $scope.action + '?up=true&memberId=' + lMemberId,
					true);
			xhr.setRequestHeader('UP-FILENAME', file.name);
			xhr.setRequestHeader('UP-SIZE', file.size);
			xhr.setRequestHeader('UP-TYPE', file.type);
			xhr.send(file);
			/*
			 * if (status) { document.getElementById(status).innerHTML = 'Loaded :
			 * 100%'; } if (show) { var newFile = document.createElement('div');
			 * newFile.innerHTML = 'Loaded : ' + file.name + ' size ' +
			 * file.size + ' B';
			 * document.getElementById(show).appendChild(newFile); }
			 */
			xhr.onreadystatechange = function() {
				if (xhr.readyState == 4) {
					data = JSON.parse(xhr.response);
					$rootScope.$broadcast('fileUploaded', data);
				}
			}

		}
	}

	// Function drop file
	this.drop = function(event) {
		event.preventDefault();
		var dt = event.dataTransfer;
		var files = dt.files;
		for ( var i = 0; i < files.length; i++) {
			var file = files[i];
			$scope.upload(file);
		}
		this.uploadPlace = document.getElementById(place);
		this.uploadPlace.setAttribute("style", "");
	}

	this.drag = function(event) {
		this.uploadPlace = document.getElementById(place);
		this.uploadPlace.setAttribute("style", "background:#CCCCCC");

		event.stopPropagation();
		event.preventDefault();

	}
	this.leave = function(event) {
		this.uploadPlace = document.getElementById(place);
		this.uploadPlace.setAttribute("style", "");
		event.stopPropagation();
		event.preventDefault();

	}

	// The inclusion of the event listeners (DragOver and drop)

	$scope.uploadPlace = document.getElementById(place);
	$scope.uploadPlace.addEventListener("dragover", this.drag, true);
	$scope.uploadPlace.addEventListener("drop", this.drop, false);
	$scope.uploadPlace.addEventListener("dragleave", this.leave, false);

}

angular.module('uploader', []).config(
		[ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/login', {
				templateUrl : 'templates/login.gui.html',
				controller : AuthenticationCtrl
			}).when('/upload', {
				templateUrl : 'templates/upload.gui.html',
				controller : UploaderCtrl
			}).otherwise({
				redirectTo : '/login'
			});
		} ]).run(function($rootScope, $location, $rootScope) {
	$rootScope.defaultPath = "upload";
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
}).directive('uploader', [ function() {

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

} ]).directive(
		'dnduploader',
		[ function() {

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

		} ]);
