import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by knyazev.v on 26.10.2017.
 */
// Реализация интерфейса FileNameFilter
public class MyFileNameFilter implements FilenameFilter {

    private String ext;

    public MyFileNameFilter(String ext){
        this.ext = ext.toLowerCase();
    }
    @Override
    public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(ext)||dir.isDirectory();
    }
}
