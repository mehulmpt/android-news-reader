package unofficial.com.inshorts.newsreader;

/**
 * Created by Mehul on 6/20/2016.
 */
public class newsItem {
    private String newsHeading;
    private String newsDesc;
    private String newsDescSmall;
    private String time;
    private String date;
    private String url;
    private String imageURL;

    public newsItem(String newsHeading, String newsDesc, String date, String time, String url, String imageURL) {
        this.date = date;
        this.imageURL = imageURL;
        this.newsDesc = newsDesc;
        this.newsHeading = newsHeading;
        this.time = time;
        this.url = url;

        this.newsDescSmall = this.newsDesc.substring(0, 50) + "...";
    }

    public String getDate() {
        return date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getNewsDesc() {
        return newsDesc;
    }

    public String getNewsDescSmall() {
        return newsDescSmall;
    }

    public String getNewsHeading() {
        return newsHeading;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}
