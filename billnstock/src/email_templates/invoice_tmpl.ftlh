<html>
  <p>Dear ${customerName},
  <p>An invoice with the following details is submitted/updated on ${modifiedDate}.Also find the enclosed document.</p>
  <p>Document Created By: ${createdBy}</p>
  <p>Status: ${documentStatus}</p>
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