
package com.ess_projects.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DATABASE_CONNECTION {
    
    private final String jdbcURL = "jdbc:mysql://localhost:3306/bucketlist";
    private final String jdbcUsername = "admin";
    private final String jdbcPassword = "hELLOwORLD1";
        
    public static void main(String[] args){
        DATABASE_CONNECTION conn = new DATABASE_CONNECTION();
        System.out.println(conn.get_connection());
    }

    
    public Connection get_connection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
        }
        // TODO Auto-generated catch block
        return conn;
    }
    
    

}
