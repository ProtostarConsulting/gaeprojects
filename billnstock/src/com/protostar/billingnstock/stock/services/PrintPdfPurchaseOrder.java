package com.protostar.billingnstock.stock.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import com.protostar.billingnstock.purchase.entities.PurchaseOrderEntity;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.tax.entities.TaxEntity;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.NumberToRupees;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

public class PrintPdfPurchaseOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfPurchaseOrder() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long poId = Long.parseLong(request.getParameter("poId"));
		Long bid = Long.parseLong(request.getParameter("bid"));

		StockManagementService stockMgmtService = new StockManagementService();

		PurchaseOrderEntity purchaseOrderEntity = stockMgmtService.getPOByID(bid, poId);
		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "PurchaseOrder_" + purchaseOrderEntity.getItemNumber() + "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='ProERP_" + fileNameAppend + ".pdf'");

		this.generatePdf(purchaseOrderEntity, outputStream);

	}

	public void generatePdf(PurchaseOrderEntity purchaseOrderEntity, OutputStream outputStream) {

		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			PDFHtmlTemplateService.addDocumentHeaderLogo(purchaseOrderEntity, document, root);

			DecimalFormat df = new DecimalFormat("#0.00");

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			Date createdDate = purchaseOrderEntity.getCreatedDate();
			Date modifiedDate = purchaseOrderEntity.getModifiedDate();

			String createdDateStr = sdfDate.format(createdDate);

			String modifiedDateStr = sdfDate.format(modifiedDate);

			String noteToCustomer = purchaseOrderEntity.getNoteToCustomer();

			double discAmt = purchaseOrderEntity.getDiscAmount();
			if (discAmt > 0) {
				root.put("Discount", df.format(discAmt));
			}

			root.put("billTo", purchaseOrderEntity.getTo());
			root.put("shipTo", purchaseOrderEntity.getShipTo());
			root.put("pONum", purchaseOrderEntity.getItemNumber());
			root.put("docStatus", purchaseOrderEntity.getStatus());
			root.put("createdDateStr", createdDateStr);
			root.put("modifiedDateStr", modifiedDateStr);
			root.put("shippedVia", "" + purchaseOrderEntity.getShippedVia());
			root.put("requisitioner", "" + purchaseOrderEntity.getRequisitioner());
			root.put("supplierName", "" + purchaseOrderEntity.getSupplier().getSupplierName());
			root.put("FOBPoint", "" + purchaseOrderEntity.getfOBPoint());
			root.put("Terms", "" + purchaseOrderEntity.getTerms());
			root.put("noteToCustomer", noteToCustomer == null ? "" : noteToCustomer);
			UserEntity createdBy = purchaseOrderEntity.getCreatedBy();
			root.put("createdBy", createdBy == null ? "" : createdBy.getFirstName() + " " + createdBy.getLastName());
			UserEntity approvedBy = purchaseOrderEntity.getApprovedBy();
			System.out.println("approvedBy: " + approvedBy);
			root.put("approvedBy",
					approvedBy == null ? "" : approvedBy.getFirstName() + " " + approvedBy.getLastName());

			StringBuffer buffer = new StringBuffer();

			Address suplAdress = purchaseOrderEntity.getSupplier().getAddress();

			if (suplAdress != null) {
				if (suplAdress.getLine1() != null && !suplAdress.getLine1().isEmpty())
					buffer.append(suplAdress.getLine1());
				if (suplAdress.getLine2() != null && !suplAdress.getLine2().isEmpty())
					buffer.append(", <br></br>" + suplAdress.getLine2());
				if (suplAdress.getCity() != null && !suplAdress.getCity().isEmpty())
					buffer.append(",<br></br>" + suplAdress.getCity());
				if (suplAdress.getState() != null && !suplAdress.getState().isEmpty())
					buffer.append(", " + suplAdress.getState());
			}

			String supplierAddress = buffer.toString();
			root.put("supplierAddress", supplierAddress);

			TaxEntity servTax = purchaseOrderEntity.getSelectedServiceTax();
			TaxEntity prodTax = purchaseOrderEntity.getSelectedProductTax();

			List<StockLineItem> serviceLineItemListForPO = purchaseOrderEntity.getServiceLineItemList();
			List<StockLineItem> productLineItemListForPO = purchaseOrderEntity.getProductLineItemList();

			if (productLineItemListForPO != null && productLineItemListForPO.size() > 0) {
				root.put("productItemList", productLineItemListForPO);
				root.put("productTax", prodTax);

			}

			if (serviceLineItemListForPO != null && serviceLineItemListForPO.size() > 0) {
				root.put("serviceItemList", serviceLineItemListForPO);
				root.put("serviceTax", prodTax);
			}

			double finalTotal = purchaseOrderEntity.getFinalTotal();
			root.put("finalTotal", finalTotal);

			NumberToRupees numberToRupees = new NumberToRupees(Math.round(finalTotal));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWords", netInWords);

			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("pdf_templates/purchase_order_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					Constants.DOCUMENT_DEFAULT_MAX_SIZE);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			PDFHtmlTemplateService.addDocumentFooter(purchaseOrderEntity, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
