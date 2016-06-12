/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import com.sfnet.serverside.ServerStarter;
import com.sfnet.socket.SocketListener;
import com.sfnet.utils.Packet;

/**
 *
 * @author Joel
 */
public class ExampleServerListener implements SocketListener
{

    @Override
    public void received(Packet packet)
    {
        int messageId = packet.readbyte();
        System.out.println("Received msg ID: " + messageId);
  
        switch(messageId)
        {
            // Receive a string based message from a player
            case 1:
                String message = packet.readstring();
                System.out.println("Server: " + message);
                ServerStarter.sendGlobalPacket(packet);
                break;
            
                // Receive a positional based messaage from the player
            case 2:
                int x = packet.readbyte();
                int y = packet.readbyte();
                System.out.println(" Server Player position update - x: " + x + " y: " + y);
                break;
        }
    }
    
}
