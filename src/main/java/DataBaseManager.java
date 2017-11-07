import DB.Product;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by knyazev.v on 02.11.2017.
 */
public class DataBaseManager {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            //Создаем экземпляр по работе с БД
            DbHandler dbHandler = DbHandler.getInstance();
            // Добавляем запись
//            dbHandler.addProduct(new LogPass("GoogleChrome","A2386(ENSprester)","http://miraclesofheaven.webteam.net/",
//                    "miracles","9p7unwmq5","0971FDA-DF7821CF-82F7F009-621A1DE2E"));
            // Получим все записи и выведим их нв консоль
            ArrayList<LogPass> logPasses = dbHandler.getAllProducts();
            for (LogPass logPas : logPasses) {
                System.out.println(logPas.toString());
            }

            dbHandler.dropTable("authors");
            // Удаление записи с id=8
//            dbHandler.deleteLogPas(700);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
