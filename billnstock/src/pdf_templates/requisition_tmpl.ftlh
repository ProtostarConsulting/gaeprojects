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

</style>

<div align="right">  	
 <table style="width: 100%; height: 100%; border: 0px">
	<tr>
		<td style="width: 40%; border: 0px"></td>
		<td style="width: 60%; border: 0px"><div align="right"><span style="font-size:16px;"><b>${businessName}</b></span> <br></br> ${businessAddress}</div></td>		
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
<div align="center"><span style="font-size:20px;"><b>Requisition</b></span>
	</div>
<p>
	<br></br>
</p>
<div class="container" style="width: 100%;">						 
<table style="width: 100%; height: 100%;">
		<tbody>
				<!-- Contains top two tables -->
				<tr>
				    <!-- Left side top table --> 
					<td style="width: 40%; border: 0px; vertical-align:top; padding-top: 0px;">										
						<table style="width: 100%; height: 100%;">
							<tbody>
								<tr>
									<td align="left" style="width: 30%; border: 0px;">
										<b>Created Date:</b>
									</td>
									<td align="left" style="border: 0px;">
										${createdDateStr}
									</td>
								</tr>
								<tr>
									<td align="left" style="border: 0px;">
										<b>Created By:</b>
									</td>
									<td align="left" style="border: 0px;">
										${createdBy}
									</td>
								</tr>
								<#if onBehalfOf??>
								<tr>
									<td align="left" style="border: 0px;">
										<b>On behalf of, if any:</b>
									</td>
									<td align="left" style="border: 0px;">
										${onBehalfOf}
									</td>
								</tr>
								<#else>	
								</#if>	
							 </tbody>
						 </table>					
					</td>
					<!-- Right side top table --> 
					<td style="width: 40%; border: 0px; vertical-align:top; padding-top: 0px;">										
						<table style="width: 100%; height: 100%;">
							<tbody>
								<tr>
									<td align="right" style="border: 0px;">
										<b>Requisition No:</b>
									</td>
									<td align="left" style="border: 0px;">
										${itemNumber}
									</td>
								</tr>								
								<tr>
									<td align="right" style="border: 0px;">
										<b>Document Status:</b>
									</td>
									<td align="left" style="border: 0px;">
										${docStatus}
									</td>
								</tr>								
								<tr>
									<td align="right" style="border: 0px;">
										<b>Approved Date:</b>
									</td>
									<td align="left" style="border: 0px;">
										${modifiedDateStr}
									</td>
								</tr>
								<tr>
									<td align="right" style="border: 0px;">
										<b>Approved By:</b>
									</td>
									<td align="left" style="border: 0px;">
										${approvedBy}
									</td>
								</tr>														
							 </tbody>
						</table>					
					</td>					
				</tr>
				<!-- Contains a row across two tables -->
				<#if docNotes??>
					<tr align="left" style="border: 0px;">
						<td colspan="2" style="border: 0px; padding: 7px;">
							<b>Document Note :</b><br></br>
							${docNotes}
					    </td>
					</tr>
				<#else>	
				</#if>								
		</tbody>
</table>
</div>
<#if serviceItemList??>
<p>
	<br></br>
    <br></br>
</p>
<table
	style="width: 100%; height: 100%; ">
	<tbody>
		<tr>
			<th
				style=""
				align="left">Line Item Name</th>
			<th
				style="width:10%;"
				align="center">Qty.</th>
		</tr>
		<#list serviceItemList as lineItem>
		  <tr>
			<td style="font-size:10px;" align="left">${lineItem.itemName}</td>
			<td style="font-size:10px;" align="center">${lineItem.qty}</td>
		  </tr>
		</#list>	
		</tbody>
</table>
<#else>
  No service items
</#if>		
</html>