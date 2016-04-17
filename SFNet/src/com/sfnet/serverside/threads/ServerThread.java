/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfnet.serverside.threads;

import com.sfnet.serverside.Client;
import com.sfnet.serverside.ServerStarter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
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
            for(int i=0; i<m_clients.size(); i++)
            {
                // Receive new messages from the clients
                m_clients.get(i).receive();
                // Send our queued packets to the clients
                m_clients.get(i).sendQueuedPackets();
                
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
