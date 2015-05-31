package com.mtdev.una.business;

import java.io.ByteArrayOutputStream;
import java.io.Writer;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Component
public class PdfGenerator {

	@Autowired
	protected DataRenderer mDataRenderer;

	public ByteArrayOutputStream generatePdfStream(
			Map<Object, Object> pContext, String pTemplatePath) {

		try {
			Writer lOutput = mDataRenderer.renderData(pContext, pTemplatePath);

			ByteArrayOutputStream os;

			os = new ByteArrayOutputStream();

			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(lOutput.toString());

			renderer.layout();
			renderer.createPDF(os);

			os.close();
			return os;
		} catch (Exception lE) {
			lE.printStackTrace();
			return null;
		}
	}

}
