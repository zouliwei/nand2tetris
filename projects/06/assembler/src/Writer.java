import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

class Writer {

    private BufferedWriter writer;

    Writer(String path, Charset encoding) throws IOException {
        writer = Files.newBufferedWriter(Paths.get(path), encoding);
    }

    void write(String str) throws IOException {
        writer.write(str);
    }

    void newLine() throws IOException {
        writer.newLine();
    }

    void close() throws IOException {
        writer.close();
    }
}
