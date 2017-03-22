package com.protostar.billingnstock.stock.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
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
import com.protostar.billingnstock.cust.entities.Customer;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity.ShipmentType;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.until.data.NumberToRupees;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

public class PrintPdfstockShipment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfstockShipment() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long stShipId = Long.parseLong(request.getParameter("stShipId"));
		Long bid = Long.parseLong(request.getParameter("bid"));

		StockManagementService stockMgmtService = new StockManagementService();

		StockItemsShipmentEntity stockShipmentEntity = stockMgmtService.getStockShipmentByID(bid, stShipId);
		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "StockShipment_" + stockShipmentEntity.getItemNumber() + "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='StockShipment" + fileNameAppend + ".pdf'");

		this.generatePdf(stockShipmentEntity, outputStream);

	}

	public void generatePdf(StockItemsShipmentEntity stockItemsShipment,OutputStream outputStream) {

		try {

			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();

			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

			Map<String, Object> root = new HashMap<String, Object>();
			PDFHtmlTemplateService.addDocumentHeaderLogo(stockItemsShipment, document, root);

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");

			ShipmentType shipmentType = stockItemsShipment.getShipmentType();
			root.put("createdDate", sdfDate.format(stockItemsShipment.getCreatedDate()));
			root.put("modifiedDate", sdfDate.format(stockItemsShipment.getModifiedDate()));
			root.put("docStatus", stockItemsShipment.getStatus());
			root.put("createdBy", stockItemsShipment.getCreatedBy().getFirstName() + " "
					+ stockItemsShipment.getCreatedBy().getLastName());
			UserEntity approvedBy = stockItemsShipment.getApprovedBy();
			root.put("approvedBy",
					approvedBy == null ? "" : approvedBy.getFirstName() + " " + approvedBy.getLastName());
			root.put("fromWarehouse", stockItemsShipment.getFromWH().getWarehouseName());
			root.put("shipmentNo", stockItemsShipment.getItemNumber());
			root.put("shipmentNotes", stockItemsShipment.getNote());
			StringBuffer buffer = new StringBuffer();
			Address warehouseAdd = stockItemsShipment.getFromWH().getAddress();
			if (warehouseAdd != null) {
				if (warehouseAdd.getLine1() != null && !warehouseAdd.getLine1().isEmpty())
					buffer.append(warehouseAdd.getLine1());
				if (warehouseAdd.getLine2() != null && !warehouseAdd.getLine2().isEmpty())
					buffer.append(", " + warehouseAdd.getLine2());
				if (warehouseAdd.getCity() != null && !warehouseAdd.getCity().isEmpty())
					buffer.append(", " + warehouseAdd.getCity());
				if (warehouseAdd.getState() != null && !warehouseAdd.getState().isEmpty())
					buffer.append(", " + warehouseAdd.getState());
			}

			String warehouseAddress = buffer.toString();
			root.put("fromWHAddress", warehouseAddress);

			if (shipmentType.equals(ShipmentType.TO_OTHER_WAREHOUSE)) {
				root.put("toWarehouse", stockItemsShipment.getToWH().getWarehouseName());
				StringBuffer newBuffer = new StringBuffer();
				Address toWHAdd = stockItemsShipment.getToWH().getAddress();
				if (toWHAdd != null) {
					if (toWHAdd.getLine1() != null && !toWHAdd.getLine1().isEmpty())
						newBuffer.append(toWHAdd.getLine1());
					if (toWHAdd.getLine2() != null && !toWHAdd.getLine2().isEmpty())
						newBuffer.append(", " + toWHAdd.getLine2());
					if (toWHAdd.getCity() != null && !toWHAdd.getCity().isEmpty())
						newBuffer.append(", " + toWHAdd.getCity());
					if (toWHAdd.getPin() != null && !toWHAdd.getPin().isEmpty())
						newBuffer.append(", " + toWHAdd.getPin());
					if (toWHAdd.getState() != null && !toWHAdd.getState().isEmpty())
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
					custName = customer.getFirstName() + " " + customer.getLastName();
				}

				root.put("customerName", custName);

				StringBuffer custBuffer = new StringBuffer();
				Address toCustAdd = stockItemsShipment.getCustomer().getAddress();

				if (toCustAdd != null) {
					if (toCustAdd.getLine1() != null && !toCustAdd.getLine1().isEmpty())
						custBuffer.append(toCustAdd.getLine1());
					if (toCustAdd.getLine2() != null && !toCustAdd.getLine2().isEmpty())
						custBuffer.append(", " + toCustAdd.getLine2());
					if (toCustAdd.getCity() != null && !toCustAdd.getCity().isEmpty())
						custBuffer.append(", " + toCustAdd.getCity());
					if (toCustAdd.getPin() != null && !toCustAdd.getPin().isEmpty())
						custBuffer.append(", " + toCustAdd.getPin());
					if (toCustAdd.getState() != null && !toCustAdd.getState().isEmpty())
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
					partnerName = partner.getFirstName() + " " + partner.getLastName();
				}

				root.put("partnerName", partnerName);

				StringBuffer partnerBuffer = new StringBuffer();
				Address toPartnerAdd = stockItemsShipment.getCustomer().getAddress();

				if (toPartnerAdd != null) {
					if (toPartnerAdd.getLine1() != null && !toPartnerAdd.getLine1().isEmpty())
						partnerBuffer.append(toPartnerAdd.getLine1());
					if (toPartnerAdd.getLine2() != null && !toPartnerAdd.getLine2().isEmpty())
						partnerBuffer.append(", " + toPartnerAdd.getLine2());
					if (toPartnerAdd.getCity() != null && !toPartnerAdd.getCity().isEmpty())
						partnerBuffer.append(", " + toPartnerAdd.getCity());
					if (toPartnerAdd.getPin() != null && !toPartnerAdd.getPin().isEmpty())
						partnerBuffer.append(", " + toPartnerAdd.getPin());
					if (toPartnerAdd.getState() != null && !toPartnerAdd.getState().isEmpty())
						partnerBuffer.append(", " + toPartnerAdd.getState());
				}

				String partnerAddress = partnerBuffer.toString();
				root.put("partnerCoAddress", partnerAddress);
			}

			List<StockLineItem> serviceLineItemList = stockItemsShipment.getServiceLineItemList();
			List<StockLineItem> productLineItemList = stockItemsShipment.getProductLineItemList();

			if (serviceLineItemList != null && serviceLineItemList.size() > 0) {
				root.put("serviceItemList", serviceLineItemList);
			}
			if (productLineItemList != null && productLineItemList.size() > 0) {
				root.put("productItemList", productLineItemList);

			}

			root.put("shipmentNote", stockItemsShipment.getNote());
			root.put("finalTotal", stockItemsShipment.getFinalTotal());

			NumberToRupees numberToRupees = new NumberToRupees(Math.round(stockItemsShipment.getFinalTotal()));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWords", netInWords);

			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("pdf_templates/stock_shipment_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);
			// return escapeHtml(byteArrayOutputStream.toString());

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			PDFHtmlTemplateService.addDocumentFooter(stockItemsShipment, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
