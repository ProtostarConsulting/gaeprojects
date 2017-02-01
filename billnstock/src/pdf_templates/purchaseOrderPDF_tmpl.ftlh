<html>

<style type="text/css">
table {
	border-collapse: collapse;
}

th {
	border: 1px solid black; 
	border-color: #cccccc; 
	border-spacing: 5px; 
	height: 30px; 
	padding: 7px;
}

td {
	border: 1px solid black; 
	border-color: #cccccc; 
	border-spacing: 5px; 
	height: 30px; 
	padding: 5px;
}

</style>

<div align="right">  	
 <table style="width: 100%; height: 100%; border: 0px">
	<tr>
		<td style="width: 40%; border: 0px"></td>
		<td style="width: 60%; border: 0px"><div align="right"> <b>${BusinessName}</b> <br></br> ${BusinessAddress}</div></td>		
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
<div align="center"><b>Purchase Order </b>
	</div>
	<p>
	<br></br>
</p>
<div align="right">
	<b>PO No:</b>${PONum}
	
</div>

<p>
	<br></br>
	
</p>
<table style="style="width: 100%; height: 100%;"">


			<tbody>
				<tr>
					<td style="width: 20%; border: 0px;"><b>Bill To:</b>
						</td>
				<td style="width: 40%; border: 0px;"></td>		
				<td style="border: 0px;"><b>Ship To:</b></td>
				</tr>
                 <tr> <td style="width: 20%; border: 0px;">${To} </td>
                 <td style="width: 40%; border: 0px;"></td>		
                  <td style="width: 20%; border: 0px;">${ShippedTo}</td>
                 </tr>	
		</tbody>
			</table>
			<p>
	<br></br>
	<br></br>
	<br></br>
	</p>
			<table
	style="width: 100%; height: 100%; ">
	<tbody>

		<tr>
			<th
				style=""
				align="center">PO DueDate</th>
			<th
				style="width:20%;"
				align="center">Requisitioner</th>
			<th
				style="width:20%;">Shipped Via
			</th>
			<th
				style=""
				align="center">F.O.B</th>
			<th
				align="center">Terms</th>
			
		</tr>
		<tr>
			<td align="center">${PODueDate}</td>
			<td align="center">${Requisitioner}</td>
			<td align="center">${ShippedVia}</td>			
			<td align="center">${FOBPoint}</td>
			<td align="center">${Terms}</td>
			
		</tr>	
		</tbody>
		</table>
		<p>
		<br></br>
	<br></br>
	</p>
	<#if productItemList??>
	<table 
	style="width: 100%; height: 100%; ">
	<caption align="left" style="border-spacing: 5px; height: 30px; padding: 10px;"><b>Products: </b></caption>
	<tbody>

		<tr>
			<th align="center">Product Name </th>
			<th
				style="width:6%;"
				align="center">Qty.</th>
			<th
				style="width:15%;" 
				align="center">Price
			</th>
			<th
				style="width:15%;"
				align="center">Sub Total</th>
			<th
				align="center">Tax Type</th>
			<th
				align="center">Tax Total</th>
			<th style="width:15%;"
				align="center">Total</th>
		</tr>
		
		<#list productItemList as lineItem>
		    <#assign subTotal = lineItem.qty * lineItem.price>	
		    <#if productTax??>
				<#assign taxAmt = productTax.taxPercenatge/100 * subTotal>
				<#assign taxCodeName = productTax.taxCodeName + "(" + productTax.taxPercenatge + "%)">
		    <#else>
  				<#assign taxAmt = 0>
  				<#assign taxCodeName = "NA">
			</#if>	
			<tr>
			<td align="center">${lineItem.stockItem.stockItemType.itemName}</td>
			<td align="center">${lineItem.qty}</td>
			<td align="right">${lineItem.price?string["0.00"]}</td>			
			<td align="right">${subTotal?string["0.00"]}</td>
			<td align="center">${taxCodeName}</td>
			<td align="right">${taxAmt?string["0.00"]}</td>
			<td align="right">${(subTotal + taxAmt)?string["0.00"]}</td>
		</tr>
		</#list>	
		</tbody>
		</table>		
<#else>
  NO products items
</#if>		
<p>
	<br></br>
	<br></br>
	<br></br>
</p>
<#if serviceItemList??>
		<table
	style="width: 100%; height: 100%; ">
	<caption align="left" style="border-spacing: 5px; height: 30px; padding: 10px;"><b>Other Costs: </b></caption>
	<tbody>

		<tr>
			<th
				style=""
				align="center">Service Name</th>
			<th
				style="width:6%;"
				align="center">Qty.</th>
			<th
				style="width:10%";align="center">Cost
			</th>
			<th
				style=""
				align="center">Sub Total</th>
			<th
				align="center">Tax Type</th>
			<th
				align="center">Tax Total</th>
			<th
			    style="width:10%;"
				align="center">Total</th>
		</tr>
		<#list serviceItemList as lineItem>
		    <#assign subTotal = lineItem.qty * lineItem.cost>
			<#if serviceTax??>
				<#assign taxAmt = serviceTax.taxPercenatge/100 * subTotal>
				<#assign taxCodeName = serviceTax.taxCodeName + "(" + serviceTax.taxPercenatge + "%)">
		    <#else>
  				<#assign taxAmt = 0>
  				<#assign taxCodeName = "NA">
			</#if>	
			<tr>
			<td align="center">${lineItem.itemName}</td>
			<td align="center">${lineItem.qty}</td>
			<td align="right">${lineItem.cost?string["0.00"]}</td>			
			<td align="right">${subTotal?string["0.00"]}</td>
			<td align="center">${taxCodeName}</td>
			<td align="right">${taxAmt?string["0.00"]}</td>
			<td align="right">${(subTotal + taxAmt)?string["0.00"]}</td>
		</tr>
		</#list>	
		</tbody>
		</table>
	<#else>
  No service items
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