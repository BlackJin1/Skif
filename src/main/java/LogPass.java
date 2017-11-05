/**
 * Класс содержит данные для доступа к сайтам
 * Created by knyazev.v on 26.10.2017.
 */
public class LogPass {
    private String appName;
    private String user;
    private String url;
    private String login;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogPass logPass = (LogPass) o;

        if (user != null ? !user.equals(logPass.user) : logPass.user != null) return false;
        if (url != null ? !url.equals(logPass.url) : logPass.url != null) return false;
        if (!login.equals(logPass.login)) return false;
        return password.equals(logPass.password);
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + login.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    public String getUser() {

        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LogPass(String appName, String user, String url, String login, String password) {

        this.appName = appName;
        this.user = user;
        this.url = url;
        this.login = login;
        this.password = password;
    }

    public LogPass(String user, String url, String login, String password) {
        this.user = user;
        this.url = url;
        this.login = login;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
