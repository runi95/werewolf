<#import "websocketmaster.ftlh" as m/>
<@m.master>
<div class="container">
    <div style="margin-top: 60px;"></div>
    <div id="menu">
        <nav class="navbar navbar-inverse navbar-fixed-top">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#mainnavbar">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="/">Werewolf</a>
                </div>
                <div id="mainnavbar" class="collapse navbar-collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Main Page, before joining a lobby -->
        <div id="joinlobbydiv" class="show">
            <div class="row">
                <div class="col-md-5 col-md-offset-1">
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
                                <input id="gameidfield" name="gameid" type="text" placeholder="Game ID"
                                       class="form-control"
                                       autocomplete="off" pattern="[A-Za-z0-9]{5}">
                                <span id="gameidglyph"></span>
                            </div>
                        </form>
                        <button id="createbutton" onclick="createLobbyButton()" name="createbutton"
                                class="btn btn-default">Create
                        </button>
                        <button type="submit" form="joinLobbyForm" id="joinbutton" name="joinbutton"
                                class="btn btn-info">Join
                        </button>
                    </div>
                    <div class="mainlobbydiv">
                        <table class="table table-condensed custom-table-striped">
                            <thead>
                            <tr>
                                <th class="text-center">Lobby Code</th>
                                <th class="text-center">Gamemode</th>
                                <th class="text-center">Players</th>
                            </tr>
                            </thead>
                            <tbody id="lobbytable"></tbody>
                        </table>
                    </div>

                </div>

                <div class="col-md-4 col-md-offset-1">
                    <div class="mainlobbydiv medieval-text">
                        Username: <span id="profilename"></span><br>
                        Games: <span id="profilegames"></span><br>
                        Wins: <span id="profilewins"></span><br>
                        Losses: <span id="profilelosses"></span><br>
                        Win Rate: <span id="profilewinrate"></span><br>
                    </div>
                </div>
                <!-- End: User Stats -->

            </div>

            <hr>

            <!-- Start: Gameplay -->
            <div class="row">
                <div id="gameplayrules" class="mainlobbydiv">
                    <h3>Gameplay</h3>
                ${gameplaytext}
                </div>
            </div>

            <!-- End: Gameplay -->
        </div>


        <div id="createlobbydiv" class="hide">
            <div class="col-md-6 col-md-offset-3">
                <form role="form" class="form-horizontal" onsubmit="submitCreateLobbyForm(); return false;"
                      id="createLobbyForm" name="createLobbyForm">
                    <div class="mainlobbydiv medieval-text">
                        User Preferences:
                    </div>
                    <div class="row"
                         style="margin-left: auto; margin-right: auto; margin-top: 20px; margin-bottom: 20px; max-width: 300px;">
                        <input id="createnicknamefield" name="createnickname" type="text" placeholder="Nickname"
                               class="form-control fancy-input" maxlength="16" autocomplete="off">
                    </div>
                    <div class="mainlobbydiv medieval-text">
                        Game mode:
                    </div>
                    <ul class="fancy-radio-ul">
                        <li>
                            <input id="onenightopt" type="radio" name="mode" value="One Night" checked>
                            <label class="medieval-text" for="onenightopt">One Night</label>

                            <div class="check"></div>
                        </li>
                        <li>
                            <input id="advancedopt" type="radio" name="mode" value="Advanced">
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

                    <div class="row" style="margin-left: auto; margin-right: auto; max-width: 200px;">
                        <label style="font-weight: 300; font-size: 1.35em;" class="medieval-text">Max Players:</label>
                        <input type="number" min="4" max="20" value="15" class="form-control fancy-input"
                               id="maxplayercount">
                    </div>
                    <div style="padding-top: 20px">
                        <a href="javascript:loadJoinLobby();" class="btn btn-default">Back</a>
                        <button id="createbutton" name="createbutton" class="btn btn-info">Create</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div id="lobby" class="hide">
        <nav class="navbar navbar-inverse navbar-fixed-top">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#lobbynavbar">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="/">Werewolf</a>
                </div>
                <div id="lobbynavbar" class="collapse navbar-collapse">
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <div class="container">
            <h1>Lobby code: <b id="gamecodenode" class="text-primary"></b></h1>
            <div class="row">
                <div class="col-md-4">
                    <div class="page-header">
                        <h1>Players</h1>
                    </div>
                    <ul id="plist" class="list-group"></ul>
                    <button id="btnready" class="btn btn-success btn-block" onclick="changeReadyState()">Ready (0/4)
                    </button>
                </div>
                <div class="col-md-4">
                    <div class="page-header">
                        <h1>Chat</h1>
                    </div>
                    <div id="lobbychatlist" class="chat"></div>
                    <form role="form" class="form-horizontal" onsubmit="sendLobbyChatMessageForm(); return false;"
                          id="createLobbyForm" name="createLobbyForm">
                        <div class="input-group">
                            <input id="lobbychatinputfield" name="lobbychatinput" type="text"
                                   class="form-control fancy-input" autocomplete="off" required="">
                            <div class="input-group-btn">
                                <button id="sendbuttonlobby" name="sendbuttonlobby" class="btn btn-info">Send</button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-md-4" style="max-width: 400px">
                    <div class="page-header">
                        <h1>Settings</h1>
                    </div>
                    <div class="mainlobbydiv medieval-text">
                        Gamemode: <span id="lobbystatsmode"></span><br>
                        Privacy: <span id="lobbystatsprivacy"></span><br>
                        Max Players: <span id="lobbystatsplayers"></span><br>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="game" class="hide">
        <div class="phase-div">
            <img id="dayphase" src="css/img/Werewolf-Day.png" class="phase phase-out">
            <img id="nightphase" src="css/img/Werewolf-Night.png" class="phase phase-out">
            <img id="deadphase" src="css/img/Werewolf-Dead.png" class="phase phase-out">
            <img id="wonphase" src="css/img/Werewolf-Won.png" class="phase phase-out">
            <img id="lostphase" src="css/img/Werewolf-Lost.png" class="phase phase-out">
        </div>
        <nav class="navbar navbar-inverse navbar-fixed-top">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#gamenavbar">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="javascript:void(0)" onclick="loadHome()">Werewolf</a>
                </div>
                <div id="gamenavbar" class="collapse navbar-collapse">
                    <ul class="nav navbar-nav">
                        <li><a id="roleref" href="javascript:void(0)" onclick="showRole()">Role</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
                    </ul>
                </div>
            </div>
        </nav>
        <!-- Start of Action div -->

        <div id="actiondiv">
            <div class="col-md-4">
                <div class="row">
                    <h3>Actions</h3>
                </div>
                <div id="dayactiondiv">
                    <table class="table table-condensed">
                        <thead>
                        <tr>
                            <th class="text-center col-xs-6">Name</th>
                            <th class="text-center col-xs-3">Lynch</th>
                        </tr>
                        </thead>
                        <tbody id="actionlist"></tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- End of Action div -->

        <!-- Start of chat div -->

        <div id="logdiv">
            <div class="col-md-4">
                <div class="row">
                    <h3>Chat</h3>
                </div>
                <div id="chatlist" class="chat"></div>
                <form role="form" class="form-horizontal" onsubmit="sendChatMessageForm(); return false;"
                      id="createLobbyForm" name="createLobbyForm">
                    <div class="input-group">
                        <input id="chatinputfield" name="chatinput" type="text"
                               class="form-control fancy-input" autocomplete="off" required="">
                        <div class="input-group-btn">
                            <button id="sendbutton" name="sendbutton" class="btn btn-info">Send</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <!-- Start of Grave div -->

        <div id="gravediv">
            <div class="col-md-4">
                <div class="row">
                    <h3>Graveyard</h3>
                </div>
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

        <!-- Modal -->
        <div id="rolemodal" class="modal fade" role="dialog">
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
    </div>
</div>
</@m.master>