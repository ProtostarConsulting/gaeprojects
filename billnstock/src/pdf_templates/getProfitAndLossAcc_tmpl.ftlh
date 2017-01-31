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
	<b>Profit And Loss</b>
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
				<th colspan="2">Purchase Accounts</th>
				<td colspan="1"><b>${totalPurches}</b></td>
			</tr>
			
			
		<tr>
		<td style="text-align: left;" id="content">
				
					<#list  ProfitAndLossAcList as groupTypeObj>
					<#if groupTypeObj.typeName=="EXPENSES">
							
						
						</#if>
						</#list>
				</td>
		</tr>
		

			<tr>
				<th colspan="2" >Gross Profit c/o</th>
				<td colspan="1"><b>${totalGrossProfit}</b></td>
			</tr>
			<tr>
				<th colspan="2">&nbsp;&nbsp;</th>

				<td style="border-bottom: double; border-top: double;">${totalLeft}</td>
			</tr>
			<tr>
				<th colspan="2">Indirect Expence</th>
				<td colspan="1"><b>${totalIndirectExpences}</b></td>
			</tr>


<tr>
		<td style="text-align: left;" id="content">
				
					<#list  ProfitAndLossAcList as groupTypeObj>
					<#list  groupTypeObj.groupList as grp>
					<#if groupTypeObj.typeName=="OTHEREXPENCES">
						
															
								<span style="padding-right: 30%;">${grp.groupName}</span>
								 ${grp.groupBalance}
									
										</#if>
										</#list>
						</#list>
				</td>
		</tr>
<tr>
<th colspan="2"><i>Nett Profit</i></th>
<td colspan="1"><b>${nettProfit}</b></td>
</tr>
			<tr style="border-bottom: double; border-top: double;">
				<td style="padding-right: 30%; text-align: right;"><b>Total
				</b></td>
				<td style="text-align: right;"><b>${totalGrossProfit} </b></td>
			</tr>



		</table>
		
	<table
			style="width: 50%; border: 1px solid #dddddd; border-color: black; float: left">
	
	<tr>
				<th colspan="2">Sales Accounts</th>
				<td colspan="1"><b>${totalSales}</b></td>
			</tr>
	
	<tr>
		<td style="text-align: left;" id="content">
				
					<#list  ProfitAndLossAcList as groupTypeObj>
					<#list  groupTypeObj.groupList as grp>
					<#if groupTypeObj.typeName=="INCOME">
						
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
				<th colspan="2">&nbsp;&nbsp;</th>
				<td colspan="1">&nbsp;&nbsp;</td>
			</tr>
			<tr>
				<th colspan="2">&nbsp;&nbsp;</th>

				<td style="border-bottom: double; border-top: double;">${totalLeft}</td>
			</tr>

			<tr>
				<th colspan="2">Gross Profit c/o</th>
				<td colspan="1"><b>${totalGrossProfit}</b></td>
			</tr>
	
		</table>
		
		
</html>