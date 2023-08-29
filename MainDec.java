import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;

public class MainDec {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        RSA_CORE r=new RSA_CORE();
        String s=r.generate();
        String key[] = s.split("_");
        BigInteger publickey ,privatekey,modulas;



        privatekey = new BigInteger(key[1]);
        modulas = new BigInteger(key[2]);



        System.out.print("Enter encrypted file path: ");
        String encryptedFile = scanner.nextLine();

        System.out.print("Enter output file path: ");
        String outputFile = scanner.nextLine();

        try {
            byte[] encryptedData = readEncryptedFile(encryptedFile);
            String msg = bytetoarray(encryptedData);
            String decryptedData = r.Decryption(msg,privatekey,modulas);
            writeFile(outputFile, decryptedData);
            System.out.println("File decrypted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String bytetoarray(byte[] b){
        String result = "";
        for (Byte bts : b) {
            result += (char) bts.intValue();}
        return result;
    }

    private static byte[] readEncryptedFile(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        fis.close();
        return bos.toByteArray();
    }

    private static void writeFile(String filePath, String data) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write(data);
        writer.close();
    }
}
