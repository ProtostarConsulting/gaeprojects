<html>

<style type="text/css">
table {
	border-collapse: collapse;
}

tab2 {
	
}
</style>

<div align="center">
 <b>${businessName}</b> <br></br> ${businessAddress}
</div>
<hr></hr>
<p>
	<br></br>
</p>
<div align="center">
	<b>Invoice</b>
</div>
<p>
	<br></br>
	
</p>
<table style="style="width: 100%; height: 100%;"">


			<tbody>
				<tr>
					<td style="width: 20%">Customer Name :
						</td>
					<td style="width: 40%">${CustomerName}</td>
					<td></td>
					<td >Invoice Id :</td>
					<td >${InvoiceId}</td>
				</tr>

				<tr>
				<td style="width: 20%">Customer Address
						</td>
					<td style="width: 20%">${CustomerAddress}</td>
					<td></td>
					<td align="left">Date:</td>
					<td align="left">${Date}</td>
				
				</tr>
				
				<tr>
				<td style="width: 20%">Note To Customer:
						</td>
				<td style="width :20%">${NoteToCustomer}</td>		
				<td></td>
				<td align="left">Discount</td>
				<td align="left">${Discount}</td>		
				</tr>
		</tbody>
			</table>
	<p>
	<br> </br>
	</p>
		<table
	style="width: 100%; height: 100%; border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">
	
	<tbody>

		<tr>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">Service Name </th>
			<th
				style=" width:10%;border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px;"
				align="center">Quantity</th>
			<th
				style="width:10%;border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px;">Price
			</th>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">Service Total(INR)</th>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">Tax Type</th>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">Tax Total(INR)</th>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">Service SubTotal(INR)</th>
		</tr>
		
		
		<#list serviceItemList as serviceItem>	
		 
		
		<tr>
		<#assign subTotal= serviceItem.sQty * serviceItem.sPrice> 
		<#assign taxP=TaxPercentage>
		
			<td 
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">${serviceItem.serviceName}</td>
			<td 
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px;"
				align="center">${serviceItem.sQty}</td>
			<td 
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px;">${serviceItem.sPrice}
			</td>			
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">${subTotal}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">${TaxType} - ${taxP}%</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">${subTotal}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">${subTotal}</td>
		</tr>
		</#list>	
		
	</tbody>
	
	</table>
	<p>
	<br></br>
	<br></br>
	<br></br>
	</p>
	<table
	style="width: 100%; height: 100%; border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;">
	
	<tbody>

		<tr>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">Product Name </th>
			<th
				style=" width:10%;border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px;"
				align="center">Quantity</th>
			<th
				style="width:10%;border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px;">Price
			</th>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">Product Total(INR)</th>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">Tax Type</th>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">Tax Total(INR)</th>
			<th
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">Final Total(INR)</th>
		</tr>
			<tr>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">${ItemName}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px;"
				align="center">${ItemQuantity}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px;">${ItemPrice}
			</td>
			
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">${ProductTotal}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">${TaxType}- ${TaxPercentage}%</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">${ProductTaxTotal}</td>
			<td
				style="border: 1px solid black; border-color: #cccccc; border-spacing: 5px; height: 30px; padding: 5px;"
				align="center">${FinalTotal}</td>
		</tr>
		
		</tbody>
		</table>
		
		<p>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	<br></br>
	</p>
	
		<div style="position: fixed; bottom: 0;" ng-show="myValue"
		class="ng-hide">
		<b><u>Note</u>:</b>This is electronically generated invoice. Needs no
		stamp or signature. 
	</div>
</html>