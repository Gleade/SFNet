/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfnet.clientside.threads;

import com.sfnet.clientside.ClientStarter;
import com.sfnet.utils.Packet;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joel
 */
public class ClientThread implements Runnable
{
    SocketChannel m_socket;
    Packet m_packet;
    ByteBuffer m_bbuffer;
    
    public ClientThread(String ipAddress, int port)
    {
        m_bbuffer = ByteBuffer.allocate(ClientStarter.MAX_BUFFER_SIZE);
        
        try
        {
            m_socket = SocketChannel.open();
        } catch (IOException ex)
        {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            // Connect to the server
            m_socket.connect(new InetSocketAddress(ipAddress, port));
            
            // Wait for the connection to finalize
            while(!m_socket.finishConnect());
      
            m_packet = new Packet();
            m_packet.writebyte((byte)25);

            sendTcp(m_packet);
            
        } catch (IOException ex)
        {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(m_socket.isConnected())
        {
            System.out.println("Connected to server @ " + ipAddress + ":" + port);
        }
    }

    @Override
    public void run()
    {
        while(m_socket.isOpen())
        {
            
            try
            {
                int bytesAvailible = m_socket.read(m_bbuffer);
             

                if(bytesAvailible > 0)
                {
                    Packet packet = new Packet(m_bbuffer);
                    System.out.println("A new packet was received.");
                    System.out.println(packet.readshort());
                }

            } catch (IOException ex)
            {
                System.out.println("Unable to receive from server");
                Logger.getLogger(ClientStarter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    private void sendTcp(Packet packet)
    {
        try
        {
            ByteBuffer buff = packet.get();
            buff.flip();
            while (buff.hasRemaining()) 
            {
                System.out.println(m_socket.write(buff));
            }
        } catch (IOException ex)
        {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
