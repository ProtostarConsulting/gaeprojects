<html>
  <p>Purchase order with the following details is ${documentStatus} on ${poModifiedDate}.</p>
  <p>${docStatusLable}: ${stockModuleApprover}</p>
  <p>Supplier: ${supplier}</p>
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