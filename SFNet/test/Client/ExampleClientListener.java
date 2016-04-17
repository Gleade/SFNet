package Client;


import com.sfnet.socket.SocketListener;
import com.sfnet.utils.Packet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Joel
 */
public class ExampleClientListener implements SocketListener
{

    @Override
    public void received(Packet packet)
    {
        // Get the message ID of the packet
        int messageId = packet.readbyte();
        System.out.println(messageId + 1);
        switch(messageId)
        {
            // Receive a string based message from a player
            case 1:
                String message = packet.readstring();
                System.out.println("Client: " + message);
                break;
            
                // Receive a positional based messaage from the player
            case 2:
                int x = packet.readbyte();
                int y = packet.readbyte();
                System.out.println("Player position update - x: " + x + " y: " + y);
                break;
        }
    }
    
}
