package com.chuwa.redbook.service;

import com.chuwa.redbook.entity.Post;

import java.sql.*;
import java.util.Date;

public class JdbcExecutor {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://192.168.5.13:3306/redbook?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USERNAME = "chuwa";
    private static final String PASSWORD = "chuwa";

    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String input = "nihao chuwa";
        try {
            // 1, load Driver
            Class.forName(DRIVER);
            // 2， connect to Database;
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            // 3， define sql statement
            String sql = "SELECT * from posts where title = " + "'nihao chuwa'";//
            // 4, create a statement object
            stmt = conn.createStatement();
            // 5, use stmt object to execute sql statement;
            rs = stmt.executeQuery(sql); // the result is return to ResultSet
            Post post = new Post();
            while(rs.next()) {
                //Mapping: map data base result to java object.
//               Date createDateTime = Date.(rs.getString("create_date_time"));
               //U.S. User: 3/12/2025 11:00:0000AM PDT
               //India User: 2025-3-12 23:00:0000 UTC-5

                post.setId(rs.getLong("id"));
                post.setContent(rs.getString("content"));
//                post.setCreateDateTime(rs.getString("create_date_time"));
                post.setDescription(rs.getString("description"));
                post.setTitle(rs.getString("title"));
//                post.setUpdateDateTime(rs.getString("update_date_time"));
            }

        } catch (SQLException | ClassNotFoundException e) {
          e.printStackTrace();
        } finally {
            // 7, close conections and other resource.
            rs.close();
            stmt.close();
            conn.close();
        }
    }

}
