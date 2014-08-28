// This is called with the results from from FB.getLoginStatus().
function statusChangeCallback(response) {
	console.log('statusChangeCallback');
	console.log(response);
	// The response object is returned with a status field that lets the
	// app know the current login status of the person.
	// Full docs on the response object can be found in the documentation
	// for FB.getLoginStatus().
	if (response.status === 'connected') {
		// Logged into your app and Facebook.
		handleFacebookResponse();
	} else if (response.status === 'not_authorized') {
		// The person is logged into Facebook, but not your app.

	} else {
		// The person is not logged into Facebook, so we're not sure if
		// they are logged into this app or not.

	}
}

// This function is called when someone finishes with the Login
// Button. See the onlogin handler attached to it in the sample
// code below.
function checkLoginState() {
	try {
		FB.getLoginStatus(function(response) {
			statusChangeCallback(response);
		});
	} catch (e) {
		console.log(e);
	}
}
function parseFacebook() {
	try {
		FB.XFBML.parse(document.getElementById('fbButton'));
	} catch (e) {
		console.log(e);
	}
}

/**
 * Handler for the signin callback triggered after the user selects an account.
 */
function onSignInCallback(resp) {
	gapi.client.load('plus', 'v1', apiClientLoaded);
}

/**
 * Sets up an API call after the Google API client loads.
 */
function apiClientLoaded() {
	gapi.client.plus.people.get({
		userId : 'me'
	}).execute(handleGoogleResponse);
}

/**
 * Response callback for when the API client receives a response.
 * 
 * @param resp
 *            The API response object with the user email and profile
 *            information.
 */
function handleGoogleResponse(resp) {
	var primaryEmail;

	if (resp && resp.emails) {

		for (var i = 0; i < resp.emails.length; i++) {
			if (resp.emails[i].type === 'account')
				primaryEmail = resp.emails[i].value;
		}
		var lResult = new Array();

		lResult['email'] = primaryEmail;
		lResult['openid'] = resp.id;
		lResult['name'] = resp.name.givenName;
		lResult['surname'] = resp.name.familyName;
		lResult['origin'] = "google";

		checkExternalAccount(lResult);

	}
}
function handleFacebookResponse() {
	console.log('Welcome!  Fetching your information.... ');
	FB.api('/me', function(response) {
		var lResult = new Array();
		lResult['email'] = response.email;
		lResult['openid'] = response.id;
		lResult['name'] = response.first_name;
		lResult['surname'] = response.last_name;
		lResult['origin'] = "facebook";

		checkExternalAccount(lResult);

		console.log('Successful login for: ' + response.name);

	});
}

function render() {
	gapi.signin
			.render(
					'customBtn',
					{
						'callback' : 'onSignInCallback',
						'clientid' : '130454341595-b6j29gukglcd2lpn8k5morb9n5nj5u1v.apps.googleusercontent.com',
						'cookiepolicy' : 'single_host_origin',
						'scope' : 'email'
					});

}

function checkExternalAccount(pContent) {
	var appElement = document.querySelector('[data-ng-app=unaBackOffice]');
	angular.element(appElement).scope().validateExternalAccount(pContent);
}