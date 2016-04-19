/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import com.sfnet.serverside.database.Column;
import com.sfnet.serverside.database.Database;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joel
 */
public class DatabaseManager
{
    private static Database m_db;
    
    public static void connect(String ipAddress, String database, String user, String pass) throws SQLException
    {
        try
        {
            m_db = new Database(ipAddress, database, user, pass);
            m_db.executeQuery("SELECT account_username FROM accounts");
            System.out.println("Connected to database.");
        
            ArrayList<Column> data = m_db.getColumns();
            


    }   catch (ClassNotFoundException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
