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
import java.util.ArrayList;
import java.util.LinkedList;
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
            m_socket.configureBlocking(false);
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
            
            sendPacketStack();
            
            try
            {
                // Read our socket if possible
                int bytesAvailible = m_socket.read(m_bbuffer);
             

                if(bytesAvailible > 0)
                {
                    // Set our packets byte buffer to the read one.
                    Packet packet = new Packet(m_bbuffer);
                    
                    // Execute our listeners receive functions
                    ClientStarter.executeListeners(packet);
                }

            } catch (IOException ex)
            {
                System.out.println("Unable to receive from server");
                Logger.getLogger(ClientStarter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
       
            
        }
    }
    
    /**
     * Send all packets waiting in queue to the server.
     */
    private void sendPacketStack()
    {
        // Get our packet stack
        LinkedList<Packet> m_packets = ClientStarter.getPacketStack();
        
        // Send all packets to the server in the queue
        while(!m_packets.isEmpty())
        {
            Packet packet = m_packets.pop();
            sendTcp(packet);
            
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
                m_socket.write(buff);
            }
        } catch (IOException ex)
        {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
