<html>
  <p>Stock shipment request with the following details is ${documentStatus} on ${shipmentModifiedDate}.</p>
  <p>${docStatusLable}: ${stockModuleApprover}</p>
  <p>From: ${fromWH}</p>
  <#if toWH??>
  <p>To :${toWH}</p>
  </#if>
  <#if customerName??>
    <p>To: ${customerName}</p>
  </#if>
  <#if partnerName??>
    <p>To :${partnerName}</p>
  </#if>
  <p>Final Total: ${finalTotal}</p>
  
  <p> Please use this URL to see further details:  <a href="${currentAppURL}"> ${currentAppURL} </a> <p>
  <br>
  <p>Thanks and Regards,</p>  
  <p>${businessName}</p>
  <p>${businessAdressLine1}</p>
  <#if businessAdressLine2??>
    <p>${businessAdressLine2}</p>
  </#if> 
  <p>${businessAdressCity} 
  		<#if businessAdressPin??>,${businessAdressPin} 
  		</#if>
  </p> 
  <p>${businessAdressState} 
  		<#if businessAdressCountry??>,${businessAdressCountry} 
  		</#if>
  </p>

</html>