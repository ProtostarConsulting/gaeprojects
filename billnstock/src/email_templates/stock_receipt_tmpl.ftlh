<html>
  <p>Stock receipt with the following details is submitted/updated on ${receiptModifiedDate}.Also find the enclosed document.</p>
   <p>Document Created By: ${createdBy}</p>
  <p>Status: ${documentStatus}</p>
  <p>${docStatusLable}: ${stockModuleApprover}</p>
  <p> 
  <#if warehouse??>
  		Warehouse: ${warehouse}
  </#if> 
  </p>
  <p>Supplier: ${supplier}</p>
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