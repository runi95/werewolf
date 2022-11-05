<#import "/spring.ftl" as spring>
<#import "master.ftlh" as m/>
<@m.master>
<@spring.bind "accountRegisterForm" />
	<div class="site-wrapper">
		<div class="site-wrapper-inner">
    		<div class="cover-container">
    			<div class="inner cover">
					<form class="form-horizontal" id="accountRegisterForm" name="accountRegisterForm" method="post">
    					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    					<fieldset>
    						<#list spring.status.errorMessages as error>
            				<span class="error">${error}</span>
            				<br>
        					</#list>
        					
        					<div class="form-group">
            					<label for="username">Username</label>
            					<div class="input-group">
            						<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                					<input id="username" name="username" type="text" placeholder="Username" class="form-control" required="">
            					</div>
        					</div>
        	
        					<!-- Password input-->
        					<div class="form-group">
            					<label for="password">Password</label>
            					<div class="input-group">
            						<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                					<input id="password" name="password" type="password" placeholder="Password" class="form-control" required="">
           		 					<input id="passwordRepeated" name="passwordRepeated" type="password" placeholder="Repeat Password" class="form-control" required="">
           		 				</div>
        					</div>
        					
        					<div class="form-group">
            					<a href="/login" class="btn btn-default">Return</a>
                				<button id="registerbutton" name="registerbutton" class="btn btn-info">Register</button>
            				</div>
            				
						</fieldset>
					</form>
				</div>
			</div>
		</div>
	</div>

</@m.master>