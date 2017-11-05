import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для обработки данных
 * Created by knyazev.v on 26.10.2017.
 */
class Manager {
    private String filePath;// Путь к ресурсам
    private String dirResult;// Путь к результатам работы
    private HashMap<String, ArrayList<LogPass>> arrayLogPasses;
    private String url;
    private String appName;
    private String login;
    private String pass;
    private int count = 0;


    Manager(String dirResult) {
        arrayLogPasses = new HashMap<String, ArrayList<LogPass>>();
        url ="";
        appName="";
        login="";
        pass="";
        this.dirResult = dirResult;
    }

    public int getCount() {
        return count;
    }

    /**
     * обработать данные по указанному пути
     * @param filePath - Путь с ресурсами
     */
    public String processData(String filePath){
        File file = new File(filePath);
        if (file.isFile()){
            // Обработать файл
            arrayLogPasses = new HashMap<String, ArrayList<LogPass>>();
            arrayLogPasses = processFile(file);
            System.out.println(file.getAbsoluteFile());
            //arrayLogPasses = new HashMap<>();
            file.delete();
            count++;
        }else {
            // Обработать директори
            processDir(file);
        }

        StringBuilder s = new StringBuilder();

        for (Map.Entry<String, ArrayList<LogPass>> entry: arrayLogPasses.entrySet()){
            s.append("\r\n"+entry.getKey());
            for (LogPass logPass:entry.getValue()) {
                s.append("\r\nLogin: "+logPass.getLogin());
                s.append("\r\nPassword: "+logPass.getPassword());
                s.append("\r\n=================================");

            }
            //s.append("*****************************************");
            writResult(entry.getKey(),entry.getValue());
        }

        return s.toString();
    }

    /**
     * Запишим рещультат в файл
     */
    private void writResult(String urlPath, ArrayList<LogPass> logPasses){
        String fileWrite;
        String newSrt   = urlPath;
        newSrt          = newSrt.replace("http://", "");
        newSrt          = newSrt.replace("pop3://", "");
        newSrt          = newSrt.replace("https://","");
        newSrt          = newSrt.replace("/", "_");
        newSrt          = newSrt.replace(":", " ");
        newSrt          = newSrt.replace("?", "");
        newSrt          = newSrt.replace("www","");
        newSrt          = newSrt.replace(".", " ");
        if (newSrt.equals("")) newSrt = "нет_хоста";
        fileWrite = dirResult + "\\" + newSrt + ".txt";
        File file = new File(fileWrite);
        boolean newFile = false;
        if (file.exists()) newFile = true;
        try (FileWriter writer = new FileWriter(file,newFile)){
            if (!newFile){
                writer.append(urlPath+
                        "\r\n========================================\r\n");
            }
            for (LogPass pass:logPasses) {
                String str = "\r\nURL:"    + pass.getUrl() +
                        "\r\nLogin: "       + pass.getLogin() +
                        "\r\nPassword: "    + pass.getPassword() +
                        "\r\n";//****************************************\r\n";
                writer.append(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Обработать файл
     * @param file - файл для анализа
     * @return - массив с результаттми
     */
    private HashMap<String, ArrayList<LogPass>> processFile(File file){
        // Прочитать данные из файла и обработать
        try (FileInputStream stream = new FileInputStream(file)){
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String strLine;
            while ((strLine = reader.readLine()) != null){
                //находим индекс первого вхождения символа ":" в подстроке
                int pos = strLine.indexOf(":");
                if (pos> -1){
                    //вычленяем имя атрибута из подстроки
                    String attributeName= strLine.substring(0,pos);
                    //вычленяем значение атрибута
                    String value = strLine.substring(pos+1,strLine.length());
                    //вывод на экран вычлененных значений в нужном нам формате.
                    determineKeyValue(attributeName,value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return arrayLogPasses;
    }

    private void processDir(File file){
        String txt = ".txt";
        String[] files = file.list(new MyFileNameFilter(txt));
        for (String path: files) {
            processData(file +"\\"+path);
        }
    }

    private void determineKeyValue(String key, String value) {
        if (key.equals("Application Name") && !value.isEmpty()){
            this.setAppName(value);
        }else if (key.equals("URL")||key.equals("Website")&& !value.isEmpty()){
            this.setUrl(value);
        }else if (key.equals("Username")&& !value.isEmpty()){
            this.setLogin(value);
        }else if (key.equals("Password")&& !value.isEmpty()){
            this.setPass(value);
        }
        createLogPas();
    }

    private void createLogPas(){
        if (!this.getUrl().equals("")&&!this.getPass().equals("")&&!this.getLogin().equals("")){
            LogPass logPass = new LogPass(this.appName, this.url, this.login, this.pass);
            this.setAppName("");
            this.setUrl("");
            this.setLogin("");
            this.setPass("");

            if (arrayLogPasses.containsKey(logPass.getUrl())){
                arrayLogPasses.get(logPass.getUrl()).add(logPass);
            }else {
                ArrayList<LogPass> logPasses = new ArrayList<>();
                logPasses.add(logPass);
                arrayLogPasses.put(logPass.getUrl(),logPasses);
            }

        }
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
