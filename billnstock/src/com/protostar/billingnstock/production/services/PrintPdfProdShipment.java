package com.protostar.billingnstock.production.services;

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
import com.protostar.billingnstock.production.entities.ProductionShipmentEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity;
import com.protostar.billingnstock.stock.entities.StockItemsShipmentEntity.ShipmentType;
import com.protostar.billingnstock.stock.entities.StockLineItem;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.entity.Address;
import com.protostar.billnstock.until.data.NumberToRupees;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

public class PrintPdfProdShipment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfProdShipment() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long prShipId = Long.parseLong(request.getParameter("stShipId"));
		Long bid = Long.parseLong(request.getParameter("bid"));

		ProductionService productionService = new ProductionService();

		ProductionShipmentEntity productionShipmentEntity = productionService.getProdShipmentByID(bid, prShipId);
		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "StockShipment_" + productionShipmentEntity.getItemNumber() + "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='StockShipment" + fileNameAppend + ".pdf'");
		this.generatePdf(productionShipmentEntity, outputStream);

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
			/////////////////////////////
		
			root.put("shipmentNo", stockItemsShipment.getItemNumber());
			root.put("shipmentNotes", stockItemsShipment.getNote());
			StringBuffer buffer = new StringBuffer();
			

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

		
			

			List<StockLineItem> serviceLineItemList = stockItemsShipment.getServiceLineItemList();
			List<StockLineItem> productLineItemList = stockItemsShipment.getProductLineItemList();

			/*if (serviceLineItemList != null && serviceLineItemList.size() > 0) {
				root.put("serviceItemList", serviceLineItemList);
			}*/
			if (productLineItemList != null && productLineItemList.size() > 0) {
				root.put("productItemList", productLineItemList);

			}

			root.put("shipmentNote", stockItemsShipment.getNote());
			root.put("finalTotal", stockItemsShipment.getFinalTotal());

			NumberToRupees numberToRupees = new NumberToRupees(Math.round(stockItemsShipment.getFinalTotal()));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("finalTotalInWords", netInWords);

			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("pdf_templates/prod_shipment_tmpl.ftlh");

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
