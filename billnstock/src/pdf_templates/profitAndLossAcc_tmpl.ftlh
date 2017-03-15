<html>

<style type="text/css">
table {
	font-family: arial, sans-serif;
	border-collapse: collapse;
	width: 100%;
}

td, th { /* //border: 1px solid #dddddd; */
	text-align: left;
	padding: 8px;
}
td{
border: 1px solid black;

}

</style>

<div align="right">  	
 <table style="width: 100%; height: 100%; border: 0px">
	<tr>
		<td style="width: 40%; border: 0px"></td>
		<td style="width: 60%; border: 0px"><div align="right"> <b>${businessName}</b> <br></br> ${businessAddress}</div></td>		
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
	<b>Profit And Loss</b>
	<br></br>
	<p align="center">${date}</p>
	
</div>
<p>
	<br></br>
	<br></br>
	<br></br>
</p>
	<table cellspacing="0"
			style="width: 100%; height: 100%; border: 1px solid #dddddd; border-color: black; float: left"><tr>
			<th colspan="3">Operating Revenue</th>
			</tr>
			<tr>
			<#list  totalSalesList as sales >
			<td colspan="2">
			 ${sales.groupName}</td>
			<td>${sales.groupBalance} </td>
			</#list>
			</tr>
<tr>
<td colspan="2"><b>Total Operating Revenue</b></td>
<td> <b>${totalOperatingRevenue} </b></td>
</tr>

<tr>
<th colspan="3">Operating Expenses</th>
</tr><tr>
<#list totalPurchaseList as  purchase >
			<td colspan="2"> ${purchase.groupName}</td>
			<td>${purchase.groupBalance} </td>
			</#list>
			</tr>
			<tr>
<td colspan="2"><b>Gross Profit</b></td>
<td> <b>${totalGrossProfit}</b> </td></tr>
<tr>
<th colspan="3">Overhead</th>
</tr>

<tr>
<#list  totalPaymentList as payment >
<td colspan="2"> ${payment.groupName}</td>
<td>${payment.groupBalance} </td>
</#list>
</tr>
<tr>
<td colspan="2"><b>Total Overhead</b></td>
<td> <b>${totalOverhead}</b> </td></tr>

<tr>
<td colspan="2"><b>Operating Income</b></td>
<td> <b>${operatingIncome}</b> </td></tr>
		
			
</table>
</html>