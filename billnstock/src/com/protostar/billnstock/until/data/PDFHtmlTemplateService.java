package com.protostar.billnstock.until.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.entities.PaymentVoucherEntity;
import com.protostar.billingnstock.account.entities.PurchaseVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceiptVoucherEntity;
import com.protostar.billingnstock.account.entities.SalesVoucherEntity;
import com.protostar.billingnstock.account.entities.VoucherEntity;
import com.protostar.billingnstock.account.services.AccountGroupService;
import com.protostar.billingnstock.account.services.AccountGroupService.GroupInfo;
import com.protostar.billingnstock.account.services.AccountGroupService.TypeInfo;
import com.protostar.billingnstock.account.services.VoucherService;
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.hr.entities.MonthlyPaymentDetailEntity;
import com.protostar.billingnstock.hr.entities.SalStruct;
import com.protostar.billingnstock.invoice.entities.InvoiceEntity;
import com.protostar.billingnstock.invoice.entities.QuotationEntity;
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.stock.entities.StockItemsReceiptEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity.ShipmentType;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billingnstock.user.entities.EmpDepartment;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.entity.BaseEntity;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class PDFHtmlTemplateService {

	private static final String FONT1 = null;
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

	public void generatePdfAccountChart(List<TypeInfo> accountChart,
			ServletOutputStream outputStream, Long bid) {

		try {
			BusinessEntity businessEntity = new BusinessEntity();
			com.protostar.billingnstock.user.services.UserService user = new com.protostar.billingnstock.user.services.UserService();
			businessEntity = user.getBusinessById(bid);
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("accountChart", accountChart);
			root.put("buisinessName", "" + businessEntity.getBusinessName());
			StringBuffer addressBuf = new StringBuffer();
			Address address = businessEntity.getAddress();

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

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// --------------------------------------------------//
	public void getProfitAndLossAcc(List<TypeInfo> list,
			ServletOutputStream outputStream, Long bid) {

		try {
			AccountGroupEntity accG = new AccountGroupEntity();
			com.protostar.billingnstock.user.services.UserService user = new com.protostar.billingnstock.user.services.UserService();
			BusinessEntity businessEntity = user.getBusinessById(bid);
			accG.setBusiness(businessEntity);
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			Date today = new Date();
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			Map<String, Object> root = new HashMap<String, Object>();
			addDocumentHeaderLogo(accG, document, root);
			root.put("ProfitAndLossAcList", list);
			root.put("date", "" + today);

			// double accountGroupTypeGroupList[];
			double operatingRevenue = 0;
			double grossProfit = 0;
			double otherExpense = 0;
			double operatingExpense = 0;
			double operatingIncome = 0;
			List<GroupInfo> totalSalesList = new ArrayList<GroupInfo>();
			List<GroupInfo> totalPurchaseList = new ArrayList<GroupInfo>();
			List<GroupInfo> totalPaymentList = new ArrayList<GroupInfo>();

			for (int count = 0; count < list.size(); count++) {
				String typeName = list.get(count).getTypeName();

				if ((typeName == "INCOME")
						&& (list.get(count).getGroupList() != null)) {
					totalSalesList = list.get(count).getGroupList();
					operatingRevenue = list.get(count).getTypeBalance();
				}

				if ((typeName == "OTHEREXPENCES")
						&& (list.get(count).getGroupList() != null)) {

					totalPaymentList = list.get(count).getGroupList();
					otherExpense = list.get(count).getTypeBalance();

				}

				if ((typeName == "EXPENSES")
						&& (list.get(count).getGroupList() != null)) {
					totalPurchaseList = list.get(count).getGroupList();
					operatingExpense = list.get(count).getTypeBalance();

				}

				grossProfit = operatingRevenue - operatingExpense;
				operatingIncome = grossProfit - otherExpense;
			}
			Template temp = getConfiguration().getTemplate(
					"pdf_templates/profitAndLossAcc_tmpl.ftlh");

			String date = "1-Apr-2016 to 15-Apr-2016";
			// nettProfit = totalGrossProfit - totalIndirectExpences;
			root.put("operatingExpense", operatingExpense);
			root.put("totalPurchase", operatingExpense);
			root.put("totalOverhead", otherExpense);
			root.put("totalOperatingRevenue", operatingRevenue);
			root.put("totalPurchaseList", totalPurchaseList);
			root.put("totalPaymentList", totalPaymentList);
			root.put("totalGrossProfit", grossProfit);
			root.put("totalSalesList", totalSalesList);
			root.put("operatingIncome", operatingIncome);
			root.put("date", date);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			addDocumentFooter(accG, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// --------------------------------------------------

	public void generatePdfBalanceSheet(List<TypeInfo> natureList,
			ServletOutputStream outputStream, Long bid) {

		try {
			AccountGroupEntity accG = new AccountGroupEntity();

			com.protostar.billingnstock.user.services.UserService user = new com.protostar.billingnstock.user.services.UserService();
			BusinessEntity businessEntity = user.getBusinessById(bid);
			accG.setBusiness(businessEntity);
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			String date = "1-Apr-2016 to 15-Apr-2016";
			Date today = new Date();
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			Map<String, Object> root = new HashMap<String, Object>();
			addDocumentHeaderLogo(accG, document, root);
			root.put("balanceSheetList", natureList);

			root.put("date", "" + today);

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/balanceSheet_tmpl.ftlh");

			double totalAsset = 0;
			double totalLiabilities2 = 0;
			double totalEQUITY = 0;
			double totalLiabilities = 0;
			AccountGroupService ag = new AccountGroupService();

			ServerMsg nettProffitOrLoss1 = ag.getProfitAndLossAccBalance(bid);
			double nettProffitOrLoss = nettProffitOrLoss1.getReturnBalance();

			for (int int2 = 0; int2 < natureList.size(); int2++) {
				String typeName = natureList.get(int2).getTypeName();
				if ((typeName == "ASSETS")
						&& (natureList.get(int2).getGroupList() != null)) {
					for (int i = 0; i < natureList.get(int2).getGroupList()
							.size(); i++) {
						System.out.println("get GRPLIST-----"
								+ natureList.get(int2).getGroupList().get(int2)
										.getGroupName());
						totalAsset = natureList.get(int2).getGroupList().get(i)
								.getGroupBalance()
								+ totalAsset;
					}
					if (totalAsset < 0) {
						totalAsset = totalAsset * (-1);
					}
				}

				if ((typeName == "LIABILITIES")
						&& (natureList.get(int2).getGroupList() != null)) {
					for (int i = 0; i < natureList.get(int2).getGroupList()
							.size(); i++) {
						totalLiabilities = natureList.get(int2).getGroupList()
								.get(i).getGroupBalance()
								+ totalLiabilities;
					}

					if (totalLiabilities < 0) {
						totalLiabilities = totalLiabilities * (-1);
					}

				}
				if ((typeName == "EQUITY")
						&& (natureList.get(int2).getGroupList() != null)) {
					for (int i = 0; i < natureList.get(int2).getGroupList()
							.size(); i++) {
						totalEQUITY = natureList.get(int2).getGroupList()
								.get(i).getGroupBalance()
								+ totalEQUITY;
					}

				}
			}

			totalLiabilities2 = totalLiabilities + totalEQUITY;
			if (nettProffitOrLoss < 0) {

				nettProffitOrLoss = nettProffitOrLoss * (-1);
				totalAsset = totalAsset + nettProffitOrLoss;

			} else {
				totalLiabilities2 = totalLiabilities2 + nettProffitOrLoss;

			}

			root.put("nettProffitOrLoss", nettProffitOrLoss);
			root.put("totalLiabilities2", totalLiabilities2);
			root.put("totalEQUITY", totalEQUITY);
			root.put("totalLiabilities", totalLiabilities);
			root.put("totalAsset", totalAsset);
			root.put("date", date);

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			addDocumentFooter(accG, writer);
			document.close();

		} catch (Exception e) {
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

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			addDocumentHeaderLogo(salesEntity, document, root);

			root.put("DebitAccount", salesEntity.getAccountType1()
					.getAccountName().toString());
			root.put("CreditAccount", salesEntity.getAccountType2()
					.getAccountName().toString());
			root.put("Amount", salesEntity.getAmount().toString());
			root.put("Narration", salesEntity.getNarration().toString());

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/sales_voucher_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			addDocumentFooter(salesEntity, writer);
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
			addDocumentHeaderLogo(receiptEntity, document, root);
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

			root.put("buisinessAddress", "" + buisinessAddress);

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/invoice_voucher_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			addDocumentFooter(receiptEntity, writer);
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
			addDocumentHeaderLogo(purchesEntity, document, root);
			root.put("CreditAccount", purchesEntity.getCreditAccount()
					.getAccountName().toString());
			root.put("DebitAccount", purchesEntity.getPurchaseAccount()
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

			root.put("buisinessAddress", "" + buisinessAddress);

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/purches_voucher_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			addDocumentFooter(purchesEntity, writer);
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
			addDocumentHeaderLogo(accountGroupEntity, document, root);
			root.put("groupName", accountGroupEntity.getGroupName());
			// root.put("accountName",accountGroupEntity.get;
			root.put("groupType", accountGroupEntity.getAccountGroupType()
					.toString());
			// root.put("balance",purchesEntity.getItem().toString());

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/Download_Account_chart_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			addDocumentFooter(accountGroupEntity, writer);
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
			/*
			 * Image logoURL = Image
			 * .getInstance("img/images/protostar_logo_pix_313_132.jpg");
			 */

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			Map<String, Object> root = new HashMap<String, Object>();
			addDocumentHeaderLogo(mtlyPayObj, document, root);

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
			addDocumentFooter(mtlyPayObj, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addDocumentHeaderLogo(BaseEntity enity, Document document,
			Map<String, Object> root) throws BadElementException,
			MalformedURLException, IOException, DocumentException {
		String bizLogoGCSURL = enity.getBusiness().getBizLogoGCSURL();
		if (bizLogoGCSURL != null && !bizLogoGCSURL.isEmpty()) {
			Image logoURL = Image.getInstance(bizLogoGCSURL);
			logoURL.setAbsolutePosition(50f, 750f);
			logoURL.scaleToFit(150f, 180f);
			document.add(logoURL);
		}

		BusinessEntity business = enity.getBusiness();

		StringBuffer addressBuf = new StringBuffer();
		Address address = business.getAddress();
		if (address != null) {
			if (address.getLine1() != null && !address.getLine1().isEmpty())
				addressBuf.append(address.getLine1());
			if (address.getLine2() != null && !address.getLine2().isEmpty())
				addressBuf.append(", <br></br>" + address.getLine2());
			if (address.getCity() != null && !address.getCity().isEmpty())
				addressBuf.append(", <br></br>" + address.getCity());
			if (address.getPin() != null && !address.getPin().isEmpty())
				addressBuf.append(", " + address.getPin());
			if (address.getState() != null && !address.getState().isEmpty())
				addressBuf.append(", " + address.getState());
			if (address.getCountry() != null && !address.getCountry().isEmpty())
				addressBuf.append(", " + address.getCountry());
		}

		String businessAddress = addressBuf.toString();
		// Top Header
		root.put("businessName", "" + business.getBusinessName());
		root.put("businessAddress", "" + businessAddress);
	}

	private void addDocumentFooter(BaseEntity enity, PdfWriter writer)
			throws BadElementException, MalformedURLException, IOException,
			DocumentException {
		PdfContentByte cb = writer.getDirectContent();

		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD,
				BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

		cb.saveState();
		cb.beginText();
		cb.moveText(20f, 20f);
		cb.setFontAndSize(bf, 10);
		cb.showText("This is electronically generated document. Needs no stamp or signature.");
		cb.endText();
		cb.restoreState();

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
			addDocumentHeaderLogo(invoiceEntity, document, root);

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

			root.put("docuStatus", invoiceEntity.getStatus());
			root.put("createdBy", invoiceEntity.getCreatedBy().getFirstName()
					+ " " + invoiceEntity.getCreatedBy().getLastName());
			UserEntity approvedBy = invoiceEntity.getApprovedBy();
			root.put("approvedBy",
					approvedBy == null ? "" : approvedBy.getFirstName() + " "
							+ approvedBy.getLastName());

			// Imported Customer entity to get name and address
			Customer cust1 = invoiceEntity.getCustomer();

			String custName = "";

			if (cust1.getIsCompany()) {
				custName = cust1.getCompanyName();
			} else if (!cust1.getIsCompany()) {
				custName = cust1.getFirstName() + " " + cust1.getLastName();
			}
			root.put("CustomerName", custName);

			StringBuffer custaddressBuf = new StringBuffer();
			Address customerAddress = cust1.getAddress();
			if (customerAddress != null) {
				if (customerAddress.getLine1() != null
						&& !customerAddress.getLine1().isEmpty())
					custaddressBuf.append(customerAddress.getLine1());
				if (customerAddress.getLine2() != null
						&& !customerAddress.getLine2().isEmpty())
					custaddressBuf.append(", <br></br>"
							+ customerAddress.getLine2());
				if (customerAddress.getCity() != null
						&& !customerAddress.getCity().isEmpty())
					custaddressBuf.append(",<br></br>"
							+ customerAddress.getCity());
				if (customerAddress.getState() != null
						&& !customerAddress.getState().isEmpty())
					custaddressBuf.append(", " + customerAddress.getState());
			}

			String custAddress = custaddressBuf.toString();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			Date now = new Date();
			String strDate = sdfDate.format(now);

			// Customer Details

			root.put("CustomerAddress", custAddress);
			// Invoice Details
			root.put("InvoiceDate", invoiceEntity.getCreatedDate());
			root.put("invoiceNumber", invoiceEntity.getItemNumber());
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

			if (invoiceEntity.getNoteToCustomer() != null
					&& !invoiceEntity.getNoteToCustomer().trim().isEmpty())
				root.put("noteToCustomer",
						"" + invoiceEntity.getNoteToCustomer());
			if (invoiceEntity.getPaymentNotes() != null
					&& !invoiceEntity.getPaymentNotes().trim().isEmpty())
				root.put("paymentNotes", "" + invoiceEntity.getPaymentNotes());
			if (invoiceEntity.getTermsAndConditions() != null
					&& !invoiceEntity.getTermsAndConditions().trim().isEmpty())
				root.put("termsAndConditions",
						"" + invoiceEntity.getTermsAndConditions());

			if (discountAmt > 0) {
				root.put("Discount", df.format(discountAmt));
			}

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/invoice_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			addDocumentFooter(invoiceEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateQuotationViewPDF(QuotationEntity quotationEntity,
			ServletOutputStream outputStream) {
		if (quotationEntity instanceof QuotationEntity) {
			generateQuotationPDF((QuotationEntity) quotationEntity,
					outputStream);
		} else {
			throw new RuntimeException(
					"Did not find this entity PDF handling method"
							+ quotationEntity.getClass());
		}
	}

	private void generateQuotationPDF(QuotationEntity quotationEntity,
			ServletOutputStream outputStream) {

		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			addDocumentHeaderLogo(quotationEntity, document, root);

			DecimalFormat df = new DecimalFormat("#0.00");

			double discAmt = quotationEntity.getInvoiceObj().getDiscAmount();
			if (discAmt > 0) {
				root.put("Discount", df.format(discAmt));
			}

			TaxEntity servTax = quotationEntity.getInvoiceObj()
					.getSelectedServiceTax();
			TaxEntity prodTax = quotationEntity.getInvoiceObj()
					.getSelectedProductTax();

			List<StockLineItem> serviceLineItemListForQuot = quotationEntity
					.getInvoiceObj().getServiceLineItemList();
			List<StockLineItem> productLineItemListForQuot = quotationEntity
					.getInvoiceObj().getProductLineItemList();

			if (serviceLineItemListForQuot != null
					&& serviceLineItemListForQuot.size() > 0) {
				root.put("serviceItemList", serviceLineItemListForQuot);
				root.put("serviceTax", servTax);
			}
			if (productLineItemListForQuot != null
					&& productLineItemListForQuot.size() > 0) {
				root.put("productItemList", productLineItemListForQuot);
				root.put("productTax", prodTax);
			}

			root.put("docuStatus", quotationEntity.getInvoiceObj().getStatus());
			root.put("createdBy", quotationEntity.getInvoiceObj()
					.getCreatedBy().getFirstName()
					+ " "
					+ quotationEntity.getInvoiceObj().getCreatedBy()
							.getLastName());
			UserEntity approvedBy = quotationEntity.getInvoiceObj()
					.getApprovedBy();
			root.put("approvedBy",
					approvedBy == null ? "" : approvedBy.getFirstName() + " "
							+ approvedBy.getLastName());

			Customer customer = quotationEntity.getInvoiceObj().getCustomer();
			String custName = "";

			if (customer.getIsCompany()) {
				custName = customer.getCompanyName();
			} else {
				custName = customer.getFirstName() + " "
						+ customer.getLastName();
			}

			root.put("CustomerName", custName);

			StringBuffer custaddressBuffer = new StringBuffer();
			Address customerAddress = customer.getAddress();
			if (customerAddress != null) {
				if (customerAddress.getLine1() != null
						&& !customerAddress.getLine1().isEmpty())
					custaddressBuffer.append(customerAddress.getLine1());
				if (customerAddress.getLine2() != null
						&& !customerAddress.getLine2().isEmpty())
					custaddressBuffer.append(", <br></br>"
							+ customerAddress.getLine2());
				if (customerAddress.getCity() != null
						&& !customerAddress.getCity().isEmpty())
					custaddressBuffer.append(",<br></br>"
							+ customerAddress.getCity());
				if (customerAddress.getState() != null
						&& !customerAddress.getState().isEmpty())
					custaddressBuffer.append(", " + customerAddress.getState());
			}

			String custAddressForQuot = custaddressBuffer.toString();

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			Date today = quotationEntity.getCreatedDate();
			String quotationDate = sdfDate.format(today);

			String noteToCust = quotationEntity.getInvoiceObj()
					.getNoteToCustomer();
			double finalTotal = quotationEntity.getInvoiceObj().getFinalTotal();

			// Customer Details

			root.put("CustomerAddress", custAddressForQuot);

			root.put("noteToCustomer", "" + noteToCust);

			// Quotation Date and No
			root.put("Date", quotationDate);
			root.put("quotationNumber", quotationEntity.getItemNumber());

			root.put("finalTotal", finalTotal);
			NumberToRupees numberToRupees = new NumberToRupees(
					Math.round(finalTotal));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWords", netInWords);

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/quotation_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			addDocumentFooter(quotationEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generatePurchaseOrderViewPdf(
			PurchaseOrderEntity purchaseOrderEntity,
			ServletOutputStream outputStream) {
		if (purchaseOrderEntity instanceof PurchaseOrderEntity) {
			generatePurchaseOrderPDF((PurchaseOrderEntity) purchaseOrderEntity,
					outputStream);
		} else {
			throw new RuntimeException(
					"Did not find this entity handling method"
							+ purchaseOrderEntity.getClass());
		}
	}

	private void generatePurchaseOrderPDF(
			PurchaseOrderEntity purchaseOrderEntity,
			ServletOutputStream outputStream) {

		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			addDocumentHeaderLogo(purchaseOrderEntity, document, root);

			DecimalFormat df = new DecimalFormat("#0.00");

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			Date poDate = purchaseOrderEntity.getPoDate();
			Date poDueDate = purchaseOrderEntity.getPoDueDate();
			String purchaseOrderDate = sdfDate.format(poDate);
			String purchaseOrderDueDate = sdfDate.format(poDueDate);

			String noteToCustomer = purchaseOrderEntity.getNoteToCustomer();

			double discAmt = purchaseOrderEntity.getDiscAmount();
			if (discAmt > 0) {
				root.put("Discount", df.format(discAmt));
			}
			root.put("billTo", purchaseOrderEntity.getTo());
			root.put("shipTo", purchaseOrderEntity.getShipTo());
			root.put("pONum", purchaseOrderEntity.getItemNumber());
			root.put("pODate", purchaseOrderDate);
			root.put("docuStatus", purchaseOrderEntity.getStatus());
			root.put("PODueDate", purchaseOrderDueDate);
			root.put("shippedVia", "" + purchaseOrderEntity.getShippedVia());
			root.put("requisitioner",
					"" + purchaseOrderEntity.getRequisitioner());
			root.put("supplierName", ""
					+ purchaseOrderEntity.getSupplier().getSupplierName());
			root.put("FOBPoint", "" + purchaseOrderEntity.getfOBPoint());
			root.put("Terms", "" + purchaseOrderEntity.getTerms());
			root.put("noteToCustomer", noteToCustomer == null ? ""
					: noteToCustomer);
			UserEntity createdBy = purchaseOrderEntity.getCreatedBy();
			root.put("createdBy",
					createdBy == null ? "" : createdBy.getFirstName() + " "
							+ createdBy.getLastName());
			UserEntity approvedBy = purchaseOrderEntity.getApprovedBy();
			System.out.println("approvedBy: " + approvedBy);
			root.put("approvedBy",
					approvedBy == null ? "" : approvedBy.getFirstName() + " "
							+ approvedBy.getLastName());

			StringBuffer buffer = new StringBuffer();

			Address suplAdress = purchaseOrderEntity.getSupplier().getAddress();

			if (suplAdress != null) {
				if (suplAdress.getLine1() != null
						&& !suplAdress.getLine1().isEmpty())
					buffer.append(suplAdress.getLine1());
				if (suplAdress.getLine2() != null
						&& !suplAdress.getLine2().isEmpty())
					buffer.append(", <br></br>" + suplAdress.getLine2());
				if (suplAdress.getCity() != null
						&& !suplAdress.getCity().isEmpty())
					buffer.append(",<br></br>" + suplAdress.getCity());
				if (suplAdress.getState() != null
						&& !suplAdress.getState().isEmpty())
					buffer.append(", " + suplAdress.getState());
			}

			String supplierAddress = buffer.toString();
			root.put("supplierAddress", supplierAddress);

			TaxEntity servTax = purchaseOrderEntity.getSelectedServiceTax();
			TaxEntity prodTax = purchaseOrderEntity.getSelectedProductTax();

			List<StockLineItem> serviceLineItemListForPO = purchaseOrderEntity
					.getServiceLineItemList();
			List<StockLineItem> productLineItemListForPO = purchaseOrderEntity
					.getProductLineItemList();

			if (productLineItemListForPO != null
					&& productLineItemListForPO.size() > 0) {
				root.put("productItemList", productLineItemListForPO);
				root.put("productTax", prodTax);

			}

			if (serviceLineItemListForPO != null
					&& serviceLineItemListForPO.size() > 0) {
				root.put("serviceItemList", serviceLineItemListForPO);
				root.put("serviceTax", prodTax);
			}

			double finalTotal = purchaseOrderEntity.getFinalTotal();
			root.put("finalTotal", finalTotal);

			NumberToRupees numberToRupees = new NumberToRupees(
					Math.round(finalTotal));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWords", netInWords);

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/purchase_order_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			addDocumentFooter(purchaseOrderEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void generateStockReceiptPdf(
			StockItemsReceiptEntity stockReceiptEntity,
			ServletOutputStream outputStream) {
		if (stockReceiptEntity instanceof StockItemsReceiptEntity) {
			generateReceiptById((StockItemsReceiptEntity) stockReceiptEntity,
					outputStream);
		} else {
			throw new RuntimeException(
					"Did not find this entity handling method"
							+ stockReceiptEntity.getClass());

		}
	}

	private void generateReceiptById(
			StockItemsReceiptEntity stockReceiptEntity,
			ServletOutputStream outputStream) {
		try {

			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			addDocumentHeaderLogo(stockReceiptEntity, document, root);

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			String receiptDate = sdfDate.format(stockReceiptEntity
					.getReceiptDate());
			root.put("poNum", stockReceiptEntity.getPoNumber());
			root.put("receiptDate", receiptDate);
			root.put("receiptNo", stockReceiptEntity.getItemNumber());
			root.put("docuStatus", stockReceiptEntity.getStatus());
			root.put("createdBy", stockReceiptEntity.getCreatedBy()
					.getFirstName()
					+ " "
					+ stockReceiptEntity.getCreatedBy().getLastName());
			UserEntity approvedBy = stockReceiptEntity.getApprovedBy();
			root.put("approvedBy",
					approvedBy == null ? "" : approvedBy.getFirstName() + " "
							+ approvedBy.getLastName());
			root.put("supplier", stockReceiptEntity.getSupplier()
					.getSupplierName());
			root.put("warehouse", stockReceiptEntity.getWarehouse()
					.getWarehouseName());

			StringBuffer buffer = new StringBuffer();
			Address warehouseAdd = stockReceiptEntity.getWarehouse()
					.getAddress();
			if (warehouseAdd != null) {
				if (warehouseAdd.getLine1() != null
						&& !warehouseAdd.getLine1().isEmpty())
					buffer.append(warehouseAdd.getLine1());
				if (warehouseAdd.getLine2() != null
						&& !warehouseAdd.getLine2().isEmpty())
					buffer.append(", " + warehouseAdd.getLine2());
				if (warehouseAdd.getCity() != null
						&& !warehouseAdd.getCity().isEmpty())
					buffer.append(", " + warehouseAdd.getCity());
				if (warehouseAdd.getState() != null
						&& !warehouseAdd.getState().isEmpty())
					buffer.append(", " + warehouseAdd.getState());
			}

			String warehouseAddress = buffer.toString();
			root.put("warehouseAddress", warehouseAddress);

			StringBuffer addressBuffer = new StringBuffer();
			Address supplierAddress = stockReceiptEntity.getSupplier()
					.getAddress();
			if (supplierAddress != null) {
				if (supplierAddress.getLine1() != null
						&& !supplierAddress.getLine1().isEmpty())
					addressBuffer.append(supplierAddress.getLine1());
				if (supplierAddress.getLine2() != null
						&& !supplierAddress.getLine2().isEmpty())
					addressBuffer.append(", " + supplierAddress.getLine2());
				if (supplierAddress.getCity() != null
						&& !supplierAddress.getCity().isEmpty())
					addressBuffer.append(", " + supplierAddress.getCity());
				if (supplierAddress.getState() != null
						&& !supplierAddress.getState().isEmpty())
					addressBuffer.append(", " + supplierAddress.getState());
			}

			String suplAdrs = addressBuffer.toString();
			root.put("supplierAddress", suplAdrs);

			root.put("receiptNote", stockReceiptEntity.getNote());

			List<StockLineItem> serviceLineItemList = stockReceiptEntity
					.getServiceLineItemList();
			List<StockLineItem> productLineItemList = stockReceiptEntity
					.getProductLineItemList();

			if (serviceLineItemList != null && serviceLineItemList.size() > 0) {
				root.put("serviceItemList", serviceLineItemList);
			}
			if (productLineItemList != null && productLineItemList.size() > 0) {
				root.put("productItemList", productLineItemList);
			}

			double finalTotal = stockReceiptEntity.getFinalTotal();
			root.put("finalTotal", finalTotal);

			NumberToRupees numberToRupees = new NumberToRupees(
					Math.round(finalTotal));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWords", netInWords);

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/stock_receipt_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			addDocumentFooter(stockReceiptEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generateStockShipmentPdf(
			StockItemsShipmentEntity stockItemsShipment,
			ServletOutputStream outputStream) {
		if (stockItemsShipment instanceof StockItemsShipmentEntity) {
			generateStockItemsShipmentPdf(
					(StockItemsShipmentEntity) stockItemsShipment, outputStream);
		} else {
			throw new RuntimeException(
					"Did not find this entity handling method"
							+ stockItemsShipment.getClass());
		}
	}

	private void generateStockItemsShipmentPdf(
			StockItemsShipmentEntity stockItemsShipment,
			ServletOutputStream outputStream) {

		try {

			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			addDocumentHeaderLogo(stockItemsShipment, document, root);

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");

			ShipmentType shipmentType = stockItemsShipment.getShipmentType();
			root.put("shipmentDate",
					sdfDate.format(stockItemsShipment.getShipmentDate()));

			root.put("docStatus", stockItemsShipment.getStatus());
			root.put("createdBy", stockItemsShipment.getCreatedBy()
					.getFirstName()
					+ " "
					+ stockItemsShipment.getCreatedBy().getLastName());
			UserEntity approvedBy = stockItemsShipment.getApprovedBy();
			root.put("approvedBy",
					approvedBy == null ? "" : approvedBy.getFirstName() + " "
							+ approvedBy.getLastName());
			root.put("fromWarehouse", stockItemsShipment.getFromWH()
					.getWarehouseName());
			root.put("shipmentNo", stockItemsShipment.getItemNumber());
			root.put("shipmentNotes", stockItemsShipment.getNote());
			StringBuffer buffer = new StringBuffer();
			Address warehouseAdd = stockItemsShipment.getFromWH().getAddress();
			if (warehouseAdd != null) {
				if (warehouseAdd.getLine1() != null
						&& !warehouseAdd.getLine1().isEmpty())
					buffer.append(warehouseAdd.getLine1());
				if (warehouseAdd.getLine2() != null
						&& !warehouseAdd.getLine2().isEmpty())
					buffer.append(", " + warehouseAdd.getLine2());
				if (warehouseAdd.getCity() != null
						&& !warehouseAdd.getCity().isEmpty())
					buffer.append(", " + warehouseAdd.getCity());
				if (warehouseAdd.getState() != null
						&& !warehouseAdd.getState().isEmpty())
					buffer.append(", " + warehouseAdd.getState());
			}

			String warehouseAddress = buffer.toString();
			root.put("fromWHAddress", warehouseAddress);

			if (shipmentType.equals(ShipmentType.TO_OTHER_WAREHOUSE)) {
				root.put("toWarehouse", stockItemsShipment.getToWH()
						.getWarehouseName());
				StringBuffer newBuffer = new StringBuffer();
				Address toWHAdd = stockItemsShipment.getToWH().getAddress();
				if (toWHAdd != null) {
					if (toWHAdd.getLine1() != null
							&& !toWHAdd.getLine1().isEmpty())
						newBuffer.append(toWHAdd.getLine1());
					if (toWHAdd.getLine2() != null
							&& !toWHAdd.getLine2().isEmpty())
						newBuffer.append(", " + toWHAdd.getLine2());
					if (toWHAdd.getCity() != null
							&& !toWHAdd.getCity().isEmpty())
						newBuffer.append(", " + toWHAdd.getCity());
					if (toWHAdd.getPin() != null && !toWHAdd.getPin().isEmpty())
						newBuffer.append(", " + toWHAdd.getPin());
					if (toWHAdd.getState() != null
							&& !toWHAdd.getState().isEmpty())
						newBuffer.append(", " + toWHAdd.getState());
				}

				String toWarehouseAddress = newBuffer.toString();
				root.put("toWHAddress", toWarehouseAddress);

			}

			if (shipmentType.equals(ShipmentType.TO_CUSTOMER)) {

				Customer customer = stockItemsShipment.getCustomer();

				String custName = "";

				if (customer.getIsCompany()) {
					custName = customer.getCompanyName();
				} else {
					custName = customer.getFirstName() + " "
							+ customer.getLastName();
				}

				root.put("customerName", custName);

				StringBuffer custBuffer = new StringBuffer();
				Address toCustAdd = stockItemsShipment.getCustomer()
						.getAddress();

				if (toCustAdd != null) {
					if (toCustAdd.getLine1() != null
							&& !toCustAdd.getLine1().isEmpty())
						custBuffer.append(toCustAdd.getLine1());
					if (toCustAdd.getLine2() != null
							&& !toCustAdd.getLine2().isEmpty())
						custBuffer.append(", " + toCustAdd.getLine2());
					if (toCustAdd.getCity() != null
							&& !toCustAdd.getCity().isEmpty())
						custBuffer.append(", " + toCustAdd.getCity());
					if (toCustAdd.getPin() != null
							&& !toCustAdd.getPin().isEmpty())
						custBuffer.append(", " + toCustAdd.getPin());
					if (toCustAdd.getState() != null
							&& !toCustAdd.getState().isEmpty())
						custBuffer.append(", " + toCustAdd.getState());
				}

				String customerAddress = custBuffer.toString();
				root.put("customerAddress", customerAddress);

			}
			if (shipmentType.equals(ShipmentType.TO_PARTNER)) {

				Customer partner = stockItemsShipment.getCustomer();
				String partnerName = "";

				if (partner.getIsCompany()) {
					partnerName = partner.getCompanyName();
				} else {
					partnerName = partner.getFirstName() + " "
							+ partner.getLastName();
				}

				root.put("partnerName", partnerName);

				StringBuffer partnerBuffer = new StringBuffer();
				Address toPartnerAdd = stockItemsShipment.getCustomer()
						.getAddress();

				if (toPartnerAdd != null) {
					if (toPartnerAdd.getLine1() != null
							&& !toPartnerAdd.getLine1().isEmpty())
						partnerBuffer.append(toPartnerAdd.getLine1());
					if (toPartnerAdd.getLine2() != null
							&& !toPartnerAdd.getLine2().isEmpty())
						partnerBuffer.append(", " + toPartnerAdd.getLine2());
					if (toPartnerAdd.getCity() != null
							&& !toPartnerAdd.getCity().isEmpty())
						partnerBuffer.append(", " + toPartnerAdd.getCity());
					if (toPartnerAdd.getPin() != null
							&& !toPartnerAdd.getPin().isEmpty())
						partnerBuffer.append(", " + toPartnerAdd.getPin());
					if (toPartnerAdd.getState() != null
							&& !toPartnerAdd.getState().isEmpty())
						partnerBuffer.append(", " + toPartnerAdd.getState());
				}

				String partnerAddress = partnerBuffer.toString();
				root.put("partnerCoAddress", partnerAddress);
			}

			List<StockLineItem> serviceLineItemList = stockItemsShipment
					.getServiceLineItemList();
			List<StockLineItem> productLineItemList = stockItemsShipment
					.getProductLineItemList();

			if (serviceLineItemList != null && serviceLineItemList.size() > 0) {
				root.put("serviceItemList", serviceLineItemList);
			}
			if (productLineItemList != null && productLineItemList.size() > 0) {
				root.put("productItemList", productLineItemList);

			}

			root.put("shipmentNote", stockItemsShipment.getNote());
			root.put("finalTotal", stockItemsShipment.getFinalTotal());

			NumberToRupees numberToRupees = new NumberToRupees(
					Math.round(stockItemsShipment.getFinalTotal()));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWords", netInWords);

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/stock_shipment_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			addDocumentFooter(stockItemsShipment, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
