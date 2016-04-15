/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfnet.socket;

import com.sfnet.utils.Packet;




/**
 *
 * @author Joel
 */
public interface SocketListener {

    /**
     * Interface for receiving a new packet
     * @param packet 
     */
    void received(Packet packet);
	
	
}
