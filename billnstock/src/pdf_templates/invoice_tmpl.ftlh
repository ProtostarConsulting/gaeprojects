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
	<span style="font-size:20px;"><b>Invoice</b></span>
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
									<td align="left" style="border: 0px;">
										<b>Invoice No:</b>	${invoiceNumber}
									</td>
								</tr>
								<tr>
									<td align="left" style="border: 0px;">
										<b>Invoice Date:</b>${createdDate}
									</td>
								</tr>
								<tr>
									<td align="left" style="border: 0px;"><b>Customer Name and Address:</b><br></br>
										${CustomerName} <br></br>
										${CustomerAddress}
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
										<b>Status:</b>
									</td>
									<td align="left" style="border: 0px;">
										${docuStatus}
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
										<b>Approved By:</b>
									</td>
									<td align="left" style="border: 0px;">
										${approvedBy}
									</td>
								</tr>
								<tr>
									<td align="right" style="border: 0px;">
										<b>Approved Date:</b>
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
				<#if noteToCustomer??>
					<tr align="left" style="border: 0px;">
						<td colspan="2" style="border: 0px; padding: 7px;">
							<b>Note To Customer:</b><br></br>
							${noteToCustomer}
					    </td>
					</tr>
				<#else>	
				</#if>								
		</tbody>
</table>
</div>

	<p>
	<br> </br>
	</p>
	<#if serviceItemList??>
		<table
	style="width: 100%; height: 100%;">
	<caption align="left" style="border-spacing: 5px; padding: 3px;"><b>Services: </b></caption>
	<tbody>

		<tr>
			<th align="center">Service Desc.</th>
			<th align="center">Qty</th>
			<th align="center">Price</th>
			<th align="center">Sub Total</th>
			<th align="center">Tax Type</th>
			<th align="center">Tax </th>
			<th align="center">Total</th>
		</tr>
		<#list serviceItemList as lineItem>
		    <#assign subTotal = lineItem.qty * lineItem.price>
			<#if serviceTax??>
				<#assign taxAmt = serviceTax.taxPercenatge/100 * subTotal>
				<#assign taxCodeName = serviceTax.taxCodeName + "(" + serviceTax.taxPercenatge + "%)">
		    <#else>
  				<#assign taxAmt = 0>
  				<#assign taxCodeName = "NA">
			</#if>	
			<tr>
			<td style="font-size:10px;" align="center">${lineItem.itemName}</td>
			<td style="font-size:10px;" align="center">${lineItem.qty}</td>
			<td style="font-size:10px;" align="right">${lineItem.price?string["0.00"]}</td>			
			<td style="font-size:10px;" align="right">${subTotal?string["0.00"]}</td>
			<td style="font-size:10px;" align="center">${taxCodeName}</td>
			<td style="font-size:10px;" align="right">${taxAmt?string["0.00"]}</td>
			<td style="font-size:10px;" align="right">${(subTotal + taxAmt)?string["0.00"]}</td>
		</tr>
		</#list>	
		</tbody>
		</table>
	<#else>
  No service items
</#if>
	
	<#if productItemList??>
	<p>
	<br></br>
	<br></br>
	</p>	
  	<table 
	style="width: 100%; height: 100%;">
	<caption align="left" style="border-spacing: 5px; padding: 3px;"><b>Products: </b></caption>
	<tbody>
		<tr>
			<th align="center">Product Name </th>
			<th align="center">Qty </th>
			<th align="center">Price </th>
			<th align="center">Sub Total</th>
			<th align="center">Tax Type </th>
			<th align="center">Tax </th>
			<th align="center">Total </th>
		</tr>
		<#list productItemList as lineItem>
		    <#assign subTotal = lineItem.qty * lineItem.price>
			<#if productTax??>
				<#assign taxAmt = productTax.taxPercenatge/100 * subTotal>
				<#assign taxCodeName = productTax.taxCodeName + "(" +productTax.taxPercenatge + "%)">
		    <#else>
  				<#assign taxAmt = 0>
  				<#assign taxCodeName = "NA">
			</#if>	
			<tr>
			<td style="font-size:10px;" align="left">${lineItem.stockItem.stockItemType.itemName}</td>
			<td style="font-size:10px;" align="center">${lineItem.qty}</td>
			<td style="font-size:10px;" align="right">${lineItem.price?string["0.00"]}
			</td>			
			<td style="font-size:10px;" align="right">${subTotal?string["0.00"]}</td>
			<td style="font-size:10px;" align="center">${taxCodeName}</td>
			<td style="font-size:10px;" align="right">${taxAmt?string["0.00"]}</td>
			<td style="font-size:10px;" align="right">${(subTotal + taxAmt)?string["0.00"]}</td>
		</tr>
		</#list>	
		</tbody>
		</table>		
<#else>
  NO products items
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
				align="right">${finalTotalInWord}</th>					
		</tr>
	</tbody>
</table>

<p>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
</p>
<#if paymentNotes??>
	<p> <b> Payment Notes: </b>
	<br></br>	
	${paymentNotes}
</p>
<#else>	
</#if>
<p>
	<br></br>	
</p>
<#if termsAndConditions??>
	<p> <b> Terms and Conditions: </b>
	<br></br>	
	${termsAndConditions}
</p>
<#else>	
</#if>					
</html>