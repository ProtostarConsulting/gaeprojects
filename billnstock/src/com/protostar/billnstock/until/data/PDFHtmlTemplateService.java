package com.protostar.billnstock.until.data;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.text.DecimalFormat;
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
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.EmpDepartment;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.Address;

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
			DecimalFormat df = new DecimalFormat("#.00");

			SalStruct salStruct = mtlyPayObj.getSalStruct();
			float basicAmt = salStruct.getMonthlyBasic() / mtlyPayObj.getTotalDays()
					* mtlyPayObj.getPayableDays();
			float hraAmt = salStruct.getMonthlyHra()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float conAmt = salStruct.getMonthlyConvence() / mtlyPayObj.getTotalDays()
					* mtlyPayObj.getPayableDays();
			float medAmt = salStruct.getMonthlyMedical() / mtlyPayObj.getTotalDays()
					* mtlyPayObj.getPayableDays();
			float eduAmt = salStruct.getMonthlyEducation() / mtlyPayObj.getTotalDays()
					* mtlyPayObj.getPayableDays();
			float adhAmt = salStruct.getMonthlyAdhocAllow()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float splAmt = salStruct.getMonthlySpecialAllow()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();

			float totalEarnings = basicAmt + hraAmt + conAmt + medAmt + eduAmt
					+ adhAmt + splAmt + mtlyPayObj.getSpecialAllow();

			float totalDeductions = mtlyPayObj.getPfDeductionAmt()
					+ mtlyPayObj.getPtDeductionAmt()
					+ mtlyPayObj.getCanteenDeductionAmt()
					+ mtlyPayObj.getItDeductionAmt()
					+ mtlyPayObj.getOtherDeductionAmt();

			UserEntity user = mtlyPayObj.getleaveDetailEntity().getUser();
			BusinessEntity business = user.getBusiness();

			StringBuffer addressBuf = new StringBuffer();
			Address address = business.getAddress();
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
			// Top Header
			root.put("buisinessName", "" + business.getBusinessName());
			root.put("buisinessAddress", "" + buisinessAddress);

			// Header Col1
			root.put("empNumber", "" + user.getEmpId());
			String empName = user.getFirstName() + " " + user.getLastName();
			root.put("empName", empName);
			EmpDepartment department = user.getDepartment();
			root.put("department",
					department == null ? "" : "" + department.getName());
			root.put("empDesignation", "" + user.getDesignationName());
			BankDetail bankDetail = user.getBankDetail();
			if (bankDetail == null)
				bankDetail = new BankDetail();
			root.put("bankName", "" + bankDetail.getBankName());
			root.put("bankAccNumber", "" + bankDetail.getBankAccountNo());
			
			root.put("panNumber", "" + "");
			root.put("pfNumber", "" + "");

			// Header Col2
			root.put("ManthlyGross",
					"Rs. " + df.format(mtlyPayObj.getMonthlyGrossSalary()));
			/*
			 * root.put("MonthlySalary",
			 * df.format(mtlyPayObj.getMonthlyGrossSalary()));
			 */
			root.put("totalDays", mtlyPayObj.getTotalDays());
			root.put("payableDays", mtlyPayObj.getPayableDays());
			root.put("leaveBalance", mtlyPayObj.getleaveDetailEntity()
					.getNextOpeningBalance());

			// Earnings Col
			root.put("Basic", df.format(basicAmt));
			root.put("Month", mtlyPayObj.getCurrentMonth());
			root.put("HRA", df.format(hraAmt));
			root.put("Conveyance", df.format(conAmt));
			root.put("Medical", df.format(medAmt));
			root.put("Education", df.format(eduAmt));
			root.put("AdhocAllow", df.format(adhAmt));
			root.put("SpecialAllow", df.format(splAmt));
			root.put("SpecialAllow2", df.format(mtlyPayObj.getSpecialAllow()));
			root.put(
					"specialAllow2Note",
					mtlyPayObj.getSpecialAllowNote() == null ? "" : mtlyPayObj
							.getSpecialAllowNote());
			root.put("totalEarnings", df.format(totalEarnings));

			// Deductions Col
			root.put("PFDeductionAmt",
					df.format(mtlyPayObj.getPfDeductionAmt()));
			root.put("PTDeductionAmt",
					df.format(mtlyPayObj.getPtDeductionAmt()));
			root.put("ITDduction", df.format(mtlyPayObj.getItDeductionAmt()));
			root.put("Canteen", df.format(mtlyPayObj.getCanteenDeductionAmt()));
			root.put("OtherDeduction",
					df.format(mtlyPayObj.getOtherDeductionAmt()));
			root.put("otherDeductionNote",
					mtlyPayObj.getOtherDeductionAmtNote() == null ? ""
							: mtlyPayObj.getOtherDeductionAmtNote());
			root.put("totalDeductions", df.format(totalDeductions));

			NumberToRupees numberToRupees = new NumberToRupees(
					Math.round(mtlyPayObj.getNetSalaryAmt()));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("NetSalary",
					df.format(Math.round(mtlyPayObj.getNetSalaryAmt())));
			root.put("NetSalaryInWords", netInWords);

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
