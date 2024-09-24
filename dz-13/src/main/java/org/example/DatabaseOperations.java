package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseOperations {

    private static final Logger logger = Logger.getLogger(DatabaseOperations.class.getName());

    // Отримання з'єднання з базою даних
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword()
        );
    }

    // Вставка нового користувача в базу даних
    public static void insertUser(String name, String email) {
        String query = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            logger.log(Level.INFO, "User inserted: {0} - {1}", new Object[]{name, email});
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting user", e);
        }
    }

    // Вибірка всіх користувачів з бази даних
    public static void selectUsers() {
        String query = "SELECT * FROM users";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                logger.log(Level.INFO, "User ID: {0}, Name: {1}, Email: {2}", new Object[]{id, name, email});
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error selecting users", e);
        }
    }

    // Оновлення даних користувача за ідентифікатором
    public static void updateUser(int id, String name, String email) {
        String query = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            logger.log(Level.INFO, "User updated with ID: {0}", id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating user", e);
        }
    }

    // Видалення користувача за ідентифікатором
    public static void deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            logger.log(Level.INFO, "User deleted with ID: {0}", id);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting user", e);
        }
    }

    // Основний метод для виконання CRUD операцій
    public static void main(String[] args) {
        // Унікальні користувачі для вставки
        insertUser("Alexander Hamilton", "alex.hamilton@example.com");
        insertUser("Eliza Schuyler", "eliza.schuyler@example.com");

        logger.info("Users after insertion:");
        selectUsers();

        // Оновлення першого користувача
        updateUser(1, "Alex Hamilton", "alexander.h@example.com");

        // Видалення другого користувача
        deleteUser(2);

        logger.info("Users after update and deletion:");
        selectUsers();
    }
}
