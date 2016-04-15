
import com.sfnet.clientside.ClientStarter;
import com.sfnet.serverside.ServerStarter;

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
        ClientStarter.connect("127.0.0.1", 1337);
    }
}
