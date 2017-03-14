<html>
  <p>Dear ${customerName},
  <p>An invoice with the following details is ${documentStatus} on ${approvedDate}.</p>
  <p>${docStatusLable}: ${invoiceModuleApprover}</p>
  <p>Warehouse: ${fromWH}</p>
  <p>Final Total: ${finalTotal}</p>
  
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