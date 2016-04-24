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
