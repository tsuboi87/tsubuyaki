package cc.shinbi.tsubuyaki.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cc.shinbi.tsubuyaki.model.entity.EntityBase;

public abstract class DAO<T extends EntityBase> {
	protected String tableName;
	protected Connection connection;

	public DAO(String tableName, Connection connection) {
		this.tableName = tableName;
		this.connection = connection;
	}
	public int count() throws SQLException {
		String sql = String.format(
				"SELECT COUNT(*) AS count FROM %s",
				this.tableName
			);
		
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		
		int count = 0;
		if(resultSet.next()) {
			count = resultSet.getInt("count");
		}
		
		resultSet.close();
		statement.close();
		
		return count;
	}
	
	protected abstract T createEntity(ResultSet resultSet) throws SQLException;

	public T findNew() throws SQLException {
		T entity = null;


		String sql = String.format(
			"SELECT * FROM %s ORDER BY id DESC LIMIT 1",
			this.tableName
		);
		
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);

		if(resultSet.next()) {
			entity = this.createEntity(resultSet);
		}

		resultSet.close();
		statement.close();

		return entity;
	}
	
	public List<T> findAll() throws SQLException {
		List<T> list = new ArrayList<T>();
		
		String sql = String.format(
				"SELECT * FROM %s",
				this.tableName
		);
		
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		
		while(resultSet.next()) {
			T entity = this.createEntity(resultSet);
			list.add(entity);
		}
		
		resultSet.close();
		statement.close();
		
		return list;
	}
	
	public T findById(int id) throws SQLException {
		T entity = null;
		
		String sql = String.format(
			"SELECT * FROM %s WHERE id = ?",
			this.tableName
		);
		
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setInt(1, id);
		
		ResultSet resultSet = statement.executeQuery();
		if(resultSet.next()) {
			entity = this.createEntity(resultSet);
		}
		
		resultSet.close();
		statement.close();
		
		return entity;
	}
	
	public void delete(int id) throws Exception {
		String sql = String.format(
			"DELETE FROM %s WHERE id = ?",
			this.tableName
		);
		
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.setInt(1, id);
		statement.execute();
		statement.close();
		}
}