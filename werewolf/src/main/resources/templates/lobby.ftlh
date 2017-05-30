<#import "websocketmaster.ftlh" as m/>
<@m.master>
	<div id="lobby" class="hide">
		<nav class="custom-nav">
			<ul>
				<li id="lobbyref" class="active"><span onclick="loadLobby()">Lobby</span></li>
				<li id="optionsref"><span onclick="loadOptions()">Options</span></li>
			</ul>
		</nav>
		<div id="lobbydiv" class="show">
			<div class="page-header">
    			<h1>Lobby code: <b class="text-primary">${gamecode}</b></h1>
    		</div>
    		<ul id="plist" class="list-group"></ul>
			<button id="btnready" class="btn btn-success btn-block" onclick="changeReadyState()">Ready (0/4)</button>
		</div>
		<div id="optionsdiv" class="hide"></div>
	</div>
	<div id="game" class="hide">
		<nav class="custom-nav">
			<div class="phase-div">
				<img id="dayphase" src="css/img/Werewolf-Day.png" class="phase phase-out">
				<img id="nightphase" src="css/img/Werewolf-Night.png" class="phase phase-out">
				<img id="deadphase" src="css/img/Werewolf-Dead.png" class="phase phase-out">
				<img id="wonphase" src="css/img/Werewolf-Won.png" class="phase phase-out">
				<img id="lostphase" src="css/img/Werewolf-Lost.png" class="phase phase-out">
			</div>
			<ul>
        		<li><span id="actionref" onclick="loadAction()">Action</span></li>
            	<li><span id="logref" onclick="loadLog()">Log</span></li>
            	<li><span id="graveref" onclick="loadGraveyard()">Graveyard</span></li>
            	<li><span id="roleref" onclick="loadRole()">Role</span></li>
			</ul>
			<div id="actiondiv" class="show">
        		<div id="dayactiondiv" class="hide">
           			<table class="table table-condensed">
               			<thead>
                   			<tr>
                   				<th class="text-center col-xs-6">Name</th>
                   				<th class="text-center col-xs-3">Lynch</th>
							</tr>
						</thead>
						<tbody id="votelist"></tbody>
					</table> 
				</div>
				<div id="nightactiondiv" class="hide">
           			<table class="table table-condensed">
               			<thead>
                   			<tr>
                   				<th class="text-center col-xs-6">Name</th>
                   				<th class="text-center col-xs-3">Action</th>
							</tr>
						</thead>
						<tbody id="nightactionlist"></tbody>
					</table> 
				</div>
				<div id="noactiondiv" class="show">
				</div>
			</div>
			<div id="logdiv" class="hide">
				<div id="loglist"></div>
			</div>
			<div id="gravediv" class="hide">
				<table class="table table-condensed">
					<thead>
						<tr>
                   			<th class="text-center col-xs-6">Name</th>
                   			<th class="text-center col-xs-3">Role</th>
                   		</tr>
					</thead>
					<tbody id="gravelist"></tbody>
				</table>
			</div>
			<div id="rolediv" class="hide">
				<h1 class="panel-title"><b id="rolename"></b></h1>
				<table id="roletable" class="table table-condensed">
			</div>
		</nav>
	</div>
	<script src="js/app.js"></script>
</@m.master>