<#import "websocketmaster.ftlh" as m/>
<@m.master>
<div id="menu">
    <nav class="custom-nav">
        <ul>
            <li class="custom-nav-right custom-nav-text"><a href="/logout">Logout</a></li>
        </ul>
    </nav>
    <div class="container" style="padding-top:20px;">
        <div id="joinlobbydiv" class="hide">
            <div class="col-md-5 col-md-offset-2" style="max-width: 700px">
                <div class="form-group">
                    <form role="form" class="form-horizontal" onsubmit="submitJoinLobbyForm(); return false;"
                        id="joinLobbyForm" name="joinLobbyForm">
                        <div id="nicknamediv" class="form-group">
                            <input id="nicknamefield" name="nickname" type="text" placeholder="Nickname"
                                class="form-control" maxlength="16" autocomplete="off" autofocus>
                            <span id="nicknameglyph"></span>
                        </div>
                        <dd class="text-muted">- leave game ID empty to create a new lobby -</dd>
                        <div id="gameiddiv" class="form-group">
                            <input id="gameidfield" name="gameid" type="text" placeholder="Game ID" class="form-control"
                                autocomplete="off" pattern="[A-Za-z0-9]{5}">
                            <span id="gameidglyph"></span>
                        </div>
                        <button id="joinbutton" name="joinbutton" class="btn btn-info">Join</button>
                    </form>
                </div>
                <div class="mainlobbydiv">
                <table class="table table-condensed custom-table-striped">
                    <thead>
                        <tr>
                            <th class="text-center">Lobby Code</th>
                            <th class="text-center">Players</th>
                        </tr>
                    </thead>
                    <tbody id="lobbytable"></tbody>
                </table>
                </div>
                <hr>
                </div>
            <div class="col-md-4" style="max-width: 400px">
                <div class="mainlobbydiv medieval-text">
                    Username: <span id="profilename"></span><br>
                    Games: <span id="profilegames"></span><br>
                    Wins: <span id="profilewins"></span><br>
                    Losses: <span id="profilelosses"></span><br>
                    Win Rate: <span id="profilewinrate"></span><br>
                </div>
                <hr>
                <div class="row">
                    <div id="gameplayrules" class="mainlobbydiv">
                        <h3>Gameplay</h3>
                        The game is played in cycles of day and night that can vary on the game mode you're playing, at night every player gets to perform their night action which is <em>usually</em> their strongest ability. During the day phase everyone can vote on whom to lynch and if anyone at any time get above 50% of the votes they'll get executed and have their role revealed. The evil roles are encouraged to lie and manipulate during this phase as telling them your true identity will most definitely get you lynched by the good town's people.
                    </div>
                </div>
            </div>
        </div>
        <div id="createlobbydiv" class="show">
            <div class="col-md-6 col-md-offset-3">
                <form role="form" class="form-horizontal" onsubmit="submitCreateLobbyForm(); return false;"
                    id="createLobbyForm" name="createLobbyForm">
                    <div class="mainlobbydiv medieval-text">
                        Game mode:
                    </div>
                    <ul class="fancy-radio-ul">
                        <li>
                            <input id="onenightopt" type="radio" name="game mode" value="One Night" checked>
                            <label class="medieval-text" for="onenightopt">One Night</label>

                            <div class="check"></div>
                        </li>
                        <li>
                            <input id="advancedopt" type="radio" name="game mode" value="Advanced">
                            <label class="medieval-text" for="advancedopt">Advanced</label>

                            <div class="check"></div>
                        </li>
                    </ul>
                    <div class="mainlobbydiv medieval-text">
                        Game Settings:
                    </div>
                    <div class="fancy-checkbox">
                        <input id="openlobbycheck" type="checkbox" name="open" value="open">
                        <label id="openlobbychecklabel" class="medieval-text" for="openlobbycheck"></label>
                    </div>

                    <div class="row">
                    <div class="col-xs-3 col-xs-offset-2">
                        <label style="font-weight: 300; font-size: 1.35em;" class="medieval-text">Max Players:</label>
                    </div>
                    <div class="col-xs-5">
                        <input type="number" min="4" max="20" value="15" class="form-control" id="maxplayercounter">
                    </div>
                    </div>
                    <div style="padding-top: 20px">
                    <a href="javascript:loadJoinLobby();" class="btn btn-default">Back</a>
                    <button id="createbutton" name="createbutton" class="btn btn-info">Create</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div id="lobby" class="hide">
    <nav class="custom-nav">
        <ul>
            <li id="lobbyref" class="active"><span onclick="loadLobby()">Lobby</span></li>
            <li id="optionsref"><span onclick="loadOptions()">Options</span></li>
            <li class="custom-nav-right custom-nav-text"><a href="/logout">Logout</a></li>
        </ul>
    </nav>
    <div id="lobbydiv" class="show">
        <div class="container">
            <div class="page-header">
                <h1>Lobby code: <b id="gamecodenode" class="text-primary"></b></h1>
            </div>
            <ul id="plist" class="list-group"></ul>
            <button id="btnready" class="btn btn-success btn-block" onclick="changeReadyState()">Ready (0/4)</button>
        </div>
    </div>
    <div id="optionsdiv" class="hide"></div>
</div>
<div id="game" class="hide">
    <div class="phase-div">
        <img id="dayphase" src="css/img/Werewolf-Day.png" class="phase phase-out">
        <img id="nightphase" src="css/img/Werewolf-Night.png" class="phase phase-out">
        <img id="deadphase" src="css/img/Werewolf-Dead.png" class="phase phase-out">
        <img id="wonphase" src="css/img/Werewolf-Won.png" class="phase phase-out">
        <img id="lostphase" src="css/img/Werewolf-Lost.png" class="phase phase-out">
    </div>
    <nav class="custom-nav">
        <ul>
            <li><span id="actionref" onclick="loadAction()">Action</span></li>
            <li><span id="logref" onclick="loadLog()">Log</span></li>
            <li><span id="graveref" onclick="loadGraveyard()">Graveyard</span></li>
            <li><span id="roleref" onclick="showRole()">Role</span></li>
        </ul>
    </nav>
    <div id="actiondiv" class="show">
        <div id="dayactiondiv" class="hide">
            <div class="container">
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
        </div>
        <div id="nightactiondiv" class="hide">
            <div class="container">
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
        </div>
        <div id="noactiondiv" class="show"></div>
    </div>
    <div id="logdiv" class="hide">
        <div id="loglist" class="container"></div>
    </div>
    <div id="gravediv" class="hide">
        <div class="container">
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
    </div>
    <div id="rolediv" class="hide">
        <div class="container">
            <h1 class="panel-title"><b id="rolename"></b></h1>
            <table id="roletable" class="table table-condensed"></table>
        </div>
    </div>
    <!-- Modal -->
    <div id="rolemodal" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <!-- Modal Header -->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">&times;</span>
                        <span class="sr-only">Close</span>
                    </button>
                    <h4 id="modalrolename" class="modal-title">Role</h4>
                </div>

                <!-- Modal Body -->
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-5 col-sm-3">
                            <ul id="goodrolelist" class="nav nav-pills nav-stacked"></ul>
                            <ul id="neutralrolelist" class="nav nav-pills nav-stacked"></ul>
                            <ul id="evilrolelist" class="nav nav-pills nav-stacked"></ul>
                        </div>
                        <div class="col-xs-7 col-sm-9">
                            <div class="row">
                                <p class="text-left" id="modalalignment"></p></div>
                            <div class="row">
                                <p class="text-left" id="modalgoal"></p></div>
                            <div class="row">
                                <p class="text-left" id="modaldescription" style="width: auto;"></p></div>
                        </div>
                    </div>
                </div>

                <!-- Modal Footer -->
                <div class="modal-footer">
                    <button type="button" class="center-block btn btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</@m.master>