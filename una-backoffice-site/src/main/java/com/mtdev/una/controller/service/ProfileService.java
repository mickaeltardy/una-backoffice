package com.mtdev.una.controller.service;

import java.io.ByteArrayOutputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.mtdev.una.business.DataRenderer;
import com.mtdev.una.business.MailManager;
import com.mtdev.una.business.PdfGenerator;
import com.mtdev.una.business.interfaces.ProfilesManager;
import com.mtdev.una.business.interfaces.UsersManager;
import com.mtdev.una.data.view.ResponseView;
import com.mtdev.una.data.view.Views;
import com.mtdev.una.model.Profile;
import com.mtdev.una.security.SecurityToolbox;
import com.mtdev.una.tools.DataTools;
import com.mtdev.una.tools.FileTools;
import com.mtdev.una.tools.Toolbox;

@RequestMapping("profile")
@Controller
public class ProfileService {

	@Autowired
	protected ProfilesManager mProfilesManager;

	@Autowired
	protected UsersManager mUsersManager;

	@Autowired
	protected SecurityToolbox mSecurityToolbox;

	@Autowired
	protected PdfGenerator mPdfGenerator;

	@Autowired
	protected MailManager mMailManager;

	@Autowired
	protected DataRenderer mDataRenderer;

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("true")
	public @ResponseBody Object registerNewApplicationAccount(
			@RequestBody Map<Object, Object> pProfileInput) {

		String lProfileUsername = (pProfileInput.containsKey("username")) ? (String) pProfileInput
				.get("username") : null;
		String lProfilePassword = (pProfileInput.containsKey("password")) ? (String) pProfileInput
				.get("password") : null;
		String lAuthUsername = mSecurityToolbox.getUsername();
		if ((mSecurityToolbox.isUserRoleOnly() && (lAuthUsername != null
				&& lProfileUsername != null && (mSecurityToolbox.getUsername()
				.compareTo(lProfileUsername)) == 0))
				|| mSecurityToolbox.isAdminRole()) {

			if (mProfilesManager.saveProfile(pProfileInput)) {

				return Toolbox.generateResult("status", "success");

			}

		}
		if (lAuthUsername == null) {
			if (!mUsersManager.doesUserExist(lProfileUsername)) {

				if (mProfilesManager.saveProfileAndUser(pProfileInput)) {
					Map<Object, Object> lContext = new HashMap<Object, Object>();
					lContext.put("password", lProfilePassword);
					lContext.put("username", lProfileUsername);

					Writer lOutput = mDataRenderer.renderData(lContext,
							"/templates/mail/mailAccountCreation.html");

					mMailManager.sendMail(lProfileUsername,
							"[UNA] Votre compte de membre", lOutput.toString());

					return Toolbox.generateResult("status", "success",
							"username", lProfileUsername, "password",
							lProfilePassword);
				}
			}
		}

		return Toolbox.generateResult("error", "failed to save profile");
	}

	@RequestMapping(value = "/{username:.+}/full", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("@AccessTool.isAuthenticated()")
	public @ResponseBody Object getProfile(
			@PathVariable("username") String pUsername) {
		return getProfileData(pUsername);
	}

	@RequestMapping(value = "/{username:.+}/public", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("@AccessTool.isAuthenticated()")
	@ResponseView(Views.Public.class)
	public @ResponseBody Object getPublicProfile(
			@PathVariable("username") String pUsername) {

		return getProfileData(pUsername);
	}

	private Object getProfileData(String pUsername) {
		Profile lProfile = mProfilesManager.getProfileByUsername(pUsername);

		if (lProfile != null) {
			return lProfile;
		}

		return Toolbox.generateResult("error", new Error("No profile"));
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getEmptyPdf", method = RequestMethod.GET, produces = "application/pdf")
	public @ResponseBody ResponseEntity<byte[]> getEmptyPdf() {
		try {

			String lMessagesStr = FileTools
					.readFile("/datasource/messages.json");
			Gson lGson = new Gson();
			Map<Object, Object> lMessages = lGson.fromJson(lMessagesStr,
					new HashMap<Object, Object>().getClass());
			Map<Object, Object> lContext = new HashMap<Object, Object>();
			lContext.put("messages", lMessages);
			List<String> lDocsToProvide = new ArrayList<String>();
			Map<Object, Object> lDocs = (Map<Object, Object>) (lMessages
					.get("docs"));
			Map<Object, Object> lData = new HashMap<Object, Object>();

			lDocsToProvide.add((String) lDocs.get("signedForm"));
			lDocsToProvide.add((String) lDocs.get("certificate"));
			lDocsToProvide.add((String) lDocs.get("photo"));
			lDocsToProvide.add((String) lDocs.get("payment"));

			lDocsToProvide.add((String) lDocs.get("studentCard"));

			lDocsToProvide.add((String) lDocs.get("employeeCard"));

			lDocsToProvide.add((String) lDocs.get("swimmingCertificate"));

			lData.put("docsToProvide", lDocsToProvide);
			lContext.put("data", lData);
			lContext.put("DataTools", DataTools.class);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/pdf"));

			ByteArrayOutputStream os = mPdfGenerator.generatePdfStream(
					lContext, "/templates/pdf/registrationFormEmpty.html");

			String filename = "output.pdf";
			headers.setContentDispositionFormData(filename, filename);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
					((ByteArrayOutputStream) os).toByteArray(), headers,
					HttpStatus.OK);

			return response;
		} catch (Exception lE) {

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getpdf", method = RequestMethod.GET, produces = "application/pdf")
	public @ResponseBody ResponseEntity<byte[]> getPdf(
			@RequestParam("username") String pUsername) {
		try {

			Profile lProfile = mProfilesManager.getProfileByUsername(pUsername);

			Map<Object, Object> lData = new HashMap<Object, Object>();

			String lMessagesStr = FileTools
					.readFile("/datasource/messages.json");
			Gson lGson = new Gson();
			Map<Object, Object> lMessages = lGson.fromJson(lMessagesStr,
					new HashMap<Object, Object>().getClass());

			lData.putAll(getMapFromProfile(lProfile));

			List<String> lDocsToProvide = new ArrayList<String>();
			Map<Object, Object> lDocs = (Map<Object, Object>) (lMessages
					.get("docs"));
			lDocsToProvide.add((String) lDocs.get("signedForm"));
			lDocsToProvide.add((String) lDocs.get("certificate"));
			lDocsToProvide.add((String) lDocs.get("photo"));
			lDocsToProvide.add((String) lDocs.get("payment"));

			if (isStudent((String) lData.get("category")))
				lDocsToProvide.add((String) lDocs.get("studentCard"));

			if (isAdultUniv((String) lData.get("category")))
				lDocsToProvide.add((String) lDocs.get("employeeCard"));

			if (isMinor((Date) lData.get("birthdate")))
				lDocsToProvide.add((String) lDocs.get("swimmingCertificate"));

			lData.put("docsToProvide", lDocsToProvide);

			Map<Object, Object> lContext = new HashMap<Object, Object>();
			lContext.put("messages", lMessages);
			lContext.put("data", lData);
			lContext.put("DataTools", DataTools.class);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/pdf"));

			ByteArrayOutputStream os = mPdfGenerator.generatePdfStream(
					lContext, "/templates/pdf/registrationForm.html");
			SimpleDateFormat lSdf = new SimpleDateFormat("yyyyMMddHHmmss");

			String filename = "Registration_form_" + lSdf.format(new Date())
					+ ".pdf";
			headers.setContentDispositionFormData(filename, filename);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
					((ByteArrayOutputStream) os).toByteArray(), headers,
					HttpStatus.OK);

			return response;
		} catch (Exception lE) {

			return null;

		}
	}

	@SuppressWarnings("unchecked")
	private Map<Object, Object> getMapFromProfile(Profile pProfile) {
		Map<Object, Object> lOutput = new HashMap<Object, Object>();

		Field[] lFields = pProfile.getClass().getDeclaredFields();
		for (Field lField : lFields) {
			try {
				if (lField.getName().compareTo("data") != 0) {
					Field field = Profile.class.getDeclaredField(lField
							.getName());
					field.setAccessible(true);
					lOutput.put(field.getName(), field.get(pProfile));
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		lOutput.putAll((Map<Object, Object>) pProfile.getData());
		return lOutput;
	}

	public boolean isStudent(String pCategory) {
		return pCategory != null && pCategory.contains("student");
	}

	public boolean isStudentOthe(String pCategory) {
		return pCategory != null && pCategory.contains("studentOther");
	}

	public boolean isUniversity(String pCategory) {
		return pCategory != null && pCategory.contains("Univ");
	}

	public boolean isScolar(String pCategory) {
		return pCategory != null && pCategory.contains("school");
	}

	public boolean isAdult(String pCategory) {
		return pCategory != null && pCategory.contains("adult");
	}

	public boolean isAdultUniv(String pCategory) {
		return pCategory != null && pCategory.contains("adultUniv");
	}

	public boolean isMinor(Date pBirthdate) {
		if (pBirthdate != null) {

			Date lToday = new Date();

			DateTime lBirthdateDT = new DateTime(pBirthdate);
			DateTime lTodayDT = new DateTime(lToday);

			if (new DateTime(lBirthdateDT.getYear() + 18,
					lBirthdateDT.getMonthOfYear(),
					lBirthdateDT.getDayOfMonth(), 0, 0, 0).compareTo(lTodayDT) > 0) {
				return true;
			}
		}

		return false;
	}
}
