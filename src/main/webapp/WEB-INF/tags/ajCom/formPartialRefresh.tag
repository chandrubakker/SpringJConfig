<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="formName" required="true" rtexprvalue="true"
	description="no need to add a '#'"%>
<%@ attribute name="validateUrl" required="true" rtexprvalue="true"%>
<%@ attribute name="noSubmit" required="false"%>
<%@ attribute name="callBackOnFail" required="false"%>
<%@ attribute name="callBackOnSuccess" required="false"%>
<%@ attribute name="asynch" required="false"%>
<%@ attribute name="someattr" required="false"%>
<spring:url value="${validateUrl}" var="processedValidateUrl" />

<script type="text/javascript">

				
$(function(){
	/* console.log("in function");
	console.log('value:#${noSubmit}'); */		
	
	
	var callBackOnFailureFunction="${callBackOnFail}";
	var callBackOnSuccessFunction="${callBackOnSuccess}";
	var asynch="${asynch}";
	if(asynch == '' ){
		   // your code here.
		asynch=false;
		 }
	
	subminForm=0;
	var $form = $('#${formName}');
				
	$('#${formName}').bind('submit',function(e) {
			
	//	console.log("form submit event");			
					
	var formData = $('#${formName}').serialize();
		
			$.ajax({
						  type: 'POST',
						  url: '${processedValidateUrl}',
						  data: formData,
						  async:asynch,
						  headers : {
								"Access-Control-Allow-Origin" : "*"
							},
						  beforeSend: function (xhr) {
							  xhr.setRequestHeader("X-Ajax-call", "true");
							  $form.find('.form-group').removeClass('has-error');
							  $form.find('.form-group').removeClass('alert alert-danger');
							  $form.find('.text-danger').empty();
					        },
					 		success: function(response){
					 			
											if (response.status == 'FAIL') 
											{
												
												
												if(callBackOnFailureFunction != '' ){
													   // your code here.
													 window[callBackOnFailureFunction]();
													 }
													 
												
												for (var i = 0; i < response.errorMessageList.length; i++)
												{
													var item = response.errorMessageList[i];
														
													  var fieldId=item.fieldName;
													  console.log(fieldId);	
													  fieldId=fieldId.replace(/\./g,'-');
												
													    fieldId=fieldId.replace(/\[/g,'-');
													    fieldId=fieldId.replace(/\]/g,'-');
													    console.log(fieldId);
													var $controlGroup = $("#form-group-"+fieldId);
												 
												 
													//console.log(fieldId);
													$controlGroup.addClass('has-error');
													$controlGroup.find('.text-danger').html(item.message); 
		
												}
												
												
											} else {
												
												if(callBackOnSuccessFunction != '' ){
												window[callBackOnSuccessFunction]();
												}
												if('${noSubmit}' == '1')
												{
			
													console.log("no submit");
													
												}
												else
												{
													console.log("submit");
													 subminForm=1;
													$('#${formName}').unbind('submit');
													return true; 
												}
												
												
											}
								         
								           
								        }	
					        
					  
					});
				
				
				  if(subminForm==1)
					  {
					  console.log("returning true");
					    return true;
					  }
				  else{
					  console.log("returning false");
					  return false;
				  }
				 
				  
				});
			      
});			
			

</script>