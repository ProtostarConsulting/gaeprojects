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
<div align="center"><span style="font-size:20px;"><b>Machine QC Record </b></span><br></br>
						for
						<b>${machineName}</b>
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
										<b>QC Name</b>	
									</td>
									<td align="left" style="border: 0px;">
										${qcname}
									</td>
								</tr>
								<tr>
									<td align="right" style="border: 0px;">
										<b>Record Date</b>
									</td>
									<td align="left" style="border: 0px;">
										${recordDateStr}
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
	
	<table 
	style="width: 100%; height: 100%; ">
	<caption align="left" style="border-spacing: 5px;padding: 3px;"></caption>
	<tbody>
	
	
	<tr>
			<td>Time/Parameter Name</td>
			<#list parameterRecordList as parameterRecord>
			<td>${parameterRecord.parameterName}</td>
			</#list>
			
		</tr>
		<#list 0..timeArrayStr?size-1 as i>
		<tr>
			<td>${timeArrayStr[i]}</td>
			<#list parameterValueList[i].paramRecordedValues as parameterRecord>
			<td>${parameterRecord.recordedValue}</td>
			</#list>
		</tr>
		</#list>
		</tbody>
		</table>
		
	
</html>