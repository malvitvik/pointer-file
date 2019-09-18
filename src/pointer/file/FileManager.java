package pointer.file;

import com.sun.istack.internal.NotNull;

import java.io.*;

public class FileManager {
    private File txtFile;
    private File serializeFile;

    public FileManager(@NotNull String txtPath, @NotNull String serializePath) {
        this.txtFile = new File(txtPath);
        this.serializeFile = new File(serializePath);

        try {
            this.txtFile.createNewFile();
            this.serializeFile.createNewFile();
        } catch (IOException ignore) {
        }
    }

    public void writeToFile(@NotNull Object data, boolean append) {
        try (FileOutputStream fos = new FileOutputStream(txtFile, append);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(data.toString().getBytes());
        } catch (FileNotFoundException e) {
            System.out.println("No file by path: " + txtFile.getPath());
        } catch (IOException ignore) {
        }

        System.out.println(append ? "Data are appended to file." : "File is rewritten.");
    }

    public String readFromFile() {
        String empty = "{}";

        try (FileInputStream fis = new FileInputStream(txtFile);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            byte[] buffer = new byte[bis.available()];
            bis.read(buffer);
            String str = new String(buffer);
            return str.isEmpty() ? empty : str;

        } catch (FileNotFoundException e) {
            System.out.println("No file by path: " + txtFile.getPath());
        } catch (IOException ignore) {
        }

        return empty;
    }

    public void serializeToFile(Serializable obj) {
        try (FileOutputStream fos = new FileOutputStream(serializeFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(obj);
            System.out.println("ZooClub is saved to file");
        } catch (FileNotFoundException fex) {
            System.out.println("No file by path: " + serializeFile.getPath());
        } catch (IOException ignore) {
        }
    }

    public <T extends Serializable> T deserializeFromFile() {
        try (FileInputStream fis = new FileInputStream(serializeFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (T) ois.readObject();
        } catch (FileNotFoundException fex) {
            System.out.println("No file by path: " + serializeFile.getPath());
        } catch (ClassNotFoundException e) {
            System.out.print(e.getMessage());
        } catch (IOException ignore) {
        }

        return null;
    }
}