<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%-- <%@ taglib tagdir="/WEB-INF/tags/ajax" prefix="ajax"%> --%>
<%-- <%@ taglib tagdir="/WEB-INF/tags/ajCom" prefix="ajCom" %> --%>
<section>
	<div class="container">
		<div
			class="col-lg-6 col-lg-offset-3 col-md-6 col-sm-8 col-xs-12 col-sm-offset-2 col-md-offset-3">
			<div>
				<h1 class="page-header">Lab Admin Login</h1>
			</div>
			<c:if test="${! empty logout}">
				<div class="alert alert-success custom-alert text-center">${logout}</div>
			</c:if>
			<c:if test="${! empty sessionExp}">
				<div class="alert alert-success custom-alert text-center">${sessionExp}</div>
			</c:if>
			<div class="panel panel-default custom-panel">
				<div class="panel-heading">
					<strong class="text-center">Enter sign-in details</strong>
				</div>
				<div class="panel-body">
					<c:if test="${param.error ne null}">
						<div class="alert-danger">Invalid username and password.</div>
					</c:if>
					<c:if test="${param.logout ne null}">
						<div class="alert-normal">You have been logged out.</div>
					</c:if>
					<c:url value="/processLogin" var="login" />
					<form:form id="login-form" action="${login}" method="post"
						modelAttribute="user">
						<div id="form-group-active" class="form-group">
							<div class="text-danger text-center">
								<form:errors path="enabled" />
							</div>
						</div>
						<div id="form-group-email">
							<div class="form-group">
								<form:input path="email" class="form-control"
									placeholder="Email" />
								<div class="text-danger">
									<form:errors path="email" />
								</div>
							</div>
						</div>
						<div id="form-group-password">
							<div class="form-group">
								<form:password path="password" class="form-control"
									placeholder="Password" />
								<div class="text-danger">
									<form:errors path="password" />
								</div>
							</div>
						</div>
						<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
						<div class="form-group">
							<button type="submit" class="btn btn-success submit_c">Sign
								In</button>
							<button type="reset" class="btn btn-default">Reset</button>
							<div style="padding-top: 5px;">
								<a href="<c:url value="/password/forgot" />" class="app-link">Forgot
									Password</a>
							</div>
						</div>
					</form:form>
					<!-- <ajax:formPartialRefresh validateUrl="/login.json"
						formName="login-form" noSubmit="0" /> -->
					<%-- <ajCom:formPartialRefresh validateUrl="http://sorcererpaws.com/client/login" formName="login-form" noSubmit="0"/> --%>
					<%-- <ajCom:formPartialRefresh validateUrl="http://localhost:9393/mPaws/client/login" formName="login-form" noSubmit="0"/> --%>
				</div>
			</div>
		</div>
	</div>
</section>