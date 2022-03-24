package es.urjc.dad.poshart.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class SessionData {
	private long userId;

	public long getUser() {
		return userId;
	}

	public void setUser(long id) {
		this.userId = id;
	}
	
	public boolean checkUser() {
		return userId>0;
	}
	
	public boolean isMine(long id) {
		return userId==id;
	}
}
