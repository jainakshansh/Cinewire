package me.akshanshjain.popularmovies.Object;

public class ReviewItem {

    private String author;
    private String content;

    private ReviewItem() {
    }

    public ReviewItem(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
