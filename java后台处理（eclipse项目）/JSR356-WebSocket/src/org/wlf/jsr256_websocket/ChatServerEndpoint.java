package org.wlf.jsr256_websocket;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * @author wlf(Andy)
 * @datetime 2016-02-16 09:28 GMT+8
 * @email 411086563@qq.com
 */
@ServerEndpoint(value = "/websocket/{user}")
public class ChatServerEndpoint {

	private static Set<Session> sessions = new HashSet<Session>();
	private Session session;

	@OnOpen
	public void open(Session session, @PathParam(value = "user") String user) {

		this.session = session;
		sessions.add(this.session);

		sendToAll(session.getRequestURI() + " 进入房间 ！");

		System.out.println(session.getRequestURI() + " 进入房间 ！");
	}

	@OnClose
	public void close() {

		sessions.remove(session);

		sendToAll(session.getRequestURI() + " 离开房间 ！");

		System.out.println(session.getRequestURI() + " 离开房间 ！");
	}

	@OnMessage
	public void message(String message) {

		sendToAll("[" + session.getRequestURI() + "]" + message);

		System.out.println("[" + session.getRequestURI() + "]" + message);
	}

	// 发送给所有的聊天者
	private void sendToAll(String text) {

		for (Session client : sessions) {
			synchronized (client) {
				client.getAsyncRemote().sendText(text);
			}
		}
	}

}
