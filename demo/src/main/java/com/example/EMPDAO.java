
package com.example;

import java.sql.*;
import java.util.*;

/**
 * This class is a DAO (Data Access Object) - Use JDBC API
 * It encapsulates all the database operations for the EMP table
 */
public class EMPDAO {
    public EMP findEmployeeById(Connection conn, String eno) throws SQLException {
        String querySQL = "SELECT * FROM EMP WHERE ENO=?";
        try (PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
            pstmt.setString(1, eno);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String ename = rs.getString("ENAME");
                    String title = rs.getString("TITLE");
                    return new EMP(eno, ename, title);
                }
                return null;
            }
        }
    }

    public int addNewEmployee(Connection conn, String eno, String ename, String title) throws SQLException {
        String insertSQL = "INSERT INTO EMP VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, eno);
            pstmt.setString(2, ename);
            pstmt.setString(3, title);
            return pstmt.executeUpdate();
        }
    }

    public int updateEmployee(Connection conn, String eno, String ename, String title) throws SQLException {
        String updateSQL = "UPDATE EMP SET ENAME=?, TITLE=? WHERE ENO=?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, ename);
            pstmt.setString(2, title);
            pstmt.setString(3, eno);
            return pstmt.executeUpdate();
        }
    }

    public int deleteEmployee(Connection conn, String eno) throws SQLException {
        String deleteSQL = "DELETE FROM EMP WHERE ENO=?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, eno);
            return pstmt.executeUpdate();
        }
    }

    public List<EMP> getAllEmployees(Connection conn) throws SQLException {
        String sql = "SELECT * FROM EMP";
        List<EMP> empList = new ArrayList<>();
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String eno = rs.getString("ENO");
                String ename = rs.getString("ENAME");
                String title = rs.getString("TITLE");
                empList.add(new EMP(eno, ename, title));
            }
        }
        return empList;    
    }
}
