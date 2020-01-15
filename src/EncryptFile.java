import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EncryptFile extends Encryption {
    private String key;
    private File originalFile;
    private File encryptedFile;
    private Path pathToEncryptedFile;

    /**
     * Constructor using only the input file
     * @param originalFile file to encrypt
     */
    public EncryptFile(File originalFile) throws Exception {
        this.originalFile = originalFile;

        // Get path to a folder which is this.file absolute path  without the extension
        this.pathToEncryptedFile = Paths.get(this.originalFile.getAbsolutePath()
            .substring(0, this.originalFile.getAbsolutePath().lastIndexOf(".")));
        init();
    }

    public EncryptFile(File originalFile, String path) throws Exception {
        this.originalFile = originalFile;
        this.pathToEncryptedFile = Paths.get(path);
        this.init();
    }

    /**
     * Check if arguments are correct and create directories
     */
    private void init() throws Exception {
        if (!originalFile.exists())
            throw new FileNotFoundException();

        // If path to the encrypted file doesn't exist, create it
        if (!new File(this.pathToEncryptedFile.toString()).exists())
            Files.createDirectories(this.pathToEncryptedFile);

        // Create new file with identical name but new extension
        this.encryptedFile = new File(this.pathToEncryptedFile.toString() + "\\" +
                this.originalFile.getName().substring(0, this.originalFile.getName().lastIndexOf(".")) +
                Extension.ENCRYPTED);

        // Create the encryptedFile or safe delete it if it already exists
        if (encryptedFile.exists()) {
            System.out.print("There is already an encrypted file.\n" +
                    "Do you really want to delete it ? (y/n)" +
                    "\n--> ");
            String line = new BufferedReader(new InputStreamReader(System.in)).readLine();

            if (!line.equals("y"))
                throw new Exception("Operation cancelled");
            Files.delete(Paths.get(this.encryptedFile.getAbsolutePath()));
        }
        Files.createFile(this.encryptedFile.toPath());

        this.key = generateFileEncryptionKey(this.originalFile);
    }

    /**
     * Method that encrypts the file and write the result to the file finalEncryptedFile
     * @throws IllegalArgumentException if this.file is a directory
     */
    public void encrypt() {
        try {
            this.encrypt(this.key, this.originalFile, this.encryptedFile);

            System.out.println("Encryption done.");
        } catch (Exception e) {
            System.out.println("Encryption failed.");
            System.out.println(e.getMessage());
        }
    }
    /*
    public File getEncryptedFile() {
        return this.encryptedFile;
    }
     */
}
