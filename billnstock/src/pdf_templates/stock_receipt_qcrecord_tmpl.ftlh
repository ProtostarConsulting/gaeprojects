<html>

<style type="text/css">
table {
	border-collapse: collapse;
	font-size:11px;
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
<div align="center"><span style="font-size:20px;"><b>Stock Receipt QC Record </b></span><br></br>
						for<br></br>
						<b>Receipt No.${receiptNo}</b>
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
									<td align="right" style="border: 0px;"> 
									</td>
								</tr>
								<tr>
									<td align="right" style="border: 0px;">
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
									</td>
								</tr>
								<tr>
									<td align="right" style="border: 0px;">
										<b>Created Date</b>
									</td>
									<td align="left" style="border: 0px;">
										${createdDateStr}
									</td>
								</tr>
								<tr>
									<td align="right" style="border: 0px;">
										<b>Created By</b>
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
	<#list 0..parameterRecordList?size-1 as i>
	<table 
	style="width: 100%; height: 100%; ">
	<caption align="left" style="border-spacing: 5px;padding: 3px;"></caption>
	<tbody>
	<tr>
			<td style="width: 15%">Parameter Name</td>
			<#list parameterRecordList[i] as parameterRecord>
			<td style="width: 15%">${parameterRecord.parameterName}</td>
			</#list>			
		</tr>

		<tr>
			<td style="width: 15%">Recorded Value</td>
			<#list parameterRecordList[i]  as parameterRecord>
			<td style="width: 15%">${parameterRecord.recordedValue}</td>
			</#list>
		</tr>
		</tbody>
		</table>
		<br></br>
		</#list>			
</html>