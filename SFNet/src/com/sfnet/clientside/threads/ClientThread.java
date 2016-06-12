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
package com.sfnet.clientside.threads;

import com.sfnet.clientside.ClientStarter;
import com.sfnet.utils.Packet;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
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
            // Check if the connection was closed via the Client Starter
            if(!ClientStarter.m_internalConnected)
            {
                try
                {
                    m_socket.close();
                } catch (IOException ex)
                {
                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
            
                sendPacketStack();

                // Clear the byte buffer of any old data
                m_bbuffer.clear();



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
            buff.position(0);
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
