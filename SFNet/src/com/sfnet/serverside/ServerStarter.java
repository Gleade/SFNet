/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfnet.serverside;

import com.sfnet.serverside.threads.ServerThread;
import com.sfnet.socket.SocketListener;
import com.sfnet.utils.Packet;
import java.util.ArrayList;



/**
 *
 * @author Joel
 */
public class ServerStarter
{
    public static int MAX_BUFFER_SIZE = 1024;
    public static int PING_TIME = 5000;
    
    static private ArrayList<SocketListener> m_listeners;
    
    // The clients on the server
    static private ArrayList<Client> m_clients = new ArrayList<Client>();
    
    /**
     * Begin listening on a desired port.
     * @param port 
     */
    public static void listen(int port)
    {
        m_listeners = new ArrayList<SocketListener>();
        (new Thread(new ServerThread(port))).start();
    }
    
    /**
     * Add a socket listener.
     * @param listener 
     */
    public static void addListener(SocketListener listener)
    {
        m_listeners.add(listener);
    }
    
        /**
     * Execute our listeners.
     * @param packet 
     */
    public static void executeListeners(Packet packet)
    {
        for(SocketListener listener : m_listeners)
        {
            listener.received(packet);
        }
    }
    
    /**
     * Add a client to the client list.
     * @param client 
     */
    public static void addClient(Client client)
    {
        m_clients.add(client);
    }
    
    /**
     * Remove a client from the client list via Id.
     * @param id 
     */
    public static void removeClient(int id)
    {
        int clientId = -1;
        
        for(int i=0; i < m_clients.size(); i++)
        {
            if(m_clients.get(i).getId() == id)
            {
                clientId = i;
            }
        }
        
        if(id != -1)
        m_clients.remove(clientId);
    }
    
    public static ArrayList<Client>getClients()
    {
        return m_clients;
    }
    
    /**
     * Send a global packet to every client.
     * @param packet 
     */
    public static void sendGlobalPacket(Packet packet)
    {
        for(Client client : m_clients)
        {
            client.sendPacket(packet);
        }
    }
    
    
}
