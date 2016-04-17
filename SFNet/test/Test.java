
import Client.ExampleClientListener;
import Server.ExampleServerListener;
import com.sfnet.clientside.ClientStarter;
import com.sfnet.serverside.ServerStarter;
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
