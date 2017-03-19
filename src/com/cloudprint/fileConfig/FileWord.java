package com.cloudprint.fileConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;

public class FileWord {
	public int DocxPage(String path) {
		/*
		 * 扩展名为docx时，获取页码
		 */
		File file = new File(path);
		int pages = 0;
		try {
			FileInputStream docx = new FileInputStream(file);
			XWPFDocument docxFile = new XWPFDocument(docx);
			pages = docxFile.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
			docx.close();
			docxFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pages;
	}

	public int DocPage(String path) {
		/*
		 * 对于doc文件,由于很多doc文件不在SummaryInformation部分存储页码信息,
		 * 导致用POI计算的总页码出问题，所以此处采用先转为pdf文件，然后计算总页码
		 */
		String pdfName = docToPdf(path);
		FilePDF filePDF = new FilePDF();
		int pages = 0;
		try {
			pages = filePDF.PDFExtension(pdfName);

		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = new File(pdfName);
		if (file.exists()) {
			file.delete();
		}
		return pages;
	}

	private String docToPdf(String filename) {
		int wdDoNotSaveChanges = 0;// 不保存待定的更改
		int wdFormatPDF = 17;// PDF 格式
		String toFilename = filename.substring(0, filename.lastIndexOf(".")) + ".pdf";
		ActiveXComponent app = null;
		try {
			app = new ActiveXComponent("Word.Application");
			app.setProperty("Visible", false);

			Dispatch docs = app.getProperty("Documents").toDispatch();
			Dispatch doc = Dispatch.call(docs, //
					"Open", //
					filename, // FileName
					false, // ConfirmConversions
					true // ReadOnly
			).toDispatch();

			File tofile = new File(toFilename);
			if (tofile.exists()) {
				tofile.delete();
			}
			Dispatch.call(doc, //
					"SaveAs", //
					toFilename, // FileName
					wdFormatPDF);

			Dispatch.call(doc, "Close", false);
			long end = System.currentTimeMillis();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (app != null)
				app.invoke("Quit", wdDoNotSaveChanges);
		}
		return toFilename;
	}
}
