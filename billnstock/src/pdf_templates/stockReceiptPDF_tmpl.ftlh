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
<div align="right"><b>Receipt No:</b>${ReceiptNo}
	</div>
	<p>
	<br></br>
</p>
<div align="center">
	<b>Stock Receipt</b>
	
</div>
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
				align="center">Receipt Date</th>
			<th
				style=""
				align="center">Warehouse</th>
			<th
				style="width:20%;"
				align="center">Supplier</th>
				
		</tr>
		<tr>
		    <td align="center">${ReceiptDate}</td>
			<td align="center">${Warehouse}</td>
			<td align="center">${Supplier}</td>
			
		</tr>	
		</tbody>
		</table>
<p>
		<br></br>
	<br></br>
	<br></br>
	</p>
		<#if serviceItemList??>
		<table
	style="width: 100%; height: 100%; ">
	<caption align="left" style="border-spacing: 5px; height: 30px; padding: 10px;"><b>Services: </b></caption>
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
			<td align="center">${lineItem.itemName}</td>
			<td align="center">${lineItem.qty}</td>
			<td align="right">${lineItem.cost?string["0.00"]}</td>			
			<td align="right">${serviceSubTotal?string["0.00"]}</td>
		</tr>
		</#list>	
		</tbody>
		</table>
	<#else>
           <#assign serviceSubTotal=0>
</#if>
<p>
	<br></br>
	<br></br>
	<br></br>
</p>
<#if productItemList??>
  	<p>
	<br></br>
	<br></br>
	<br></br>
	</p>
	<table 
	style="width: 100%; height: 100%; ">
	<caption align="left" style="border-spacing: 5px; height: 30px; padding: 10px;"><b>Products: </b></caption>
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
			<td align="center">${lineItem.stockItem.stockItemType.itemName}</td>
			<td align="center">${lineItem.qty}</td>
			<td align="right">${lineItem.price?string["0.00"]}</td>			
			<td align="right">${productSubTotal?string["0.00"]}</td>
		</tr>
		</#list>	
		</tbody>
		</table>		
<#else>
  <#assign productSubTotal=0>
</#if>	
<table
	style="width: 100%; height: 100%; border-spacing: 5px; height: 30px; padding: 5px;">
	
	<tbody>

		<tr>			
			<th
				style="width:100%;"
				align="right">Final Total(INR):&nbsp;${(productSubTotal+serviceSubTotal)?string["0.00"]}</th>			
		</tr>
		
	</tbody>
</table>			
</html>