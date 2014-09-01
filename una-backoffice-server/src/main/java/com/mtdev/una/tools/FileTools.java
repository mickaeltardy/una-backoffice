package com.mtdev.una.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileTools {

	@SuppressWarnings("resource")
	public static InputStream getInputStream(String pFilePath) {

		InputStream lInputStream = null;
		String lFile = pFilePath;
		try {
			if (lFile != null && !lFile.isEmpty()) {
				File lTemplateFile = new File(lFile);
				if (lTemplateFile.exists())
					lInputStream = new FileInputStream(lTemplateFile);
			}

		} catch (FileNotFoundException lE) {
		}
		if (lInputStream == null)
			lInputStream = FileTools.class.getResourceAsStream(pFilePath);
		return lInputStream;
	}

	public static String getStringFromStream(InputStream pStream) {
		try {
			InputStream lInputStream = pStream;
			InputStreamReader lInputStreamReader = new InputStreamReader(
					lInputStream);

			BufferedReader lBufferedReader = new BufferedReader(
					lInputStreamReader);

			StringBuilder sb = new StringBuilder();

			String line;
			while ((line = lBufferedReader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}

			lBufferedReader.close();
			lInputStreamReader.close();
			lInputStream.close();

			String lFileContent = sb.toString();

			return lFileContent;
		} catch (Exception lE) {
			return null;
		}

	}

	public static String readFile(String pFilePath) {
		InputStream lStream = FileTools.getInputStream(pFilePath);

		return FileTools.getStringFromStream(lStream);
	}

}
