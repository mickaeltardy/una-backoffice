package com.mtdev.una.controller.service;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mtdev.una.business.DataRenderer;
import com.mtdev.una.business.MailManager;
import com.mtdev.una.business.interfaces.ProfilesManager;
import com.mtdev.una.business.interfaces.UsersManager;
import com.mtdev.una.model.User;
import com.mtdev.una.tools.Toolbox;

@Controller
@RequestMapping("/registration")
public class RegistrationService {

	@Autowired
	// UserDao mUserDao;
	UsersManager mUsersManager;

	@Autowired
	ProfilesManager mProfilesManager;

	@Autowired
	protected DataRenderer mDataRenderer;

	@Autowired
	protected MailManager mMailManager;
	

	@RequestMapping(value = "/external", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("true")
	public @ResponseBody Object registerNewExternalAccount(
			@RequestParam("email") String pUsername,
			@RequestParam(required = false, value = "name") String pName,
			@RequestParam(required = false, value = "surname") String pSurname,
			@RequestParam("openid") String pOpenid,
			@RequestParam("origin") String pOrigin) {

		Map<String, String> lResponse = new HashMap<String, String>();

		if (!StringUtils.isEmpty(pUsername) && !StringUtils.isEmpty(pOpenid)
				&& !StringUtils.isEmpty(pOrigin)) {

			User lDbUser = mUsersManager.getUserByUsername(pUsername);

			if (lDbUser == null) {
				lDbUser = mUsersManager.createUser(pUsername, null, false);
				// TODO Refactoring
				mProfilesManager
						.createProfile(pUsername, pName, pSurname, true);

				lResponse.put("status", "created");
			}
			if (!this.doesOpenidExist(pOrigin, lDbUser)) {
				lDbUser.putOpenid(pOrigin, Toolbox.md5Spring(pOpenid));
				mUsersManager.saveUser(lDbUser);

				if (lResponse.containsKey("status"))
					lResponse.put("status", "completed");
			} else {
				lResponse.put("status", "untouched");
			}

			if (lDbUser != null) {
				lResponse.put("result", "success");
				lResponse.put("username", pUsername);
				lResponse.put("openid", pOpenid);
			}
		}

		return lResponse;

	}

	/**
	 * TODO to implement
	 * 
	 * @param pUsername
	 * @param pName
	 * @param pSurname
	 * @param pPassword
	 * @return
	 */
	@RequestMapping(value = "/internal", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("true")
	public @ResponseBody Object registerNewApplicationAccount(
			@RequestParam("email") String pUsername,
			@RequestParam(value = "name") String pName,
			@RequestParam(value = "surname") String pSurname,
			@RequestParam(value = "password") String pPassword) {

		if (!mUsersManager.doesUserExist(pUsername)) {
			User lUser = mUsersManager.createUser(pUsername, pPassword, false);
			// TODO Refactoring
			mProfilesManager.createProfile(pUsername, pName, pSurname, true);

			if (mUsersManager.saveUser(lUser))
				return Toolbox.generateResult("result", "success");

		}

		return Toolbox.generateResult("error", new Error(
				"Unable to create account"));

	}

	@RequestMapping(value = "/restorePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("true")
	public @ResponseBody Object restorePassword(
			@RequestParam("username") String pUsername) {
		User lDbUser = mUsersManager.getUserByUsername(pUsername);
		if (lDbUser != null) {

			String lNewPassword = Toolbox.generateRandomString(8);

			String lNewEncPassword = Toolbox.md5Spring(lNewPassword);

			lDbUser.setPassword(lNewEncPassword);



			if(mUsersManager.saveUser(lDbUser)){
				Map<Object, Object> lContext = new HashMap<Object, Object>();
				lContext.put("newPassword", lNewPassword);

				mDataRenderer
						.setResourceTemplate("/templates/mail/newPassMailNotification.html");
				Writer lOutput = mDataRenderer.renderData(lContext,
						"/templates/mail/newPassMailNotification.html");
				
				mMailManager.sendMail(lDbUser.getUsername(), "[UNA] Nouveau mot de passe", lOutput.toString());
			}

			return Toolbox.generateResult("status", "success");

		} else {
			return Toolbox.generateResult("error", new Error("No such user"));
		}

	}

	/**
	 * 
	 * TODO to implement
	 * 
	 * @param pEmail
	 * @return
	 */
	@RequestMapping("/checkEmailExistence")
	public @ResponseBody boolean checkEmailExistence(
			@RequestParam(value = "email", required = false) String pEmail,
			HttpServletRequest pRequest) {

		return (mUsersManager.doesUserExist(pEmail));
	}

	private boolean doesOpenidExist(String pOrigin, User pDbUser) {
		boolean lResult = false;
		if (pOrigin != null && pDbUser != null && pDbUser.getOpenids() != null) {
			pDbUser.getOpenids().containsKey(pOrigin);
		}

		return lResult;
	}
	

}
