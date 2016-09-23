package br.com.amil.a5.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.amil.a5.model.Message;
import br.com.amil.a5.model.NickName;
import br.com.amil.a5.model.SendMessage;
import br.com.amil.a5.model.StatisticUser;
import br.com.amil.a5.model.StatisticWeapon;
import br.com.amil.a5.model.UserSession;
import br.com.amil.a5.util.JsonUtility;

@ApplicationScoped
@ServerEndpoint("/game")
public class Server {
	private static final HashMap<String,UserSession> users = new HashMap<String,UserSession>(); 
	private static final HashMap<String,Integer> weaponsAttack = new HashMap<String,Integer>();
	private static final HashMap<String,Integer> weaponsKills = new HashMap<String,Integer>();
	
	private static final HashMap<String,Integer> userKills = new HashMap<String,Integer>();
	private static final HashMap<String,Integer> userAttacks = new HashMap<String,Integer>();
	private static final HashMap<String,Integer> userKilleds = new HashMap<String,Integer>();
	
    @SuppressWarnings("rawtypes")
	@OnMessage
    public String onMessage(String message, Session s) {
		GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
    	
    	Iterator itUsers;
        Message messageReturn = new Message();
        messageReturn.setAction("OK");
        
        Message messageObj = gson.fromJson(message, Message.class);
        if (messageObj.getAction().equals("statistics_users")) {
        	HashMap<String,StatisticUser> listUsers = new HashMap<String,StatisticUser>(); 
        	itUsers = userAttacks.entrySet().iterator();
        	while (itUsers.hasNext()) {
        		Entry thisEntry = (Entry) itUsers.next();
        		String nickName = (String) thisEntry.getKey();
        		Integer quant	= (Integer) thisEntry.getValue();
        		
        		StatisticUser userStat = new StatisticUser();
        		if (listUsers.containsKey(nickName))
        			userStat = listUsers.get(nickName);
        		userStat.setNickName(nickName);
        		userStat.setAttack(quant);
        		
        		listUsers.put(nickName, userStat);
        	}
        	
        	itUsers = userKills.entrySet().iterator();
        	while (itUsers.hasNext()) {
        		Entry thisEntry = (Entry) itUsers.next();
        		String nickName = (String) thisEntry.getKey();
        		Integer quant	= (Integer) thisEntry.getValue();
        		
        		StatisticUser userStat = new StatisticUser();
        		if (listUsers.containsKey(nickName))
        			userStat = listUsers.get(nickName);
        		userStat.setNickName(nickName);
        		userStat.setKills(quant);

        		listUsers.put(nickName, userStat);
        	}
        	
        	itUsers = userKilleds.entrySet().iterator();
        	while (itUsers.hasNext()) {
        		Entry thisEntry = (Entry) itUsers.next();
        		String nickName = (String) thisEntry.getKey();
        		Integer quant	= (Integer) thisEntry.getValue();
        		
        		StatisticUser userStat = new StatisticUser();
        		if (listUsers.containsKey(nickName))
        			userStat = listUsers.get(nickName);
        		userStat.setNickName(nickName);
        		userStat.setKilleds(quant);

        		listUsers.put(nickName, userStat);
        	}
        	
        	List<StatisticUser> listStatistics = new ArrayList<StatisticUser>();
        	itUsers = listUsers.entrySet().iterator();
        	while (itUsers.hasNext()) {
        		Entry thisEntry = (Entry) itUsers.next();
        		listStatistics.add((StatisticUser) thisEntry.getValue());
        	}
        	
    		Message messageSend = new Message();
    		try {
        		messageSend.setAction("statistics_users_ack");
        		messageSend.setData(listStatistics.toArray(new StatisticUser[] { }));
    			s.getBasicRemote().sendText(JsonUtility.getJson(messageSend));
			} catch (IOException e) {
				e.printStackTrace();
			}
        } else if (messageObj.getAction().equals("statistics_weapons")) {
        	HashMap<String,StatisticWeapon> listWeapons = new HashMap<String,StatisticWeapon>(); 
        	itUsers = weaponsAttack.entrySet().iterator();
        	while (itUsers.hasNext()) {
        		Entry thisEntry = (Entry) itUsers.next();
        		String weapon = (String) thisEntry.getKey();
        		Integer quant	= (Integer) thisEntry.getValue();
        		
        		StatisticWeapon userStat = new StatisticWeapon();
        		if (listWeapons.containsKey(weapon))
        			userStat = listWeapons.get(weapon);
        		userStat.setWeapon(weapon);
        		userStat.setAttack(quant);
        		
        		listWeapons.put(weapon, userStat);
        	}
        	
        	itUsers = weaponsKills.entrySet().iterator();
        	while (itUsers.hasNext()) {
        		Entry thisEntry = (Entry) itUsers.next();
        		String weapon = (String) thisEntry.getKey();
        		Integer quant	= (Integer) thisEntry.getValue();
        		
        		StatisticWeapon userStat = new StatisticWeapon();
        		if (listWeapons.containsKey(weapon))
        			userStat = listWeapons.get(weapon);
        		userStat.setWeapon(weapon);
        		userStat.setKills(quant);
        		
        		listWeapons.put(weapon, userStat);
        	}
        	
        	List<StatisticWeapon> listStatistics = new ArrayList<StatisticWeapon>();
        	itUsers = listWeapons.entrySet().iterator();
        	while (itUsers.hasNext()) {
        		Entry thisEntry = (Entry) itUsers.next();
        		listStatistics.add((StatisticWeapon) thisEntry.getValue());
        	}
        	
    		Message messageSend = new Message();
    		try {
        		messageSend.setAction("statistics_weapons_ack");
        		messageSend.setData(listStatistics.toArray(new StatisticWeapon[] { }));
    			s.getBasicRemote().sendText(JsonUtility.getJson(messageSend));
			} catch (IOException e) {
				e.printStackTrace();
			}
        } else if (messageObj.getAction().equals("register")) {
        	NickName nickName = gson.fromJson(message, NickName.class);
        	itUsers = users.entrySet().iterator();
        	while (itUsers.hasNext()) {
        		Entry thisEntry = (Entry) itUsers.next();
        		UserSession userLogged = (UserSession) thisEntry.getValue();
        		if (userLogged.getNickName() != null && userLogged.getNickName().equals(nickName.getNickName())) {
            		Message messageSend = new Message();
            		messageSend.setAction("error");
            		messageSend.setData("NickName já utilizado!");
            		try {
            			userLogged.getSession().getBasicRemote().sendText(JsonUtility.getJson(messageSend));
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
        	        return "";
        		}
        	}
        	
        	if (users.containsKey(s.getId())) {
        		UserSession user = users.get(s.getId());
        		user.setNickName(nickName.getNickName());
        		user.setWeapon(nickName.getWeapon());

        		try {
	        		users.put(s.getId(), user);
	        		messageReturn.setAction("register_ack");
					s.getBasicRemote().sendText(JsonUtility.getJson(messageReturn));
				} catch (IOException e) {
					e.printStackTrace();
				}

            	itUsers = users.entrySet().iterator();
            	while (itUsers.hasNext()) {
            		Entry thisEntry = (Entry) itUsers.next();
            		UserSession userLogged = (UserSession) thisEntry.getValue();
            		if (userLogged.getNickName() != null && !userLogged.getNickName().equals(nickName.getNickName())) {
	            		try {
	                		Message messageSend = new Message();
	                		messageSend.setAction("register_info");
	                		messageSend.setData(user.getNickName());
							userLogged.getSession().getBasicRemote().sendText(JsonUtility.getJson(messageSend));

	                		messageSend = new Message();
	                		messageSend.setAction("register_info");
	                		messageSend.setData(userLogged.getNickName());
	                		s.getBasicRemote().sendText(JsonUtility.getJson(messageSend));
	            		} catch (IOException e) {
							e.printStackTrace();
						}
            		}
            	}        		
        	}
        } else if (messageObj.getAction().equals("unregister")) {
        	NickName nickName = gson.fromJson(message, NickName.class);
        	unRegister(nickName, s);
        } else if (messageObj.getAction().equals("attack")) {
        	SendMessage sendKill = gson.fromJson(message, SendMessage.class);

        	int quant = 0;
        	if (weaponsAttack.containsKey(sendKill.getWeapon()))
        		quant = weaponsAttack.get(sendKill.getWeapon());
        	quant++;
        	weaponsAttack.put(sendKill.getWeapon(), quant);

        	quant = 0;
        	if (userAttacks.containsKey(sendKill.getNickName()))
        		quant = userAttacks.get(sendKill.getNickName());
        	quant++;
        	userAttacks.put(sendKill.getNickName(), quant);
        	
        	itUsers = users.entrySet().iterator();
        	while (itUsers.hasNext()) {
        		Entry thisEntry = (Entry) itUsers.next();
        		UserSession userLogged = (UserSession) thisEntry.getValue();
        		if (userLogged.getNickName().equals(sendKill.getMessageTo())) {
            		try {
                		SendMessage messageSend = new SendMessage();
                		messageSend.setAction("attack_receive");
                		messageSend.setData("[" + sendKill.getNickName() + "]: atacou você.");
                		messageSend.setNickName(sendKill.getNickName());
                		messageSend.setWeapon(sendKill.getWeapon());
                		userLogged.getSession().getBasicRemote().sendText(JsonUtility.getJson(messageSend));
                		break;
            		} catch (IOException e) {
						e.printStackTrace();
					}
        		}
        	}        		
    	} else if (messageObj.getAction().equals("send_message")) {
        	SendMessage sendMessage = gson.fromJson(message, SendMessage.class);
        	itUsers = users.entrySet().iterator();
        	while (itUsers.hasNext()) {
        		Entry thisEntry = (Entry) itUsers.next();
        		UserSession userLogged = (UserSession) thisEntry.getValue();
        		if (userLogged.getNickName().equals(sendMessage.getMessageTo()) || sendMessage.getMessageTo().equals("*")) {
            		try {
                		Message messageSend = new Message();
                		messageSend.setAction("message_receive");
                		messageSend.setData("[" + sendMessage.getNickName() + "]: " + sendMessage.getMessage());
                		userLogged.getSession().getBasicRemote().sendText(JsonUtility.getJson(messageSend));
                		break;
            		} catch (IOException e) {
						e.printStackTrace();
					}
        		}
        	}        		
    	} else if (messageObj.getAction().equals("defense")) {
        	SendMessage nickName = gson.fromJson(message, SendMessage.class);
        	itUsers = users.entrySet().iterator();
        	while (itUsers.hasNext()) {
        		Entry thisEntry = (Entry) itUsers.next();
        		UserSession userLogged = (UserSession) thisEntry.getValue();
        		if (userLogged.getNickName().equals(nickName.getMessageTo())) {
            		try {
                		Message messageSend = new Message();
                		messageSend.setAction("message_receive");
                		messageSend.setData("[" + nickName.getNickName() + "]: se defendeu de um ataque recebido.");
                		userLogged.getSession().getBasicRemote().sendText(JsonUtility.getJson(messageSend));
                		break;
            		} catch (IOException e) {
						e.printStackTrace();
					}
        		}
        	}        		
    	} else if (messageObj.getAction().equals("dead_attack")) {
        	SendMessage nickName = gson.fromJson(message, SendMessage.class);

        	int quant = 0;
        	if (weaponsKills.containsKey(nickName.getWeapon()))
        		quant = weaponsKills.get(nickName.getWeapon());
        	quant++;
        	weaponsKills.put(nickName.getWeapon(), quant);

        	quant = 0;
        	if (userKilleds.containsKey(nickName.getNickName()))
        		quant = userKilleds.get(nickName.getNickName());
        	quant++;
        	userKilleds.put(nickName.getNickName(), quant);

        	quant = 0;
        	if (userKills.containsKey(nickName.getMessageTo()))
        		quant = userKills.get(nickName.getMessageTo());
        	quant++;
        	userKills.put(nickName.getMessageTo(), quant);
        	
        	itUsers = users.entrySet().iterator();
        	while (itUsers.hasNext()) {
        		Entry thisEntry = (Entry) itUsers.next();
        		UserSession userLogged = (UserSession) thisEntry.getValue();
        		try {
        			Message messageSend = new Message();
	        		if (!userLogged.getNickName().equals(nickName.getNickName())) {
	        			messageSend.setAction("message_receive");
                		messageSend.setData("[" + nickName.getNickName() + "]: não se defendeu ataque recebido.");
	        		} else {
	        			messageSend.setAction("message_receive");
                		messageSend.setData("Você não se defendeu a tempo de um ataque e seu jogo acabou.");
	        		}
            		userLogged.getSession().getBasicRemote().sendText(JsonUtility.getJson(messageSend));
        		} catch (IOException e) {
					e.printStackTrace();
				}
        	}        		
        	unRegister(nickName, s);
    	}

        return JsonUtility.getJson(messageReturn);
    }

    @OnOpen
    public void onOpen(Session s) {
    	if (!users.containsKey(s.getId())) { 
    		UserSession userSession = new UserSession();
    		userSession.setSession(s);
    		
    		users.put(s.getId(), userSession);  
    	}
    	try {
    		Message message = new Message();
    		message.setAction("register");
    		
			s.getBasicRemote().sendText(JsonUtility.getJson(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
       
    @OnClose
    public void onClose(CloseReason c) {
    }
       
    @OnError
    public void onError(Throwable t) {
    }
    
    @SuppressWarnings("rawtypes")
	private void unRegister(NickName nickName, Session s) {
    	Iterator itUsers;
        Message messageReturn = new Message();
		try {
			UserSession userSession = users.get(s.getId());
			userSession.setNickName("");
			userSession.setWeapon("");
			
        	users.put(s.getId(), userSession);
    		messageReturn.setAction("unregister_ack");
			s.getBasicRemote().sendText(JsonUtility.getJson(messageReturn));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	itUsers = users.entrySet().iterator();
    	while (itUsers.hasNext()) {
    		Entry thisEntry = (Entry) itUsers.next();
    		UserSession userLogged = (UserSession) thisEntry.getValue();
    		if (userLogged.getNickName() != null && !userLogged.getNickName().equals(nickName.getNickName())) {
        		try {
            		Message messageSend = new Message();
            		messageSend.setAction("unregister_info");
            		messageSend.setData(nickName.getNickName());
					userLogged.getSession().getBasicRemote().sendText(JsonUtility.getJson(messageSend));
        		} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}        		
    }
}
