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
import com.protostar.billingnstock.account.entities.AccountGroupEntity;
import com.protostar.billingnstock.account.services.AccountGroupService.TypeInfo;
import com.protostar.billingnstock.user.entities.BusinessEntity;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;
import com.protostar.billnstock.until.data.ServerMsg;

import freemarker.template.Template;

public class PdfBalanceSheet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PdfBalanceSheet() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Long bid = Long.parseLong(request.getParameter("bid"));
		response.setContentType("application/PDF");
		ServletOutputStream outputStream = response.getOutputStream();
		Date today = new Date();
		String fileNameAppend = today.getDay() + "_" + today.getMonth() + "_"
				+ today.getYear();
		response.setHeader("Content-disposition",
				"inline; filename='Downloaded_" + fileNameAppend + ".pdf'");
AccountGroupService accountgroup=new AccountGroupService();
AccountGroupEntity accGroupEntity=new AccountGroupEntity();

List<TypeInfo> balanceSheetList = accountgroup.getBalanceSheet(bid);
this.generatePdfBalanceSheet(balanceSheetList,
		outputStream,bid);
		
	}
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
			PDFHtmlTemplateService pdfHtmlTemplateService = new PDFHtmlTemplateService();
			pdfHtmlTemplateService.addDocumentHeaderLogo(accG, document, root);
			root.put("balanceSheetList", natureList);

			root.put("date", "" + today);

			Template temp = pdfHtmlTemplateService.getConfiguration().getTemplate(
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
			pdfHtmlTemplateService.addDocumentFooter(accG, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
