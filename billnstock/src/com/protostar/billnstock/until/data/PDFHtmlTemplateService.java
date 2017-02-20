package com.protostar.billnstock.until.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.io.output.TaggedOutputStream;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.entities.PurchaseVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceiptVoucherEntity;
import com.protostar.billingnstock.account.entities.SalesVoucherEntity;
import com.protostar.billingnstock.account.entities.VoucherEntity;
import com.protostar.billingnstock.account.services.AccountGroupService;
import com.protostar.billingnstock.account.services.VoucherService;
import com.protostar.billingnstock.account.services.AccountGroupService.TypeInfo;
import com.protostar.billingnstock.account.services.AccountingService;
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

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/profitAndLossAcc_tmpl.ftlh");

			double netPandL = 0;
			double totalSales = 0;
			double totalIndirectExpences2 = 0;
			double totalPurches = 0;
			double totalIndirectExpences = 0;
			double totalGrossProfit = 0;
			double totalGrossLoss = 0;
			double totalLeft = 0;
			double totalRight = 0;
			String typeName;
			double totalOtherExp = 0;
			double nettProfit = 0;
			double totalGrossProfitCo = 0;
			double totalGrossProfitBf = 0;
			double totalOpeningStockBalance = 0;
			double totalClosingStockBalance = 0;
			// //
			double goodsSold = 0;
			double salesReturn = 0;
			double cashSales = 0;
			double creditSales = 0;

			AccountGroupService accGS = new AccountGroupService();

			totalClosingStockBalance = accGS.getClosingStockBalance(bid)
					.getReturnBalance();
			System.out.println("totalClosingStockBalance-----"
					+ totalClosingStockBalance);

			for (int int2 = 0; int2 < list.size(); int2++) {
				typeName = list.get(int2).getTypeName().toString();
				if ((typeName == "INCOME")
						&& (list.get(int2).getGroupList() != null)) {
					for (int i = 0; i < list.get(int2).getGroupList().size(); i++) {
						totalSales = list.get(int2).getGroupList().get(i)
								.getGroupBalance()
								+ totalSales;
					}
					if (totalSales < 0) {
						totalSales = totalSales * (-1);
					}
				}

				if ((typeName == "OTHEREXPENCES")
						&& (list.get(int2).getGroupList() != null)) {
					for (int i = 0; i < list.get(int2).getGroupList().size(); i++) {
						totalIndirectExpences = list.get(int2).getGroupList()
								.get(i).getGroupBalance()
								+ totalIndirectExpences;

					}

					if (totalIndirectExpences < 0) {
						totalIndirectExpences = totalIndirectExpences * (-1);
					}

				}
				if ((typeName == "EXPENSES")
						&& (list.get(int2).getGroupList() != null)) {
					for (int i = 0; i < list.get(int2).getGroupList().size(); i++) {
						totalPurches = list.get(int2).getGroupList().get(i)
								.getGroupBalance()
								+ totalPurches;
					}
					if (totalPurches < 0) {
						totalPurches = totalPurches * -1;
					}

				}

			}

			totalIndirectExpences2 = totalIndirectExpences + totalPurches;
			if (totalIndirectExpences2 < 0) {
				totalIndirectExpences2 = totalIndirectExpences2 * (-1);
			}

			totalLeft = totalOpeningStockBalance + totalPurches;
			totalRight = totalClosingStockBalance + totalSales;

			VoucherService vs = new VoucherService();
			// SalesVoucherEntity SalesVoucherEntity=new SalesVoucherEntity();
			List<SalesVoucherEntity> SalesVoucherList = vs
					.getlistSalesVoucher(bid);
			for (int k = 0; k < SalesVoucherList.size(); k++) {

				goodsSold = goodsSold + SalesVoucherList.get(k).getAmount();

			}
			// ///////////
			totalGrossProfit = totalSales - goodsSold;
			totalGrossProfitCo = totalGrossProfit;
			totalGrossProfitBf = totalGrossProfit;

			// ///////////

			if (totalGrossProfit < 0) {
				totalGrossLoss = totalGrossProfit;
			}
			String date = "1-Apr-2016 to 15-Apr-2016";
			nettProfit = totalGrossProfit - totalIndirectExpences;
			root.put("totalPurches", totalPurches);
			root.put("totalGrossProfit", totalGrossProfit);
			root.put("totalIndirectExpences", totalIndirectExpences);
			root.put("nettProfit", nettProfit);
			root.put("totalSales", totalSales);
			root.put("totalGrossProfit", totalGrossProfit);
			root.put("totalLeft", totalLeft);
			root.put("totalRight", totalRight);
			root.put("totalGrossProfit", totalGrossProfit);
			root.put("totalClosingStockBalance", totalClosingStockBalance);
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

	public void generatePdfBalanceSheet(List<TypeInfo> list,
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
			root.put("balanceSheetList", list);

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

			for (int int2 = 0; int2 < list.size(); int2++) {
				String typeName = list.get(int2).getTypeName();
				if ((typeName == "ASSETS")
						&& (list.get(int2).getGroupList() != null)
						&& !(list.get(int2).getGroupList().get(int2)
								.getGroupName()
								.equalsIgnoreCase("Sundry Debtors"))) {
					for (int i = 0; i < list.get(int2).getGroupList().size(); i++) {
						System.out.println("get GRPLIST-----"
								+ list.get(int2).getGroupList().get(int2)
										.getGroupName());
						totalAsset = list.get(int2).getGroupList().get(i)
								.getGroupBalance()
								+ totalAsset;
					}
					if (totalAsset < 0) {
						totalAsset = totalAsset * (-1);
					}
				}

				if ((typeName == "LIABILITIES")
						&& (list.get(int2).getGroupList() != null)) {
					for (int i = 0; i < list.get(int2).getGroupList().size(); i++) {
						totalLiabilities = list.get(int2).getGroupList().get(i)
								.getGroupBalance()
								+ totalLiabilities;
					}

					if (totalLiabilities < 0) {
						totalLiabilities = totalLiabilities * (-1);
					}

				}
				if ((typeName == "EQUITY")
						&& (list.get(int2).getGroupList() != null)) {
					for (int i = 0; i < list.get(int2).getGroupList().size(); i++) {
						totalEQUITY = list.get(int2).getGroupList().get(i)
								.getGroupBalance()
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
			Image logoURL = Image
					.getInstance("img/images/protostar_logo_pix_313_132.jpg");
			logoURL.setAbsolutePosition(50f, 788f);
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

			document.open();
			Image logoURL = Image
					.getInstance("img/images/protostar_logo_pix_313_132.jpg");
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
			root.put("groupName", accountGroupEntity.getGroupName());

			root.put("groupType", accountGroupEntity.getAccountGroupType()
					.toString());

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
				addressBuf.append(", " + address.getLine2());
			if (address.getCity() != null && !address.getCity().isEmpty())
				addressBuf.append(", <br></br>" + address.getCity());
			if (address.getState() != null && !address.getState().isEmpty())
				addressBuf.append(", " + address.getState());
			if (address.getPin() != null && !address.getPin().isEmpty())
				addressBuf.append(", " + address.getPin());
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
		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);
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
			root.put("CustomerName", custName);
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
			root.put("NoteToCustomer", "" + invoiceEntity.getNoteToCustomer());

			if (discountAmt > 0) {
				root.put("Discount", df.format(discountAmt));
			}

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/invoicePDF_tmpl.ftlh");

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

			Customer customer = quotationEntity.getInvoiceObj().getCustomer();
			String customerName = customer.getCompanyName();

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
			root.put("CustomerName", customerName);
			root.put("CustomerAddress", custAddressForQuot);
			root.put("NoteToCust", "" + noteToCust);

			// Quotation Date and No
			root.put("Date", quotationDate);
			root.put("QuotationNumber", quotationEntity.getItemNumber());

			root.put("finalTotal", finalTotal);
			NumberToRupees numberToRupees = new NumberToRupees(
					Math.round(finalTotal));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWords", netInWords);

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/quotationPDF_tmpl.ftlh");

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

			root.put("To", purchaseOrderEntity.getTo());
			root.put("ShippedTo", purchaseOrderEntity.getShipTo());
			root.put("PONum", purchaseOrderEntity.getItemNumber());
			root.put("PODate", purchaseOrderDate);
			root.put("PODueDate", purchaseOrderDueDate);
			root.put("ShippedVia", "" + purchaseOrderEntity.getShippedVia());
			root.put("Requisitioner",
					"" + purchaseOrderEntity.getRequisitioner());
			root.put("Supplier", ""
					+ purchaseOrderEntity.getSupplier().getSupplierName());
			root.put("FOBPoint", "" + purchaseOrderEntity.getfOBPoint());
			root.put("Terms", "" + purchaseOrderEntity.getTerms());
			root.put("Note", "" + purchaseOrderEntity.getNoteToCustomer());

			TaxEntity servTax = purchaseOrderEntity.getSelectedServiceTax();
			TaxEntity prodTax = purchaseOrderEntity.getSelectedProductTax();

			List<StockLineItem> serviceLineItemListForPO = purchaseOrderEntity
					.getServiceLineItemList();
			List<StockLineItem> productLineItemListForPO = purchaseOrderEntity
					.getProductLineItemList();

			if (productLineItemListForPO != null
					&& productLineItemListForPO.size() > 0) {
				root.put("productItemList", productLineItemListForPO);
				root.put("serviceTax", servTax);

			}

			if (serviceLineItemListForPO != null
					&& serviceLineItemListForPO.size() > 0) {
				root.put("serviceItemList", serviceLineItemListForPO);
				root.put("productTax", prodTax);
			}

			double finalTotal = purchaseOrderEntity.getFinalTotal();
			root.put("finalTotal", finalTotal);

			NumberToRupees numberToRupees = new NumberToRupees(
					Math.round(finalTotal));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWords", netInWords);

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/purchaseOrderPDF_tmpl.ftlh");

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
			root.put("ReceiptDate", receiptDate);
			root.put("ReceiptNo", stockReceiptEntity.getItemNumber());
			root.put("Supplier", stockReceiptEntity.getSupplier()
					.getSupplierName());
			root.put("Warehouse", stockReceiptEntity.getWarehouse()
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
			root.put("WarehouseAddress", warehouseAddress);

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
			root.put("SupplierAddress", suplAdrs);

			List<StockLineItem> serviceLineItemList = stockReceiptEntity
					.getServiceLineItemList();
			List<StockLineItem> productLineItemList = stockReceiptEntity
					.getProductLineItemList();

			if (serviceLineItemList != null && serviceLineItemList.size() > 0) {
				root.put("serviceItemList", serviceLineItemList);
			}
			if (productLineItemList != null && productLineItemList.size() > 0) {
				root.put("productItemList", productLineItemList);
				for (int i = 0; i < productLineItemList.size(); i++) {
					double productTotal = (productLineItemList.get(i).getQty())
							* (productLineItemList.get(i).getPrice());
					productTotal += productTotal;
					root.put("ProductTotal", productTotal);
				}

			}

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/stockReceiptPDF_tmpl.ftlh");

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
			root.put("ShipmentDate",
					sdfDate.format(stockItemsShipment.getShipmentDate()));

			root.put("FromWarehouse", stockItemsShipment.getFromWH()
					.getWarehouseName());
			root.put("ShipmentNo", stockItemsShipment.getItemNumber());

			if (shipmentType.equals(ShipmentType.TO_OTHER_WAREHOUSE)) {
				root.put("ToWarehouse", stockItemsShipment.getToWH()
						.getWarehouseName());
			}

			if (shipmentType.equals(ShipmentType.TO_CUSTOMER)) {
				root.put("Customer", stockItemsShipment.getCustomer()
						.getCompanyName());

			}
			if (shipmentType.equals(ShipmentType.TO_PARTNER)) {
				root.put("Partner", stockItemsShipment.getCustomer()
						.getCompanyName());
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

			Template temp = getConfiguration().getTemplate(
					"pdf_templates/stockShipmentPDF_tmpl.ftlh");

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
