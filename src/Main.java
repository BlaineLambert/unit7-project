import javax.xml.crypto.Data;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (var connection = Database.connect()) {
            if (connection != null) {
                System.out.print("Enter Name> ");
                String name = scanner.next();
                Person person = new Person(name);
                System.out.println("Connected to Database");
                System.out.println(connection);
                System.out.println(connection.getClientInfo());
                insert(person);
            } else {
                System.out.println("Connection is Null");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static int insert(Person person) {
        var sql = "INSERT INTO person(name) " + "VALUES(?)";
        try (
            var conn = Database.connect();
            var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // bind the values

            pstmt.setString(1, person.getName());

            // execute the INSERT statement and get the inserted id
            int insertedRow = pstmt.executeUpdate();
            if (insertedRow > 0) {
                var rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static void add(){

    }
}
