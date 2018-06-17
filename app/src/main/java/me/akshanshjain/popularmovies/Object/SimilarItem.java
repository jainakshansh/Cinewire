package me.akshanshjain.popularmovies.Object;

public class SimilarItem {

    private String name;
    private String overview;
    private String image;

    private SimilarItem() {
    }

    public SimilarItem(String name, String overview, String image) {
        this.name = name;
        this.overview = overview;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getImage() {
        return image;
    }
}
