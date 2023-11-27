package com.document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class CachedDocument implements Document {
    private String path;
    private Connection conn;

    public CachedDocument(String path) {
        this.path = path;
        this.conn = connect();
    }

    private Connection connect() {
        String url = "jdbc:sqlite:documents.db";
        
        if(this.conn != null) {
            return this.conn;
        }

        try {
            this.conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        createNewTableIfNotExist();
        return conn;
    }

    private void createNewTableIfNotExist() {
        String sql = "CREATE TABLE IF NOT EXISTS documents (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	path text NOT NULL,\n"
                + "	text text NOT NULL\n"
                + ");";
        try (Connection conn = this.connect();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getIfAlreadyExists() {
        String sql = "SELECT text FROM documents WHERE path = ?";
        String result = null;
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, path);
            ResultSet rs = pstmt.executeQuery();
            result = rs.getString("text");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public void cacheDocument(String text) {
        String sql = "INSERT INTO documents(path, text) VALUES(?, ?)";
        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, path);
            pstmt.setString(2, text);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String parse() {
        String result = getIfAlreadyExists();
        if (result == null) {
            result = new CachedDocument(path).parse();
            cacheDocument(result);
            System.out.println("Cached document");
        }
        return result;
    }
}
