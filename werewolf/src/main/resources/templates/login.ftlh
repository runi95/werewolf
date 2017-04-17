<#import "master.ftlh" as m/>
<@m.master>
	<div class="site-wrapper">
		<div class="site-wrapper-inner">
    		<div class="cover-container">
    			<div class="inner cover">
    				<form class="form-horizontal" id="accountRegisterForm" name="accountRegisterForm" method="post">
    					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    					<fieldset>
							<#if error.isPresent()>
            					<p class="text-danger">Username or password is wrong!</p>
        					</#if>

    						<div class="form-group">
    							<label for="username">Username:</label>
      							<div class="input-group">
      								<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
      								<input id="username" name="username" type="text" placeholder="Username" autofocus class="form-control" required="">
      							</div>
    						</div>

    						<div class="form-group">
      							<label for="username">Password:</label>
      							<div class="input-group">
      								<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
      								<input id="password" name="password" type="password" palceholder="Password" class="form-control" required="">
      							</div>
    						</div>

    						<!-- Button-->
        					<div class="form-group">
            					<label class="control-label" for="loginbutton"></label>
                				<a href="/register" class="btn btn-default">Register</a>
                				<button id="loginbutton" name="loginbutton" class="btn btn-info">Sign in</button>
        					</div>

    					</fieldset>
  					</form>
       			</div>
        	</div>
		</div>
	</div>
</@m.master>