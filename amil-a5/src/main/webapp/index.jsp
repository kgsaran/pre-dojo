<html>
	<head>
		<link rel="stylesheet" type="text/css" href="bootstrap-3.3.7-dist/css/bootstrap.min.css" />
		<link rel="stylesheet" type="text/css" href="bootstrap-3.3.7-dist/css/bootstrap-theme.min.css" />
		<style>
			section {
			    width: 80%;
			    height: 200px;
			    margin: auto;
			    padding: 10px;
			}
			div#div-login {
			    width: 50%;
			    height: 200px;
			    float: left;
			}
			div#div-controls {
			    margin-left: 15%;
			    height: 200px;
			}		
		</style>
		<script type="text/javascript" src="js/jquery-3.1.0.min.js"></script>
		<script type="text/javascript" src="js/jquery.formatDateTime.min.js"></script>
		<script type="text/javascript" src="bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
		<script type="text/javascript">
			var timeOutDefense = null;
			var nickNameAttack = "";
			var weaponAttack = "";
	        function openConnection() {
	            if (conn.readyState === undefined || conn.readyState > 1) {
	            	var wsImpl = window.WebSocket || window.MozWebSocket;
	
	            	conn = new wsImpl('ws://'+ document.location.host + document.location.pathname + 'game');
	                conn.onmessage = function (event) {
						var message = eval('(' + event.data + ')');
		                
		                if (message.action == "register") {
			                $("#logMessage").val($("#logMessage").val() + '\n' + $.formatDateTime('dd/mm/yy hh:ii:ss', new Date()) + ' - Aguardando seu registro de usuário.');
		                    $("#div-login").show();
		                    $("#btnLogout").attr("disabled","disabled");
			            } else if (message.action == "register_ack") {
			                $("#logMessage").val($("#logMessage").val() + '\n' + $.formatDateTime('dd/mm/yy hh:ii:ss', new Date()) + ' - Usuário registrado com sucesso.');
		                    $("#div-controls").show();
		                    $("#div-login").find("input").each(function( index ) {
			                    $(this).attr("disabled","disabled");
		                    });
		                    $("#btnLogout").removeAttr("disabled");
			            } else if (message.action == "unregister_ack") {
			                $("#logMessage").val($("#logMessage").val() + '\n' + $.formatDateTime('dd/mm/yy hh:ii:ss', new Date()) + ' - Usuário desregistrado com sucesso.');
		                    $("#div-controls").hide();
		                    $("#div-login").find("input").each(function( index ) {
			                    $(this).removeAttr("disabled");
		                    });
		                    $("#btnLogout").attr("disabled","disabled");

		    				$('#cbUsers')
		    			    .empty()
		    			    .append($('<option>', {
		                        value: '*',
		                        text: '-- Todos --'
		                    }));
			            } else if (message.action == "register_info") {
			                $("#logMessage").val($("#logMessage").val() + '\n' + $.formatDateTime('dd/mm/yy hh:ii:ss', new Date()) + ' - Usuário [' + message.data + '] acabou de se logar.');
			                $("#cbUsers").append($('<option>', {
			                    value: message.data,
			                    text: message.data
			                }));
			            } else if (message.action == "unregister_info") {
			                $("#logMessage").val($("#logMessage").val() + '\n' + $.formatDateTime('dd/mm/yy hh:ii:ss', new Date()) + ' - Usuário [' + message.data + '] acabou de se deslogar.');
			                $("#cbUsers option[value='" + message.data + "']").remove();
			            } else if (message.action == "message_receive") {
			                $("#logMessage").val($("#logMessage").val() + '\n' + $.formatDateTime('dd/mm/yy hh:ii:ss', new Date()) + ' - Mensagem: ' + message.data);
			                $("#txtMessage").val("");
			            } else if (message.action == "error") {
			                $("#logMessage").val($("#logMessage").val() + '\n' + $.formatDateTime('dd/mm/yy hh:ii:ss', new Date()) + ' - Erro: ' + message.data);
			                $("#txtMessage").val("");
			            } else if (message.action == "attack_receive") {
			                $("#logMessage").val($("#logMessage").val() + '\n' + $.formatDateTime('dd/mm/yy hh:ii:ss', new Date()) + ' - Você foi atacado por [' + message.nickName + '] com a arma [' + message.weapon +']');
			                $("#btnDefense").show();

							nickNameAttack = message.nickName;
							weaponAttack = message.weapon;
			                
			                timeOutDefense = setTimeout("deadAttack('" + nickNameAttack + "','" + weaponAttack + "');",5000);
			            }
	                };
	                conn.onopen = function (event) {
	                };
	                conn.onerror = function (event) {
	                };
	                conn.onclose = function (event) {
	                };
	            }
	        }

	        function deadAttack(nickNameAttack, weaponAttack) {
				var data = {
					action : "dead_attack",
					nickName: $("#txtNickName").val(),
					messageTo: nickNameAttack,
					weapon: weaponAttack
				};
				conn.send(JSON.stringify(data));
		    }

            $(document).ready(function () {
                window.open('', '_self', '');
                window.moveTo(0, 0);
                window.resizeTo(screen.availWidth, screen.availHeight);

                conn = {}, window.WebSocket = window.WebSocket || window.MozWebSocket;

                $("#div-login").hide();
                $("#div-controls").hide();

                openConnection();

				$("#btnLogin").bind( "click",function(event) {
					if ($("#txtNickName").val() == "") {
						alert("Você deve utilizar um NickName válido!");
						return false;
					}
					if ($("#txtWeapon").val() == "") {
						alert("Você deve utilizar um Arma válida!");
						return false;
					}

					var data = {
						action : "register",
						nickName: $("#txtNickName").val(),
						weapon: $("#txtWeapon").val()
					};
					conn.send(JSON.stringify(data));
				});
				
				$("#btnLogout").bind("click",function(event) {
					var data = {
						action : "unregister",
						nickName: $("#txtNickName").val(),
						weapon: $("#txtWeapon").val()
					};
					conn.send(JSON.stringify(data));
				});
				
				$("#cbUsers").append($('<option>', {
                    value: '*',
                    text: '-- Todos --'
                }));
                
				$("#btnMessage").bind("click",function(event) {
					var data = {
						action : "send_message",
						nickName: $("#txtNickName").val(),
						weapon: $("#txtWeapon").val(),
						messageTo: $("#cbUsers").val(),
						message: $("#txtMessage").val()
					};
					conn.send(JSON.stringify(data));

					$("#txtMessage").val("");
				});

				$("#btnAttack").bind("click",function(event) {
					var data = {
						action : "attack",
						nickName: $("#txtNickName").val(),
						weapon: $("#txtWeapon").val(),
						messageTo: $("#cbUsers").val()
					};
					conn.send(JSON.stringify(data));
				});

				$("#btnDefense").bind("click",function(event) {
					var data = {
						action : "defense",
						nickName: $("#txtNickName").val(),
						messageTo: nickNameAttack,
						weapon: weaponAttack
					};
					conn.send(JSON.stringify(data));
					clearTimeout(timeOutDefense);

					$("#btnDefense").hide();
				});


				$("#btnDefense").hide();
            });
		</script>
	</head>
	<body>
		<div>
			<textarea rows="10" style="width: 100%;" id="logMessage" readonly>
			</textarea>
		</div>
		<section>
		    <div id="div-login">
				<fieldset>
					<legend>Registro</legend>
					<label>NickName: <input id="txtNickName" type="text"/></label>
					<label>Arma: <input id="txtWeapon" type="text"/></label>
					<br/>
					<input type="button" id="btnLogin" value="Login" />
					<input type="button" id="btnLogout" value="Logout" />
				</fieldset>
		    </div>
		    <div id="div-controls">
				<fieldset>
					<legend>Controles</legend>
					<label>Usuários logados: <select id="cbUsers" style="width: 200px;"></select></label>
					<label>Mensagem para o usuário selecionado: <input id="txtMessage" type="text"/>&nbsp;<input type="button" id="btnMessage" value="Enviar" /></label>
					<input type="button" id="btnAttack" value="Atacar" />
					<input type="button" id="btnDefense" value="Defender" />
				</fieldset>
		    </div>
		</section>
	</body>
</html>
