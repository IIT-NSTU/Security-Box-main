import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter server IP: ");
        String serverIP = scanner.nextLine();

        System.out.print("Enter server port: ");
        int serverPort = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter filename: ");
        String filename = scanner.nextLine();

        System.out.print("Enter save location (including file name and path): ");
        String saveLocation = scanner.nextLine();

        scanner.close();

        try {
            Socket socket = new Socket(serverIP, serverPort);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF(filename);
            String res = in.readUTF();
            if (res.equals("FOUND")) {
                System.out.println("File " + filename + " found at server");
                String n = in.readUTF();
                String m = in.readUTF();
                System.out.println("Server handled " + n + " requests, " + m + " requests were successful");

                try (FileOutputStream fos = new FileOutputStream(saveLocation)) {
                    System.out.println("Downloading file " + filename);
                    int i = 0;
                    do {
                        byte[] buf = new byte[1024];
                        i = in.read(buf);
                        if (i != -1) fos.write(buf, 0, i);
                    } while (i != -1);
                    System.out.println("Download complete");
                }
            } else {
                System.out.println("File " + filename + " not found at server");
                String n = in.readUTF();
                String m = in.readUTF();
                System.out.println("Server handled " + n + " requests, " + m + " requests were successful");
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
