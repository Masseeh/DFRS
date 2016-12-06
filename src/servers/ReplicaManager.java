package servers;

import frontEnd.Protocol;
import net.rudp.ReliableServerSocket;
import net.rudp.ReliableSocket;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * Created by masseeh on 11/1/16.
 */
public class ReplicaManager {
    public static int ID = 0;

    private int replicaManagerPort;
    Process mtlReplica = null;
    Process ndhReplica = null;
    Process wstReplica = null;
    int[] errors;

    public ReplicaManager(int replicaManagerPort, int id ) {
        this.replicaManagerPort = replicaManagerPort;
        errors = new int[3];
        ID = id;
    }


    public static void main(String args[]) {

        ReplicaManager replicaManager = new ReplicaManager(Integer.valueOf(args[0]),Integer.valueOf(args[1]));

        replicaManager.initProcesses();

        replicaManager.restartReplica(0);

        replicaManager.listen();


    }

    public void dump(Process p , String server) {
        try {

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            System.out.println("Here is the standard output of the command " + server + " ");
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            System.out.println("Here is the standard error of the command (if any) " + server + " :");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initProcesses() {

        try {

            if (ID == 0) {

                mtlReplica = Runtime.getRuntime().exec(Protocol.MTL_0_JAR);
                ndhReplica = Runtime.getRuntime().exec(Protocol.NDH_0_JAR);
                wstReplica = Runtime.getRuntime().exec(Protocol.WST_0_JAR);
            } else if (ID == 1) {
                mtlReplica = Runtime.getRuntime().exec(Protocol.MTL_1_JAR);
                ndhReplica = Runtime.getRuntime().exec(Protocol.NDH_1_JAR);
                wstReplica = Runtime.getRuntime().exec(Protocol.WST_1_JAR);

            }

        }catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void listen() {
        try {
            ReliableServerSocket serverSocket = new ReliableServerSocket(replicaManagerPort);

            while (true) {

                ReliableSocket socket = (ReliableSocket)serverSocket.accept();
                new Thread(new Handler(socket)).start();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restartReplica(int idx) {

        String city = "";
        try {
            switch (idx) {
                case 0:
                    city = "mtl";
                    mtlReplica.destroy();

                    if (ID == 0) {
                        mtlReplica = Runtime.getRuntime().exec(Protocol.MTL_0_JAR);
                    } else if (ID == 1) {
                        mtlReplica = Runtime.getRuntime().exec(Protocol.MTL_1_JAR);

                    }

                    break;
                case 1:
                    city = "ndh";
                    ndhReplica.destroy();

                    if (ID == 0) {
                        ndhReplica = Runtime.getRuntime().exec(Protocol.NDH_0_JAR);
                    } else if (ID == 1) {
                        ndhReplica = Runtime.getRuntime().exec(Protocol.NDH_1_JAR);

                    }


                    break;
                case 2:
                    city = "wst";
                    wstReplica.destroy();


                    if (ID == 0) {
                        wstReplica = Runtime.getRuntime().exec(Protocol.WST_0_JAR);
                    } else if (ID == 1) {
                        wstReplica = Runtime.getRuntime().exec(Protocol.WST_1_JAR);

                    }

                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = Protocol.RESOURCES + city + ID + "/actions.log";

        if (ID == 0) {

            try {

                BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(address)));

                String line = bf.readLine();

                int port = 0;

                switch (idx) {
                    case 0:
                        port = Protocol.FIRST_REPLICA_PORT_MTL;
                        break;
                    case 1:
                        port = Protocol.FIRST_REPLICA_PORT_NDL;
                        break;
                    case 2:
                        port = Protocol.FIRST_REPLICA_PORT_WA;
                        break;
                }

                ReliableSocket socket = new ReliableSocket();
                socket.connect(new InetSocketAddress("127.0.0.1", port));

                OutputStream out = socket.getOutputStream();

                while (line != null) {

                    if (line.contains("INFO")) {

                        line = line.substring(7);

                        line = "R," + line;

                        out.write(line.getBytes());
                        out.flush();

                        line = bf.readLine();
                    }
                }

                out.close();
                socket.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (ID == 1) {

            try {
                BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(address)));

                String line = bf.readLine();

                int port = 0;

                switch (idx) {
                    case 0:
                        port = Protocol.SECOND_REPLICA_PORT_MTL;
                        break;
                    case 1:
                        port = Protocol.SECOND_REPLICA_PORT_NDL;
                        break;
                    case 2:
                        port = Protocol.SECOND_REPLICA_PORT_WA;
                        break;
                }

                ReliableSocket socket = new ReliableSocket();
                socket.connect(new InetSocketAddress("127.0.0.1",port));

                OutputStream out = socket.getOutputStream();

                while (line != null) {
                    if (line != "\n") {

                        line = "R," + line;

                        out.write(line.getBytes());
                        out.flush();

                        line = bf.readLine();
                    }
                }

                out.close();
                socket.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }

    class Handler implements Runnable {

        private ReliableSocket socket;

        public Handler(ReliableSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream in = socket.getInputStream();

                byte[] buffer = new byte[Protocol.MSG_LENGTH];

                int size = in.read(buffer);

                in.close();
                socket.close();

                String city = new String(buffer,0, size);

                switch (city) {
                    case "mtlReplica":
                        errors[0]++;
                        break;
                    case "NDL":
                        errors[1]++;
                        break;
                    case "WA":
                        errors[2]++;
                        break;
                }

                for (int i=0; i<3; i++) {
                    if (errors[i] >=0 ) {
                        restartReplica(i);
                        errors[i] = 0;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
