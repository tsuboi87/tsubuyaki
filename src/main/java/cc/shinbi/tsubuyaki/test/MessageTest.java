package cc.shinbi.tsubuyaki.test;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

import cc.shinbi.tsubuyaki.model.dao.MessageDAO;
import cc.shinbi.tsubuyaki.model.entity.Message;
import cc.shinbi.tsubuyaki.util.DbUtil;

class MessageTest {
	
	@Test

	public void testMessages() throws ClassNotFoundException, SQLException, 
NoSuchAlgorithmException {
		Connection connection = DbUtil.connect();
		MessageDAO dao = new MessageDAO(connection);
		
		List<Message> list = dao.findAll(); 
		for(Message message : list) {
			System.out.println(message.getText());
		}
	}
}
