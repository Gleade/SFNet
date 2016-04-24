/*
    SFNet (Simple Fast Networking) - Copyright (c) Joel Craig (Gleade)

    This software is provided 'as-is', without any express or implied warranty.
    In no event will the authors be held liable for any damages arising from
    the use of this software.

    Permission is granted to anyone to use this software for any purpose,
    including commercial applications, and to alter it and redistribute it
    freely, subject to the following restrictions:

    1. The origin of this software must not be misrepresented; you must not claim
       that you wrote the original software. If you use this software in a product,
       an acknowledgment in the product documentation would be appreciated but is
       not required.

    2. Altered source versions must be plainly marked as such, and must not be
       misrepresented as being the original software.

    3. This notice may not be removed or altered from any source distribution

 */

package com.sfnet.serverside.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

