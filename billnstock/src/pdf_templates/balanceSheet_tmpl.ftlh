<html>

<style type="text/css">
table {
	
	 border-color:black;
	  border: 1px solid;
}</style>

<div align="center">
 <b>${buisinessName}</b> <br></br> ${buisinessAddress}
 
</div>

<hr></hr>
<p>
	<br></br>
</p>

<div align="center"  >
	<b>BalanceSheet</b>
	<br></br>
	<p align="center">as at ${date}</p>
</div>
<p>
	<br></br>
	<br></br>
</p>

	<table
			style="width: 50%; border: 1px solid #dddddd; border-color: black; float: left">
			
			
			<tr>
				<th colspan="2">Equity</th>
				<td colspan="1"></td>
			</tr>
			<tr>
			<td colspan="2"></td>
			<td style="text-align: left;" id="content">
			<#list  balanceSheetList as groupTypeObj>
					<#list  groupTypeObj.groupList as grp>
					<#if groupTypeObj.typeName=="EQUITY">
						
							<div style="text-align: right;">
							
								<span style="padding-right: 30%;">${grp.groupName}</span>
								 <#if grp.groupBalance < 0>(</#if><span
									style="text-align: right;">${grp.groupBalance}</span>
									<#if grp.groupBalance < 0>)</#if>
									
							</div>
						
						</#if>
						</#list>
						</#list> 
						</td>
			</tr>
			<tr>
				<th colspan="2">LIABILITIES</th>
				<td colspan="1"></td>
			</tr>
			
			
			<tr>
			<td colspan="2"></td>
			<td style="text-align: left;" id="content">
			<#list  balanceSheetList as groupTypeObj>
					<#list  groupTypeObj.groupList as grp>
					<#if groupTypeObj.typeName=="LIABILITIES">
						
							
								<span style="padding-right: 30%;">${grp.groupName}</span>
								 <#if grp.groupBalance < 0>(</#if><span
									style="text-align: right;">${grp.groupBalance}</span>
									<#if grp.groupBalance < 0>)</#if>
									
						
						</#if>
						</#list>
						</#list> 
						</td>
						</tr>			
			<tr>
			<#if nettProffitOrLoss??>
				<td><b>Profit & Loss A/c </b></td>
				<td><b>${nettProffitOrLoss}</b></td>
				</#if>
			</tr>
							
			<tr>
			<td>Opening Balance </td>
			<td>&nbsp;&nbsp;</td>
			
			</tr>
			<tr>
			<#if nettProffitOrLoss??>
			<td>Current Period </td>
			<td style="border-bottom: medium; ">${nettProffitOrLoss}</td>
			</#if>
			</tr><tr>
				<td class="bottom" style="text-align: right; align: bottom"
					style="padding-right: 30%;"><b>Total Liabilities</b></td>
				<td style="align: right;">
				<#if totalLiabilities2??>
			<b>${totalLiabilities2} </b>
			</#if>
				</td>

	</tr>
	</table>
	<table
			style="width: 50%; border: 1px solid #dddddd; border-color: black; float: left">
			
			
			<tr>
				<th colspan="2">ASSETS</th>
				<th style="text-align: center;"><br></br> <span> </span></th>
			</tr>
	
	
	
	<tr>
			<td colspan="2"></td>
			<td style="text-align: left;">
			<#list  balanceSheetList as groupTypeObj>
					<#list  groupTypeObj.groupList as grp>
					<#if groupTypeObj.typeName=="ASSETS">
						
							
								<span style="padding-right: 30%;">${grp.groupName}</span>
								<span style="text-align: right;">${grp.groupBalance}</span>
									
									
						
						</#if>
						</#list>
						</#list>
		</td>
</tr>	
<tr>
				<th colspan="2"></th>
				<td colspan="1"></td>
			</tr>	
			<tr>
			<td colspan="2"></td>
			<td style="text-align: left;" id="content">
			<#list  balanceSheetList as groupTypeObj>
					<#list  groupTypeObj.groupList as grp>
					<#if groupTypeObj.typeName=="LIABILITIES">
						
							
								<span style="padding-right: 30%;">${grp.groupName}</span>
								 <#if grp.groupBalance < 0>(</#if><span
									style="text-align: right;">${grp.groupBalance}</span>
									<#if grp.groupBalance < 0>)</#if>
									
						
						</#if>
						</#list>
						</#list>
						</td>
</tr>		
	<tr>
	<#if nettProffitOrLoss??>
	
				<td><b>Profit & Loss A/c </b></td>
				<td><b>${nettProffitOrLoss}</b></td>
	</#if>
	</tr>
	<tr>
		<#if  nettProffitOrLoss??>
			<td>Opening Balance </td>
			<td>&nbsp;&nbsp;</td>
		</#if>
	</tr>
	<tr>
			<td>Current Period </td>
			<td style="border-bottom: medium; ">
			<#if nettProffitOrLoss??>
			${nettProffitOrLoss}
			</#if>
			</td>
	</tr>
			
	<tr>
			<td class="bottom" style="text-align: right; align: bottom"
				style="padding-right: 30%;"><b>Total Asset</b>
				
			</td>
			<td style="align: right;">
			<#if totalAsset??>
			<b>${totalAsset} </b>
			</#if>
			</td>

	</tr>
	
	
	</table>
		
		
		
</html>