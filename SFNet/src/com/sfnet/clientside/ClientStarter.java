/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfnet.clientside;

import com.sfnet.clientside.threads.ClientThread;
import com.sfnet.socket.SocketListener;
import com.sfnet.utils.Packet;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Joel
 */
public class ClientStarter
{
    public static int MAX_BUFFER_SIZE = 1024;
    
    
    // Our listeners that will receive our packets
    static private ArrayList<SocketListener> m_listeners;
    
    /**
     * Our packets that need to be sent
     */
    static private LinkedList<Packet> m_packets;
    
    // Our connection status
    static public boolean isConnected = false;
    
    /**
     * Connect to the desired ip address / port combonation.
     * @param port 
     */
    public static void connect(String ipAddress, int port)
    {
       
        // Create a list of listeners
        m_listeners = new ArrayList<SocketListener>();
        
        // Create a list of packets that need to be sent
        m_packets = new LinkedList<Packet>();
        
        // Create our client thread
        (new Thread(new ClientThread(ipAddress, port))).start();
    }
    
    /**
     * Add a socket listener to our listeners.
     * @param socketListener 
     */
    public static void addListener(SocketListener socketListener)
    {
        m_listeners.add(socketListener);
    }
    
    /**
     * Execute our listeners.
     * @param packet 
     */
    public static void executeListeners(Packet packet)
    {
        for(int i=0; i < m_listeners.size(); i++)
        {
            m_listeners.get(i).received(packet);
        }
    }
    
    /**
     * Get the packet stack that will be sent to the server.
     * @return 
     */
    public static LinkedList<Packet> getPacketStack()
    {
        return m_packets;
    }
    
    /**
     * Add a packet to the packet queue & send it to the server.
     * @param packet 
     */
    public static void sendPacket(Packet packet)
    {
        m_packets.add(packet);
    }
}
