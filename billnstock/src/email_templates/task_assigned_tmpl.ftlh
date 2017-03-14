<html>
  <p>Dear ${assignedTo},</p>
  <p>A new task is assigned to you in ProERP system. Please find the details below.</p>
  <p><b>Task No:</b> ${taskNo}.</p>
  <p><b>Assigned Date:</b> ${assignedDate}.</p>
  <p><b>Assigned By:</b> ${assignedBy}</p>
  <p><b>Title:</b> ${taskTitle}</p>
  <p><b>Description:</b> ${taskDesciption}</p>
  <#if estCompletionDate??>
  <p><b>Estimated Completion Date:</b> ${estCompletionDate}</p>
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