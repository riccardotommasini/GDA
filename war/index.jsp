<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:template title="Golem Data Analyser - Homepage">
	<jsp:body>
			
	<c:if test="${resetSent}">
		<div class="alert alert-success"> Un'email per il reset della password e' stata inviata! </div>
	</c:if>
	<c:if test="${resetError}">
		<div class="alert alert-warning"> Uhm, non è stato possibile inviarti un'email per il reset, sicuro di aver inserito l'indirizzo email corretto? </div>
	</c:if>
	<c:if test="${resetEmpty}">
		<div class="alert alert-warning"> Non hai inserito nessun indirizzo email per il reset! </div>
	</c:if>

	
	<div class="container">
		<div class="row">
			<div class="col-md-9">
				<div class="jumbotron">
				  <h1>Golem Data Analyser</h1>
				</div>
			</div>
			
			<c:if test="${empty user }">
				<div class="col-md-3">
					<ul class="nav nav-tabs">
						<li <c:if test="${!toggleRegistration}">class="active"</c:if>><a href="#login" data-toggle="tab">Login</a></li>
						<li <c:if test="${toggleRegistration}">class="active"</c:if>><a href="#register" data-toggle="tab">Registrazione</a></li>
					</ul>
					<div class="tab-content">
						<div class="tab-pane <c:if test="${!toggleRegistration}">active </c:if>" id="login">
							<c:if test="${errorLogin}">
								<div class="alert alert-warning"> Email o password potrebbero essere errati! </div>
							</c:if>
							<form role="form" name="loginForm" action="/login" method="post">
								<div class="form-group">
									<label for="inputEmail1">Email</label>
									<input type="email" class="form-control" name="email" id="inputEmail1" placeholder="E-mail" required>
								</div>
								<div class="form-group">
									<label for="inputPassword1">Password</label>
									<input type="password" class="form-control" id="inputPassword1" name="password" placeholder="Password" required>
								</div>
								<button type="submit" class="btn btn-primary">Login</button>
							</form>
							<a data-toggle="modal" data-target="#pswReset">Password dimenticata?</a>
							<br><br>
							<hr>
						</div>
						<div class="tab-pane <c:if test="${toggleRegistration}">active</c:if>" id="register">
							<c:if test="${errorReg}">
								<div class="alert alert-warning"> Completa tutti i campi in modo corretto! </div>
							</c:if>
							<c:if test="${existingEmail}">
								<div class="alert alert-warning"> Esiste già un utente con l'email che hai inserito! L'email deve essere unica.  </div>
							</c:if>
							<span class="help-block">Completa tutti i campi per registrarti</span>
							<form role="form" name="registrationForm" method="post" action="/registration">
								<div class="form-group">
									<label for="inputName">Nome</label>
									<input type="text" name="name" class="form-control" id="inputName" placeholder="Nome" required>
								</div>
								<div class="form-group">
									<label for="inputSurname">Cognome</label>
									<input type="text" name="surname" class="form-control" id="inputSurame" placeholder="Cognome" required>
								</div>
								<div class="form-group">
									<label for="inputEmail2">Email</label>
									<input type="email" name="email" class="form-control" id="inputEmail2" placeholder="E-mail" required>
								</div>
								<div class="form-group">
									<label for="inputPassword2">Password</label>
									<input type="password" name="password" class="form-control" id="inputPassword2" placeholder="Password" required>
								</div>
								<button type="submit" class="btn btn-primary">Registrazione</button>
							</form>
						</div>
					</div>
				</div>
			</c:if>
		</div>
	</div>
	
	
	
	<div class="modal fade" id="pswReset" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title">Password Reset</h4>
				</div>
				<form role="form" method="post" action="/home" >
					<div class="modal-body">
						<div class="form-group">
							<label for="inputEmail3">Email</label>
							<input type="email" name="emailReset" class="form-control" id="inputEmail3" placeholder="E-mail" required>
						</div>
						<input type="hidden" name="reset" value="send">
					</div>
					<div class="modal-footer">
						<button type="submit" class="btn btn-primary">Invia</button>
					</div>
				</form>
			</div><!-- /.modal-content -->
		</div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	</jsp:body>
</t:template>