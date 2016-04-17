# SFNet

SFNet (Simple Fast Networking)

A simple Java networking library for TCP (no UDP yet) server-client connections.

Currently supports:
- server-client connections
- easy-to-use packet container
- sending / receiving packets


Example:

/**
    Test Package
**/

public class Test
{
    public static void main(String args[])
    {
        System.out.println("Server.");
        ServerStarter.listen(1337);
        ServerStarter.addListener(new ExampleServerListener());
        ClientStarter.connect("127.0.0.1", 1337);
        ClientStarter.addListener(new ExampleClientListener());
        
        Packet testPacket = new Packet();
        testPacket.writebyte((byte)1);
        testPacket.writestring("Hello World!");
        ClientStarter.sendPacket(testPacket);
    }
}

/**
    Client Listener
**/

public class ExampleClientListener implements SocketListener
{

    @Override
    public void received(Packet packet)
    {
        // Get the message ID of the packet
        int messageId = packet.readbyte();

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

/**
   Server Listener
**/

public class ExampleServerListener implements SocketListener
{

    @Override
    public void received(Packet packet)
    {
        int messageId = packet.readbyte();

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
