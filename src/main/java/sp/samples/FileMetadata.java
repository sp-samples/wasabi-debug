package sp.samples;


import java.io.InputStream;
import lombok.Value;

@Value
public class FileMetadata {
 String name;
 InputStream content;
 Long sizeInBytes;
}
