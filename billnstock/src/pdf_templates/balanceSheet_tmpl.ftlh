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
</div>
<p>
	<br></br>
	<br></br>
</p>

<table
			style="width: 100%;  hight: 100%; ">
			<tr>
			<th colspan="2"></th>
			
				<th style="text-align: center;">ProERP</th>
			</tr>
			<tr>
				<td colspan="3"></td>

			</tr>
			<tr>
				<td colspan="3"><u><b>Sources of Funds:</b></u></td>

			</tr>
			<#list balanceSheetList as groupTypeObj>	
			<tr>
				<td colspan="2">${groupTypeObj.typeName}</td>
				<td style="text-align: left;">
					<#list groupTypeObj.groupList as grp>
					<p style="padding-right: 65%;text-align: left;">${grp.groupName}</p> 
					   
					 <p style="text-align: right;">${grp.groupBalance}</p>
					
					
						</#list>	
					
					</td>			
			</tr>
				</#list>	

</table>





	
</html>