public class Book {
    private String name;
    private String author;
    private String genre;
    private double price;

    public Book(String name, String author, String genre, double price) {
        this.name = name;
        this.author = author;
        this.genre = genre; // Initialize genre in the constructor
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() { // Getter for genre
        return genre;
    }

    public void setGenre(String genre) { // Setter for genre
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", price=" + price +
                '}';
    }
}
