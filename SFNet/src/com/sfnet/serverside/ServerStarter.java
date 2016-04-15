/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfnet.serverside;

import com.sfnet.serverside.threads.ServerThread;



/**
 *
 * @author Joel
 */
public class ServerStarter
{
    public static int MAX_BUFFER_SIZE = 1024;
    
    public static boolean isDebug;
    
    /**
     * Begin listening on a desired port.
     * @param port 
     */
    public static void listen(int port)
    {
        isDebug = true;
        (new Thread(new ServerThread(port))).start();
    }
}
