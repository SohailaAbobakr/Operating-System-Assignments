import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class WriteToFile {
    String path = "logs.text";
    File writeFile = new File(path);
    FileOutputStream writer = new FileOutputStream(writeFile, true);

    public WriteToFile() throws IOException {
    }

    public void print(String current) throws IOException {
        writer.write(current.getBytes(StandardCharsets.UTF_8));
        writer.write('\n');

    }

}
