<#import "websocketmaster.ftlh" as m/>
<@m.master>
	<div id="lobby" class="show">
		<nav class="navbar navbar-static-top">
			<ul class="breadcrumb">
        		<li id="lobbyref" class="breadcrumbs-text breadcrumbs-text-current" onclick="loadLobby()">Lobby</li>
            	<li id="optionsref" class="breadcrumbs-text" onclick="loadOptions()">Options</li>
			</ul>
			<div id="lobbydiv" class="show">
				<div class="page-header">
    				<h1>Lobby code: <b class="text-primary">${gamecode}</b></h1>
    			</div>
    			<ul id="plist" class="list-group"></ul>
				<button id="btnready" class="btn btn-success btn-block" onclick="changeReadyState()">Ready (0/4)</button>
			</div>
			<div id="optionsdiv" class="hide">
				
			</div>
		</nav>
	</div>
	<div id="game" class="hide">
		<nav class="navbar navbar-static-top">
			<ul class="breadcrumb">
        		<li id="voteref" class="breadcrumbs-text breadcrumbs-text-current" onclick="loadVote()">Vote</li>
            	<li id="logref" class="breadcrumbs-text" onclick="loadLog()">Log</li>
            	<li id="graveref" class="breadcrumbs-text" onclick="loadGraveyard()">Graveyard</li>
			</ul>
        	<div id="votediv" class="show">
           		<table class="table table-condensed">
               		<thead>
                   		<tr>
                   			<th class="text-center col-xs-6">Name</th>
                   			<th class="text-center col-xs-3">Votes</th>
						</tr>
					</thead>
					<tbody id="votelist"></tbody>
				</table> 
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
		</nav>
	</div>
	<script src="js/app.js"></script>
</@m.master>