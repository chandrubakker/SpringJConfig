<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ attribute name="formId" required="true" rtexprvalue="true" description="no need to add a '#'"%>
<%@ attribute name="validateURL" required="true" rtexprvalue="true" description="REST validation url" %>
<%@ attribute name="noSubmit" required="false" description="integer: 0 or 1"%>
<%@ attribute name="requestMethod" required="true" description="type of HTTP request, Ex: GET, POST, PUT, DELETE"%>
<%@ attribute name="callBackOnFail" required="false"%>
<%@ attribute name="callBackOnSuccess" required="false"%>
<%@ attribute name="asynch" required="false" description="boolean"%>
<%@ attribute name="busyMessage" required="false" description="String: this message will be shown on busy indicator"%>
<spring:url value="${validateURL}" var="processedValidateUrl" />

<script type="text/javascript">
	$(function() {
		var callBackOnFailureFunction = "${callBackOnFail}";
		var callBackOnSuccessFunction = "${callBackOnSuccess}";
		var requestType = "${requestMethod}";
		var asynch = "${asynch}";
		var busyText = "${busyMessage}";
		if(asynch === '') {
			asynch = false;
		}
		
		subminForm=0;
		
		var $form = $('#${formId}');
		$('#${formId}').bind('submit', function(e) {
			console.log("form submit event: " + e);
			var formData = $('#${formId}').serialize();
			console.log(formData);
			
			$(".busy-modal-title").text(busyText);
			$("#busy-modal").modal('show');
			setTimeout(function() {
				$.ajax({
					type: requestType,
					url: '${processedValidateUrl}',
					async: asynch,
					data: formData,
					beforeSend: function(jqXHR) {
						jqXHR.setRequestHeader("X-Ajax-call", "true");
						$form.find('.form-group').removeClass('has-error');
						$form.find('.form-group').removeClass('alert alert-danger');
						$form.find('.text-danger').empty();
					},
					success: function(data, textStatus, jqXHR) {
						
						var status = jqXHR.status;
						console.log("STATUS: " + status);
						
						console.log("FORM SUBMITTING : SUCCESS.");
						if(callBackOnSuccessFunction !== "") {
							window[callBackOnSuccessFunction]();
						}
						
						if('${noSubmit}' === '1') {
							console.log("no submit");
						} else {
							console.log("submit");
							subminForm=1;
							$('#${formId}').unbind('submit');
							return true;
						}
					},
					error: function(jqXHR, textStatus, errorThrown) {
						
						var status = jqXHR.status;
						console.log("STATUS: " + status);
						
						console.log("FORM SUBMITTING : FAILED.");
						//console.log(jqXHR);
						console.log(jqXHR.responseJSON);
						
						if(callBackOnFailureFunction !== "") {
							window[callBackOnFailureFunction]();
						}
						
						var errorObject = jqXHR.responseJSON;
						
						for(var i = 0; i < errorObject.errorMessageList.length; i++) {
							var item = errorObject.errorMessageList[i];
							var fieldId=item.fieldName;
							console.log(fieldId);
							fieldId=fieldId.replace(/\./g,'-');
							fieldId=fieldId.replace(/\[/g,'-');
						    fieldId=fieldId.replace(/\]/g,'-');
						    console.log(fieldId);
						    var $controlGroup = $("#form-group-"+fieldId);
							
						    console.log(fieldId);
							$controlGroup.addClass('has-error');
							$controlGroup.find('.text-danger').html(item.message);
						}
						console.log(textStatus, jqXHR.status);
						console.log(errorThrown);
					},
					statusCode: {
						404: function() {
							console.log("404: resource not found.");
						},
						400: function() {
							console.log("400: validation errors.");
						},
						500: function() {
							console.log("500: internal server error.");
						},
						403: function() {
							console.log("403: access denied.");
						},
						201: function() {
							console.log("201: object created.");
						},
						200: function() {
							console.log("200: object updated.");
						},
						204: function() {
							console.log("204: object deleted.");
						}
					}
				});
				
				clearTimeout(this);
				$("#busy-modal").modal('hide');
			}, 1500);			
			
			
			if(subminForm === 1) {
				console.log("JSP SUBMIT");
				return true;
			} else {
				console.log("AJAX SUBMIT");
				return false;
			}
			
		});
	});
</script>