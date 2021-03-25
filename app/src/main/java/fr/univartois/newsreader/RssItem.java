package fr.univartois.newsreader;

public class RssItem {
    public String title;
    public String description;
    public String pubDate;
    public String link;

    public RssItem(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

    public RssItem() {

    }
}
