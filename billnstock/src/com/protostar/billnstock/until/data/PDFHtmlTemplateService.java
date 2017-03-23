package com.protostar.billnstock.until.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.MalformedURLException;
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
import com.protostar.billingnstock.account.entities.PurchaseVoucherEntity;
import com.protostar.billingnstock.account.entities.ReceiptVoucherEntity;
import com.protostar.billingnstock.account.entities.SalesVoucherEntity;
import com.protostar.billingnstock.account.entities.VoucherEntity;
import com.protostar.billingnstock.account.services.AccountGroupService;
import com.protostar.billingnstock.account.services.AccountGroupService.GroupInfo;
import com.protostar.billingnstock.account.services.AccountGroupService.TypeInfo;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.entity.BaseEntity;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class PDFHtmlTemplateService {

	static Configuration cfg = null;

	public static Configuration getConfiguration() {
		if (cfg != null) {
			return cfg;
		}

		Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);

		// cfg.setDirectoryForTemplateLoading(new
		// File("/WEB-INF/email_templates"));
		cfg.setClassForTemplateLoading(cfg.getClass(), "/");

		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setLogTemplateExceptions(false);
		return cfg;
	}

	public void generatePdfAccountChart(List<TypeInfo> accountChart, ServletOutputStream outputStream, Long bid) {

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
			Template temp = getConfiguration().getTemplate("pdf_templates/accountChart_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
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
	public void getProfitAndLossAcc(List<TypeInfo> list, ServletOutputStream outputStream, Long bid) {

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

				if ((typeName == "INCOME") && (list.get(count).getGroupList() != null)) {
					totalSalesList = list.get(count).getGroupList();
					operatingRevenue = list.get(count).getTypeBalance();
				}

				if ((typeName == "OTHEREXPENCES") && (list.get(count).getGroupList() != null)) {

					totalPaymentList = list.get(count).getGroupList();
					otherExpense = list.get(count).getTypeBalance();

				}

				if ((typeName == "EXPENSES") && (list.get(count).getGroupList() != null)) {
					totalPurchaseList = list.get(count).getGroupList();
					operatingExpense = list.get(count).getTypeBalance();

				}

				grossProfit = operatingRevenue - operatingExpense;
				operatingIncome = grossProfit - otherExpense;
			}
			Template temp = getConfiguration().getTemplate("pdf_templates/profitAndLossAcc_tmpl.ftlh");

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
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
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

	public void generatePdfBalanceSheet(List<TypeInfo> natureList, ServletOutputStream outputStream, Long bid) {

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

			Template temp = getConfiguration().getTemplate("pdf_templates/balanceSheet_tmpl.ftlh");

			double totalAsset = 0;
			double totalLiabilities2 = 0;
			double totalEQUITY = 0;
			double totalLiabilities = 0;
			AccountGroupService ag = new AccountGroupService();

			ServerMsg nettProffitOrLoss1 = ag.getProfitAndLossAccBalance(bid);
			double nettProffitOrLoss = nettProffitOrLoss1.getReturnBalance();

			for (int int2 = 0; int2 < natureList.size(); int2++) {
				String typeName = natureList.get(int2).getTypeName();
				if ((typeName == "ASSETS") && (natureList.get(int2).getGroupList() != null)) {
					for (int i = 0; i < natureList.get(int2).getGroupList().size(); i++) {
						System.out.println(
								"get GRPLIST-----" + natureList.get(int2).getGroupList().get(int2).getGroupName());
						totalAsset = natureList.get(int2).getGroupList().get(i).getGroupBalance() + totalAsset;
					}
					if (totalAsset < 0) {
						totalAsset = totalAsset * (-1);
					}
				}

				if ((typeName == "LIABILITIES") && (natureList.get(int2).getGroupList() != null)) {
					for (int i = 0; i < natureList.get(int2).getGroupList().size(); i++) {
						totalLiabilities = natureList.get(int2).getGroupList().get(i).getGroupBalance()
								+ totalLiabilities;
					}

					if (totalLiabilities < 0) {
						totalLiabilities = totalLiabilities * (-1);
					}

				}
				if ((typeName == "EQUITY") && (natureList.get(int2).getGroupList() != null)) {
					for (int i = 0; i < natureList.get(int2).getGroupList().size(); i++) {
						totalEQUITY = natureList.get(int2).getGroupList().get(i).getGroupBalance() + totalEQUITY;
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

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
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

	public void generateVoucherPDF(VoucherEntity voucherEntity, ServletOutputStream outputStream) {

		if (voucherEntity instanceof SalesVoucherEntity) {
			generateSalesVoucherPDF((SalesVoucherEntity) voucherEntity, outputStream);
		} else if (voucherEntity instanceof ReceiptVoucherEntity) {
			generateReceiptVoucherPDF((ReceiptVoucherEntity) voucherEntity, outputStream);
		}

		else if (voucherEntity instanceof PurchaseVoucherEntity) {
			generatePurchesVoucherPDF((PurchaseVoucherEntity) voucherEntity, outputStream);
		}

		else {
			throw new RuntimeException("Did not find this entity PDF handling methods: " + voucherEntity.getClass());
		}
	}

	private void generateSalesVoucherPDF(SalesVoucherEntity salesEntity, ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			addDocumentHeaderLogo(salesEntity, document, root);

			root.put("DebitAccount", salesEntity.getAccountType1().getAccountName().toString());
			root.put("CreditAccount", salesEntity.getAccountType2().getAccountName().toString());
			root.put("Amount", salesEntity.getAmount().toString());
			root.put("Narration", salesEntity.getNarration().toString());

			Template temp = getConfiguration().getTemplate("pdf_templates/sales_voucher_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
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

	private void generateReceiptVoucherPDF(ReceiptVoucherEntity receiptEntity, ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			addDocumentHeaderLogo(receiptEntity, document, root);
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

			Template temp = getConfiguration().getTemplate("pdf_templates/invoice_voucher_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
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

	private void generatePurchesVoucherPDF(PurchaseVoucherEntity purchesEntity, ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			addDocumentHeaderLogo(purchesEntity, document, root);
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

			Template temp = getConfiguration().getTemplate("pdf_templates/purches_voucher_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
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

	private void generateAccountChartpdf(AccountGroupEntity accountGroupEntity, ServletOutputStream outputStream) {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			addDocumentHeaderLogo(accountGroupEntity, document, root);
			root.put("groupName", accountGroupEntity.getGroupName());
			// root.put("accountName",accountGroupEntity.get;
			root.put("groupType", accountGroupEntity.getAccountGroupType().toString());
			// root.put("balance",purchesEntity.getItem().toString());

			Template temp = getConfiguration().getTemplate("pdf_templates/Download_Account_chart_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
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

	public static void addDocumentHeaderLogo(BaseEntity enity, Document document, Map<String, Object> root)
			throws BadElementException, MalformedURLException, IOException, DocumentException {
		String bizLogoGCSURL = enity.getBusiness().getBizLogoGCSURL();
		if (bizLogoGCSURL != null && !bizLogoGCSURL.isEmpty()) {
			Image logoURL = Image.getInstance(bizLogoGCSURL);
			logoURL.setAbsolutePosition(50f, 740f);
			logoURL.scaleToFit(150f, 80f);
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

	public static void addDocumentFooter(BaseEntity enity, PdfWriter writer)
			throws BadElementException, MalformedURLException, IOException, DocumentException {

		PdfContentByte cb = writer.getDirectContent();
		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

		cb.saveState();
		cb.beginText();
		cb.moveText(20f, 20f);
		cb.setFontAndSize(bf, 10);
		cb.showText("This is electronically generated document. Needs no stamp or signature.");
		cb.endText();
		cb.restoreState();

	}

}
