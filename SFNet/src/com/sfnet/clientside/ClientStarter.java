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
