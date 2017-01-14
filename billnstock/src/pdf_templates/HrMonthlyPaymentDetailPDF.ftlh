<html>

<style type="text/css">
table {
	border-collapse: collapse;
}

tab2 {
	
}
</style>

<div align="right">  	
 <table style="width: 100%; hight: 100%;">
	<tr>
		<td style="width: 40%;"></td>
		<td style="width: 60%;"><div align="right"> <b>${businessName}</b> <br></br> ${businessAddress}</div></td>		
	</tr>
  </table>	
</div>
<p>
	<br></br>
</p>
<hr></hr>
<p>
	<br></br>
</p>
<div align="center">
	<b>Salary Slip for ${Month}</b>
</div>
<p>
	<br></br>
	<br></br>
</p>
<table style="width: 100%; hight: 100%;">
	<tr>
		<td style="width: 20%;">Employee No:</td>
		<td style="width: 40%;">${empNumber}</td>
		<td></td>
		<td>Monthly Gross:</td>
		<td>${ManthlyGross}</td>
	</tr>
	<tr>
		<td>Employee Name:</td>
		<td>${empName}</td>
		<td></td>
		<td align="left">Month Days:</td>
		<td align="left">${totalDays}</td>
	</tr>
	<tr>
		<td>Designation:</td>
		<td>${empDesignation}</td>
		<td align="left"></td>
		<td align="left">Payable Days:</td>
		<td align="left">${payableDays}</td>

	</tr>
	<tr>
		<td align="left">Department:</td>
		<td align="left">${department}</td>
		<td align="left"></td>
		<td align="left">Balance Leaves:</td>
		<td align="left">${leaveBalance}</td>
	</tr>
	<tr>
		<td align="left">Bank Name:</td>
		<td align="left">${bankName}</td>
		<td align="left"></td>
		<td align="left">PAN Number:</td>
		<td align="left">${panNumber}</td>
	</tr>
	<tr>
		<td align="left">Acc Number:</td>
		<td align="left">002701347556</td>
		<td align="left"></td>
		<td align="left">PF Number:</td>
		<td align="left">${pfNumber}</td>
	</tr>
</table>

<p>
	<br></br>
</p>

<table
	style="width: 100%; hight: 100%; border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">

	<tbody>

		<tr>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">Earnings</th>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px;"
				align="center">INR</th>
			<th
				style="width: 5%; border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px;">
			</th>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">Deductions</th>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">INR</th>
		</tr>


		<tr>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">Basic</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${Basic}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">Provident
				Fund(PF)</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${PTDeductionAmt}</td>
		</tr>
		<tr>

		</tr>


		<tr>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">HRA</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${HRA}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">Professional
				Tax</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${PTDeductionAmt}</td>
		</tr>

		<tr>

			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">Conveyance</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${Conveyance}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">Canteen</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${Canteen}</td>
		</tr>

		<tr>

			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">Medical</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${Medical}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">IT
				Dduction</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${ITDduction}</td>

		</tr>
		<tr>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">Education</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${Education}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">
				ESI Deduction</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right" align="right">${esiDeductionAmt}</td>

		</tr>
		<tr>

			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">Adhoc
				Allow</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${AdhocAllow}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">
				Other Deduction ${otherDeductionNote}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right" align="right">${OtherDeduction}</td>

		</tr>
		<tr>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">Special
				Allow</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${SpecialAllow}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
		</tr>

		<tr>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">Special
				Allow-2 ${specialAllow2Note}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${SpecialAllow2}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
		</tr>
		<tr>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">Overtime
				Amount ${overtimeNote}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right">${overtimeAmt}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
		</tr>
		<tr>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>			
		</tr>
		<tr>

			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"><b>Total
					Earnings</b></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right"><b>${totalEarnings}</b></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"><b>Total
					Deduction</b></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right"><b>${totalDeductions}</b></td>

		</tr>

		<tr>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"><b>Net
					Salary</b></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="right"><b>${NetSalary}</b></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"></td>
		</tr>
		<tr>
			<td colspan="5"
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">
				${NetSalaryInWords}</td>
		</tr>

	</tbody>
</table>
<p>
	<br></br>
</p>
<p>
	<br></br>
</p>
<p>
	<br></br>
</p>
<p>
	<br></br>
</p>
<p>
	<br></br>
</p>
<p>
	<br></br>
</p>
<p>
	<br></br>
</p>
<p>
	<br></br>
</p>
<p>
	<br></br>
</p>
<p>
	<br></br>
</p>
<p>
	*<small>This is computer generated statement and does not
		require any signature or stamp.</small>
</p>

</html>
