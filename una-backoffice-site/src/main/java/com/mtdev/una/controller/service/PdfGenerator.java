package com.mtdev.una.controller.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Controller
@RequestMapping("/pdf")
public class PdfGenerator {

	@RequestMapping(value = "/get", produces="application/pdf")
	public @ResponseBody ResponseEntity<byte[]> getPdf() {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/pdf"));

			OutputStream os;

			os = new ByteArrayOutputStream();

			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString("<html><body><h1>Header 1</h1><h2>Header 2</h2><p>Paragrpah</p></body></html>");

			renderer.layout();
			renderer.createPDF(os);

			os.close();

			String filename = "output.pdf";
			headers.setContentDispositionFormData(filename, filename);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
					((ByteArrayOutputStream) os).toByteArray(), headers, HttpStatus.OK);
			
			return response;
		} catch (Exception lE) {

		}
		return null;
	}

}
