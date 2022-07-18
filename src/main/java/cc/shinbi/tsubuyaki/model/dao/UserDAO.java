package cc.shinbi.tsubuyaki.model.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;
import cc.shinbi.tsubuyaki.model.Const;
import cc.shinbi.tsubuyaki.model.entity.User;

public class UserDAO extends DAO<User> {
	public UserDAO(Connection connection)
			throws SQLException, NoSuchAlgorithmException {
		super("users", connection);
		this.initialize();
	}
		
	@Override
	protected User createEntity(ResultSet resultSet) throws SQLException {
		User user = new User();

		user.setId(resultSet.getInt("id"));
		user.setAccount(resultSet.getNString("account"));
		user.setName(resultSet.getString("name"));
		user.setAdmin(resultSet.getBoolean("is_admin"));
		user.setPassword(resultSet.getString("password"));
		user.setSalt(resultSet.getString("salt"));;
		user.setCreatedAt(resultSet.getTimestamp("created_at"));
		user.setUpdatedAt(resultSet.getTimestamp("updated_at"));
		
		return user;
	}
	
	private static String createHash(String password, String salt, String pepper)
				throws NoSuchAlgorithmException {
		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
		String string = password + salt + pepper;
		
		byte[] bytes = sha256.digest(string.getBytes());
		String hash = String.format(
				"%040x",
				new BigInteger(1, bytes)
		);
		
		return hash;
	}
	
	public User addUser(String account, String name, String password, boolean isAdmin)
			throws SQLException, NoSuchAlgorithmException {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		String salt = UUID.randomUUID().toString();
		String hash = UserDAO.createHash(password, salt, Const.PEPPER);
		
		String sql = "INSERT INTO users (account, name, password, "
				+ "salt, is_admin, created_at, updated_at) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setString(1, account);
		statement.setString(2, name);
		statement.setString(3, hash);
		statement.setString(4, salt);
		statement.setBoolean(5, isAdmin);
		statement.setTimestamp(6, now);
		statement.setTimestamp(7, now);
		
		statement.execute();
		statement.close();
		User user = this.findNew();
		return user;
	}
	
	private void initialize() throws SQLException, NoSuchAlgorithmException {
		if(this.count() == 0) {
			this.addUser(
					Const.DEFAULT_USER_ACCOUNT,
					Const.DEFAULT_USER_NAME,
					Const.DEFAULT_USER_PASSWORD,
					true
				);

		}
	}
	
	public User findByAccount(String account) throws SQLException {
		User user = null;
		
		String sql = "SELECT * FROM users WHERE account = ?";
		
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setString(1, account);
		
		ResultSet resultSet = statement.executeQuery();
		if(resultSet.next()) {
			user = this.createEntity(resultSet);
		}
		resultSet.close();
		statement.close();
		return user;
	}
	
	public User login(String account, String password)
			throws SQLException, NoSuchAlgorithmException {
		User user = this.findByAccount(account);

		if(user != null) {
			String salt = user.getSalt();
			String hash = UserDAO.createHash(password, salt, Const.PEPPER);
			if(!hash.equals(user.getPassword())) {
				user = null;
			}
		}
		
		return user;
	}
	
	public User updateUser(int id, String account, String name, boolean isAdmin)
			throws SQLException {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		String sql = "UPDATE users SET account = ?, name = ?, is_admin = ?, "
				+ "updated_at = ? WHERE id = ?";
		
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setString(1, account);
		statement.setString(2, name);
		statement.setBoolean(3, isAdmin);
		statement.setTimestamp(4, now);
		statement.setInt(5, id);
		
		statement.execute();
		statement.close();
		
		User user = this.findById(id);
		return user;
	}
	
	public User updatePassword(int id, String password)
			throws SQLException, NoSuchAlgorithmException {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		User user = this.findById(id);
		String salt = user.getSalt();
		String hash = UserDAO.createHash(password, salt, Const.PEPPER);
		

		String sql = "UPDATE users SET password = ?, updated_at = ? WHERE id = ?";
		
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setString(1, hash);
		statement.setTimestamp(2, now);
		statement.setInt(3, id);
		
		statement.execute();
		statement.close();
		
		user = this.findById(id);
		return user;
	}
}