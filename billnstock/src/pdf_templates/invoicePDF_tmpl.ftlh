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
<div class="container">
<table style="width: 100%; height: 100%;">


			<tbody>
				<tr>
					<td style="width: 20%; border: 0px;"><b>Customer Name:</b>
						</td>
					<td style="width: 40%; border: 0px;">${CustomerName}</td>
					<td style="border: 0px;"></td>
					<td align="right" style="width: 20%; border: 0px;"><b>Invoice No:</b></td>
					<td align="right" style="width: 15%; border: 0px;">${invoiceNumber}</td>
				</tr>

				<tr>
				    <td style="border: 0px;"><b>Customer Address:</b></td>
					<td style="border: 0px;">${CustomerAddress}</td>
					<td style="border: 0px;"></td>
					<td align="right" style="border: 0px;"><b>Invoice Date:</b></td>
					<td align="right" style="border: 0px;">${Date}</td>				
				</tr>
				
				<tr>
				<td style="width: 20%" style="border: 0px;">
						</td>
				<td style="width :20%; border: 0px;"></td>		
				<td style="border: 0px;"></td>
				<#if Discount??>
					<td align="left" style="border: 0px;"><b>Discount:</b></td>
					<td align="left" style="border: 0px;">${Discount}</td>	
				<#else>
					<td style="border: 0px;"></td>		
					<td style="border: 0px;"></td>
				</#if>	
				</tr>
				
				<tr>
				<td style="border: 0px;"><b>Note To Customer:</b></td>
				<td style="border: 0px;"></td>		
				<td style="border: 0px;"></td>
				<td style="border: 0px;"></td>
				<td style="border: 0px;"></td>	
				</tr>
				<tr style="border: 0px;">
				<td colspan="5" style="width: 80%; border: 0px;">${NoteToCustomer}</td>	
				<td style="border: 0px;"></td>
				<td style="border: 0px;"></td>
				<td style="border: 0px;"></td>
				<td style="border: 0px;"></td>	
				</tr>
		</tbody>
			</table>
</div>

	<p>
	<br> </br>
	</p>
	<#if serviceItemList??>
		<table
	style="width: 100%; height: 100%;">
	<caption align="left" style="border-spacing: 5px;"><b>Services: </b></caption>
	<tbody>

		<tr>
			<th
				style=""
				align="center">Service Desc</th>
			<th
				
				align="center">Qty</th>
			<th
				
				align="center">Price
			</th>
			<th
				style=""
				align="center">Sub Total</th>
			<th
				align="center">Tax Type</th>
			<th
				align="center">Tax Total</th>
			<th
				align="center">Total</th>
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
			<td style="font-size:10px;" align="left">${lineItem.itemName}</td>
			<td style="font-size:14px"; align="center">${lineItem.qty}</td>
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
	<p>
	<br></br>
	<br></br>
	<br></br>
	
	</p>
	<#if productItemList??>
  	
	<table 
	style="width: 100%; height: 100%; font-size:11px;">
	<caption align="left" style="border-spacing: 5px;"><b>Products: </b></caption>
	<tbody>

		<tr>
			<th align="center">Product Name </th>
			<th
				
				align="center">Qty</th>
			<th
				
				align="center">Price
			</th>
			<th align="center">Sub Total</th>
			<th align="center">Tax Type</th>
			<th align="center">Tax Total</th>
			<th align="center">Total</th>
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
</html>