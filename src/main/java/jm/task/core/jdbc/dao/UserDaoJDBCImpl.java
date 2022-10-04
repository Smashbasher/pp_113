package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    Connection connection = null;
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        connection = Util.getConnection();
        try (Statement stateCreation = connection.createStatement()) {
            stateCreation.executeUpdate("CREATE TABLE IF NOT EXISTS users " +
                    "(id BIGSERIAL PRIMARY KEY, name VARCHAR(255), last_name VARCHAR(255), age INT)");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        connection = Util.getConnection();
        try (Statement stateDrop = connection.createStatement()) {
            stateDrop.executeUpdate("DROP TABLE IF EXISTS users");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        connection = Util.getConnection();
        try (PreparedStatement stateSaving = connection.prepareStatement("INSERT INTO users (name, last_name, age) VALUES (?, ?, ?)")) {
            stateSaving.setString(1, name);
            stateSaving.setString(2, lastName);
            stateSaving.setByte(3, age);
            stateSaving.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        connection = Util.getConnection();
        try (PreparedStatement sateRemovement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            sateRemovement.setLong(1, id);
            sateRemovement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        connection = Util.getConnection();
        List<User> allUsers = new ArrayList<>();
        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM users")) {
            while(resultSet.next()) {
                User user = new User(resultSet.getString("name"),
                        resultSet.getString("last_name"), resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                allUsers.add(user);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allUsers;
    }

    public void cleanUsersTable() {
        connection = Util.getConnection();
        try (Statement stateClean = connection.createStatement()) {
            stateClean.executeUpdate("TRUNCATE TABLE users");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
