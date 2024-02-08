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
                String action = "";
                while (!action.equals("quit")) {
                    System.out.print("[V]iew, [A]dd, or [U]pdate?> ");
                    action = scanner.next();
                    if (action.equals("A")) {
                        System.out.print("[P]erson or [B]ook?> ");
                        action = scanner.next();
                        if (action.equals("P")) {
                            System.out.print("Enter Name> ");
                            String name = scanner.next();
                            Person person = new Person(name);
                            System.out.println("Connected to Database");
                            System.out.println(connection);
                            System.out.println(connection.getClientInfo());
                            insertPerson(person);
                        }
                        if (action.equals("B")){
                            System.out.print("Enter Author Name> ");
                            String author = scanner.next();
                            System.out.print("Enter Book Name> ");
                            String name = scanner.next();
                            Book book = new Book(author, name);
                            System.out.println("Connected to Database");
                            System.out.println(connection);
                            System.out.println(connection.getClientInfo());
                            insertBook(book);
                        }
                    }
                }
            } else {
                System.out.println("Connection is Null");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static int insertPerson(Person person) {
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

    public static int insertBook(Book book) {
        var sql = "INSERT INTO book(author, name) " + "VALUES(?,?)";
        try (
                var conn = Database.connect();
                var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // bind the values

            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getAuthor());

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
}
