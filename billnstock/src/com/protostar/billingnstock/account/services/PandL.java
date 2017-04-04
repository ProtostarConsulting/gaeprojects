package com.protostar.billingnstock.account.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.entities.CurrentFinancialYear;
import com.protostar.billingnstock.account.services.AccountGroupService.GroupInfo;
import com.protostar.billingnstock.account.services.AccountGroupService.TypeInfo;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

public class PandL extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PandL() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Long bid = Long.parseLong(request.getParameter("bid"));
		PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
		response.setContentType("application/PDF");
		ServletOutputStream outputStream = response.getOutputStream();
		Date today = new Date();
		String fileNameAppend = today.getDay() + "_" + today.getMonth() + "_"
				+ today.getYear();
		response.setHeader("Content-disposition",
				"inline; filename='Downloaded_" + fileNameAppend + ".pdf'");
AccountGroupService accountgroup=new AccountGroupService();
AccountGroupEntity accGroupEntity=new AccountGroupEntity();

List<TypeInfo> PandL = accountgroup.getProfitAndLossAcc(bid);//BalanceSheet(bid);
this.getProfitAndLossAcc(PandL,
		outputStream,bid);
		
		
	}
	public void getProfitAndLossAcc(List<TypeInfo> list,
			ServletOutputStream outputStream, Long bid) {
		DateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
		AccountingService accountingService=new AccountingService();
		CurrentFinancialYear currentFinancialYear = accountingService.getCurrentFinancialYear(bid);

		try {
			AccountGroupEntity accG = new AccountGroupEntity();
			com.protostar.billingnstock.user.services.UserService user = new com.protostar.billingnstock.user.services.UserService();
			BusinessEntity businessEntity = user.getBusinessById(bid);
			accG.setBusiness(businessEntity);
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			Date today = new Date();
			PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			Map<String, Object> root = new HashMap<String, Object>();
			pdfHtmlTemplateService.addDocumentHeaderLogo(accG, document, root);
			root.put("ProfitAndLossAcList", list);
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
			Template temp = pdfHtmlTemplateService.getConfiguration().getTemplate(
					"pdf_templates/profitAndLossAcc_tmpl.ftlh");

			String date =df.format(currentFinancialYear.getFromDate())+" to "+df.format(currentFinancialYear.getToDate());//+currentFinancialYear.getToDate().toString();// "1-Apr-2016 to 15-Apr-2016";
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
			pdfHtmlTemplateService.addDocumentFooter(accG, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
