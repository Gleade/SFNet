/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfnet.serverside;


import com.sfnet.utils.Packet;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joel
 */
public class Client
{
    private SocketChannel m_socket;
    private int m_id;
    private ByteBuffer m_bbuffer;
    private Packet m_packet;
    
    public Client(SocketChannel sock, int id) throws IOException
    {
        m_socket = sock;
        m_id = id;
        m_socket.configureBlocking(false);
        m_bbuffer = ByteBuffer.allocate(ServerStarter.MAX_BUFFER_SIZE);
    }
    
    public void receive()
    {
        try
        {
            int bytesAvailable = m_socket.read(m_bbuffer);
            
            if(bytesAvailable > 0)
            {
                m_bbuffer.order(ByteOrder.LITTLE_ENDIAN);
                Packet packet = new Packet(m_bbuffer);
                System.out.println("A new packet was received: " + packet.readbyte());
            }
            
        } catch (IOException ex)
        {
            System.out.println("Unable to receive from client: " + m_id);
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getId()
    {
        return m_id;
    }
    
    
    public void close() throws IOException
    {
        m_socket.close();
    }
}
