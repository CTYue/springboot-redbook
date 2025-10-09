package com.chuwa.redbook.service;

import com.chuwa.redbook.entity.Post;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.ArrayList;
import java.util.List;

public class JdbcTemplateExecutor {
    public static void main(String... args) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        // Create DataSource with connection details
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.5.13:3306/redbook?allowPublicKeyRetrieval=true&useSSL=false");
        dataSource.setUsername("chuwa");
        dataSource.setPassword("chuwa");
        List postList = new ArrayList();
        jdbcTemplate.setDataSource(dataSource);
//        jdbcTemplate.query("select * from posts limit 1",(rs) -> {
//                    System.out.println("Query executed successfully!");
//                    while (rs.next()) {
//                        // Process your result set
//                        System.out.println("Row data: " + rs.getString("title"));
//                    }
//                    return null;
//                }
//        );
        postList = jdbcTemplate.query("select * from posts limit 1", new BeanPropertyRowMapper<Post>(Post.class));
        postList.forEach((e)-> System.out.println(e.toString()));
    }
}
