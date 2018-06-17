package me.akshanshjain.popularmovies.Object;

public class TrailerItem {

    private String name;
    private String key;

    private TrailerItem() {
    }

    public TrailerItem(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
