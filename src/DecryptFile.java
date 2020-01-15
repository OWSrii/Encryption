import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DecryptFile extends Encryption {

    /**
     * @fixme use the decryption key
     * @fixme fix the extensions on decryption
     */

    private String key;
    private String pathToDecryptedFile = null;
    private File encryptedFile;
    private File decryptedFile;

    /**
     * @param encryptedFile the file to be decrypted
     */
    public DecryptFile(File encryptedFile) throws IOException {
        this.encryptedFile = encryptedFile;
        this.decryptedFile = new File(this.encryptedFile.getAbsolutePath()
                .substring(0, this.encryptedFile.getAbsolutePath().lastIndexOf(".")));
        init();
    }

    /**
     * @param encryptedFile the encrypted file
     * @param pathToDecryptedFile specified path to the decrypted file
     * Initialize the decryptedFile field
     */
    public DecryptFile(File encryptedFile, String pathToDecryptedFile) throws IOException {
        this.encryptedFile = encryptedFile;
        this.pathToDecryptedFile = pathToDecryptedFile;
        init();
        this.decryptedFile = new File(this.pathToDecryptedFile + "\\" + this.encryptedFile.getName()
                .substring(0, this.encryptedFile.getName().lastIndexOf(".")));
    }

    /**
     * Check if the encrypted file can be used
     * @throws FileNotFoundException if file does not exist
     * @throws IllegalArgumentException if encrypted file is usable
     */

    public void init() throws IOException {
        if (this.encryptedFile.isDirectory()) throw new IllegalArgumentException();
        if (!this.encryptedFile.exists()) throw new FileNotFoundException("File does not exist");

        if (this.pathToDecryptedFile != null) {
            if (new File(pathToDecryptedFile).isFile())
                throw new IllegalArgumentException();

            if (!new File(pathToDecryptedFile).exists())
                Files.createDirectories(Paths.get(pathToDecryptedFile));
        }

        this.key = this.encryptedFile.getName().substring(0, this.encryptedFile.getName().lastIndexOf("."));

        if (this.key.length() > 16)
            this.key = this.key.substring(0, 16);

        else {
            StringBuilder sb = new StringBuilder(this.key);
            while (sb.length() < 16) {
                sb.append("_");
            }
            this.key = sb.toString();
        }
    }

    /**
     * Decrypt the file and writes it to the specified decrypted file
     * @throws Exception if operation is cancelled
     */

    public void decrypt() throws Exception {
        if (this.decryptedFile.exists()) {
            System.out.print("There is already a decrypted file.\n" +
                    "Do you really want to delete it ? (y/n)" +
                    "\n--> ");
            String line = new BufferedReader(new InputStreamReader(System.in)).readLine();

            if (!line.equals("y"))
                throw new OperationCancelledException("Operation cancelled");
        }
        try {
            decrypt(key, this.encryptedFile, this.decryptedFile);
        }
        catch (Exception e) {
            System.out.println("Decryption failed");
            throw new Exception("Decryption failed");
        }
        System.out.println("Decryption done.");
    }
}
