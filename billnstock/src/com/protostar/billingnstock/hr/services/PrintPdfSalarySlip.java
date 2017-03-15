package com.protostar.billingnstock.hr.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.protostar.billingnstock.hr.entities.MonthlyPaymentDetailEntity;
import com.protostar.billingnstock.hr.entities.SalStruct;
import com.protostar.billingnstock.user.entities.EmpDepartment;
import com.protostar.billingnstock.user.entities.UserEntity;
import com.protostar.billnstock.until.data.BankDetail;
import com.protostar.billnstock.until.data.EmployeeDetail;
import com.protostar.billnstock.until.data.NumberToRupees;
import com.protostar.billnstock.until.data.PDFHtmlTemplateService;

import freemarker.template.Template;

/**
 * Servlet implementation class PrintPdfSalarySlip
 */
public class PrintPdfSalarySlip extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PrintPdfSalarySlip() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Long id = Long.parseLong(request.getParameter("id"));
		Long bid = Long.parseLong(request.getParameter("bid"));
		String entity = request.getParameter("entityname");
		String month = request.getParameter("month");

		response.setContentType("application/PDF");
		ServletOutputStream outputStream = response.getOutputStream();

		Date date = new Date();
		String DATE_FORMAT = "dd/MMM/yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		String fileNameAppend = "SalarySlip_" + month + "_" + sdf.format(date);
		response.setHeader("Content-disposition", "inline; filename='ProERP_" + fileNameAppend + ".pdf'");

		HrService hrService = new HrService();
		MonthlyPaymentDetailEntity monthlyPaymentDetailEntity = null;

		if (entity.toString().equals("MonthlyPaymentDetailEntity"))

		{
			monthlyPaymentDetailEntity = hrService.getMonthlyPaymentByID(bid, month, id);

			this.generatePdf(monthlyPaymentDetailEntity, outputStream);
		}

	}

	private void generatePdf(MonthlyPaymentDetailEntity mtlyPayObj, ServletOutputStream outputStream) {

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
			PDFHtmlTemplateService.addDocumentHeaderLogo(mtlyPayObj, document, root);

			DecimalFormat df = new DecimalFormat("#0.00");

			SalStruct salStruct = mtlyPayObj.getSalStruct();
			float basicAmt = salStruct.getMonthlyBasic() / mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float hraAmt = salStruct.getMonthlyHra() / mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float conAmt = salStruct.getMonthlyConvence() / mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float medAmt = salStruct.getMonthlyMedical() / mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float eduAmt = salStruct.getMonthlyEducation() / mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float adhAmt = salStruct.getMonthlyAdhocAllow() / mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();
			float splAmt = salStruct.getMonthlySpecialAllow() / mtlyPayObj.getTotalDays() * mtlyPayObj.getPayableDays();

			float specialAllow2 = mtlyPayObj.getSpecialAllow();
			float overtimeAmt = mtlyPayObj.getOvertimeAmt();
			float totalEarnings = basicAmt + hraAmt + conAmt + medAmt + eduAmt + adhAmt + splAmt + specialAllow2;

			float pfDeductionAmt = mtlyPayObj.getPfDeductionAmt();
			float ptDeductionAmt = mtlyPayObj.getPtDeductionAmt();
			float canteenDeductionAmt = mtlyPayObj.getCanteenDeductionAmt();
			float itDeductionAmt = mtlyPayObj.getItDeductionAmt();
			float esiDeductionAmt = mtlyPayObj.getEsiDeductionAmt();
			float otherDeductionAmt = mtlyPayObj.getOtherDeductionAmt();
			float totalDeductions = pfDeductionAmt + ptDeductionAmt + canteenDeductionAmt + itDeductionAmt
					+ esiDeductionAmt + otherDeductionAmt;

			UserEntity user = mtlyPayObj.getleaveDetailEntity().getUser();

			EmployeeDetail employeeDetail = user.getEmployeeDetail();
			// Header Col1
			root.put("empNumber", "" + employeeDetail.getEmpId());
			String empName = user.getFirstName() + " " + user.getLastName();
			root.put("empName", empName);
			EmpDepartment department = employeeDetail.getDepartment();
			root.put("department", department == null ? "" : "" + department.getName());
			root.put("empDesignation", "" + employeeDetail.getDesignation());
			BankDetail bankDetail = employeeDetail.getBankDetail();
			if (bankDetail == null)
				bankDetail = new BankDetail();
			root.put("bankName", "" + bankDetail.getBankName());
			root.put("bankAccNumber", "" + bankDetail.getBankAccountNo());

			root.put("panNumber", "" + employeeDetail.getPanCardNumber());
			root.put("pfNumber", "" + employeeDetail.getEpfNumber());

			// Header Col2
			root.put("ManthlyGross", "Rs. " + df.format(mtlyPayObj.getMonthlyGrossSalary()));
			/*
			 * root.put("MonthlySalary",
			 * df.format(mtlyPayObj.getMonthlyGrossSalary()));
			 */
			root.put("totalDays", mtlyPayObj.getTotalDays());
			root.put("payableDays", mtlyPayObj.getPayableDays());
			root.put("overtimeAmt", overtimeAmt > 0 ? df.format(overtimeAmt) : "");
			root.put("overtimeNote",
					overtimeAmt > 0 ? "(" + mtlyPayObj.getleaveDetailEntity().getOvertimeDays() + " days)" : "");
			root.put("leaveBalance", mtlyPayObj.getleaveDetailEntity().getNextOpeningBalance());

			// Earnings Col
			root.put("Basic", df.format(basicAmt));
			root.put("Month", mtlyPayObj.getCurrentMonth());
			root.put("HRA", df.format(hraAmt));
			root.put("Conveyance", df.format(conAmt));
			root.put("Medical", df.format(medAmt));
			root.put("Education", df.format(eduAmt));
			root.put("AdhocAllow", adhAmt > 0 ? df.format(adhAmt) : "");
			root.put("SpecialAllow", splAmt > 0 ? df.format(splAmt) : "");
			root.put("SpecialAllow2", specialAllow2 > 0 ? df.format(specialAllow2) : "");
			root.put("specialAllow2Note",
					mtlyPayObj.getSpecialAllowNote() == null ? "" : "(" + mtlyPayObj.getSpecialAllowNote() + ")");
			root.put("totalEarnings", df.format(totalEarnings));

			// Deductions Col
			root.put("PFDeductionAmt", df.format(pfDeductionAmt));
			root.put("PTDeductionAmt", df.format(ptDeductionAmt));
			root.put("ITDduction", df.format(itDeductionAmt));
			root.put("Canteen", df.format(canteenDeductionAmt));
			root.put("esiDeductionAmt", df.format(esiDeductionAmt));
			root.put("OtherDeduction", df.format(otherDeductionAmt));
			root.put("otherDeductionNote", mtlyPayObj.getOtherDeductionAmtNote() == null ? ""
					: "(" + mtlyPayObj.getOtherDeductionAmtNote() + ")");
			root.put("totalDeductions", df.format(totalDeductions));

			NumberToRupees numberToRupees = new NumberToRupees(Math.round(mtlyPayObj.getNetSalaryAmt()));
			String netInWords = numberToRupees.getAmountInWords();
			root.put("NetSalary", df.format(Math.round(mtlyPayObj.getNetSalaryAmt())));
			root.put("NetSalaryInWords", netInWords);

			Template temp = PDFHtmlTemplateService.getConfiguration()
					.getTemplate("pdf_templates/HrMonthlyPaymentDetailPDF.ftlh");

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(5000);
			Writer out = new PrintWriter(byteArrayOutputStream);
			temp.process(root, out);

			String pdfXMLContent = byteArrayOutputStream.toString();

			worker.parseXHtml(writer, document, new StringReader(pdfXMLContent));
			PDFHtmlTemplateService.addDocumentFooter(mtlyPayObj, writer);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
