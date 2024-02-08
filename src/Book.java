public class Book {
    private String name;
    private String author;

    public Book(String author, String name) {
        this.name = name;
        this.author = author;
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

    @Override
    public String toString() {
        return "Person{" +
                "author=" + author +
                ", name='" + name + '\'' +
                '}';
    }
}

