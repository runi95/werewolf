<#import "websocketmaster.ftlh" as m/>
<@m.master>
	<div id="lobby" class="hide">
		<nav class="navbar navbar-inverse navbar-static-top">
			<div class="container-fluid">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>                        
					</button>
					<p class="navbar-text">${gamecode}</p>
				</div>
				<div class="collapse navbar-collapse" id="myNavbar">
					<ul class="nav navbar-nav">
						<li id="lobbyref" class="active"><p onclick="loadLobby()">Lobby</p></li>
						<li id="optionsref"><p onclick="loadOptions()">Options</p></li>
					</ul>
				</div>
			</div>
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
		<nav class="navbar navbar-static-top">
			<div class="phase-div">
				<img id="dayphase" src="css/img/Werewolf-Day.png" class="phase phase-out">
				<img id="nightphase" src="css/img/Werewolf-Night.png" class="phase phase-out">
				<img id="deadphase" src="css/img/Werewolf-Dead.png" class="phase phase-out">
				<img id="wonphase" src="css/img/Werewolf-Won.png" class="phase phase-out">
				<img id="lostphase" src="css/img/Werewolf-Lost.png" class="phase phase-out">
			</div>
			<ul class="breadcrumb">
        		<li id="actionref" class="breadcrumbs-text breadcrumbs-text-current" onclick="loadAction()">Action</li>
            	<li id="logref" class="breadcrumbs-text" onclick="loadLog()">Log</li>
            	<li id="graveref" class="breadcrumbs-text" onclick="loadGraveyard()">Graveyard</li>
            	<li id="roleref" class="breadcrumbs-text" onclick="loadRole()">Role</li>
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