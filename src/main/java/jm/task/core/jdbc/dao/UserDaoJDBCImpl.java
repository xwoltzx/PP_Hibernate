package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection = Util.getConnection();
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String query = "CREATE TABLE IF NOT EXISTS Users (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), lastName VARCHAR(255), age INT);";
        try (Statement statement = connection.createStatement();){
            statement.execute(query);
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка в создании таблицы");
        }
    }

    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS Users;";
        try (Statement statement = connection.createStatement();){
            statement.execute(query);
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка в удалении таблицы");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO Users (name, lastName, age) VALUES(?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query);){
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка в добавлении в таблицу");
        }
    }

    public void removeUserById(long id) {
        String query = "DELETE FROM Users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка в удалении из таблицы");
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM Users;";
        try (Statement statement = connection.createStatement();) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User tempUser = new User(resultSet.getString(2), resultSet.getString(3), resultSet.getByte(4));
                tempUser.setId(resultSet.getLong(1));
                userList.add(tempUser);
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка в получении данных из таблицы");
        }
        return userList;
    }

    public void cleanUsersTable() {
        String query = "DELETE FROM Users;";
        try (Statement statement = connection.createStatement();) {
            statement.execute(query);
        } catch (SQLException exception) {
            throw new RuntimeException("Ошибка в удалении данных из таблицы");
        }
    }
}
