package DB;

import java.sql.SQLException;

/**
 * Created by knyazev.v on 02.11.2017.
 */
public class DataBaseManager {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Conn conn = new Conn();
        conn.Conn();
        conn.CreateDB();
        conn.WriteDB();
        conn.ReadDB();
        conn.CloseDB();
    }
}
