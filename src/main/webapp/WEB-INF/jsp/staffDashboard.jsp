<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true"%>

<!DOCTYPE html>
<html lang="en">
<%@include file="pageHeader.jsp"%>
<body>
	<%@include file="header.jsp"%>
	<main id="content" class="mainContent sutd-template" role="main">
	<div class="container">
		<div id="messageBox" class="hidden"></div>
			<c:if test="${not empty req_error}">
				<div id="errorMsg">
					<p class="text-danger">${req_error}</p>
				</div>
			</c:if>
			<c:remove var="req_error" scope="session" /> 
		<div id="registrationList" >
			<h2>Client Registration</h2>
				<c:if test="${not empty registrationList}">
				<form id="editRegistrationForm" action="staffDashboard" method="post" >
					<table border="1" cellpadding="5" class="commonTable">
						<tr>
							<th style="width: 150px">Full name</th>
							<th style="width: 150px">FIN</th>
							<th style="width: 150px">Date of birth</th>
							<th style="width: 150px">occupation</th>
							<th style="width: 250px">Address</th>
							<th style="width: 100px">Decision</th>
						</tr>
						<c:forEach var="client" items="${sessionScope.registrationList}">
							<tr>
								<td>${client.fullName}</td>
								<td>${client.fin}</td>
								<td>${client.dateOfBirth}</td>
								<td>${client.occupation}</td>
								<td>${client.address}</td>
								<td><select name="decision" id="staffDecision">
										<option value="waiting"></option>
										<option value="approve">Approve</option>
										<option value="decline">Decline</option>
									</select>
									<input type="hidden" name="user_id" value="${client.user.id}"> 
									<input type="hidden" name="user_email" value="${client.email}">
								</td>
							</tr>
						</c:forEach>
					</table>
				<input type="hidden" name="actionType" value="registrationDecisionAction">
				<!-- Record internal toolbar -->
				<div id="submitBar">
					<div class="btn-group toolbar" role="group">
						<button id="regDecisionBtn" type="submit" value="staffDashboard" class="btn btn-default">Update</button>
					</div>
				</div>
			</form>
			</c:if>
				<c:if test="${empty registrationList}">
				<div>Empty</div>	
			</c:if>
		</div>
		
		<div id="transactionList" >
			<form id="editTransactionForm" action="staffDashboard" method="post" >
				<h2>Client Transactions</h2>
				<input type="hidden" name="actionType" value="transactionDecisionAction">
				<!-- Record internal toolbar -->
				<div id="submitBar">
					<div class="btn-group toolbar" role="group">
						<button id="transDecisionBtn" type="submit" value="staffDashboard" class="btn btn-default">Update</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	</main>
</body>
</html>
