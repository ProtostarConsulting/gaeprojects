package com.protostar.billingnstock.production.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.protostar.billingnstock.production.entities.QCMachineDailyRecordEntity;
import com.protostar.billingnstock.production.entities.QCParameterRecord;
import com.protostar.billingnstock.production.entities.QCTimeParameterValue;
import com.protostar.billingnstock.stock.entities.StockLineItemsByCategory;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.Constants;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

/**
 * Servlet implementation class PrintQCMachineRecordPdf
 */
public class PrintQCMachineRecordPdf extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PrintQCMachineRecordPdf() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		Long qcRecordId = Long.parseLong(request.getParameter("qcRecordId"));
		Long bid = Long.parseLong(request.getParameter("bid"));;
		
		ProductionService prodService = new ProductionService();
		QCMachineDailyRecordEntity qcMachineRecord = prodService.getQCMachineRecordById(bid, qcRecordId);
		response.setContentType("application/PDF");

		ServletOutputStream outputStream = response.getOutputStream();
		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "QCMachineRecord" + "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='ProERP_" + fileNameAppend + ".pdf'");

		this.generatePdf(qcMachineRecord, outputStream);

	}

	private void generatePdf(QCMachineDailyRecordEntity qcMachineRecord,
			ServletOutputStream outputStream) {
		// TODO Auto-generated method stub
		
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
			Map<String, Object> root = new HashMap<String, Object>();
			PDFHtmlTemplateService.addDocumentHeaderLogo(qcMachineRecord, document, root);

			DecimalFormat df = new DecimalFormat("#0.00");

			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy");
			SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a");
			TimeZone timeZone=TimeZone.getTimeZone("IST");
			sdfDate.setTimeZone(timeZone);
			sdfTime.setTimeZone(timeZone);
			Date createdDate = qcMachineRecord.getCreatedDate();
			Date modifiedDate = qcMachineRecord.getModifiedDate();
			String createdDateStr = sdfDate.format(createdDate);
			String modifiedDateStr = sdfDate.format(modifiedDate);
			root.put("createdDateStr", createdDateStr);
			root.put("modifiedDateStr", modifiedDateStr);
			UserEntity createdBy = qcMachineRecord.getCreatedBy();
			root.put("createdBy", createdBy == null ? "" : createdBy.getFirstName() + " " + createdBy.getLastName());
			String machineName = qcMachineRecord.getMachineQc().getMachine().getMachineName();
			root.put("machineName", machineName);
			String qcname = qcMachineRecord.getMachineQc().getQcName();
			root.put("qcname", qcname);
			Date recordDate = qcMachineRecord.getRecordDate();
			String recordDateStr = sdfDate.format(recordDate);
			root.put("recordDateStr", recordDateStr);
			List<QCParameterRecord> parameterRecordList = qcMachineRecord.getParameterValueList().get(0).getParamRecordedValues();
			root.put("parameterRecordList", parameterRecordList);
			String tempTimeArrayStr1 = null;
			List<String> timeArrayStr = new ArrayList<String>();
			for (int i = 0; i < qcMachineRecord.getParameterValueList().size(); i++) {
				Date timeArray = qcMachineRecord.getParameterValueList().get(i).getTime();
				tempTimeArrayStr1 = sdfTime.format(timeArray);
				timeArrayStr.add(tempTimeArrayStr1);
			}
			root.put("timeArrayStr", timeArrayStr);
			
			List<QCTimeParameterValue> parameterValueList = qcMachineRecord.getParameterValueList();
			root.put("parameterValueList", parameterValueList);
			
			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("pdf_templates/qcmachine_record_tmpl.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
					Constants.DOCUMENT_DEFAULT_MAX_SIZE);
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