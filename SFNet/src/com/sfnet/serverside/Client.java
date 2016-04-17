/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfnet.serverside;


import com.sfnet.clientside.ClientStarter;
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
    private ByteBuffer m_bbuffer;
    private Packet m_packet;
    private LinkedList<Packet> m_packets;
    public Client(SocketChannel sock, int id) throws IOException
    {
        m_socket = sock;
        m_id = id;
        m_socket.configureBlocking(false);
        m_bbuffer = ByteBuffer.allocate(ServerStarter.MAX_BUFFER_SIZE);
        m_packets = new LinkedList<Packet>();
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
            
        } catch (IOException ex)
        {
            System.out.println("Unable to receive from client: " + m_id);
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (IOException ex)
        {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
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
