package es.urjc.dad.poshart.service;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import es.urjc.dad.poshart.model.User;

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
}
