import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    private static final int NUMBER_OF_MAX_THREAD = 10;
    private static final int PORT = 4000;

    public static void main(String[] args) {
        try{
            ServerSocket server =new ServerSocket(PORT);
            ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_MAX_THREAD);


            while (true){
                Socket client= server.accept();
                executor.execute(new ClientHandler(client));
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }
}


class ClientHandler extends Thread{

    public static Integer N=0;
    public static Integer M=0;


    Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    public void run() {
        int n;

        synchronized (N){
            N++;
            n=N;
        }

        String clientIP=client.getInetAddress().getHostAddress();

        try{

            DataInputStream in=new DataInputStream(client.getInputStream());
            DataOutputStream out=new DataOutputStream(client.getOutputStream());

            String x=in.readUTF();

            System.out.println("REQ "+n+": File "+x+" requested from "+clientIP);

            File reqFile=new File(x);
            if(reqFile.exists()){

                out.writeUTF("FOUND");

                System.out.println("REQ "+n+": Successful");

                synchronized (M){
                    M++;
                    System.out.println("REQ "+n+": Total successful requests so far = "+M);


                    out.writeUTF(String.valueOf(n));
                    out.writeUTF(String.valueOf(M));
                }


                try (FileInputStream fis = new FileInputStream(x)) {
                    int i = 0;


                    do {
                        byte[] buf = new byte[1024];
                        i = fis.read(buf);

                        if(i!=-1)out.write(buf,0,i);

                    } while (i != -1);

                    System.out.println("REQ "+n+": File transfer complete");
                    out.close(); //closing connection with client
                }


            }else{
                out.writeUTF("NOT FOUND");
                System.out.println("REQ "+n+": Not Successful");

                synchronized (M){
                    System.out.println("REQ "+n+": Total successful requests so far = "+M);
                }


                out.writeUTF(String.valueOf(n));
                out.writeUTF(String.valueOf(M));
            }


        }catch (IOException e){
            System.out.println(e);
        }
    }
}
