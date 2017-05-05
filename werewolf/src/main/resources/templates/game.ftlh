<#import "websocketmaster.ftlh" as m/>
<@m.master>
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
    <script src="js/game.js"></script>
</@m.master>