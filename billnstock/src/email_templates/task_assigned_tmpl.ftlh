<html>
  <p>Dear ${assignedTo},</p>
  <p>A new task has been assigned to you in ProERP system. Please find the details below.</p>
  <p>Task No: ${taskNo}.</p>
  <p>Assigned Date: ${assignedDate}.</p>
  <p>Assigned By: ${assignedBy}</p>
  <p>Title: ${taskTitle}</p>
  <p>Description :${taskDesciption}</p>
  <#if estCompletionDate??>
  <p>Estimated Completion Date: ${estCompletionDate}</p>
  </#if>
  
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