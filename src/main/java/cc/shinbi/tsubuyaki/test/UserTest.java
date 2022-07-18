package cc.shinbi.tsubuyaki.test;


import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import cc.shinbi.tsubuyaki.model.Const;
import cc.shinbi.tsubuyaki.model.dao.UserDAO;
import cc.shinbi.tsubuyaki.model.entity.User;
import cc.shinbi.tsubuyaki.util.DbUtil;

class UserTest {

	@Test
	void testDafaultUser() 
			throws NoSuchAlgorithmException, SQLException, ClassNotFoundException {
		Connection connection = DbUtil.connect();
		UserDAO dao = new UserDAO(connection);
		
		String account = Const.DEFAULT_USER_ACCOUNT;
		String[] passwords = {Const.DEFAULT_USER_PASSWORD, "Wrong Password"};
		
		for(String password : passwords) {
			User user = dao.login(account, password);
			if(user == null) {
				System.out.println("ログイン失敗");
			}
			else {
				System.out.println("ログイン成功");
				System.out.println(
					String.format(
							"Account: %s, Name: %s, Created At: %s, Updated At: %s",
							user.getAccount(),
							user.getName(),
							user.getCreatedAt().toString(),
							user.getUpdatedAt().toString()
						)
					);	
				}
			}
		connection.close();
	}

}
