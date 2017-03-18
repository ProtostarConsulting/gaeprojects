package com.protostar.billingnstock.account.services;

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

public class PdfVouchers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PdfVouchers() {
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
		
		if (entity.toString().equals("SalesVoucherEntity")) {
			voucherEntity = voucherService.getSalesListById(id, bid);
			this.generateVoucherPDF(voucherEntity,
					outputStream);
			
			
			
		}
		if (entity.toString().equals("ReceiptVoucherEntity"))

		{
			voucherEntity = voucherService.getRecieptListById(id, bid);
			this.generateVoucherPDF(voucherEntity,
					outputStream);
		}
		if (entity.toString().equals("PurchaseVoucherEntity"))

		{
			voucherEntity = voucherService.getPurchesListById(id, bid);
			this.generateVoucherPDF(voucherEntity,
					outputStream);
		}
		
		System.out.println("entity" + entity);
		
		
		
		
}	
	
	public void generateVoucherPDF(VoucherEntity voucherEntity,
			ServletOutputStream outputStream) {

		if (voucherEntity instanceof SalesVoucherEntity) {
			generateSalesVoucherPDF((SalesVoucherEntity) voucherEntity,
					outputStream);
		} else if (voucherEntity instanceof ReceiptVoucherEntity) {
			generateReceiptVoucherPDF((ReceiptVoucherEntity) voucherEntity,
					outputStream);
		}

		else if (voucherEntity instanceof PurchaseVoucherEntity) {
			generatePurchesVoucherPDF((PurchaseVoucherEntity) voucherEntity,
					outputStream);
		}

		else {
			throw new RuntimeException(
					"Did not find this entity PDF handling methods: "
							+ voucherEntity.getClass());
		}
	}
	PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
	private void generateSalesVoucherPDF(SalesVoucherEntity salesEntity,
			ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

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


	private void generateReceiptVoucherPDF(ReceiptVoucherEntity receiptEntity, ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			pdfHtmlTemplateService.addDocumentHeaderLogo(receiptEntity, document, root);
			root.put("CreditAccount", receiptEntity.getAccountType1().getAccountName().toString());
			root.put("DebitAccount", receiptEntity.getAccountType2().getAccountName().toString());
			root.put("Amount", receiptEntity.getAmount().toString());
			root.put("Narration", receiptEntity.getNarration().toString());
			root.put("buisinessName", "" + receiptEntity.getBusiness().getBusinessName());
			root.put("buisinessAddress", "" + receiptEntity.getBusiness().getAddress().toString());

			// Top Header
			root.put("buisinessName", "" + receiptEntity.getBusiness().getBusinessName());// //getbusiness.getBusinessName());
			StringBuffer addressBuf = new StringBuffer();
			Address address = receiptEntity.getBusiness().getAddress();
			if (address != null) {
				if (address.getLine1() != null && !address.getLine1().isEmpty())
					addressBuf.append(address.getLine1());
				if (address.getLine2() != null && !address.getLine2().isEmpty())
					addressBuf.append(", " + address.getLine2());
				if (address.getCity() != null && !address.getCity().isEmpty())
					addressBuf.append(", " + address.getCity());
				if (address.getState() != null && !address.getState().isEmpty())
					addressBuf.append(", " + address.getState());
			}

			String buisinessAddress = addressBuf.toString();

			root.put("buisinessAddress", "" + buisinessAddress);

			Template temp = pdfHtmlTemplateService.getConfiguration().getTemplate("pdf_templates/invoice_voucher_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			pdfHtmlTemplateService.addDocumentFooter(receiptEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void generatePurchesVoucherPDF(PurchaseVoucherEntity purchesEntity, ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			pdfHtmlTemplateService.addDocumentHeaderLogo(purchesEntity, document, root);
			root.put("CreditAccount", purchesEntity.getCreditAccount().getAccountName().toString());
			root.put("DebitAccount", purchesEntity.getPurchaseAccount().getAccountName().toString());
			root.put("Amount", purchesEntity.getAmount().toString());
			root.put("Items", purchesEntity.getItem().toString());
			root.put("Accdetail", purchesEntity.getAccdetail().toString());
			root.put("Narration", purchesEntity.getNarration().toString());
			root.put("buisinessName", "" + purchesEntity.getBusiness().getBusinessName());

			root.put("BankAccountNo.", "" + purchesEntity.getAccdetail().toString());

			StringBuffer addressBuf = new StringBuffer();
			Address address = purchesEntity.getBusiness().getAddress();
			if (address != null) {
				if (address.getLine1() != null && !address.getLine1().isEmpty())
					addressBuf.append(address.getLine1());
				if (address.getLine2() != null && !address.getLine2().isEmpty())
					addressBuf.append(", " + address.getLine2());
				if (address.getCity() != null && !address.getCity().isEmpty())
					addressBuf.append(", " + address.getCity());
				if (address.getState() != null && !address.getState().isEmpty())
					addressBuf.append(", " + address.getState());
			}

			String buisinessAddress = addressBuf.toString();

			root.put("buisinessAddress", "" + buisinessAddress);

			Template temp = pdfHtmlTemplateService.getConfiguration().getTemplate("pdf_templates/purches_voucher_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			pdfHtmlTemplateService.addDocumentFooter(purchesEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
}
