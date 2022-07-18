package cc.shinbi.tsubuyaki.model;

import cc.shinbi.tsubuyaki.model.entity.Message;
import cc.shinbi.tsubuyaki.model.entity.User;

public class Pair {
	private User user;
	private Message message;

	public Pair(User user, Message message) {
		this.user = user;
		this.message = message;
	}
	
	public User getUser() {
		return user;
	}
	
	public Message getMessage() {
		return message;
	}

}
