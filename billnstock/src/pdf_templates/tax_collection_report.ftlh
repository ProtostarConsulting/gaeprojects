<html>

<style type="text/css">
table {
	border-collapse: collapse;
	font-size:11px;
}

th {
	border: 1px solid black; 
	border-color: #cccccc; 
	border-spacing: 5px; 
	height: 20px; 
	padding: 7px;
}

td {
	border: 1px solid black; 
	border-color: #cccccc; 
	border-spacing: 5px; 
	height: 15px; 
	padding: 5px;
}

p {
	font-size:11px;
}
</style>

<div align="right">  	
 <table style="width: 100%; height: 100%; border: 0px;">
	<tr>
		<td style="width: 40%; border: 0px"></td>
		<td style="width: 60%; border: 0px"><div align="right"><span style="font-size:16px;"> <b>${businessName}</b></span> <br></br> ${businessAddress}</div></td>		
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
	<span style="font-size:20px;"><b>Tax Collection Report</b></span>
</div>
<p>
	<br></br>
</p>
<div align="left">
	<span style="font-size:12px;"><b>Tax Name:</b>&nbsp;${taxName}</span>
</div>
<div align="left">
	<span style="font-size:12px;"><b>From:</b>${fromDate?string["dd-MMM-yyyy"]} &nbsp;<b>To:</b>${toDate?string["dd-MMM-yyyy"]}</span>
</div>
<p>
	<br></br>
</p>
<#if taxItemList??>
<table
	style="width: 100%; height: 100%;">
	<tbody>

		<tr>
			<th align="center">Invoice No.</th>
			<th align="center">Invoice Paid Date</th>
			<th align="center">Tax Amount</th>
			
			
		</tr>
			<#list taxItemList as taxItem>
				
			<#if taxItem.taxAmt gt 0>	
			<tr>
			<td style="font-size:10px;" align="center">${taxItem.invoiceItemNumber}</td>
			<td style="font-size:10px;" align="center">${taxItem.invoiceDate?string["dd-MMM-yyyy"]}</td>
			<td style="font-size:10px;" align="right">${taxItem.taxAmt}</td>			
				
			</tr>
			</#if>
		</#list>
		</tbody>
		</table>
<#else> Tax Report List is empty.
</#if>
<table
	style="width: 100%; height: 100%; border-spacing: 5px; height: 30px; padding: 5px;">	
	<tbody>
		<tr>			
			<th
				style="width:100%;"
				align="right">Total:&nbsp;${taxTotal?string["0.00"]}</th>			
		</tr>
		<tr>
			<th
				style="width:100%;"
				align="right">${taxTotalInWords}</th>					
		</tr>
	</tbody>
</table>
</html>