<html>

<style type="text/css">
table {
	
	 border-color:black;
	  border: 1px solid;
}</style>

<div align="center">
 <b>${buisinessName}</b> <br></br> ${buisinessAddress}
 
</div>

<hr></hr>
<p>
	<br></br>
</p>

<div align="center"  >
	<b>BalanceSheet</b>
	<br></br>
	<p align="center">as at ${date}</p>
</div>
<p>
	<br></br>
	<br></br>
</p>

	<table
			style="width: 50%; border: 1px solid #dddddd; border-color: black; float: left">
			<tr>
				<th colspan="2">Assets</th>

				<th style="text-align: center;"><br></br> <span> </span></th>
			</tr>
			<tr>
				<td colspan="3"></td>

			</tr>
			<tr>
			<#list balanceSheetList as groupTypeObj>

				<td colspan="2"></td>
				<td style="text-align: left;" id="content">
				<#if groupTypeObj.typeName=='Assets'>
					<div>
						<div style="text-align: right;">
						<#list  groupTypeObj.groupList as grp>
							<span style="padding-right: 30%;">${grp.groupName}</span>  <#if grp.groupBalance < 0>(</#if><span
								style="text-align: right;">${grp.groupBalance}</span>
								<#if grp.groupBalance < 0>)</#if> <br> </br>
								</#list>
						</div>
						<div class="bottom"style="text-align: right; align:bottom">
						<span style="padding-right: 30%;"><b>Total Asset</b></span><span
							style="align: right;"><b>${totalAsset} </b></span>
					</div>
					</div>
					</#if>
				</td>
				</#list>	
			</tr>

		</table>
		
		
		
		
		<table
			style="width: 50%; border: 1px solid #dddddd; border-color: black; float: left">

			<tr>
				<th colspan="2">Equity</th>
				<td colspan="1"></td>
			</tr>
			<tr>
				<td colspan="2"></td>
				<td style="text-align: left;" id="content">
					<div>
					<#list  balanceSheetList as groupTypeObj>
					<#if groupTypeObj.typeName=='EQUITY'>
						<div>
							<div style="text-align: right;">
							<#list  groupTypeObj.groupList as grp>
								<span style="padding-right: 30%;">{{grp.groupName}}</span>
								 <#if grp.groupBalance < 0>(</#if><span
									style="text-align: right;">{grp.groupBalance}</span>
									<#if grp.groupBalance < 0>)</#if>
									<#/list>
							</div>
						</div>
						</#if>
						</#list>
					</div>
				</td>


			</tr>
			<tr>
				<th colspan="2">Liabilities</th>
				<td colspan="1"></td>
			</tr>
			<tr>
				<td colspan="2"></td>
				<td style="text-align: left;">
					<div>
					<#list accountGroupTypeGroupList as  groupTypeObj >
						<div ng-if="groupTypeObj.typeName=='Liabilities'">
							<div ng-repeat="grp in groupTypeObj.groupList"
								style="text-align: right;">
								<span style="padding-right: 30%;">{{grp.groupName}}</span> <span
									flex></span> <span ng-if="grp.groupBalance < 0">(</span><span
									style="text-align: right;">{{grp.groupBalance|positive|number:2}}</span><span
									ng-if="grp.groupBalance < 0">)</span> <br> <br>
							</div>
						</div>
					</div>
					<div style="text-align: right;">
						<span style="padding-right: 30%;"><b>Total Liabilities</b></span><span
							style="align: right;"><b>{{totalLiabilities2}} </b></span>
							</#list>
					</div>
				</td>
			</tr>

		</table>
		
		
		
</html>