<html>

<style type="text/css">
table {
	border-collapse: collapse;
}
th {
	border: 1px ; 
	border-color: #cccccc; 
	border-spacing: 5px; 
	height: 30px; 
	padding: 7px;
	 border-collapse: collapse;
}
td {
	border-right: 1px; 
	border-color: #cccccc; 
	border-spacing: 5px; 
	height: 30px; 
	padding: 5px;
	 border-collapse: collapse;
	 width:50%;
}
tr{
border-right: 1px solid black; 
	border-color: #cccccc; 

}
table tr td:nth-child(1) {
    border-right: 1px solid black;
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
			style="width: 100%; height: 100%; border: 1px solid #dddddd; border-color: black; float: left">
						
			<tr style="border-right: 1px solid black;  ">
				<th colspan="2"style="border: 0px;">Purchase Accounts</th>
				<td  colspan="1"><b>${totalPurches}</b></td>
				<#if totalSales gt 0>
				<th colspan="2"style="border: 0px;">Sales Accounts</th>
				<td colspan="1"style="border: 0px;"><b>${totalSales}</b></td>
				</#if>
				</tr>
				<tr style="border-right: 1px solid black;  ">
				<#if totalClosingStockBalance gt 0>
				<th colspan="2"style="border: 0px;"></th>
				<td colspan="1"style="border: 0px;"></td>
				<th colspan="2"style="border: 0px;">Closing Stock</th>
				<td colspan="1"style="border: 0px;"><b>${totalClosingStockBalance}</b></td>
				
				</#if>
			</tr>
			
		<tr style="border: 0px;">
			<td colspan="2" style="border: 0px;"></td>
			<td style="text-align: left;" >
			<#list  ProfitAndLossAcList as groupTypeObj>
					<#list  groupTypeObj.groupList as grp>
					<#if groupTypeObj.typeName=="EXPENSES">
						
												
							<span style="padding-right: 30%;border: 0px;">${grp.groupName}</span>
							&nbsp;&nbsp;
								<span style="text-align: right;border: 0px;">${grp.groupBalance}</span>
							
						
						</#if>
						</#list>
						</#list> 
						</td>
						<td colspan="2"style="border: 0px;"></td>
			<td style="text-align: left;"style="border: 0px;">
			
			<#list  ProfitAndLossAcList as groupTypeObj>
					<#list  groupTypeObj.groupList as grp>
					<#if groupTypeObj.typeName=="INCOME">
						
							
							
								
						</#if>
						</#list>
						</#list>
		</td>
			</tr>
			
			
			
			
			
			
			<tr>
				<th colspan="2"style="border: 0px;" >Gross Profit c/o</th>
				<td colspan="1"><b>${totalGrossProfit}</b></td>
				<th colspan="2"style="border: 0px;" ></th>
				<td colspan="1"><b></b></td>
			</tr>
			<tr>
				<th colspan="2"style="border: 0px;" >&nbsp;&nbsp;</th>
				<td style="border-bottom: double; border-top: double;">${totalLeft}</td>
				
				<th colspan="2"style="border: 0px;" >&nbsp;&nbsp;</th>
				<td style="border-bottom: double; border-top: double;">${totalRight}</td>
			</tr>
			
			<tr>
				<th colspan="2"style="border: 0px;" >Indirect Expence</th>
				<td colspan="1" ><b>${totalIndirectExpences}</b></td>
				<th colspan="2" style="border: 0px;">Gross Profit c/o</th>
				<td colspan="1"><b>${totalGrossProfit}</b></td>
			</tr>


<tr style="border: 0px;">
			<td colspan="2" style="border: 0px;"></td>
			<td style="text-align: left;" >
			<#list  ProfitAndLossAcList as groupTypeObj>
					<#list  groupTypeObj.groupList as grp>
					<#if groupTypeObj.typeName=="OTHEREXPENCES">
						
							<div style="text-align: right;border: 0px;">
							
							
							<span style="padding-right: 30%;border: 0px;">${grp.groupName}</span>
							&nbsp;&nbsp;
								<span style="text-align: right;border: 0px;">${grp.groupBalance}</span>
							</div>
						
						</#if>
						</#list>
						</#list> 
						</td>
		</tr>
<tr>
<th colspan="2"style="border: 0px;"><i>Nett Profit</i></th>
<td colspan="1" ><b>${nettProfit}</b></td>
</tr>

<tr>
<th colspan="2"style=" border: 0px;"><b></b></th>
<td colspan="1" ><b></b></td>

<th style="right;border: 0px;"><b></b></th>
<td colspan="1" style="border: 0px;"><b></b></td>
</tr>	<tr>
				<th colspan="2"style="border: 0px;" >Total</th>
				<td colspan="1"><b>${totalGrossProfit}</b></td>
				<th colspan="2"style="border: 0px;" >Total</th>
				<td colspan="1"><b>${totalGrossProfit}</b></td>
				
				
			</tr>
</table>
</html>