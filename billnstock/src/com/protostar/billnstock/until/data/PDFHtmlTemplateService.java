package com.protostar.billnstock.until.data;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.entities.PurchaseVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceiptVoucherEntity;
import com.protostar.billingnstock.account.entities.SalesVoucherEntity;
import com.protostar.billingnstock.account.entities.VoucherEntity;
import com.protostar.billingnstock.account.services.AccountGroupService.TypeInfo;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.hr.entities.MonthlyPaymentDetailEntity;
import com.protostar.billingnstock.hr.entities.SalStruct;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.invoice.entities.StockLineItem;
import com.protostar.billingnstock.tax.entities.TaxEntity;
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
	
	
	
public void  generatePdfAccountChart(List<TypeInfo> accountChart,ServletOutputStream outputStream,Long bid){
		
		try {
			BusinessEntity businessEntity=new BusinessEntity();
			com.protostar.billingnstock.user.services.UserService user = new com.protostar.billingnstock.user.services.UserService();
			businessEntity=user.getBusinessById(bid);
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("accountChart", accountChart);
		root.put("buisinessName" ,""+ businessEntity.getBusinessName());
		StringBuffer addressBuf = new StringBuffer();
		Address address =  businessEntity.getAddress();
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
		Template temp = getConfiguration().getTemplate(
				"pdf_templates/accountChart_tmpl.ftlh");

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
				5000);
		Writer out = new PrintWriter(byteArrayOutputStream);
		temp.process(root, out);
	
		String pdfXMLContent = byteArrayOutputStream.toString();

		worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
		document.close();
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		

}
	
	
	
	
	public void  generatePdfBalanceSheet(List<TypeInfo> balanceSheetList,ServletOutputStream outputStream,Long bid){
		
		try {
			
			BusinessEntity businessEntity=new BusinessEntity();
			com.protostar.billingnstock.user.services.UserService user = new com.protostar.billingnstock.user.services.UserService();
			businessEntity=user.getBusinessById(bid);
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("balanceSheetList", balanceSheetList);
		root.put("buisinessName" ,""+ businessEntity.getBusinessName());
		StringBuffer addressBuf = new StringBuffer();
		Address address =  businessEntity.getAddress();
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
		Template temp = getConfiguration().getTemplate(
				"pdf_templates/balanceSheet_tmpl.ftlh");

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
				5000);
		Writer out = new PrintWriter(byteArrayOutputStream);
		temp.process(root, out);
	
		String pdfXMLContent = byteArrayOutputStream.toString();

		worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
		document.close();
		
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
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
			Image logoURL = Image.getInstance("img/images/protostar_logo_pix_313_132.jpg");
			   logoURL.setAbsolutePosition(50f,788f);
			   logoURL.scaleToFit(90f, 90f);
			   String logo = String.valueOf(document.add(logoURL));
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			root.put("logo", logo);
			root.put("DebitAccount", salesEntity.getAccountType1()
					.getAccountName().toString());
			root.put("CreditAccount", salesEntity.getAccountType2()
					.getAccountName().toString());
			root.put("Amount", salesEntity.getAmount().toString());
			root.put("Narration", salesEntity.getNarration().toString());
			root.put("buisinessName", ""
					+ salesEntity.getBusiness().getBusinessName());
			StringBuffer addressBuf = new StringBuffer();
			Address address = salesEntity.getBusiness().getAddress();
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
			Template temp = getConfiguration().getTemplate(
					"pdf_templates/sales_voucher_tmpl.ftlh");

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

	private void generateReceiptVoucherPDF(ReceiptVoucherEntity receiptEntity,
			ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();Image logoURL = Image.getInstance("img/images/protostar_logo_pix_313_132.jpg");
			   logoURL.setAbsolutePosition(50f, 788f);
			   logoURL.scaleToFit(90f, 90f);
			   String logo = String.valueOf(document.add(logoURL));

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();

			root.put("CreditAccount", receiptEntity.getAccountType1()
					.getAccountName().toString());
			root.put("DebitAccount", receiptEntity.getAccountType2()
					.getAccountName().toString());
			root.put("Amount", receiptEntity.getAmount().toString());
			root.put("Narration", receiptEntity.getNarration().toString());
			root.put("buisinessName", ""
					+ receiptEntity.getBusiness().getBusinessName());
			root.put("buisinessAddress", ""
					+ receiptEntity.getBusiness().getAddress().toString());

			// Top Header
			root.put("buisinessName", ""
					+ receiptEntity.getBusiness().getBusinessName());// //getbusiness.getBusinessName());
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
			// Top Header
			// root.put("buisinessName", "" + business.getBusinessName());
			root.put("buisinessAddress", "" + buisinessAddress);

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/invoice_voucher_tmpl.ftlh");

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

	private void generatePurchesVoucherPDF(PurchaseVoucherEntity purchesEntity,
			ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			Image logoURL = Image
					.getInstance("img/images/protostar_logo_pix_313_132.jpg");
			logoURL.setAbsolutePosition(50f, 800f);
			logoURL.scaleToFit(90f, 90f);
			String logo = String.valueOf(document.add(logoURL));

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
			root.put("buisinessName", ""
					+ purchesEntity.getBusiness().getBusinessName());

			root.put("BankAccountNo.", ""
					+ purchesEntity.getAccdetail().toString());

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
			// Top Header
			// root.put("buisinessName", "" + business.getBusinessName());
			root.put("buisinessAddress", "" + buisinessAddress);

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
			Image logoURL = Image
					.getInstance("img/images/protostar_logo_pix_313_132.jpg");
			logoURL.setAbsolutePosition(50f, 750f);
			logoURL.scaleToFit(90f, 90f);
			String logo = String.valueOf(document.add(logoURL));

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
			Image logoURL = Image
					.getInstance("img/images/protostar_logo_pix_313_132.jpg");
			logoURL.setAbsolutePosition(50f, 800f);
			logoURL.scaleToFit(90f, 90f);
			String logo = String.valueOf(document.add(logoURL));
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			Map<String, Object> root = new HashMap<String, Object>();
			DecimalFormat df = new DecimalFormat("#0.00");

			SalStruct salStruct = mtlyPayObj.getSalStruct();
			float basicAmt = salStruct.getMonthlyBasic()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float hraAmt = salStruct.getMonthlyHra()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float conAmt = salStruct.getMonthlyConvence()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float medAmt = salStruct.getMonthlyMedical()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float eduAmt = salStruct.getMonthlyEducation()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float adhAmt = salStruct.getMonthlyAdhocAllow()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float splAmt = salStruct.getMonthlySpecialAllow()
					/ mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();

			float specialAllow2 = mtlyPayObj.getSpecialAllow();
			float overtimeAmt = mtlyPayObj.getOvertimeAmt();
			float totalEarnings = basicAmt + hraAmt + conAmt + medAmt + eduAmt
					+ adhAmt + splAmt + specialAllow2;

			float pfDeductionAmt = mtlyPayObj.getPfDeductionAmt();
			float ptDeductionAmt = mtlyPayObj.getPtDeductionAmt();
			float canteenDeductionAmt = mtlyPayObj.getCanteenDeductionAmt();
			float itDeductionAmt = mtlyPayObj.getItDeductionAmt();
			float esiDeductionAmt = mtlyPayObj.getEsiDeductionAmt();
			float otherDeductionAmt = mtlyPayObj.getOtherDeductionAmt();
			float totalDeductions = pfDeductionAmt + ptDeductionAmt
					+ canteenDeductionAmt + itDeductionAmt + esiDeductionAmt
					+ otherDeductionAmt;

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

			EmployeeDetail employeeDetail = user.getEmployeeDetail();
			// Header Col1
			root.put("empNumber", "" + employeeDetail.getEmpId());
			String empName = user.getFirstName() + " " + user.getLastName();
			root.put("empName", empName);
			EmpDepartment department = employeeDetail.getDepartment();
			root.put("department",
					department == null ? "" : "" + department.getName());
			root.put("empDesignation", "" + employeeDetail.getDesignation());
			BankDetail bankDetail = employeeDetail.getBankDetail();
			if (bankDetail == null)
				bankDetail = new BankDetail();
			root.put("bankName", "" + bankDetail.getBankName());
			root.put("bankAccNumber", "" + bankDetail.getBankAccountNo());

			root.put("panNumber", "" + employeeDetail.getPanCardNumber());
			root.put("pfNumber", "" + employeeDetail.getEpfNumber());

			// Header Col2
			root.put("ManthlyGross",
					"Rs. " + df.format(mtlyPayObj.getMonthlyGrossSalary()));
			/*
			 * root.put("MonthlySalary",
			 * df.format(mtlyPayObj.getMonthlyGrossSalary()));
			 */
			root.put("totalDays", mtlyPayObj.getTotalDays());
			root.put("payableDays", mtlyPayObj.getPayableDays());
			root.put("overtimeAmt", overtimeAmt > 0 ? df.format(overtimeAmt)
					: "");
			root.put("overtimeNote", overtimeAmt > 0 ? "("
					+ mtlyPayObj.getleaveDetailEntity().getOvertimeDays()
					+ " days)" : "");
			root.put("leaveBalance", mtlyPayObj.getleaveDetailEntity()
					.getNextOpeningBalance());

			// Earnings Col
			root.put("Basic", df.format(basicAmt));
			root.put("Month", mtlyPayObj.getCurrentMonth());
			root.put("HRA", df.format(hraAmt));
			root.put("Conveyance", df.format(conAmt));
			root.put("Medical", df.format(medAmt));
			root.put("Education", df.format(eduAmt));
			root.put("AdhocAllow", adhAmt > 0 ? df.format(adhAmt) : "");
			root.put("SpecialAllow", splAmt > 0 ? df.format(splAmt) : "");
			root.put("SpecialAllow2",
					specialAllow2 > 0 ? df.format(specialAllow2) : "");
			root.put("specialAllow2Note",
					mtlyPayObj.getSpecialAllowNote() == null ? "" : "("
							+ mtlyPayObj.getSpecialAllowNote() + ")");
			root.put("totalEarnings", df.format(totalEarnings));

			// Deductions Col
			root.put("PFDeductionAmt", df.format(pfDeductionAmt));
			root.put("PTDeductionAmt", df.format(ptDeductionAmt));
			root.put("ITDduction", df.format(itDeductionAmt));
			root.put("Canteen", df.format(canteenDeductionAmt));
			root.put("esiDeductionAmt", df.format(esiDeductionAmt));
			root.put("OtherDeduction", df.format(otherDeductionAmt));
			root.put("otherDeductionNote",
					mtlyPayObj.getOtherDeductionAmtNote() == null ? "" : "("
							+ mtlyPayObj.getOtherDeductionAmtNote() + ")");
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

	public void generateInvoiceViewPDF(InvoiceEntity invoiceEntity,
			ServletOutputStream outputStream) {
		if (invoiceEntity instanceof InvoiceEntity) {
			generateInvoicePDF((InvoiceEntity) invoiceEntity, outputStream);
		} else {
			throw new RuntimeException(
					"Did not find this entity PDF handling methods: "
							+ invoiceEntity.getClass());
		}
	}

	private void generateInvoicePDF(InvoiceEntity invoiceEntity,
			ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();

			DecimalFormat df = new DecimalFormat("#0.00");

			double discountAmt = invoiceEntity.getDiscAmount();
			String purchaseOrderNo = invoiceEntity.getpOrder();
			double finalTotal = invoiceEntity.getFinalTotal();

			TaxEntity serviceTax = invoiceEntity.getSelectedServiceTax();
			TaxEntity productTax = invoiceEntity.getSelectedProductTax();

			// Imported Sales entity to get sales order number
			// SalesOrderEntity soe = invoiceEntity.getSalesOrderId();

			// Long salesOrderNo = soe.getId();

			List<StockLineItem> serviceLineItemList = invoiceEntity
					.getServiceLineItemList();
			List<StockLineItem> productLineItemList = invoiceEntity
					.getProductLineItemList();

			// Imported Customer entity to get name and address
			Customer cust1 = invoiceEntity.getCustomer();

			String custName = cust1.getCompanyName();

			StringBuffer custaddressBuf = new StringBuffer();
			Address customerAddress = cust1.getAddress();
			if (customerAddress != null) {
				if (customerAddress.getLine1() != null
						&& !customerAddress.getLine1().isEmpty())
					custaddressBuf.append(customerAddress.getLine1());
				if (customerAddress.getLine2() != null
						&& !customerAddress.getLine2().isEmpty())
					custaddressBuf.append(", <br></br>" + customerAddress.getLine2());
				if (customerAddress.getCity() != null
						&& !customerAddress.getCity().isEmpty())
					custaddressBuf.append(",<br></br>" + customerAddress.getCity());
				if (customerAddress.getState() != null
						&& !customerAddress.getState().isEmpty())
					custaddressBuf.append(", " + customerAddress.getState());
			}

			String custAddress = custaddressBuf.toString();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			Date now = new Date();
			String strDate = sdfDate.format(now);

			// Customer Details
			root.put("CustomerName", custName);
			root.put("CustomerAddress", custAddress);
			// Invoice Details
			root.put("InvoiceDate", invoiceEntity.getCreatedDate());
			root.put("invoiceNumber", invoiceEntity.getInvoiceNumber());
			root.put("Date", strDate);

			// root.put("SalesOrderNo", salesOrderNo);

			if (serviceLineItemList != null && serviceLineItemList.size() > 0) {
				root.put("serviceItemList", serviceLineItemList);
				root.put("serviceTax", serviceTax);
			}
			if (productLineItemList != null && productLineItemList.size() > 0) {
				root.put("productItemList", productLineItemList);
				root.put("productTax", productTax);
			}

			root.put("serviceTax", serviceTax);
			// root.put("TaxPercentage", df.format(taxPercentage));
			// root.put("ServiceTaxTotal", df.format(serviceTaxTotal));
			// root.put("ServiceTotal", df.format(serviceTotal));

			// Product Table
			// root.put("productTotal", df.format(productTotal));
			root.put("finalTotal", finalTotal);
			NumberToRupees numberToRupees = new NumberToRupees(
					Math.round(finalTotal));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWord", netInWords);
			// root.put("FinalInWords", invoiceEntity.getFinalTotal());
			root.put("PurchaseOrderNo", purchaseOrderNo);
			root.put("NoteToCustomer", "" + invoiceEntity.getNoteToCustomer());

			if (discountAmt > 0) {
				root.put("Discount", df.format(discountAmt));
			}

			BusinessEntity business = invoiceEntity.getBusiness();

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

			String businessAddress = addressBuf.toString();
			// Top Header
			root.put("businessName", "" + business.getBusinessName());
			root.put("businessAddress", "" + businessAddress);

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/invoicePDF_tmpl.ftlh");

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
	
	
	private void generatePdfBalanceSheet(ServletOutputStream outputStream){
		
		
		
		
		
		
		
	}
	
}
