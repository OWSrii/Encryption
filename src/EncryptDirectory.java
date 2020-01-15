import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;

public class EncryptDirectory extends Encryption {
    private File originalDirectory;
    private File destinationDirectory;
    private ArrayList<File> allFiles;
    private String key;

    public EncryptDirectory(File originalDirectory) throws OperationCancelledException, IOException {
        this.originalDirectory = originalDirectory;
        this.destinationDirectory = originalDirectory;
        this.init();
    }

    public EncryptDirectory(File originalDirectory, File destinationDirectory) throws OperationCancelledException, IOException {
        this.originalDirectory = originalDirectory;
        this.destinationDirectory = destinationDirectory;
        this.init();
    }

    private void init() throws OperationCancelledException, IOException {
        if (this.originalDirectory.isFile() || this.destinationDirectory.isFile()) throw new IllegalArgumentException("Directory expected.");
        if (this.originalDirectory == null || this.destinationDirectory == null) throw new IllegalArgumentException("Specified directory is null");

        this.allFiles = this.getFileTree(originalDirectory);
        if (this.allFiles == null) throw new OperationCancelledException("There's no files to encrypt");
        this.createTree(this.allFiles);

        this.key = generateFileEncryptionKey(originalDirectory);
    }

    public void encrypt() throws IOException {
        Date beginning = new Date();
        for (File f : this.allFiles) {
            String pathToOutputFile;
            if (this.destinationDirectory.getAbsolutePath().charAt(this.destinationDirectory.getAbsolutePath().length() - 1) == '\\')
                pathToOutputFile = this.destinationDirectory.getAbsolutePath() + getPathFromDirectory(f, this.originalDirectory);
            else
                pathToOutputFile = this.destinationDirectory.getAbsolutePath() + '\\' + getPathFromDirectory(f, this.originalDirectory);

            try {
                pathToOutputFile = pathToOutputFile.substring(0, pathToOutputFile.lastIndexOf(".")) + Extension.ENCRYPTED.name;
            }
            catch (Exception e) {
                System.out.println("Couldn't encrypt " + pathToOutputFile);
            }
            File outputFile = new File(pathToOutputFile);

            if (!outputFile.exists())
                Files.createDirectories(outputFile.getParentFile().toPath());

            try {
                System.out.println("Encrypting " + f.getAbsolutePath());
                Date date = new Date();
                this.encrypt(this.key, f, outputFile);
                System.out.println("Encryption done (" + Double.toString(new Date().getTime() - date.getTime()) + "ms)");
            }
            catch (Exception e) {
                System.out.println("Encryption failed on file " + f.getAbsolutePath());
            }
        }

        System.out.println("Processed finished in " + Double.toString(new Date().getTime() - beginning.getTime()) + "ms");
    }
}
