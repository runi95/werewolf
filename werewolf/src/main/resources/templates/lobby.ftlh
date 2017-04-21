<#import "websocketmaster.ftlh" as m/>
<@m.master>
	<div class="site-wrapper">
		<div class="site-wrapper-inner">
    		<div class="cover-container">
    			<div class="inner cover">
    				<div class="page-header">
    					<h1>Lobby code: <b class="text-primary">${gamecode}</b></h1>
    				</div>
    				<ul id="plist" class="list-group"></ul>
					<button id="btnready" class="btn btn-success btn-block" onClick="changeReadyState()">Ready</button>
   			 		<script src="js/app.js"></script>
   			 	</div>
			</div>
   		</div>
	</div>
</@m.master>