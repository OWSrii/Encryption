import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author OWSrii
 * This is a project to encrypt and decrypt files
 * @since 12/29/2019
 * @version 1.1
 */

public class Main {

    public static void main(String[] args) throws Exception {
        /*
        switch (args[0]) {
            case "-h":
                InputStream is = new FileInputStream("help.txt");
                byte[] helpFileBytes = new byte[is.available()];
                is.read(helpFileBytes);

                for (byte b : helpFileBytes) {
                    System.out.print((char) b);
                }
                break;

            case "-e":
                if (args.length == 2) {
                    try {
                       EncryptFile file = new EncryptFile(new File(args[1]));
                       file.encrypt();
                    }
                    catch (Exception e) {
                        throw new IllegalArgumentException();
                    }
                }

                else if (args.length == 4 && args[2].equals("-o")) {
                    try {
                        EncryptFile file = new EncryptFile(new File(args[1]), args[3]);
                        file.encrypt();
                    }
                    catch (Exception e) {
                        throw new IllegalArgumentException();
                    }
                }
                else {
                    throw new IllegalArgumentException("Arguments not valid");
                }

                break;

            case "-d":

                if (args.length == 2) {
                    try {
                        DecryptFile file = new DecryptFile(new File(args[1]));
                        file.decrypt();
                    }
                    catch (Exception e) {
                        throw new IllegalArgumentException();
                    }
                }
                else if (args.length == 4 && args[2].equals("-o")) {
                    try {
                        DecryptFile file = new DecryptFile(new File(args[1]), args[3]);
                        file.decrypt();
                    }
                    catch (Exception e) {
                        throw new IllegalArgumentException();
                    }
                }

                else {
                    throw new IllegalArgumentException("Arguments not valid");
                }

                break;
        }

         */
    }
}
