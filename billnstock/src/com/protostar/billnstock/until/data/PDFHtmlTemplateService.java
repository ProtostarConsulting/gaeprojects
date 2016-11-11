package com.protostar.billnstock.until.data;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.entities.PurchaseVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceiptVoucherEntity;
import com.protostar.billingnstock.account.entities.SalesVoucherEntity;
import com.protostar.billingnstock.account.entities.VoucherEntity;
import com.protostar.billingnstock.hr.entities.MonthlyPaymentDetailEntity;
import com.protostar.billingnstock.hr.entities.SalStruct;
import com.protostar.billingnstock.user.entities.UserEntity;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class PDFHtmlTemplateService {

	static Configuration cfg = null;

	public Configuration getConfiguration() {
		if (cfg != null) {
			return cfg;
		}

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);

		// cfg.setDirectoryForTemplateLoading(new
		// File("/WEB-INF/email_templates"));
		cfg.setClassForTemplateLoading(this.getClass(), "/");

		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		return cfg;

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

	private void generateSalesVoucherPDF(SalesVoucherEntity salesEntity,
			ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			root.put("DebitAccount", salesEntity.getAccountType1()
					.getAccountName().toString());
			root.put("CreditAccount", salesEntity.getAccountType2()
					.getAccountName().toString());
			root.put("Amount", salesEntity.getAmount().toString());
			root.put("Narration", salesEntity.getNarration().toString());
			// root.put("DebitAccount",
			// salesEntity.getAccountType1().toString());

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/sales_voucher_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateReceiptVoucherPDF(ReceiptVoucherEntity receiptEntity,
			ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			root.put("CreditAccount", receiptEntity.getAccountType1()
					.getAccountName().toString());
			root.put("DebitAccount", receiptEntity.getAccountType2()
					.getAccountName().toString());
			root.put("Amount", receiptEntity.getAmount().toString());
			root.put("Narration", receiptEntity.getNarration().toString());

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/invoice_voucher_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void generatePurchesVoucherPDF(PurchaseVoucherEntity purchesEntity,
			ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			root.put("CreditAccount", purchesEntity.getAccountType2()
					.getAccountName().toString());
			root.put("DebitAccount", purchesEntity.getAccountType1()
					.getAccountName().toString());
			root.put("Amount", purchesEntity.getAmount().toString());
			root.put("Items", purchesEntity.getItem().toString());
			root.put("Accdetail", purchesEntity.getAccdetail().toString());
			root.put("Narration", purchesEntity.getNarration().toString());

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/purches_voucher_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void generateAccountChartpdf(AccountGroupEntity accountGroupEntity,
			ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			root.put("groupName", accountGroupEntity.getPrimaryType());
			// root.put("accountName",accountGroupEntity.get;
			root.put("groupType", accountGroupEntity.getPrimaryType());
			// root.put("balance",purchesEntity.getItem().toString());

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/Download_Account_chart_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void generateHrPDF(
			MonthlyPaymentDetailEntity monthlyPaymentDetailEntity,
			ServletOutputStream outputStream) {
		if (monthlyPaymentDetailEntity instanceof MonthlyPaymentDetailEntity) {
			generateHrMonthlyPaymentDetailPDF(
					(MonthlyPaymentDetailEntity) monthlyPaymentDetailEntity,
					outputStream);
		} else {
			throw new RuntimeException(
					"Did not find this entity PDF handling methods: "
							+ monthlyPaymentDetailEntity.getClass());
		}

	}

	private void generateHrMonthlyPaymentDetailPDF(
			MonthlyPaymentDetailEntity mtlyPayObj,
			ServletOutputStream outputStream) {

		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();

			SalStruct salStruct = mtlyPayObj.getSalStruct();
			float basicAmt = salStruct.getBasic() / mtlyPayObj.getTotalDays()
					* mtlyPayObj.getPayableDays();
			float hraAmt = salStruct.getHramonthly()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float conAmt = salStruct.getConvence() / mtlyPayObj.getTotalDays()
					* mtlyPayObj.getPayableDays();
			float medAmt = salStruct.getMedical() / mtlyPayObj.getTotalDays()
					* mtlyPayObj.getPayableDays();
			float eduAmt = salStruct.getEducation() / mtlyPayObj.getTotalDays()
					* mtlyPayObj.getPayableDays();
			float adhAmt = salStruct.getAdhocAllow()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float splAmt = salStruct.getSpecialAllow()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();

			float totalEarnings = basicAmt + hraAmt + conAmt + medAmt + eduAmt
					+ adhAmt + splAmt + mtlyPayObj.getSpecialAllow();

			float totalDeductions = mtlyPayObj.getPfDeductionAmt()
					+ mtlyPayObj.getPtDeductionAmt()
					+ mtlyPayObj.getCanteenDeductionAmt()
					+ mtlyPayObj.getItDeductionAmt()
					+ mtlyPayObj.getOtherDeductionAmt();

			UserEntity user = mtlyPayObj.getleaveDetailEntity().getUser();
			root.put("Name", user.getFirstName() +" "+ user.getLastName());
					root.put("ManthlyGross", mtlyPayObj.getMonthlyGrossSalary());
			root.put("PayDays", mtlyPayObj.getPayableDays());
			root.put("MonthlySalary", mtlyPayObj.getMonthlyGrossSalary());
			root.put("PFDeductionAmt", mtlyPayObj.getPfDeductionAmt());
			root.put("PTDeductionAmt", mtlyPayObj.getPtDeductionAmt());
			root.put("Canteen", mtlyPayObj.getCanteenDeductionAmt());
			root.put("OtherDeduction", mtlyPayObj.getOtherDeductionAmt());
			root.put("ITDduction", mtlyPayObj.getItDeductionAmt());
			root.put("NetSalary", mtlyPayObj.getNetSalaryAmt());
			root.put("Basic", basicAmt);
			root.put("Month", mtlyPayObj.getCurrentMonth());
			root.put("HRA", hraAmt);
			root.put("Conveyance", conAmt);
			root.put("Medical", medAmt);
			root.put("Education", eduAmt);
			root.put("AdhocAllow", adhAmt);
			root.put("SpecialAllow", splAmt);
			root.put("SpecialAllow2", mtlyPayObj.getSpecialAllow());
			root.put("totalEarnings", totalEarnings);
			root.put("totalDeductions", totalDeductions);
			root.put("totalDays", mtlyPayObj.getTotalDays());


			Template temp = getConfiguration().getTemplate(
					"pdf_templates/HrMonthlyPaymentDetailPDF.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
