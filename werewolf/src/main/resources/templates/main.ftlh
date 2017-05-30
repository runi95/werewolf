<#import "websocketmaster.ftlh" as m/>
<@m.master>
	<div>
		<nav class="custom-nav">
			<ul>
				<li class="active"><span>Profile</span></li>
				<li><span>Lobby</span></li>
				<li class="custom-nav-right custom-nav-text">User: <b>${username}</b></li>
			</ul>
		</nav>
		<br>
		<div class="form-group">
    					<div class="absolute-center">
    				    <form class="form-horizontal" action="/lobby" id="joinLobbyForm" name="joinLobbyForm" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
<fieldset>
    	<div class="form-group">
      		<div class="input-group">
      			<span class="input-group-addon"><i></i></span>
      			<input id="nickname" name="nickname" type="text" placeholder="Nickname" class="form-control" maxlength="16" autocomplete="off" autofocus>
      		</div>	
			<dd class="text-muted">- leave game ID empty to create a new lobby -</dd>
			<#if error??>
            	<p class="text-danger">${error}</p>
        	</#if>
      		<div class="input-group">
      			<span class="input-group-addon"><i></i></span>
				<input id="gameid" name="gameid" type="text" placeholder="Game ID" class="form-control" autocomplete="off" pattern="[A-Za-z0-9]{5}">
			</div>
            <button id="joinbutton" name="joinbutton" class="btn btn-info">Join</button>
            </fieldset>
        				</form>
		</div>
		</div>
	</div>
</@m.master>