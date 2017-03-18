/*package com.protostar.billingnstock.account.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.protostar.billingnstock.account.entities.PurchaseVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceiptVoucherEntity;
import com.protostar.billingnstock.account.entities.SalesVoucherEntity;
import com.protostar.billingnstock.account.entities.VoucherEntity;
import com.protostar.billingnstock.hr.entities.MonthlyPaymentDetailEntity;
import com.protostar.billingnstock.hr.services.HrService;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;
public class PdfSales extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PdfSales() {
		super();
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Long id = Long.parseLong(request.getParameter("id"));
		Long bid = Long.parseLong(request.getParameter("bid"));
		String entity = String.valueOf(request.getParameter("entityname"));
		String entityId = String.valueOf(request.getParameter("entityId"));
		String month=String.valueOf(request.getParameter("month"));
		String str1 = String.valueOf(request.getParameter("str"));

		
		response.setContentType("application/PDF");
		ServletOutputStream outputStream = response.getOutputStream();
		Date today = new Date();
		String fileNameAppend = today.getDay() + "_" + today.getMonth() + "_"
				+ today.getYear();
		response.setHeader("Content-disposition",
				"inline; filename='Downloaded_" + fileNameAppend + ".pdf'");

		VoucherService voucherService = new VoucherService();
		VoucherEntity voucherEntity = null;
		
			this.generateSalesVoucherPDF(voucherEntity,
					outputStream);
		
		
	
}
	private void generateSalesVoucherPDF(SalesVoucherEntity salesEntity,
			ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			pdfHtmlTemplateService.addDocumentHeaderLogo(salesEntity, document, root);

			root.put("DebitAccount", salesEntity.getAccountType1()
					.getAccountName().toString());
			root.put("CreditAccount", salesEntity.getAccountType2()
					.getAccountName().toString());
			root.put("Amount", salesEntity.getAmount().toString());
			root.put("Narration", salesEntity.getNarration().toString());

			Template temp =pdfHtmlTemplateService. getConfiguration().getTemplate(
					"pdf_templates/sales_voucher_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			pdfHtmlTemplateService.addDocumentFooter(salesEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
*/