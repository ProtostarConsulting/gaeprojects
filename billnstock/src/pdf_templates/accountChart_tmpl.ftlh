<html>

<style type="text/css">
table {
	
	 border-color:#0099ff;
	  border: 1px solid black;
}</style>

<div align="center">
 <b>${buisinessName}</b> <br></br> ${buisinessAddress}
</div>

<hr></hr>
<p>
	<br></br>
</p>

<div align="center">
	<b>Account Chart</b>
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
			<#list accountChart as groupTypeObj>	
			<tr>
				<td colspan="2">${groupTypeObj.typeName}</td>
				<td style="text-align: left;">
					<#list groupTypeObj.groupList as grp>
					<p style="padding-right: 65%;text-align: left;">${grp.groupName}</p> 
					   <#list  grp.accInfoList as acc>
					   <p style="text-align: right;"><b>${acc.accName}</b>  	&nbsp;	&nbsp;	&nbsp; 	${acc.accBalance}</p>
		
					
					</#list>	
						</#list>	
					
					</td>			
			</tr>
				</#list>	

</table>





	
</html>