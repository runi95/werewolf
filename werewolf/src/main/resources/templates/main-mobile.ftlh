<#import "websocketmaster.ftlh" as m/>
<@m.master>
<div class="container">
    <div style="margin-top: 60px; margin-bottom: 20px;"></div>
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
            <div class="form-group">
                <form role="form" class="form-horizontal" onsubmit="submitJoinLobbyForm(); return false;"
                      id="joinLobbyForm" name="joinLobbyForm">
                    <div class="row">
                        <div class="col-xs-10 col-xs-offset-1">
                            <div id="nicknamediv" class="form-group">
                                <input id="nicknamefield" name="nickname" type="text" placeholder="Nickname"
                                       class="form-control" maxlength="16" autocomplete="off" autofocus>
                                <span id="nicknameglyph"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-10 col-xs-offset-1">
                            <div id="gameiddiv" class="form-group">
                                <input id="gameidfield" name="gameid" type="text" placeholder="Game ID"
                                       class="form-control" autocomplete="off" pattern="[A-Za-z0-9]{5}">
                                <span id="gameidglyph"></span>
                            </div>
                        </div>
                    </div>
                </form>
                <div class="row">
                    <div class="col-xs-10 col-xs-offset-1">
                        <div class="col-xs-6">
                            <button id="createbutton" onclick="createLobbyButton()" name="createbutton"
                                    class="btn btn-default btn-block">Create
                            </button>
                        </div>
                        <div class="col-xs-6">
                            <button type="submit" form="joinLobbyForm" id="joinbutton" name="joinbutton"
                                    class="btn btn-info btn-block">Join
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Start of Open Lobby table -->
            <div class="mainlobbydiv row">
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
            <!-- End of Open Lobby table -->

            <hr>

            <!-- Start of User Stats -->
            <div class="row">
                <div class="mainlobbydiv medieval-text">
                    Username: <span id="profilename"></span><br>
                    Games: <span id="profilegames"></span><br>
                    Wins: <span id="profilewins"></span><br>
                    Losses: <span id="profilelosses"></span><br>
                    Win Rate: <span id="profilewinrate"></span><br>
                </div>
            </div>
            <!-- End: User Stats -->

            <hr>

            <!-- Start: Rules -->
            <div class="row">
                <div id="gameplayrules" class="mainlobbydiv">
                    <h3>Gameplay</h3>
                ${gameplaytext}
                </div>
            </div>
            <!-- End: Rules -->

        </div>
        <!-- End: Main Page -->

        <!-- Start of Create Lobby -->
        <div id="createlobbydiv" class="hide">
            <form role="form" class="form-horizontal" onsubmit="submitCreateLobbyForm(); return false;"
                  id="createLobbyForm" name="createLobbyForm">
                <div class="mainlobbydiv medieval-text">
                    User Preferences:
                </div>
                <div class="row"
                     style="margin: 20px 20px auto auto; max-width: 300px;">
                    <input id="createnicknamefield" name="createnickname" type="text" placeholder="Nickname"
                           class="form-control fancy-input" maxlength="16" autocomplete="off">
                </div>
                <div class="mainlobbydiv medieval-text">
                    Game mode:
                </div>
                <ul class="fancy-radio-ul">
                    <#list gameplaymodes as modes>
                        <li>
                            <input id="${modes}opt" type="radio" name="mode" value="${modes}" checked>
                            <label class="medieval-text" for="${modes}opt">${modes}</label>

                            <div class="check"></div>
                        </li>
                    </#list>
                </ul>
                <div class="mainlobbydiv medieval-text">
                    Game Settings:
                </div>
                <div class="fancy-checkbox">
                    <input id="openlobbycheck" type="checkbox" name="open" value="open">
                    <label id="openlobbychecklabel" class="medieval-text" for="openlobbycheck"></label>
                </div>

                <div class="container">
                    <label style="font-weight: 300; font-size: 1.35em;" class="medieval-text control-label col-xs-8"
                           for="maxplayercount">Max Players:</label>
                    <div class="col-xs-4">
                        <input type="number" min="4" max="20" value="15" class="form-control fancy-input"
                               id="maxplayercount">
                    </div>
                </div>
                <div style="padding-top: 20px">
                    <a href="javascript:loadJoinLobby();" class="btn btn-default">Back</a>
                    <button id="createbutton" name="createbutton" class="btn btn-info">Create</button>
                </div>
            </form>
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
                <div class="page-header">
                    <h1>Players</h1>
                </div>
                <ul id="plist" class="list-group"></ul>
                <button id="btnready" class="btn btn-success btn-block" onclick="changeReadyState()">Ready (0/4)
                </button>
                <div class="page-header">
                    <h1>Chat</h1>
                </div>
                <div id="lobbychatlist" class="chat"></div>
                <form role="form" class="form-horizontal" onsubmit="sendLobbyChatMessageForm(); return false;"
                      id="lobbychat" name="Lobby Chat">
                    <div class="input-group">
                        <input id="lobbychat" name="lobbychatinput" title="Chat" type="text"
                               class="form-control fancy-input" autocomplete="off" required="">
                        <div class="input-group-btn">
                            <button id="sendbuttonlobby" name="sendbuttonlobby" class="btn btn-info">Send</button>
                        </div>
                    </div>
                </form>
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

    <!-- Start of Game -->

    <div id="game" class="hide" data-spy="scroll" data-target=".navbar" data-offset="60">
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
                        <li><a id="actionref" href="#actiondiv">Action</a></li>
                        <li><a id="logref" href="#chatdiv">Log</a></li>
                        <li><a id="graveref" href="#gravediv">Graveyard</a></li>
                        <li><a id="roleref" href="javascript:void(0)" onclick="showRole()">Role</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Start of Action div -->

        <div id="actiondiv" class="container-fluid">
            <h3>Day/Night</h3>
            <div id="dayactiondiv">
                <div class="container">
                    <table class="table table-condensed">
                        <thead>
                        <tr>
                            <th class="text-center col-xs-6">Name</th>
                            <th class="text-center col-xs-3">Action</th>
                        </tr>
                        </thead>
                        <tbody id="actionlist"></tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- End of Action div -->

        <hr>

        <!-- Start of Chat div -->

        <div id="chatdiv" class="container-fluid">
            <h3>Chat</h3>

            <div id="chatlist" class="chat"></div>
            <form role="form" class="form-horizontal" onsubmit="sendChatMessageForm(); return false;"
                  id="ingamechat" name="In Game Chat">
                <div class="input-group">
                    <input id="chat" name="chatinput" title="Chat" type="text"
                           class="form-control fancy-input" autocomplete="off" required="">
                    <div class="input-group-btn">
                        <button id="sendbutton" name="sendbutton" class="btn btn-info">Send</button>
                    </div>
                </div>
            </form>
        </div>

        <!-- End of Chat div -->

        <hr>

        <!-- Start of Grave div -->

        <div id="gravediv" class="container-fluid">
            <h3>Graveyard</h3>
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

        <!-- End of Grave div -->

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
    </div>
</div>
</@m.master>