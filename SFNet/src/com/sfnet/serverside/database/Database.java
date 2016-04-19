/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfnet.serverside.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Joel
 */
public class Database
{
    
    String m_driver = "org.gjt.mm.mysql.Driver";
    String m_user, m_pass;
    Connection m_con;
    ResultSet m_resultSet;
    
    boolean m_isConnected = false;
            
    public Database(String ipAddress, String database, String user, String pass) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        try
        {
          //create a java mysql database connection
          //String m_url = "jdbc:mysql://localhost:3306/" + database;
            
          String m_url = "jdbc:mysql://" + ipAddress + "/" + database;
          m_user = user;
          m_pass = pass;
          Class.forName("com.mysql.jdbc.Driver").newInstance();

          m_con = DriverManager.getConnection(m_url, m_user, m_pass);
          

          m_isConnected = true;


        }
        catch (SQLException e)
        {
            m_isConnected = false;
            throw new SQLException(e);
        }
    }
    
    public void close() throws SQLException
    {
        if(m_isConnected)
        {
            m_con.close();
        }
    }
    
    public void executeQuery(String query)
    {
        try
        {
            // Prepare our query and execute it
            PreparedStatement preparedStatement = m_con.prepareStatement(query);
            m_resultSet = preparedStatement.executeQuery();
            
        
            
            // Get our result

            
        } catch (SQLException ex)
        {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    public ArrayList<Column> getColumns() 
    {
        try
        {            
            ArrayList<Column> columnList = new ArrayList<Column>();
            

            
            // Set the column count
            int count = 1;
            
            // While there is a result set available, add a column to our column list
            while(m_resultSet.next())
            {
                columnList.add(new Column(m_resultSet)); 

                count++;
            }
            
            
            return columnList;
            
        } catch (SQLException ex)
        {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    

}

