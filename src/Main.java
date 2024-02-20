import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = Database.connect();
             Scanner scanner = new Scanner(System.in)) {
            if (connection != null) {
                String action = "";
                while (!action.equalsIgnoreCase("quit")) {

                    System.out.print("[V]iew, [A]dd, [D]elete, Check [I]n, Check [O]ut, or [S]earch> ");
                    action = scanner.nextLine();
                    if (action.equalsIgnoreCase("A")) {
                        addAction(scanner, connection);
                    } else if (action.equalsIgnoreCase("D")) {
                        deleteAction(scanner, connection);
                    } else if (action.equalsIgnoreCase("I")) {
                        checkInAction(scanner, connection);
                    } else if (action.equalsIgnoreCase("O")) {
                        checkOutAction(scanner, connection);
                    } else if (action.equalsIgnoreCase("V")) {
                        view(connection, scanner);
                    } else if (action.equalsIgnoreCase("S")) {
                        searchAction(scanner, connection);
                    } else {
                        System.out.println(action + " " + "is a invalid option");
                    }
                }
            } else {
                System.out.println("Connection is Null");
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void searchAction(Scanner scanner, Connection connection) throws SQLException {
        String action = "";
        System.out.print("Would you like to search [B]ooks, [M]ovies, or [P]eople?> ");
        action = scanner.nextLine();
        if (action.equalsIgnoreCase("B")){
            System.out.print("Enter search keyword: ");
            String keyword = scanner.nextLine();
            System.out.println("Searching for: " + keyword);
            searchBooks(connection, keyword);
        } else if (action.equalsIgnoreCase("M")) {
            System.out.print("Enter search keyword: ");
            String keyword = scanner.next();
            System.out.println("Searching for: " + keyword);
            searchMovies(connection, keyword);
        } else if (action.equalsIgnoreCase("P")) {
            System.out.print("Enter search keyword: ");
            String keyword = scanner.next();
            System.out.println("Searching for: " + keyword);
            searchPeople(connection, keyword);
        } else {
            System.out.println("Invalid Input");
        }

    }

    private static void searchBooks(Connection connection, String keyword) throws SQLException {
        String sql = "SELECT * FROM book WHERE name LIKE ? OR author LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Books matching the search:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Author: " + rs.getString("author") +
                        ", Name: " + rs.getString("name") + ", Price: " + rs.getDouble("price"));
            }
        }
    }

    private static void searchPeople(Connection connection, String keyword) throws SQLException {
        String sql = "SELECT * FROM person WHERE firstname LIKE ? OR lastname LIKE ? OR phonenumber LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setString(3, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            System.out.println("People matching the search:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", First Name: " + rs.getString("firstname") +
                        ", Last Name: " + rs.getString("lastname") + ", Phone Number: " + rs.getString("phonenumber"));
            }
        }
    }

    private static void searchMovies(Connection connection, String keyword) throws SQLException {
        String sql = "SELECT * FROM movie WHERE name LIKE ? OR director LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Movies matching the search:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Director: " + rs.getString("director") +
                        ", Name: " + rs.getString("name") + ", Price: " + rs.getDouble("price"));
            }
        }
    }

    private static void addAction(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("[P]erson, [B]ook, or [M]ovie?> ");
        String action = scanner.nextLine();
        if (action.equalsIgnoreCase("P")) {
            System.out.print("Enter First Name> ");
            String firstname = scanner.nextLine();
            System.out.print("Enter Last Name> ");
            String lastname = scanner.nextLine();
            System.out.print("Enter Phone Number> ");
            String phonenumber = scanner.nextLine();
            insertPerson(connection, firstname, lastname, phonenumber);
        } else if (action.equalsIgnoreCase("B")) {
            System.out.print("Enter Author Name> ");
            String author = scanner.nextLine();
            System.out.print("Enter Book Name> ");
            String name = scanner.nextLine();
            System.out.print("Enter Price> ");
            double price = scanner.nextDouble();
            insertBook(connection, author, name, price);
        } else if (action.equalsIgnoreCase("M")) {
            System.out.print("Enter Director Name> ");
            String director = scanner.nextLine();
            System.out.print("Enter Movie Name> ");
            String name = scanner.nextLine();
            System.out.print("Enter Price> ");
            double price = scanner.nextDouble();
            insertMovie(connection, director, name, price);
        } else {
            System.out.println("Invalid option. Please choose again.");
        }
    }


    private static void deleteAction(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("[P]erson, [B]ook, or [M]ovie?> ");
        String action = scanner.nextLine();
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
        } else if (action.equalsIgnoreCase("M")) {
            System.out.print("Enter Movie ID to delete> ");
            int movieId = getIntInput(scanner);
            int deletedRows = deleteMovie(connection, movieId);
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

    public static int insertPerson(Connection connection, String firstname, String lastname, String phonenumber) throws SQLException {
        String sql = "INSERT INTO person(firstname, lastname, phonenumber) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, firstname);
            pstmt.setString(2, lastname);
            pstmt.setString(3, phonenumber);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Return the new ID
                    }
                }
            }
        }
        return -1;
    }


    public static int insertMovie(Connection connection, String director, String name, double price) throws SQLException {
        String sql = "INSERT INTO movie(director, name, price) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, director);
            pstmt.setString(2, name);
            pstmt.setDouble(3, price);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int movieId = rs.getInt(1);
                        System.out.println("Movie inserted successfully with ID: " + movieId);
                        return movieId; // Return the new movie ID
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return -1;
    }


    public static int insertBook(Connection connection, String author, String name, double price) throws SQLException {
        String sql = "INSERT INTO book(author, name, price) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, author);
            pstmt.setString(2, name);
            pstmt.setDouble(3, price);
            int insertedRow = pstmt.executeUpdate();
            if (insertedRow > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
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


    public static int deleteMovie(Connection connection, int movieId) throws SQLException {
        String sql = "DELETE FROM movie WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, movieId);
            int deletedRows = pstmt.executeUpdate();
            if (deletedRows > 0) {
                System.out.println("Movie deleted successfully.");
                return deletedRows;
            } else {
                System.out.println("Movie not found or deletion failed.");
                return 0;
            }
        } catch (SQLException e) {
            System.err.println("SQL error during movie deletion: " + e.getMessage());
            return -1;
        }
    }


    private static void checkOutAction(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("Check out a [B]ook or [M]ovie?> ");
        String type = scanner.next();
        if (type.equalsIgnoreCase("B")) {
            System.out.print("Enter Person ID> ");
            int personId = getIntInput(scanner);
            System.out.print("Enter Book ID> ");
            int bookId = getIntInput(scanner);
            checkOutBook(connection, personId, bookId);
        } else if (type.equalsIgnoreCase("M")) {
            System.out.print("Enter Person ID> ");
            int personId = getIntInput(scanner);
            System.out.print("Enter Movie ID> ");
            int movieId = getIntInput(scanner);
            checkOutMovie(connection, personId, movieId);
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void checkInAction(Scanner scanner, Connection connection) throws SQLException {
        System.out.print("Check in a [B]ook or [M]ovie?> ");
        String type = scanner.next();
        System.out.print("Enter Person ID> ");
        int personId = getIntInput(scanner);

        if (type.equalsIgnoreCase("B")) {
            listCheckedOutBooks(connection, personId);
            System.out.print("Enter ID to check in: ");
            int bookId = getIntInput(scanner);
            checkInBook(connection, personId, bookId);
        } else if (type.equalsIgnoreCase("M")) {
            listCheckedOutMovies(connection, personId);
            System.out.print("Enter ID to check in: ");
            int movieId = getIntInput(scanner);
            checkInMovie(connection, personId, movieId);
        } else {
            System.out.println("Invalid option.");
        }
    }


    public static void checkOutBook(Connection connection, int personId, int bookId) throws SQLException {
        // Check if the book exists and is not already checked out
        String bookCheckSql = "SELECT COUNT(*) FROM book WHERE id = ?";
        String checkoutCheckSql = "SELECT COUNT(*) FROM person_books WHERE book_id = ? AND return_date IS NULL";
        try (
                PreparedStatement bookCheckStmt = connection.prepareStatement(bookCheckSql);
                PreparedStatement checkoutCheckStmt = connection.prepareStatement(checkoutCheckSql);
        ) {
            bookCheckStmt.setInt(1, bookId);
            checkoutCheckStmt.setInt(1, bookId);

            try (
                    ResultSet bookCheckResult = bookCheckStmt.executeQuery();
                    ResultSet checkoutCheckResult = checkoutCheckStmt.executeQuery();
            ) {
                if (!bookCheckResult.next() || bookCheckResult.getInt(1) == 0) {
                    System.out.println("Book with ID " + bookId + " does not exist.");
                    return;
                }

                if (checkoutCheckResult.next() && checkoutCheckResult.getInt(1) > 0) {
                    System.out.println("This book is already checked out.");
                    return;
                }
            }
        }

        // Check if the person exists
        String personCheckSql = "SELECT COUNT(*) FROM person WHERE id = ?";
        try (PreparedStatement personCheckStmt = connection.prepareStatement(personCheckSql)) {
            personCheckStmt.setInt(1, personId);

            try (ResultSet personCheckResult = personCheckStmt.executeQuery()) {
                if (!personCheckResult.next() || personCheckResult.getInt(1) == 0) {
                    System.out.println("Person with ID " + personId + " does not exist.");
                    return;
                }
            }
        }

        // Perform the check-out operation
        String sql = "INSERT INTO person_books (person_id, book_id, checkout_date) VALUES (?, ?, CURRENT_DATE)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, personId);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
            System.out.println("Book checked out successfully.");
        }
    }

    public static void checkOutMovie(Connection connection, int personId, int movieId) throws SQLException {
        // Check if the movie exists and is not already checked out
        String movieCheckSql = "SELECT COUNT(*) FROM movie WHERE id = ?";
        String checkoutCheckSql = "SELECT COUNT(*) FROM person_movies WHERE movie_id = ? AND return_date IS NULL";
        try (
                PreparedStatement movieCheckStmt = connection.prepareStatement(movieCheckSql);
                PreparedStatement checkoutCheckStmt = connection.prepareStatement(checkoutCheckSql);
        ) {
            movieCheckStmt.setInt(1, movieId);
            checkoutCheckStmt.setInt(1, movieId);

            try (
                    ResultSet movieCheckResult = movieCheckStmt.executeQuery();
                    ResultSet checkoutCheckResult = checkoutCheckStmt.executeQuery();
            ) {
                if (!movieCheckResult.next() || movieCheckResult.getInt(1) == 0) {
                    System.out.println("Movie with ID " + movieId + " does not exist.");
                    return;
                }

                if (checkoutCheckResult.next() && checkoutCheckResult.getInt(1) > 0) {
                    System.out.println("This movie is already checked out.");
                    return;
                }
            }
        }

        // Check if the person exists
        String personCheckSql = "SELECT COUNT(*) FROM person WHERE id = ?";
        try (PreparedStatement personCheckStmt = connection.prepareStatement(personCheckSql)) {
            personCheckStmt.setInt(1, personId);

            try (ResultSet personCheckResult = personCheckStmt.executeQuery()) {
                if (!personCheckResult.next() || personCheckResult.getInt(1) == 0) {
                    System.out.println("Person with ID " + personId + " does not exist.");
                    return;
                }
            }
        }

        // Perform the check-out operation
        String sql = "INSERT INTO person_movies (person_id, movie_id, checkout_date) VALUES (?, ?, CURRENT_DATE)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, personId);
            pstmt.setInt(2, movieId);
            pstmt.executeUpdate();
            System.out.println("Movie checked out successfully.");
        }
    }




    public static void checkInBook(Connection connection, int personId, int bookId) throws SQLException {
        String sql = "DELETE FROM person_books WHERE person_id = ? AND book_id = ? AND return_date IS NULL";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, personId);
            pstmt.setInt(2, bookId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Book checked in successfully.");
            } else {
                System.out.println("No such checkout found.");
            }
        }
    }



    public static void checkInMovie(Connection connection, int personId, int movieId) throws SQLException {
        String sql = "DELETE FROM person_movies WHERE person_id = ? AND movie_id = ? AND return_date IS NULL";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, personId);
            pstmt.setInt(2, movieId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Movie checked in successfully.");
            } else {
                System.out.println("Movie check-in failed. It might not be checked out or is already returned.");
            }
        }
    }

    private static void listCheckedOutBooks(Connection connection, int personId) throws SQLException {
        String sql = "SELECT b.id, b.name FROM book b JOIN person_books pb ON b.id = pb.book_id WHERE pb.person_id = ? AND pb.return_date IS NULL";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Books currently checked out:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Book Name: " + rs.getString("name"));
            }
        }
    }


    private static void listCheckedOutMovies(Connection connection, int personId) throws SQLException {
        String sql = "SELECT m.id, m.name FROM movie m JOIN person_movies pm ON m.id = pm.movie_id WHERE pm.person_id = ? AND pm.return_date IS NULL";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Movies currently checked out:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Movie Name: " + rs.getString("name"));
            }
        }

    }

    private static void view(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("[P]erson, [B]ooks, [M]ovies, Available Books [ab], Available Movies [am] Checked Out Books [cb], or Checked Out Movies [cm]> ");
        String action = scanner.next();
        if (action.equalsIgnoreCase("P")) {
            viewAllPerson(connection);
        } else if (action.equalsIgnoreCase("B")) {
            viewAllBooks(connection);
        } else if (action.equalsIgnoreCase("M")) {
            viewAllMovie(connection);
        } else if (action.equalsIgnoreCase("AB")) {
            viewAvailableBooks(connection);
        } else if (action.equalsIgnoreCase("AM")) {
            viewAvailableMovies(connection);
        } else if (action.equalsIgnoreCase("CB")) {
            viewAllCheckedOutBooks(connection);
        } else if (action.equalsIgnoreCase("CM")) {
            viewAllCheckedOutMovies(connection);
        } else {
            System.out.println("Invalid Action.");
        }
    }

    private static void viewAllPerson(Connection connection) throws SQLException {
        String sql = "SELECT * FROM person";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", First Name: " + rs.getString("firstname") + ", Last Name: " + rs.getString("lastname"));
            }
        }

    }

    private static void viewAvailableBooks(Connection connection) throws SQLException {
        String sql = "SELECT * FROM book WHERE id NOT IN (SELECT book_id FROM person_books WHERE return_date IS NULL)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Available Books:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Author: " + rs.getString("author") +
                        ", Name: " + rs.getString("name") + ", Price: " + rs.getDouble("price"));
            }
        }
    }

    private static void viewAvailableMovies(Connection connection) throws SQLException {
        String sql = "SELECT * FROM movie WHERE id NOT IN (SELECT movie_id FROM person_movies WHERE return_date IS NULL)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Available Movies:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Director: " + rs.getString("director") +
                        ", Name: " + rs.getString("name") + ", Price: " + rs.getDouble("price"));
            }
        }
    }

    private static void viewAllBooks(Connection connection) throws SQLException {
        String sql = "SELECT * FROM book";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Author: " + rs.getString("author") + ", Name: " + rs.getString("name") + ", Price: " + rs.getInt("price"));
            }
        }
    }

    private static void viewAllMovie(Connection connection) throws SQLException {
        String sql = "SELECT * FROM movie";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Director: " + rs.getString("director") + ", Name: " + rs.getString("name") + ", Price: " + rs.getInt("price"));
            }
        }
    }

    private static void viewAllCheckedOutBooks(Connection connection) throws SQLException {
        String sql = "SELECT * FROM person_books";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Person ID: " + rs.getInt("person_id") + ", Book ID: " + rs.getInt("book_id") + ", Checked Out: " + rs.getString("checkout_date"));
            }
        }
    }

    private static void viewAllCheckedOutMovies(Connection connection) throws SQLException {
        String sql = "SELECT * FROM person_movies";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Person ID: " + rs.getInt("person_id") + ", Movie ID: " + rs.getInt("movie_id") + ", Checked Out: " + rs.getString("checkout_date"));
            }
        }
    }
}