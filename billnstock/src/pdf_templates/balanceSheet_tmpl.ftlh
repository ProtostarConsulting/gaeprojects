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
	 width:50%;

}</style>

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
	<b>Balance Sheet</b>
	<br></br>
	<p align="center">${date}</p>
	
</div>
<p>
	<br></br>
	<br></br>
	<br></br>
</p>

	<table
			style="width: 100%; border: 1px solid #dddddd; border-color: black; float: left">
			
			
			<tr>
				<th colspan="2"style="border: 0px;">Equity</th>
				<td colspan="1"></td>
				<th colspan="2"style="border: 0px;">ASSETS</th>
				<td colspan="1"style="border: 0px;"></td>
			</tr>
			
			<tr style="border: 0px;">
			<td colspan="2" style="border: 0px;"></td>
			<td style="text-align: left;" >
			<#list  balanceSheetList as groupTypeObj>
					<#list  groupTypeObj.groupList as grp>
					<#if groupTypeObj.typeName=="EQUITY">
						
							<div style="text-align: right;border: 0px;">
							
							
							<span style="padding-right: 30%;border: 0px;">${grp.groupName}</span>
							&nbsp;&nbsp;
								<span style="text-align: right;border: 0px;">${grp.groupBalance}</span>
							</div>
						
						</#if>
						</#list>
						</#list> 
						</td>
						<td colspan="2"style="border: 0px;"></td>
			<td style="text-align: left;"style="border: 0px;">
			
			
			<#list  balanceSheetList as groupTypeObj>
					<#list  groupTypeObj.groupList as grp>
					<#if groupTypeObj.typeName=="ASSETS">
						
							
								<span style="padding-right: 30%;border: 0px;">${grp.groupName}</span>
								
								<span style="text-align: right;border: 0px;">${grp.groupBalance}</span>
									
									
						
						</#if>
						</#list>
						</#list>
		</td></tr>
			
			<tr>
				<th colspan="2" style="border: 0px;">LIABILITIES</th>
				<td colspan="1" ></td>
			</tr>
			
			
			<tr style="border: 0px;">
			<td colspan="2" style="border: 0px;"></td>
			<td style="text-align: left;" id="content">
			<#list  balanceSheetList as groupTypeObj>
					<#list  groupTypeObj.groupList as grp>
					<#if groupTypeObj.typeName=="LIABILITIES">
						
							
								<span style="padding-right: 30%;border: 0px;">${grp.groupName}</span>
								
								<span style="text-align: right;border: 0px;">${grp.groupBalance}</span>
						
						</#if>
						</#list>
						</#list> 
						</td>
						</tr>	
						
						
						
						
						
						<tr>
						<td colspan="2"style="border: 0px;">Current Period</td>
						<td colspan="1"style="border-bottom: medium; ">${nettProffitOrLoss}</td>
						<th colspan="2" style="border: 0px;">Profit & Loss A/c</th>
						<td colspan="1" ><b>${nettProffitOrLoss}</b></td>	
						
						</tr>
						<tr>
						<td colspan="2"style="border: 0px;"></td>
						<td colspan="1"style="border-bottom: medium; "></td>
						<td colspan="2" style="border: 0px;">Opening Balance</td>
						<td colspan="1" ><b></b></td>	
						
						
						
						</tr>
						
						
					
						<tr>
						<td colspan="2"style="border: 0px;"></td>
						<td colspan="1"style="border-bottom: medium; "></td>
						<td colspan="2" style="border: 0px;">Current Period</td>
						<td colspan="1" ><b>${nettProffitOrLoss}</b></td>	
						
						
						
						</tr>
					
						
								
	<tr style="border: 0px;">
				<td colspan="2"class="bottom" style="text-align: right; border: 0px;align: bottom"
					style="padding-right: 30%;border: 0px;"><b>Total Liabilities</b></td>
				<td style="align: right;">
				<#if totalLiabilities2??>
			<b>${totalLiabilities2} </b>
			</#if>
				</td>
				
				
				<td colspan="2"class="bottom" style="text-align: right; border: 0px;align: bottom"
					style="padding-right: 30%;border: 0px;"><b>Total Assets</b></td>
				<td style="align: right;">
				<#if totalAsset??>
			<b>${totalAsset} </b>
			</#if>
				</td>
				
				

	</tr>
	</table>
	
		
		
</html>