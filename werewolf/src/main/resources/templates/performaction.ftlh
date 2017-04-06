<#import "master.ftlh" as m/>
<@m.master>
	<div class="site-wrapper">
		<div class="site-wrapper-inner">
    		<div class="cover-container">
    			<div class="inner cover">
    				<div class="form-group">
    					<div class="alert alert-success" id="countdowntimer">30</div>
    					<form class="form-horizontal" id="joinGameForm" name="joinGameForm" method="post">
      						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      						<fieldset>
      							<div class="input-group">
      								<span class="input-group-addon"><i></i></span>
      								<input id="nickname" name="nickname" type="text" placeholder="Nickname" class="form-control">
      							</div>	
      					
      							<div class="input-group">
      								<span class="input-group-addon"><i></i></span>
      								<input id="gameid" name="gameid" type="text" placeholder="Game ID" class="form-control" required="">
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
    <script>
   	 	var secondsleft = 30;
		var x = setInterval(function() {
  			document.getElementById("countdowntimer").innerHTML = secondsleft;
  			secondsleft = secondsleft - 1;
  			
  			if(secondsleft == 20) {
  				document.getElementById("countdowntimer").className = "alert alert-info";
  			}
  			
  			if(secondsleft == 10) {
  				document.getElementById("countdowntimer").className = "alert alert-danger";
  			}

  			if (secondsleft < 0) {
    			clearInterval(x);
  			}
		}, 1000);
	</script>
</@m.master>