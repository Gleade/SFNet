/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfnet.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Joel
 */
public class Packet
{
    private ByteBuffer m_buffer;
    
    /**
     * Keep track of our current buffer size.
     */
    private int m_currentSize;
    
    private int m_readPos;

    
    public Packet()
    {
        m_currentSize = 0;
        m_readPos = 0;
        
    }
    
    public Packet(ByteBuffer buffer)
    {
        m_currentSize = buffer.capacity();
        m_buffer = buffer;
        m_buffer.flip();
        m_buffer.order(ByteOrder.LITTLE_ENDIAN);
        m_readPos = 0;
    }
    
    /**
     * Write 1 byte to the buffer.
     * @param value 
     */
    public void writebyte(byte value)
    {
        // Write 1 byte to the buffer
        
        increaseBuffer(1);
        
        m_buffer.put((byte)value);
    }
    
    /**
     * Write 2 bytes to the buffer.
     * @param value 
     */
    public void writeshort(short value)
    {
        // Write 2 bytes to the buffer
        increaseBuffer(2);
        
        m_buffer.putShort((short) value);
    }
    
    /**
     * Write a string to our packet buffer.
     * @param str 
     */
    public void writestring(String str)
    {
        // Increase the buffer by string length * 2 (char = 2 bytes each)
        // + 2 bytes for the string size
        increaseBuffer((str.length() * 2) + 2);
        
       // System.out.println("Prior size: " + ((str.length() * 2)+2));
        
        // Write the size of our string to the buffer
        m_buffer.putShort((short)(str.length() + 1));
        
        // Write each char to the buffer
        for(int i=0; i<str.length(); i++)
        {
            m_buffer.putChar(str.charAt(i));

        }
    
    }
    
    /**
     * Clear the buffer.
     */
    public void clear()
    {
        if(m_buffer != null)
            m_buffer.clear();
    }
    
    private void increaseBuffer(int size)
    {
        // Increase total size of the buffer
        m_currentSize += size;
       
        // Store the data to write to our new buffer
        if(m_buffer != null)
        {
            m_buffer.flip();
            byte [] data = new byte[m_buffer.capacity()];
            m_buffer.get(data);

            // Re-allocate a new buffer of the new size
            m_buffer = ByteBuffer.allocate(m_currentSize);
            m_buffer.order(ByteOrder.LITTLE_ENDIAN);

            // Write the old data to the byte buffer
            //m_buffer.put(data, 0, data.length);
            m_buffer.position(0);
            m_buffer.put(data);
            
        }
        else
        {
            m_buffer = ByteBuffer.allocate(size);
            m_buffer.order(ByteOrder.LITTLE_ENDIAN);
        }
        
        
        //System.out.println("[Packet][BBUFF Size: " + m_buffer.capacity());
     
    }
    
    public byte readbyte()
    {
        byte result = m_buffer.get(m_buffer.position());
        m_buffer.position(m_buffer.position() + 1);
        return result;
    }
    
    /**
     * Read a string from the packet buffer.
     * @return 
     */
    public String readstring()
    {
        String finalStr = "";
        int strSize = readshort();
        for(int i=0; i < strSize-1; i++)
        {
            char tempChar =  m_buffer.getChar();
            finalStr += String.valueOf(tempChar);
        }
        
        
        
        return finalStr;
    }
    
    /**
     * Read a short from the packet buffer.
     * @return 
     */
    public short readshort()
    {
        if(m_buffer != null)
        {
            return (short)m_buffer.getShort();
        }
        else
        {
            return -1;
        }
    }
    
    
    // Return the bute buffer as array
    private byte[] getBufferArray()
    {
        return m_buffer.array();
    }
    
    /**
     * Send our packet to the output stream.
     * @param tcpOutP
     * @throws IOException 
     */
    public ByteBuffer get()
    {
        return m_buffer;
    }
    
    /**
     * Print the buffer.
     */
    public void print()
    {
        System.out.print(m_buffer.toString());
    }
}
