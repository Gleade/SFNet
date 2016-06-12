
import Client.ExampleClientListener;
import Server.DatabaseManager;
import Server.ExampleServerListener;
import com.sfnet.clientside.ClientStarter;
import com.sfnet.serverside.ServerStarter;
import com.sfnet.utils.Packet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        
       /* try
        {
          //  DatabaseManager.connect("localhost:3306", "dire", "root", "password");
        } catch (SQLException ex)
        {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}
