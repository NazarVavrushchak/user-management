package com.nazar.usermanagement.repository;

import com.nazar.usermanagement.DTO.RoleDTO;
import com.nazar.usermanagement.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleJdbc {
    private static Connection connection;

    @Value("${spring.datasource.url}")
    private static String dbUrl;

    @Value("${spring.datasource.username}")
    private static String dbUsername;

    @Value("${spring.datasource.password}")
    private static String dbPassword;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(dbUrl , dbUsername , dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Role findById(Long id) {
        Role role = null;
        String sql = "SELECT * FROM roles WHERE id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                role = new Role();
                role.setRoleId(resultSet.getLong("role_id"));
                role.setRole(Role.RoleType.valueOf(resultSet.getString("role")));
            }
        }catch(SQLException e){
            throw new RuntimeException();
        }
        return role;
    }

    public void save(Role role) {
        String sql = "INSERT INTO roles (role) VALUES (?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, role.getRole().name());
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException();
        }
    }

    public List<Role> findAll(){
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT * FROM roles";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Role role = new Role();
                role.setRoleId(resultSet.getLong("role_id"));
                role.setRole(Role.RoleType.valueOf(resultSet.getString("role")));
                roles.add(role);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roles;
    }

    public void addRole(Role role){
        String sql = "INSERT INTO roles (role_id , role) VALUES (?,?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, role.getRoleId());
            preparedStatement.setString(2, role.getRole().name());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException();
        }
    }
}