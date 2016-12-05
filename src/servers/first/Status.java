package servers.first;

import java.util.HashMap;

/**
 * Created by masseeh on 12/5/16.
 */
public class Status {

    public static int NUMBER = 3;
    public static HashMap<String , Integer> flightCountPorts = new HashMap<>();
    public static HashMap<String , Integer> transferPorts = new HashMap<>();

    static
    {
        flightCountPorts.put("MTL" , 5555);
        flightCountPorts.put("WST" , 5556);
        flightCountPorts.put("NDH" , 5557);

        transferPorts.put("montreal" , 6666);
        transferPorts.put("washington" , 6667);
        transferPorts.put("new delhi" , 6668);
    }
}
