package com.mtdev.una.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.mtdev.una.tools.FileTools;

@Component("dataRenderer")
public class DataRenderer {

	protected String mResourceTemplate = "/default.tpl.html";

	@Autowired(required = false)
	@Qualifier("confRoot")
	protected String mConfRoot;

	@Autowired(required = false)
	public VelocityEngine mVelocityEngine;

	public Writer renderData(Map<Object, Object> pContext, String mTemplatePath) {

		pContext = this.improveContext(pContext);

		try {
			Properties lProperties = this.getVelocityProperties(pContext);
			if (mConfRoot != null)
				lProperties.setProperty("file.resource.loader.path", new File(
						mConfRoot).toString());

			Velocity.init(lProperties);

			VelocityContext lVeloContext = new VelocityContext(pContext);

			String template = FileTools.readFile(mTemplatePath);
			
			StringWriter lWriter = new StringWriter();
			Velocity.evaluate(lVeloContext, lWriter, "TemplateName", template);

			return lWriter;
		} catch (Exception lE) {

			return null;
		}

	}

	/**
	 * Add some useful stuff in the data context
	 * 
	 * @param pContext
	 * @return improved context
	 */
	protected Map<Object, Object> improveContext(Map<Object, Object> pContext) {
		/*
		 * Magic element allowing to do some custom useful things. Example below
		 * 
		 * #set($date =
		 * $object.getClass().forName("java.util.Date").newInstance())
		 * 
		 * ${date}
		 * 
		 * #set($math = $object.getClass().forName("java.lang.Math"))
		 * 
		 * $math.pow(2, 5)
		 */
		Object lObject = new Object();
		pContext.put("magic", lObject);

		return pContext;
	}

	public Properties getVelocityProperties(Map<Object, Object> pContext) {
		Properties lProperties = new Properties();
		try {
			lProperties.load(this.getClass().getResourceAsStream(
					"/velocity.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lProperties;
	}



	public String getResourceTemplate() {
		return mResourceTemplate;
	}

	public void setResourceTemplate(String pResourceTemplate) {
		mResourceTemplate = pResourceTemplate;
	}

}
