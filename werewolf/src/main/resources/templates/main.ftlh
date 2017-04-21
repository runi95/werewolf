<#import "master.ftlh" as m/>
<@m.master>
	<div class="site-wrapper">
		<div class="site-wrapper-inner">
    		<div class="cover-container">
    			<div class="inner cover">
    				<div class="form-group">
    				    <form class="form-horizontal" action="/lobby" id="joinLobbyForm" name="joinLobbyForm" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <fieldset>
      							<div class="input-group">
      								<span class="input-group-addon"><i></i></span>
      								<input id="nickname" name="nickname" type="text" placeholder="Nickname" class="form-control" maxlength="16" autocomplete="on" autofocus>
      							</div>	

                                <dd class="text-muted">- leave game ID empty to create a new lobby -</dd>

								<#if error??>
            						<p class="text-danger">${error}</p>
        						</#if>

      							<div class="input-group">
      								<span class="input-group-addon"><i></i></span>
   								    <input id="gameid" name="gameid" type="text" placeholder="Game ID" class="form-control" autocomplete="off" pattern="[A-Za-z0-9]{5}">
      							</div>

      							<div class="form-group">
            						<label class="control-label" for="joinbutton"></label>
                					<button id="joinbutton" name="joinbutton" class="btn btn-info">Join</button>
        						</div>
        					</fieldset>
        				</form>
    				</div>
    			</div>
    		</div>
    	</div>
    </div>
</@m.master>