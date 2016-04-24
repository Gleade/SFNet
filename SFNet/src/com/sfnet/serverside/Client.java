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

package com.sfnet.serverside;


import com.sfnet.clientside.threads.ClientThread;
import com.sfnet.utils.Packet;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
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
    private boolean m_isConnected;
    private ByteBuffer m_bbuffer;
    private Packet m_packet;
    private LinkedList<Packet> m_packets;
    
    // Ping timers
    long m_currentTime;
    
    public Client(SocketChannel sock, int id) throws IOException
    {
        m_socket = sock;
        m_id = id;
        m_socket.configureBlocking(false);
        m_bbuffer = ByteBuffer.allocate(ServerStarter.MAX_BUFFER_SIZE);
        m_packets = new LinkedList<Packet>();
        m_currentTime  = System.currentTimeMillis();
    }
    
    /**
     * Send our Packet queue to the server.
     */
    public void sendQueuedPackets()
    {
        while(!m_packets.isEmpty())
        {
            Packet packet = m_packets.pop();
            sendTcp(packet);
            
        }
    }
    
    /**
     * Add a packet to the queue to be sent to the client.
     * @param packet 
     */
    public void sendPacket(Packet packet)
    {
        m_packets.add(packet);
    }
    
    public void receive()
    {
        try
        {
            // Read available data
            int bytesAvailable = m_socket.read(m_bbuffer);
            
            // Set a new packets byte buffer
            if(bytesAvailable > 0)
            {
                m_bbuffer.order(ByteOrder.LITTLE_ENDIAN);
                Packet packet = new Packet(m_bbuffer);
                ServerStarter.executeListeners(packet);
            }
            
            m_isConnected = true;
        } catch (IOException ex)
        {
            m_isConnected = false;
            System.out.println("Unable to receive from client: " + m_id);
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void ping()
    {
        long time = System.currentTimeMillis() - m_currentTime ;

        if(time >= ServerStarter.PING_TIME)
        {
            System.out.println("Sent PING");
            m_packet = new Packet();
            m_packet.writebyte((byte)-126);
            sendTcp(m_packet);
            m_currentTime  = System.currentTimeMillis();
        }
    }
    
    /**
     * Send a packet to the client.
     * @param packet 
     */
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
            
            m_isConnected = true;
            
        } catch (IOException ex)
        {
            try
            {
                m_isConnected = false;
                m_socket.close();
            } catch (IOException ex1)
            {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get the clients socket channel.
     * @return 
     */
    public SocketChannel getSocketChannel()
    {
        return m_socket;
    }
    
    public int getId()
    {
        return m_id;
    }
    
    /**
     * Check if the connection is still connected or not.
     * @return 
     */
    public boolean isConnected()
    {
        return m_isConnected;
    }
    
    
    /**
     * Close the socket.
     * @throws IOException 
     */
    public void close() throws IOException
    {
        m_socket.close();
    }
}
