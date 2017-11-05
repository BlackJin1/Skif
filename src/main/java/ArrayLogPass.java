import java.util.ArrayList;

/**
 * Created by knyazev.v on 26.10.2017.
 */
public class ArrayLogPass {
    private String url;
    private ArrayList<LogPass> logPasses;

    public ArrayLogPass(String url, ArrayList<LogPass> logPasses) {
        this.url = url;
        this.logPasses = logPasses;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<LogPass> getLogPasses() {
        return logPasses;
    }

    public void setLogPasses(ArrayList<LogPass> logPasses) {
        this.logPasses = logPasses;
    }
}
