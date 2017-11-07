import DB.Product;
import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
/**
 * Created by Вячеслав on 06.11.2017.
 */
public class DbHandler {
    // Константа с адресом подключения
    private static final String CON_STR = "jdbc:sqlite:E:/log.s3db";//MyDataBase.s3db

    // Используем шаблон одиночка, чтобы не плодить экземпляры данного класса
    private static DbHandler instance = null;

    public static synchronized DbHandler getInstance() throws SQLException {
        if (instance == null) {
            instance = new DbHandler();
        }
        return instance;
    }

    // Объект в котором будет храниться соединение с БД
    private Connection connection;

    private DbHandler() throws SQLException {
        // Регестрируем драйвер, с которым будем работать
        // в нешем случае Sqlite
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(CON_STR);
    }

    public ArrayList<LogPass> getAllProducts() {
        // Statement - используется чтобы выполнить SQl запрос
        try (Statement statement = this.connection.createStatement()){
            // В данный список будем загружать продукты, полученные из БД
            ArrayList<LogPass> products = new ArrayList<>();
            // В resultSet будет храниться результат нашего запроса,
            // который выполняется командой statement.executeQuery()
            ResultSet resultSet = statement.executeQuery("SELECT * FROM data");
            // Проходимся по нашему resultSet и заносим данные в products
            while (resultSet.next()){
                products.add(new LogPass(resultSet.getString("application"),
                                            resultSet.getString("user_comp"),
                                            resultSet.getString("host"),
                                            resultSet.getString("login"),
                                            resultSet.getString("password"),
                                            resultSet.getString("MachineID")));
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            //Если произошла ошибка, вернем пустую коллекцию
            return new ArrayList<>();
        }
    }

    // Добавление продукта в БД
    public void addLogPass(LogPass logPass) {
        // Создаем подготовленное выражение, чтобы избежать SQL инекций
        try (PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO data(`user_comp`, `host`, `login`,`password`,`application`,`MachineID`) " +
                        "VALUES(?, ?, ?, ?, ?, ?)")) {
            statement.setObject(1, logPass.getUser());
            statement.setObject(2, logPass.getUrl());
            statement.setObject(3, logPass.getLogin());
            statement.setObject(4, logPass.getPassword());
            statement.setObject(5, logPass.getAppName());
            statement.setObject(6, logPass.getMachineID());
            // Выполняем запрос
            statement.execute();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Создание таблицы
    public void createTable(String tableName) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "CREATE TABLE data (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                                        "user_comp TEXT, " +
                                        "host TEXT, " +
                                        "login TEXT, " +
                                        "password TEXT, " +
                                        "application TEXT, " +
                                        "MachineID TEXT);" )){
           // statement.setObject(1, tableName);
            // Выполним запрос
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Удаление таблицы
    public void dropTable(String tableName) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                    "DROP TABLE data")){
//                statement.setObject(1, tableName);
                // Выполним запрос
                statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Удаление продуктов по ID
    public void deleteLogPas(int id) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM data WHERE id < ?")){
                    statement.setObject(1,id);
                    // Выполняем запрос
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
