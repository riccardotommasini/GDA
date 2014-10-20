<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:template title="TBS - Pagina conferma">
	<jsp:attribute name="header"></jsp:attribute>
	<jsp:body>
	
	<c:choose>
		<c:when test="${resultReset}">
			<div class="alert alert-success"> Password cambiata con successo! </div>
		</c:when>
		<c:when test="${resultPsw}">
			<div class="alert alert-success"> Password cambiata con successo!</div>
		</c:when>
		<c:when test="${confirmation}">
			<form method="post" style="margin-top: 1em;">
				<p>Per confermare la tua registrazione, clicca su Conferma</p>
				<input type="hidden" name="confirmCode" value="${confirmCode}">
				<div class="form-actions">
					<button type="submit" class="btn btn-primary">Conferma</button>
				</div>
			</form>	
		</c:when>
		<c:when test="${resetting}">
			<div class="col-md-4">
			<form role="form" method="post" style="margin-top: 1em;">
				<div class="form-group">
					<label for="psw">Inserisci la nuova password:</label>
					<input type="password" name="newPsw" class="form-control" id="psw" placeholder="Password">
				</div>
				<input type="hidden" name="resetCode" value="${resetCode}">
				<div class="form-actions">
					<button type="submit" class="btn btn-primary">Conferma</button>
				</div>
			</form>	
			</div>
		</c:when>
	</c:choose>
	
	</jsp:body>
</t:template>