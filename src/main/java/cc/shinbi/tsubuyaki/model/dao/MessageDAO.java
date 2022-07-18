package cc.shinbi.tsubuyaki.model.dao;

import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.shinbi.tsubuyaki.model.Const;
import cc.shinbi.tsubuyaki.model.Image;
import cc.shinbi.tsubuyaki.model.Pair;
import cc.shinbi.tsubuyaki.model.entity.Message;
import cc.shinbi.tsubuyaki.model.entity.User;
import cc.shinbi.tsubuyaki.util.StringUtil;

public class MessageDAO extends DAO<Message> {
	public MessageDAO(Connection connection) throws SQLException, NoSuchAlgorithmException {
		super("messages", connection);
		this.initialize();
	}

	@Override
	protected Message createEntity(ResultSet resultSet) throws SQLException {
		Message message = new Message();

		message.setId(resultSet.getInt("id"));
		message.setUserId(resultSet.getInt("user_id"));
		message.setText(resultSet.getString("text"));
		message.setPublicMessage(resultSet.getBoolean("is_public"));
		message.setImageFileName(resultSet.getString("image_file_name"));
		message.setCreatedAt(resultSet.getTimestamp("created_at"));
		message.setUpdatedAt(resultSet.getTimestamp("updated_at"));

		return message;
	}

	public Message addMessage(int userId, String text, boolean isPublic) throws SQLException {
		Timestamp now = new Timestamp(System.currentTimeMillis());

		String sql = "INSERT INTO messages (user_id, text, is_public, " + "created_at, updated_at)"
				+ "VALUES(?, ?, ?, ?, ?)";

		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setInt(1, userId);
		statement.setString(2, StringUtil.escapeHtml(text));
		statement.setBoolean(3, isPublic);
		statement.setTimestamp(4, now);
		statement.setTimestamp(5, now);

		statement.execute();
		statement.close();

		Message message = this.findNew();
		return message;
	}

	private void initialize() throws SQLException, NoSuchAlgorithmException {
		if (this.count() == 0) {
			UserDAO userDao = new UserDAO(this.connection);
			List<User> users = userDao.findAll();
			if (users.size() > 0) {
				User user = users.get(0);
				this.addMessage(user.getId(), Const.FIRST_MESSAGE, true);
			}
		}
	}

	private List<Pair> createPairs(List<Message> messages) throws SQLException, NoSuchAlgorithmException {
		List<Pair> list = new ArrayList<Pair>();
		UserDAO userDao = new UserDAO(this.connection);
		List<User> users = userDao.findAll();

		Map<Integer, User> userMap = new HashMap<Integer, User>();
		for (User user : users) {
			user.setPassword(null);
			user.setSalt(null);
			userMap.put(user.getId(), user);
		}

		for (Message message : messages) {
			User user = userMap.get(message.getUserId());
			Pair pair = new Pair(user, message);
			list.add(pair);
		}

		return list;
	}

	public List<Pair> findVisible(int userId) throws SQLException, NoSuchAlgorithmException {
		List<Message> list = new ArrayList<Message>();
		String sql = "SELECT * FROM messages WHERE user_id = ? OR is_public = ? " + "ORDER BY created_at DESC LIMIT 50";
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setInt(1, userId);
		statement.setBoolean(2, true);
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			Message message = this.createEntity(resultSet);
			list.add(message);
		}
		resultSet.close();
		statement.close();
		List<Pair> pairs = this.createPairs(list);
		return pairs;
	}

	public Message updateMessage(int id, String text, boolean isPublic) throws SQLException {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE messages SET text = ?, is_public = ?, updated_at = ? " + "WHERE id = ?";
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setString(1, StringUtil.escapeHtml(text));
		statement.setBoolean(2, isPublic);
		statement.setTimestamp(3, now);
		statement.setInt(4, id);
		statement.execute();
		statement.close();
		Message message = this.findById(id);

		return message;
	}

	public void removeImage(int id) throws SQLException {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE messages SET image_file_name = ?, image = ?, updated_at = ? "

				+ "WHERE id = ?";

		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setNull(1, Types.VARCHAR);
		statement.setNull(2, Types.BLOB);
		statement.setTimestamp(3, now);
		statement.setInt(4, id);
		statement.execute();
		statement.close();
	}

	public void setImage(int id, String fileName, InputStream stream) throws SQLException {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		String sql = "UPDATE messages SET image_file_name = ?, image = ?, updated_at = ? "

				+ "WHERE id = ?";

		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setString(1, fileName);
		statement.setBlob(2, stream);
		statement.setTimestamp(3, now);
		statement.setInt(4, id);
		statement.execute();
		statement.close();
	}

	public Image getImage(int id) throws SQLException {
		String sql = "SELECT image, image_file_name FROM messages WHERE id = ?";
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setInt(1, id);
		Image image = null;
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {
			String fileName = resultSet.getString("image_file_name");
			Blob blob = resultSet.getBlob("image");
			if (blob != null) {
				image = new Image(fileName, blob.getBinaryStream());
			}
		}
		resultSet.close();
		statement.close();
		return image;
	}

}
