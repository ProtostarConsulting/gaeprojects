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
<div align="center"><span style="font-size:20px;"><b>Production Requisition </b></span>
<p align="center"> for</p>
<p align="center"><b>${productName}</b></p>

	</div>

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
									<td align="left" style="border: 0px;"><b>Delivery Date:</b>${deliveryDate}<br></br>
										 <br></br>
										
									</td>
								</tr>
								<tr>
									<td align="left" style="border: 0px;"><b> Delivery Time</b>
										${deliveryTime}
									</td>
								</tr>
								<tr>
									<td align="left" style="border: 0px;">
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
										
									</td>
									<td align="left" style="border: 0px;">
										
									</td>
								</tr>
								<tr>
									<td align="right" style="border: 0px;">
										
									</td>
									<td align="left" style="border: 0px;">
										
									</td>
								</tr>
								<tr>
									<td align="right" style="border: 0px;">
										<b>Created Date:</b>
									</td>
									<td align="left" style="border: 0px;">
										${createdDateStr}
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
										
									</td>
									<td align="left" style="border: 0px;">
										
									</td>
								</tr>
								<tr>
									<td align="right" style="border: 0px;">
									</td>
									<td align="left" style="border: 0px;">
										
									</td>
								</tr>
								
								<tr>
									<td align="right" style="border: 0px;">
										
									</td>
									<td align="left" style="border: 0px;">
									
									</td>
								</tr>
														
							 </tbody>
						</table>					
					</td>					
				</tr>
				<tr align="left" style="border: 0px;">
					<td style="width: 60%;"  style="border: 0px; padding: 7px;"><br></br>
										</td>
				
					<td style="width: 40%;"  style="border: 0px; padding: 7px;">
								<table style="width: 100%; height: 100%;">
					 			<tr>
					 				<td  style="width:10%;border: 0px;">
										
									</td>
									<td  style="width:40%;border: 0px;">
										
									</td> 
								</tr>	
								<tr>
								<td  style="width:10%;border: 0px;">
										
									</td>
								<td style="width:40%;border: 0px;">
										
									</td>
								</tr>	
								</table>
					</td>
				</tr>
				<!-- Contains a row across two tables -->
											
		</tbody>
</table>

	</div>
			<p>
		<br></br>
	    <br></br>
	</p>
	
	<table 
	style="width: 100%; height: 100%; ">
	<caption align="left" style="border-spacing: 5px;padding: 3px;"></caption>
	<tbody>
	
	
	<tr>
			<th align="center">Category Name </th>
			<th
				style="width:46%;"
				align="center">Items.</th>
			<th
				style="width:10%;" 
				align="center">Qty.
			</th>
			
		</tr>
			<#list productItemList as lineItem>
		    
	    <tr>
			<td style="font-size:10px;" align="center">${lineItem.cat.catName}</td>
			<td style="font-size:10px;" align="center">
			
			
			<table style="width: 100%; height: 100%;border: 0px;">
			<#list lineItem.items as item>
			<tr style="border: 0px">
			
			<td style="font-size:10px;border: 0px;" align="center;">${item.itemName}</td>
			
			
			
			</tr>
			</#list>
			</table>
			</td>
			
			<td style="font-size:10px;" align="center">
			<#if  lineItem.items??>
			<table style="width: 100%; height: 100%;border: 0px;">
			<#list lineItem.items as item>
			<tr style="border: 0px">
			
			<td style="font-size:10px;border: 0px;" align="center;">${item.qty}</td>
			
			
			</tr>
			</#list>
			</table>
			<#else>No qty
			</#if>
			</td>
		</tr>
		</#list>
		
		</tbody>
		</table>
		
	
</html>