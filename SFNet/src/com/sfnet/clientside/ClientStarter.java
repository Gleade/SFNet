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

/**
 *
 * @author Joel
 */
public class ClientStarter
{
    public static int MAX_BUFFER_SIZE = 1024;
    
    public static boolean isDebug;
    
    static private ArrayList<SocketListener> m_listeners;
    
    /**
     * Connect to the desired ip address / port combonation.
     * @param port 
     */
    public static void connect(String ipAddress, int port)
    {
        isDebug = true;
        
        // Create a list of listeners
        m_listeners = new ArrayList<SocketListener>();
        
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
    
    public static void executeListeners(Packet packet)
    {
        for(int i=0; i < m_listeners.size(); i++)
        {
            m_listeners.get(i).received(packet);
        }
    }
}
