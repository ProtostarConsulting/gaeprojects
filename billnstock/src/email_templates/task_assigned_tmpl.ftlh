<html>
	<#if taskStatus ='COMPLETED'>
	<p>Task assigned to ${assignedTo} in ProERP system has been completed. Please find the details below.</p>
  <#else><p>A new task is assigned to ${assignedTo} in ProERP system. Please find the details below.</p>
  </#if>
  <p><b>Task No:</b> ${taskNo}.</p>
  <p><b>Status:</b> ${taskStatus}</p>
  <p><b>Assigned Date:</b> ${assignedDate}.</p>
  <p><b>Assigned By:</b> ${assignedBy}</p>
  <p><b>Title:</b> ${taskTitle}</p>
  <p><b>Description:</b> ${taskDesciption}</p>
  <#if taskStatus ='OPEN' || taskStatus ='INPROGRESS'>
  <p><b>Estimated Completion Date:</b>${estCompletionDate}</p>
  <#else><p><b>Completion Date:</b> ${completionDate}</p>
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