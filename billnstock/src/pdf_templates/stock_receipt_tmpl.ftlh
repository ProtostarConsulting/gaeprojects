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
	<b>Stock Receipt</b>
	
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
					<td style="width: 60%; border: 0px;">
						<table style="width: 100%; height: 100%;">
							<tbody>
								<tr>
									<td align="left" style="border: 0px;"><b>Warehoue Name and Address:</b><br></br>
										${warehouse} <br></br>
										${warehouseAddress}
									</td>
								</tr>
								<tr>
									<td align="left" style="border: 0px;"><b>Supplier Name and Address:</b><br></br>
										${supplier} <br></br>
										${supplierAddress}
									</td>
								</tr>
							 </tbody>
						 </table>					
					</td>
					<!-- Right side top table --> 
					<td style="width: 40%; border: 0px; vertical-align:top; padding-top: 0px;">										
						<table style="width: 100%; height: 100%;">
							<tbody>
								<tr>
									<td align="right" style="border: 0px;">
										<b>Receipt No:</b>
									</td>
									<td align="left" style="border: 0px;">
										${receiptNo}
									</td>
								</tr>
								<tr>
								<td align="right" style="border: 0px;">
										<b>Status:</b>
									</td>
									<td align="left" style="border: 0px;">
										${docStatus}
									</td>
								</tr>
								<tr>
									<td align="right" style="border: 0px;">
										<b>Created Date:</b>
									</td>
									<td align="left" style="border: 0px;">
										${createdDate}
									</td>
								</tr>
								<tr>
								<td align="right" style="border: 0px;">
										<b>Created By:</b>
									</td>
									<td align="left" style="border: 0px;">
										${createdBy}
									</td>
								</tr>
								<tr>
								<td align="right" style="border: 0px;">
										<#if docStatus = 'REJECTED'> 
											<b>Rejected By:</b> 
										<#else>
											<b>Approved By:</b>									
										</#if>
									</td>
									<td align="left" style="border: 0px;">
										${approvedBy}
									</td>
								</tr>
								<tr>
									<td align="right" style="border: 0px;">
										<#if docStatus = 'REJECTED'> 
											<b>Rejected Date:</b> 
										<#else>
										<b>Approved Date:</b>
										</#if>
									</td>
									<td align="left" style="border: 0px;">
										${modifiedDate}
									</td>
								</tr>
								<#if Discount??>
								<tr>
									<td align="right" style="border: 0px;">
										<b>Discount:</b>
									</td>
									<td align="left" style="border: 0px;">
										${Discount}
									</td>
								</tr>
								<#else>									
								</#if>								
							 </tbody>
						</table>					
					</td>					
				</tr>
				<!-- Contains a row across two tables -->
				<#if receiptNote??>
					<tr align="left" style="border: 0px;">
						<td colspan="2" style="border: 0px; padding: 7px;">
							<b>Note :</b><br></br>
							<pre>${receiptNote}</pre>
					    </td>
					</tr>
				<#else>	
				</#if>								
		</tbody>
</table>
</div>
<p>
	<br></br>
	<br></br>
	</p>
		<#if productItemList??>
	<table 
	style="width: 100%; height: 100%; ">
	<caption align="left" style="border-spacing: 5px;padding: 3px;"><b>Products: </b></caption>
	<tbody>

		<tr>
			<th align="center">Product Name </th>
			<th
				style=""
				align="center">Qty.</th>
			<th
				style="" 
				align="center">Price
			</th>
			<th
				style=""
				align="center">Sub Total</th>
		</tr>
		
		<#list productItemList as lineItem>
		    <#assign productSubTotal = lineItem.qty * lineItem.price>	
			<tr>
			<td style="font-size:10px;" align="center">${lineItem.stockItem.stockItemType.itemName}</td>
			<td style="font-size:10px;" align="center">${lineItem.qty}</td>
			<td style="font-size:10px;" align="right">${lineItem.price?string["0.00"]}</td>			
			<td style="font-size:10px;" align="right">${productSubTotal?string["0.00"]}</td>
		</tr>
		</#list>	
		</tbody>
		</table>		
<#else>
  <#assign productSubTotal=0>
</#if>		
<#if serviceItemList??>
<p>
	<br></br>
	<br></br>
</p>
		<table
	style="width: 100%; height: 100%; ">
	<caption align="left" style="border-spacing: 5px;padding: 3px;"><b>Other Costs: </b></caption>
	<tbody>

		<tr>
			<th
				style=""
				align="center">Service Name</th>
			<th
				style=""
				align="center">Qty.</th>
			<th
				style="" align="center">Cost
			</th>
			<th
				style=""
				align="center">Sub Total</th>
		</tr>
		<#list serviceItemList as lineItem>
		    <#assign serviceSubTotal = lineItem.qty * lineItem.cost>	
			<tr>
			<td style="font-size:10px;" align="center">${lineItem.itemName}</td>
			<td style="font-size:10px;" align="center">${lineItem.qty}</td>
			<td style="font-size:10px;" align="right">${lineItem.cost?string["0.00"]}</td>			
			<td style="font-size:10px;" align="right">${serviceSubTotal?string["0.00"]}</td>
		</tr>
		</#list>	
		</tbody>
		</table>
	<#else>
           <#assign serviceSubTotal=0>
</#if>
<table
	style="width: 100%; height: 100%; border-spacing: 5px; height: 30px; padding: 5px;">	
	<tbody>
		<tr>			
			<th
				style="width:100%;"
				align="right">Final Total:&nbsp;${finalTotal?string["0.00"]}</th>			
		</tr>
		<tr>
			<th
				style="width:100%;"
				align="right">${finalTotalInWords}</th>					
		</tr>
	</tbody>
</table>		
</html>