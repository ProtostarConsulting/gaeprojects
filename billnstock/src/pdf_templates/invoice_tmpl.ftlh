<html>

<style type="text/css">
table {
	border-collapse: collapse;
	font-size: 10px;
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
	font-size: 9px;
}

p {
	font-size: 11px;
}
</style>

<div align="right">
	<table style="width: 100%; height: 100%; border: 0px;">
		<tr>
			<td style="width: 40%; border: 0px"></td>
			<td style="width: 60%; border: 0px"><div align="right">
					<span style="font-size: 16px;"> <b>${businessName}</b></span> <br></br>
					${businessAddress}
				</div></td>
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
	<span style="font-size: 20px;"><b>Invoice</b></span>
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
								<td align="left" style="border: 0px;"><b>Invoice No:</b>
									${invoiceNumber}</td>
							</tr>
							<tr>
								<td align="left" style="border: 0px;"><b>Invoice Date:</b>${createdDate}
								</td>
							</tr>
							<tr>
								<td align="left" style="border: 0px;"><b>Customer Name
										and Address:</b><br></br> ${CustomerName} <br></br>
									${CustomerAddress}</td>
							</tr>
						</tbody>
					</table>
				</td>
				<!-- Right side top table -->
				<td
					style="width: 40%; border: 0px; vertical-align: top; padding-top: 0px;">
					<table style="width: 100%; height: 100%;">
						<tbody>
							<tr>
								<td align="right" style="border: 0px;"><b>Status:</b></td>
								<td align="left" style="border: 0px;">${docStatus}</td>
							</tr>
							<tr>
								<td align="right" style="border: 0px;"><b>Created By:</b></td>
								<td align="left" style="border: 0px;">${createdBy}</td>
							</tr>
							<tr>
								<td align="right" style="border: 0px;"><#if docStatus =
									'REJECTED'> <b>Rejected By:</b> <#else> <b>Approved By:</b>
									</#if>
								</td>
								<td align="left" style="border: 0px;">${approvedBy}</td>
							</tr>
							<tr>
								<td align="right" style="border: 0px;"><#if docStatus =
									'REJECTED'> <b>Rejected Date:</b> <#else> <b>Approved Date:</b>
									</#if>
								</td>
								<td align="left" style="border: 0px;">${modifiedDate}</td>
							</tr>
							<#if Discount??>
							<tr>
								<td align="right" style="border: 0px;"><b>Discount:</b></td>
								<td align="left" style="border: 0px;">${Discount}</td>
							</tr>
							<#else> </#if>
						</tbody>
					</table>
				</td>
			</tr>
			<!-- Contains a row across two tables -->
			<#if noteToCustomer??>
			<tr align="left" style="border: 0px;">
				<td colspan="2" style="border: 0px; padding: 7px;"><b>Note
						To Customer:</b><br></br> <pre>${noteToCustomer}</pre></td>
			</tr>
			<#else> </#if>
		</tbody>
	</table>
</div>

<p>
	<br> </br>
</p>
<#if serviceItemList??>
<table style="width: 100%; height: 100%;">
	<caption align="left" style="border-spacing: 5px; padding: 3px;">
		<b>Services: </b>
	</caption>
	<tbody>

		<tr>
			<th align="center">Service Desc.</th>
			<th align="center">Qty</th>
			<th align="center">Price</th>
			<th align="center">Total</th> <#if serviceTaxTotal gt 0>
			<th align="center">Tax</th> </#if>
		</tr>
		<#list serviceItemList as lineItem> 
		<#assign subTotal = lineItem.qty *lineItem.price>
		 <#if serviceTax??> 
		 <#assign taxAmt = serviceTax.taxPercenatge/100 * subTotal> 
		 <#assign taxCodeName =	serviceTax.taxCodeName + "(" + serviceTax.taxPercenatge + "%)">
		<tr>
			<td align="center">${lineItem.itemName}</td>
			<td align="center">${lineItem.qty}</td>
			<td align="right">${lineItem.price?string["0.00"]}</td>
			<td align="right">${subTotal?string["0.00"]}</td>
			<td style="width: 30%;" align="right">${taxCodeName}:&nbsp;${taxAmt?string["0.00"]}
				<#if serviceTax.withSubHeads>

				<table style="width: 100%; height: 100%;">
					<#list serviceTax.subHeads as subHead> 
					<#assign subHeadTax =subHead.taxSubCodeName+"("+subHead.taxPercenatge+ "%)"> 
					<#assign subTotal = lineItem.qty * lineItem.price> 
					<#assign subHeadTaxAmt =subHead.taxPercenatge/100 * subTotal>
					<tbody>
						<tr>
							<td style="border: 0px;" align="right">
								${subHeadTax}:&nbsp;${subHeadTaxAmt?string["0.00"]}</td>
						</tr>
					</tbody>
					</#list>
				</table> </#if>
			</td>
		</tr>
		<#elseif lineItem.selectedTaxItem??> 
		<#assign taxAmt =lineItem.selectedTaxItem.taxPercenatge/100 * subTotal> 
		<#assign taxCodeName = lineItem.selectedTaxItem.taxCodeName + "(" +
		lineItem.selectedTaxItem.taxPercenatge + "%)">

		<tr>
			<td align="center">${lineItem.itemName}</td>
			<td align="center">${lineItem.qty}</td>
			<td align="right">${lineItem.price?string["0.00"]}</td>
			<td align="right">${subTotal?string["0.00"]}</td>
			<td style="width: 30%;" align="right">
				${taxCodeName}:&nbsp;${taxAmt?string["0.00"]} <#if
				lineItem.selectedTaxItem.withSubHeads>

				<table style="width: 100%; height: 100%;" align="right">
					<#list lineItem.selectedTaxItem.subHeads as subHead> <#assign
					subHeadTax = subHead.taxSubCodeName+"("+subHead.taxPercenatge+
					"%)"> <#assign subTotal = lineItem.qty * lineItem.price> <#assign
					subHeadTaxAmt = subHead.taxPercenatge/100 * subTotal>
					<tbody>
						<tr>
							<td style="border: 0px;" align="right">
								${subHeadTax}:&nbsp;${subHeadTaxAmt?string["0.00"]}</td>
						</tr>
					</tbody>
					</#list>
				</table> </#if>

			</td>
		</tr>
		<#else> <#assign taxCodeName = "NA">
		<tr>
			<td align="center">${lineItem.itemName}</td>
			<td align="center">${lineItem.qty}</td>
			<td align="right">${lineItem.price?string["0.00"]}</td>
			<td align="right">${subTotal?string["0.00"]}</td> 
			<#if serviceTaxTotal gt 0>
			<td style="width: 30%;" align="right">${taxCodeName}</td> </#if>
		</tr>
		</#if> </#list>
	</tbody>
</table>
<table
	style="width: 100%; height: 100%; border-spacing: 5px; height: 30px; padding: 5px;">
	<#assign serviceSubTotal = serviceTotal + serviceTaxTotal>
	<tbody>
		<tr>
			<th style="width: 100%;" align="right">Service
				Total:&nbsp;${serviceTotal?string["0.00"]}</th>
		</tr>
		<#if serviceTaxTotal gt 0>
		<tr>
			<th style="width: 100%;" align="right">Tax
				Total:&nbsp;${serviceTaxTotal?string["0.00"]}</th>
		</tr>
		</#if> <#if productItemList??>
		<tr>
			<th style="width: 100%;" align="right">Service Sub
				Total:&nbsp;${serviceSubTotal?string["0.00"]}</th>
		</tr>
		</#if>
	</tbody>
</table>
<#else> No service items </#if> <#if productItemList??>
<p>
	<br></br> <br></br>
</p>
<table style="width: 100%; height: 100%;">
	<caption align="left" style="border-spacing: 5px; padding: 3px;">
		<b>Products: </b>
	</caption>
	<tbody>
		<tr>
			<th align="center">Product Name</th>
			<th align="center">Qty</th>
			<th align="center">Price</th>
			<th align="center">Total</th> <#if productTaxTotal gt 0>
			<th align="center">Tax</th> </#if>
		</tr>
		<#list productItemList as lineItem> <#assign subTotal = lineItem.qty *
		lineItem.price> <#if productTax??> <#assign taxAmt =
		productTax.taxPercenatge/100 * subTotal> <#assign taxCodeName =
		productTax.taxCodeName + "(" +productTax.taxPercenatge + "%)">
		<tr>
			<td align="left">${lineItem.stockItem.stockItemType.itemName}</td>
			<td align="center">${lineItem.qty}</td>
			<td align="right">${lineItem.price?string["0.00"]}</td>

			<td align="right">${subTotal?string["0.00"]}</td>
			<td style="width: 30%;" align="right">${taxCodeName}:&nbsp;${taxAmt?string["0.00"]}
				<#if productTax.withSubHeads>
				<table style="width: 100%; height: 100%;">
					<#list productTax.subHeads as subHead> 
					<#assign subHeadTax = subHead.taxSubCodeName+"("+subHead.taxPercenatge+ "%)"> 
					<#assign subTotal = lineItem.qty * lineItem.price> 
					<#assign subHeadTaxAmt = subHead.taxPercenatge/100 * subTotal>
					<tbody>
						<tr>
							<td style="border: 0px;" align="right">
								${subHeadTax}:&nbsp;${subHeadTaxAmt?string["0.00"]}</td>
						</tr>
					</tbody>
					</#list>
				</table> </#if>
			</td>
		</tr>
		<#elseif lineItem.selectedTaxItem??> 
		<#assign taxAmt =lineItem.selectedTaxItem.taxPercenatge/100 * subTotal> 
		<#assign taxCodeName = lineItem.selectedTaxItem.taxCodeName + "(" +	lineItem.selectedTaxItem.taxPercenatge + "%)">

		<tr>
			<td align="center">${lineItem.stockItem.stockItemType.itemName}</td>
			<td align="center">${lineItem.qty}</td>
			<td align="right">${lineItem.price?string["0.00"]}</td>
			<td align="right">${subTotal?string["0.00"]}</td>
			<td style="width: 30%;" align="right">${taxCodeName}:&nbsp;${taxAmt?string["0.00"]}

				<#if lineItem.selectedTaxItem.withSubHeads>
				<table style="width: 100%; height: 100%;" align="right">
					<#list lineItem.selectedTaxItem.subHeads as subHead> <#assign
					subHeadTax = subHead.taxSubCodeName+"("+subHead.taxPercenatge+
					"%)"> <#assign subTotal = lineItem.qty * lineItem.price> <#assign
					subHeadTaxAmt = subHead.taxPercenatge/100 * subTotal>
					<tbody>
						<tr>
							<td style="border: 0px;" align="right">
								${subHeadTax}:&nbsp;${subHeadTaxAmt?string["0.00"]}</td>
						</tr>
					</tbody>
					</#list>
				</table> </#if>
			</td>
		</tr>
		<#else><#assign taxCodeName = "NA">
		<tr>
			<td align="center">${lineItem.stockItem.stockItemType.itemName}</td>
			<td align="center">${lineItem.qty}</td>
			<td align="right">${lineItem.price?string["0.00"]}</td>
			<td align="right">${subTotal?string["0.00"]}</td>
			<#if productTaxTotal gt 0>
			<td style="width: 30%;" align="right">${taxCodeName}</td></#if>
		</tr>

		</#if> </#list>

	</tbody>
</table>
<table
	style="width: 100%; height: 100%; border-spacing: 5px; height: 30px; padding: 5px;">
	<#assign productSubTotal = productTotal + productTaxTotal>
	<tbody>
		<tr>
			<th style="width: 100%;" align="right">Product
				Total:&nbsp;${productTotal?string["0.00"]}</th>
		</tr>
		<#if productTaxTotal gt 0>
		<tr>
			<th style="width: 100%;" align="right">Tax
				Total:&nbsp;${productTaxTotal?string["0.00"]}</th>
		</tr>
		</#if> <#if serviceItemList??>
		<tr>
			<th style="width: 100%;" align="right">Product Sub
				Total:&nbsp;${productSubTotal?string["0.00"]}</th>
		</tr>
		</#if>
	</tbody>
</table>
<#else> NO products items </#if>

<table
	style="width: 100%; height: 100%; border-spacing: 5px; height: 30px; padding: 5px;">
	<tbody>
		<tr>
			<th style="width: 100%;" align="right">Final
				Total:&nbsp;${finalTotal?string["0.00"]}</th>
		</tr>
		<tr>
			<th style="width: 100%;" align="right">${finalTotalInWord}</th>
		</tr>
	</tbody>
</table>

<p>
	<br></br> <br></br> <br></br> <br></br> <br></br>
</p>
<#if paymentNotes??>
<p>
	<b> Payment Notes: </b> <br></br> ${paymentNotes}
</p>
<#else> </#if>
<p>
	<br></br>
</p>
<#if termsAndConditions??>
<p>
	<b> Terms and Conditions: </b> <br></br> ${termsAndConditions}
</p>
<#else> </#if>
</html>