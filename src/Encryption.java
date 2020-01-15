import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Encryption {
    public static final String ALGORITHM = "AES";
    public static final String TRANSFORMATION = "AES";

    /**
     * Call decrypt method or encrypt method
     * @param key decryption key
     * @param inputFile original file or encrypted file
     * @param outputFile encrypted file or new decrypted file
     * @throws CryptoException thrown if encryption fails
     */
    static void doCrypto(int cipherMode, String key, File inputFile, File outputFile) throws CryptoException {
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            if (cipherMode == Cipher.ENCRYPT_MODE)
                encrypt(inputFile, outputFile, cipher);
            else if (cipherMode == Cipher.DECRYPT_MODE)
                decrypt(inputFile, outputFile, cipher);

        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }
    }

    /**
     * Encrypt the file and write to a new encrypted file
     * @param inputFile original file to encrypt
     * @param outputFile encrypted file
     * @param cipher Cipher instance
     */
    private static void encrypt(File inputFile, File outputFile, Cipher cipher) throws IOException, BadPaddingException, IllegalBlockSizeException {
        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] extensionBytes = new byte[0];

        // Add to the first line of the file the extension
        String fileExtension = getExtension(inputFile.getName());
        for (Extension e : Extension.values()) {
            if (e.name.equals(fileExtension)) {
                extensionBytes = (e.name + "\n").getBytes();
                break;
            }
        }

        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);
        inputBytes = collapseList(extensionBytes, inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();
    }

    /**
     * Decrypt the file and writes the content to the new file
     * @param inputFile encrypted file
     * @param outputFile new file containing decrypted file
     * @param cipher Cipher instance
     */
    private static void decrypt(File inputFile, File outputFile, Cipher cipher) throws IOException, BadPaddingException, IllegalBlockSizeException {
        InputStream inputStream = new FileInputStream(inputFile);

        byte[] inputBytes = new byte[(int) inputFile.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        String extension;
        File tempFile = File.createTempFile("dtf", ".encryption");
        new FileOutputStream(tempFile).write(outputBytes);
        extension = new BufferedReader(new FileReader(tempFile)).readLine();

        FileOutputStream outputStream = new FileOutputStream(outputFile.getAbsolutePath() + extension);
        outputStream.write(removeExtensionFromFile(tempFile, extension));
        tempFile.delete();

        inputStream.close();
        outputStream.close();
    }

    /**
     * @param s filename / path
     * @return the extension of the file
     */
    public static String getExtension(String s) {
        return s.substring(s.lastIndexOf("."));
    }

    /**
     * @return a list containing list1 + list2
     */
    public static byte[] collapseList(byte[] list1, byte[] list2) {
        byte[] allBytes = new byte[list1.length + list2.length];
        ArrayList<Byte> bytes = new ArrayList<>();

        for (byte b : list1) {
            bytes.add(b);
        }
        for (byte b : list2) {
            bytes.add(b);
        }

        for (int i = 0; i < allBytes.length; i++) {
            allBytes[i] = bytes.get(i);
        }

        return allBytes;
    }

    /**
     * @param file tempFile containing the decrypted file with the extension
     * @param extension contains the extension to remove from the file
     * @return the file without the extension
     */
    private static byte[] removeExtensionFromFile(File file, String extension) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder sbStringFile = new StringBuilder();

        while (reader.ready()) {
            sbStringFile.append(reader.readLine());
        }

        String stringFile = sbStringFile.toString();
        extension += '\n';

        return stringFile.substring(extension.length() - 1).getBytes();
    }

    /**
     * Return an ArrayList of all the files contained by the specified directory
     * @param directory first directory of the file tree
     * @return an ArrayList with all the files of the file tree
     */
    public ArrayList<File> getFileTree(File directory) {
        if (directory == null) throw new IllegalArgumentException("File cannot be null");
        if (directory.isFile()) throw new IllegalArgumentException("File at " + directory.getAbsolutePath() + " is not a directory");
        if (!directory.exists()) throw new IllegalArgumentException("The file : " + directory.getAbsolutePath() + " doesn't exist");

        ArrayList<File> allFileFromDirectory = new ArrayList<>();

        Queue<File> queue = new LinkedList<>();
        queue.add(directory);

        while (!queue.isEmpty()) {
            File currentFile = queue.poll();
            File[] files = currentFile.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory())
                        queue.add(file);
                    else if (file.isFile())
                        allFileFromDirectory.add(file);
                }
            }
        }

        return allFileFromDirectory;
    }

    /**
     * Create the file tree of a list of files
     * @param files list of files
     */
    public void createTree(List<File> files) throws IOException {
        for (File file : files) {
            if (!file.exists())
                Files.createDirectories(file.toPath());
        }
    }

    /**
     * Return 16 bytes decryption key
     */
    String generateFileEncryptionKey(File file) {
        String key;
        if (file.isFile())
            key = file.getName().substring(0, file.getName().lastIndexOf("."));
        else
            key = file.getName();

        if (key.length() > 16)
            key = key.substring(0, 16);

        else {
            StringBuilder sb = new StringBuilder(key);
            while (sb.length() < 16) {
                sb.append("_");
            }
            key = sb.toString();
        }
        return key;
    }

    public static String getPathFromDirectory(File file, File directory) {
        if (directory.getAbsolutePath().charAt(directory.getAbsolutePath().length() - 1) == '\n')
            return file.getAbsolutePath().substring(directory.getAbsolutePath().length() - 1);
        else
            return file.getAbsolutePath().substring(directory.getAbsolutePath().length());
    }

    public void encrypt(String key, File inputFile, File outputFile) throws CryptoException {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }

    public void decrypt(String key, File inputFile, File outputFile) throws CryptoException {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }
}
