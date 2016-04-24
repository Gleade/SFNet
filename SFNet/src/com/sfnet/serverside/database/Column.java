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
