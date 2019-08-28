package pointer.file;

import java.io.*;

public class FileManager {
    private File txtFile;
    private File serializeFile;

    public FileManager(String txtPath, String serializePath) {
        this.txtFile = new File(txtPath);
        this.serializeFile = new File(serializePath);

        try {
            this.txtFile.createNewFile();
            this.serializeFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendToFile(String data) {
        writeToFile(data, true);
    }

    public void rewriteFile(String data) {
        writeToFile(data, false);
    }

    private void writeToFile(String data, boolean append) {
        try (FileOutputStream fos = new FileOutputStream(txtFile, append);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(data.getBytes());
        } catch (FileNotFoundException e) {
            System.out.println("No file by path: " + txtFile.getPath());
        } catch (IOException e) {
        }
    }

    public String readFromFile() {
        try (FileInputStream fis = new FileInputStream(txtFile);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            byte[] buffer = new byte[bis.available()];
            bis.read(buffer);
            String str = new String(buffer);
            return str.isEmpty() ? "{}" : str;

        } catch (FileNotFoundException e) {
            System.out.println("No file by path: " + txtFile.getPath());
        } catch (IOException e) {
        }

        return "{}";
    }

    public void serializeToFile(Serializable obj) {
        try (FileOutputStream fout = new FileOutputStream(serializeFile);
             ObjectOutputStream oos = new ObjectOutputStream(fout)) {
            oos.writeObject(obj);
        } catch (FileNotFoundException fex) {
            System.out.println("No file by path: " + serializeFile.getPath());
        } catch (IOException ioex) {
        }
    }

    public <T extends Serializable> T deserializeFromFile() {
        try (FileInputStream fis = new FileInputStream(serializeFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (T) ois.readObject();
        } catch (FileNotFoundException fex) {
            System.out.println("No file by path: " + serializeFile.getPath());
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
