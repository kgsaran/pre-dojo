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
		<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="js/dataTables.bootstrap.min.js"></script>
		<script type="text/javascript">
			var gridUser = null; 
			function openConnection() {
	            if (conn.readyState === undefined || conn.readyState > 1) {
	            	var wsImpl = window.WebSocket || window.MozWebSocket;
	
	            	conn = new wsImpl('ws://'+ document.location.host + '/amil-a5/game');
	                conn.onmessage = function (event) {
						var message = eval('(' + event.data + ')');
		                
		                if (message.action == "statistics_users_ack") {
		                	$.each(message.data, function( index, value ) {
								$("#tableUser").append(
									"<tr>"+
										"<td>" + message.data[index].nickName + "</td>"+ 
										"<td>" + message.data[index].attack + "</td>"+ 
										"<td>" + message.data[index].kills + "</td>"+ 
										"<td>" + message.data[index].killeds + "</td>"+ 
									"</tr>"
								);
		                	});			                
		                } else if (message.action == "statistics_weapons_ack") {
		                	$.each(message.data, function( index, value ) {
								$("#tableWeapon").append(
									"<tr>"+
										"<td>" + message.data[index].weapon + "</td>"+ 
										"<td>" + message.data[index].attack + "</td>"+ 
										"<td>" + message.data[index].kills + "</td>"+ 
									"</tr>"
								);
		                	});			                
		                }
			        };
	                conn.onopen = function (event) {
	    				var data = {
    						action : "statistics_users"
    					};
    					conn.send(JSON.stringify(data));

	    				var data = {
    						action : "statistics_weapons"
    					};
    					conn.send(JSON.stringify(data));
	                };
	                conn.onerror = function (event) {
	                };
	                conn.onclose = function (event) {
	                };
	            }
	        }

            $(document).ready(function () {
                window.open('', '_self', '');
                window.moveTo(0, 0);
                window.resizeTo(screen.availWidth, screen.availHeight);

                conn = {}, window.WebSocket = window.WebSocket || window.MozWebSocket;

                openConnection();
            });
		</script>
	</head>
	<body>
		<section>
		    <div id="div-login">
				<fieldset>
					<legend>Usuários</legend>
					<table id="tableUser">
					  <thead>
					    <tr>
					      <th data-field="nickName" style="width: 200px;">NickName</th>
					      <th data-field="attacks"  style="width: 100px;">Ataques</th>
					      <th data-field="kills"	style="width: 100px;">Assasinatos</th>
					      <th data-field="killeds"	style="width: 100px;">Mortes</th>
					    </tr>
					  </thead>
					</table>					
				</fieldset>
		    </div>
		    <div id="div-controls">
				<fieldset>
					<legend>Armas</legend>
					<table id="tableWeapon">
					  <thead>
					    <tr>
					      <th data-field="weapon" style="width: 200px;">Arma</th>
					      <th data-field="attacks"  style="width: 100px;">Ataques</th>
					      <th data-field="kills"	style="width: 100px;">Assasinatos</th>
					    </tr>
					  </thead>
					</table>					
				</fieldset>
		    </div>
		</section>
	</body>
</html>
