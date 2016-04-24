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

package com.sfnet.serverside.threads;

import com.sfnet.serverside.Client;
import com.sfnet.serverside.ServerStarter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joel
 */
public class ServerThread implements Runnable
{
    ServerSocketChannel m_socket;

    
    public ServerThread(int port)
    {
        
        try
        {
            // Open our socket and bind it to our desired port
            m_socket = ServerSocketChannel.open();
            m_socket.configureBlocking(false);
            m_socket.bind(new InetSocketAddress(port));
            System.out.println("Listening on port: " + port);

            
            
        } catch (IOException ex)
        {
            System.out.println("Failed to listen on port: " + port);
            System.out.println(ex.getStackTrace());
        }
    }

    @Override
    public void run()
    {
        while(m_socket.isOpen())
        {
            
            // Accept new clients to the server
            ArrayList<Client> m_clients = ServerStarter.getClients();
            try
            {
                // Accept new clients
                SocketChannel pendingSocket = m_socket.accept();
                
                if(pendingSocket != null)
                {
                    // Add a new client to our list of clients with a unique ID                    
                    ServerStarter.addClient(new Client(pendingSocket, generateUniqueClientId()));
                }
                
            } catch (IOException ex)
            {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Receive any new packets from the client
            for(int i =0; i < m_clients.size(); i++)
            {
                // Get our client
                Client client = m_clients.get(i);
                
                // Check if the client is connected before we do anything
                if(client.isConnected())
                {
                    // Receive new messages from the clients
                    client.receive();
                    // Send our queued packets to the clients
                    client.sendQueuedPackets();
                    // Ping our clients
                    client.ping();
                }
                else
                {
                    // Remove the disconnected client(s)
                    ServerStarter.removeClient(client.getId());
                }
            }
            
        }
    }
    
    
    /**
     * Checks if the client ID is unique.
     * @param id
     * @return 
     */
    private boolean isUnique(int id)
    {
        ArrayList<Client> m_clients = ServerStarter.getClients();
        
        for(int i=0; i<m_clients.size(); i++)
        {
            if(id == m_clients.get(i).getId())
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Generate a new client ID.
     * @return 
     */
    private int generateUniqueClientId()
    {
        int uniqueId = 0;
        
        while(!isUnique(uniqueId))
        {
            uniqueId ++;
        }
        
        return uniqueId;
    }
}
