package com.db;

import com.financer.Transaction;
import com.financer.TransactionType;
import com.financer.Category;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
    private static final String URL = "jdbc:postgresql://localhost:5432/beza1";

    private static final String USER = "postgres";
    private static final String PASSWORD = "be1234be";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS transactions(" +
                "id SERIAL PRIMARY KEY," +
                "type VARCHAR(10)," +
                "category VARCHAR(20)," +
                "amount DOUBLE PRECISION," +
                "transaction_date DATE)";
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Database initialized: Table is ready!");
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    public static void saveTransaction(Transaction t) {
        String sql = "INSERT INTO transactions (type,category,amount,transaction_date)VALUES(?,?,?,?)";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, t.getType().name());
            pstmt.setString(2, t.getCategory().name());
            pstmt.setDouble(3, t.getAmount());
            pstmt.setDate(4, Date.valueOf(t.getDate()));

            pstmt.executeUpdate();
            System.out.println("Transaction saved to database!");

        } catch (SQLException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    public static List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT *FROM transactions";

        try (Connection conn = getConnection();

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                TransactionType type = TransactionType.valueOf(rs.getString("type"));
                Category category = Category.valueOf(rs.getString("category"));
                double amount = rs.getDouble("amount");
                LocalDate date = rs.getDate("transaction_date").toLocalDate();
                Transaction t = new Transaction(id, type, category, amount, date);
                transactions.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error loading transaction: " + e.getMessage());
        }

        return transactions;
    }

    public static void deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction #" + id + " deleted from database!");
            } else {
                System.out.println("No transaction found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting transaction " + e.getMessage());
        }
    }
}
