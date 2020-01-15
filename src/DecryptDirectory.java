import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;

public class DecryptDirectory extends Encryption {
    private File originalDirectory;
    private File destinationDirectory;
    private ArrayList<File> allFiles;
    private String key;

    public DecryptDirectory(File originalDirectory) throws OperationCancelledException, IOException {
        this.originalDirectory = originalDirectory;
        this.destinationDirectory = originalDirectory;
        this.init();
    }

    public DecryptDirectory(File originalDirectory, File destinationDirectory) throws OperationCancelledException, IOException {
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

    public void decrypt() throws IOException {
        Date beginning = new Date();

        for (File f : this.allFiles) {

        }

        System.out.println("Processed finished in " + (new Date().getTime() - beginning.getTime()) + "ms");
    }
}
