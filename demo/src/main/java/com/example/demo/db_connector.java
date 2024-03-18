package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;


public class db_connector {
    private String servername = "localhost";
    private String username = "root";
    private String password = "";
    private String dbname = "rubrica";
    private Connection conn;

    public db_connector() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://"+ servername +":3306/"  + dbname, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateTable(String table, Map<String, Object> fields, Map<String, Object> where) {
        String sql = "UPDATE " + table + " SET ";
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            sql += entry.getKey() + " = ?, ";
        }
        sql = sql.substring(0, sql.length() - 2); // Remove the last comma and space
        if (where != null && !where.isEmpty()) {
            sql += " WHERE ";
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                sql += entry.getKey() + " = ? AND ";
            }
            sql = sql.substring(0, sql.length() - 5); // Remove the last ' AND '
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            for (Object value : fields.values()) {
                stmt.setObject(i++, value);
            }
            if (where != null && !where.isEmpty()) {
                for (Object value : where.values()) {
                    stmt.setObject(i++, value);
                }
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public ResultSet readTable(String table, Map<String, Object> where) {
        String sql = "SELECT * FROM " + table;
        if (where != null && !where.isEmpty()) {
            sql += " WHERE ";
            for (String field : where.keySet()) {
                sql += field + " = ? AND ";
            }
            sql = sql.substring(0, sql.length() - 5); // Remove the last ' AND '
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            int i = 1;
            if (where != null && !where.isEmpty()) {
                for (Object value : where.values()) {
                    stmt.setObject(i++, value);
                }
            }
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(String table, Map<String, Object> where) {
        String sql = "DELETE FROM " + table + " WHERE ";
        for (String field : where.keySet()) {
            sql += field + " = ? AND ";
        }
        sql = sql.substring(0, sql.length() - 5); // Remove the last ' AND '

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            for (Object value : where.values()) {
                stmt.setObject(i++, value);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(String table, Map<String, Object> values) {
        String sql = "INSERT INTO " + table + " (";
        String placeholders = " VALUES (";
        for (String field : values.keySet()) {
            sql += field + ", ";
            placeholders += "?, ";
        }
        sql = sql.substring(0, sql.length() - 2) + ")"; // Remove the last comma and space
        placeholders = placeholders.substring(0, placeholders.length() - 2) + ")"; // Remove the last comma and space
        sql += placeholders;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = 1;
            for (Object value : values.values()) {
                stmt.setObject(i++, value);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<String> findDistinctGroupNames(int id_user) {
        String sql = "SELECT DISTINCT group_name FROM contact WHERE id_user = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id_user);
            ResultSet rs = stmt.executeQuery();
            List<String> groupNames = new ArrayList<>();
            while (rs.next()) {
                groupNames.add(rs.getString("group_name"));
            }
            return groupNames;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}





