import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class MainEnc {
    public static BigInteger publickey ,privatekey,modulas;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        RSA_CORE r=new RSA_CORE();
        String s=r.generate();
        String key[] = s.split("_");


         publickey = new BigInteger(key[0]);
        privatekey = new BigInteger(key[1]);
        modulas = new BigInteger(key[2]);






        System.out.print("Enter input file path: ");
        String inputFile = scanner.nextLine();

        System.out.print("Enter output file path: ");
        String outputFile = scanner.nextLine();

        try {
            String inputData = readFile(inputFile);
            String encryptedData = r.Encryption(inputData,publickey,modulas);
            byte[] bytemsg = encryptedData.getBytes();
            writeFile(outputFile, bytemsg);
            System.out.println("File encrypted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String readFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        return content.toString();
    }

    private static void writeFile(String filePath, byte[] data) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(data);
        fos.close();
    }
}
