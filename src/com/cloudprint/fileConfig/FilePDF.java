package com.cloudprint.fileConfig;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;

public class FilePDF {
	public int PDFExtension(String path) throws InvocationTargetException {

		int pages = 0;

		path = path.replace("\\", "\\\\");
		PdfReader reader=null;
		try {
			reader = new PdfReader(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfDocument pdfDocument = new PdfDocument(reader);

		pages = pdfDocument.getNumberOfPages();

		try {
			reader.close();
			pdfDocument.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pages;

	}
}
