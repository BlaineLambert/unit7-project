import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = Database.connect();
             Scanner scanner = new Scanner(System.in)) {
            if (connection != null) {
                String action = "";
                while (!action.equalsIgnoreCase("quit")) {
                    System.out.print("[V]iew, [A]dd, [D]elete, or [U]pdate?> ");
                    action = scanner.next();
                    switch (action.toUpperCase()) {
                        case "A":
                            addAction(scanner, connection);
                            break;
                        case "D":
                            deleteAction(scanner, connection);
                            break;
                        // Add cases for viewing and updating if needed
                        default:
                            System.out.println("Invalid option. Please choose again.");
                    }
                }
            } else {
                System.out.println("Connection is Null");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void addAction(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("[P]erson or [B]ook?> ");
        String action = scanner.next();
        if (action.equalsIgnoreCase("P")) {
            System.out.print("Enter Name> ");
            String name = scanner.next();
            Person person = new Person(name);
            insertPerson(connection, person);
        } else if (action.equalsIgnoreCase("B")) {
            System.out.print("Enter Author Name> ");
            String author = scanner.next();
            System.out.print("Enter Book Name> ");
            String name = scanner.next();
            Book book = new Book(author, name);
            insertBook(connection, book);
        }
    }

    private static void deleteAction(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("[P]erson or [B]ook?> ");
        String action = scanner.next();
        if (action.equalsIgnoreCase("P")) {
            System.out.print("Enter Person ID to delete> ");
            int personId = getIntInput(scanner);
            int deletedRows = deletePerson(connection, personId);
            if (deletedRows > 0) {
                System.out.println("Person deleted successfully.");
            } else {
                System.out.println("Person not found or deletion failed.");
            }
        } else if (action.equalsIgnoreCase("B")) {
            System.out.print("Enter Book ID to delete> ");
            int bookId = getIntInput(scanner);
            int deletedRows = deleteBook(connection, bookId);
            if (deletedRows > 0) {
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("Book not found or deletion failed.");
            }
        }
    }

    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.next(); // clear invalid input
                System.out.print("Invalid input. Please enter an integer: ");
            }
        }
    }

    public static int insertPerson(Connection connection, Person person) throws SQLException {
        String sql = "INSERT INTO person(name) VALUES(?)";
        try (var pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, person.getName());
            int insertedRow = pstmt.executeUpdate();
            if (insertedRow > 0) {
                var rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public static int insertBook(Connection connection, Book book) throws SQLException {
        String sql = "INSERT INTO book(author, name) VALUES(?, ?)";
        try (var pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getAuthor());
            pstmt.setString(2, book.getName());
            int insertedRow = pstmt.executeUpdate();
            if (insertedRow > 0) {
                var rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public static int deletePerson(Connection connection, int personId) throws SQLException {
        String sql = "DELETE FROM person WHERE id = ?";
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, personId);
            return pstmt.executeUpdate();
        }
    }

    public static int deleteBook(Connection connection, int bookId) throws SQLException {
        String sql = "DELETE FROM book WHERE id = ?";
        try (var pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            return pstmt.executeUpdate();
        }
    }
}