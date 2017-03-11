<html>
  <p>Stock receipt with the following details has been approved on ${receiptDate}.</p>
  <p>Created By: ${createdBy}</p>
  <p> 
  <#if warehouse??>
  		Warehouse: ${warehouse}
  </#if> 
  </p>
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