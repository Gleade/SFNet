/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfnet.serverside.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Joel
 */
public class Column
{
    ArrayList<String> m_rowData;
    int m_count = 1;
    
    /**
     * Create a column that stores row data from a ResultSet.
     * @param rs
     * @throws SQLException 
     */
    public Column(ResultSet rs) throws SQLException
    {
        m_rowData = new ArrayList<String>();
        m_count = 1;
        boolean finished = false;
        
        
        // Add row data into our our list from the result set
        while(!finished)
        {
            String rowData = null;
            try
            {
                rowData = rs.getString(m_count);
            }
            catch(Exception e)
            {
                
            }
            
            if(rowData != null)
            {
                System.out.println(rowData);
                m_rowData.add(rowData);
                m_count++;
            }
            else
            {
                finished = true;
            }

            
        }
    }
    
    /**
     * Get row data as a String.
     * @param row
     * @return 
     */
    public String getRowAsString(int row)
    {
        if(row <= m_rowData.size() && row >= 0)
        {
            return m_rowData.get(row);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Get row data as an Integer.
     * @param row
     * @return 
     */
    public Integer getRowAsInt(int row)
    {
         if(row <= m_rowData.size() && row >= 0)
        {
            return Integer.parseInt(m_rowData.get(row));
        }
        else
        {
            return null;
        }       
    }

    /**
     * Get row count for this column.
     * @return 
     */
    public int getRowCount()
    {
        return m_count;
    }
}
