<#import "websocketmaster.ftlh" as m/>
<@m.master>
    <div id="menu">
        <nav class="custom-nav">
		    <ul>
			    <li id="profileref" class="active"><span onClick="loadProfile()">Profile</span></li>
			    <li id="openlobbyref"><span onClick="loadOpenLobby()">Lobby</span></li>
			    <li class="custom-nav-right custom-nav-text">User: <b>${username}</b></li>
		    </ul>
	    </nav>
	    <div class="container" style="padding-top:20px">
            <div id="profilediv" class="show">
                <ul class="custom-list-group">
                    <li class="custom-list-group">Username: <span id="profilename"></span></li>
                    <li class="custom-list-group">Games: <span id="profilegames"></span></li>
                    <li class="custom-list-group">Wins: <span id="profilewins"></span></li>
                    <li class="custom-list-group">Losses: <span id="profilelosses"></span></li>
                    <li class="custom-list-group">Win Rate: <span id="profilewinrate"></span>%</li>
                </ul>
            </div>
            <div id="openlobbydiv" class="hide">
                <div class="form-group">
                    <div class="absolute-center">
                        <form role="form" class="form-horizontal" onsubmit="submitJoinLobbyForm(); return false;" id="joinLobbyForm" name="joinLobbyForm">
                            <div id="nicknamediv" class="form-group">
                                <input id="nicknamefield" name="nickname" type="text" placeholder="Nickname" class="form-control" maxlength="16" autocomplete="off" autofocus>
                                <span id="nicknameglyph"></span>
                            </div>
                            <dd class="text-muted">- leave game ID empty to create a new lobby -</dd>
                            <div id="gameiddiv" class="form-group">
                                <input id="gameidfield" name="gameid" type="text" placeholder="Game ID" class="form-control" autocomplete="off" pattern="[A-Za-z0-9]{5}">
                                <span id="gameidglyph"></span>
                            </div>
                            <button id="joinbutton" name="joinbutton" class="btn btn-info">Join</button>
                        </form>
                    </div>
                </div>
                <table class="table table-condensed">
                    <thead>
                        <tr>
                            <th class="text-center">Lobby Code</th>
                            <th class="text-center">Players</th>
                        </tr>
                    </thead>
                    <tbody id="lobbytable">
                    </tbody>
                </table>
            </div>
	    </div>
	</div>
	<div id="lobby" class="hide">
        <nav class="custom-nav">
    	    <ul>
    		    <li id="lobbyref" class="active"><span onclick="loadLobby()">Lobby</span></li>
    			<li id="optionsref"><span onclick="loadOptions()">Options</span></li>
    	    </ul>
    	</nav>
    	<div id="lobbydiv" class="show">
    	    <div class="page-header">
        	    <h1>Lobby code: <b id="gamecodenode" class="text-primary"></b></h1>
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
    			<div id="noactiondiv" class="show"></div>
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
</@m.master>