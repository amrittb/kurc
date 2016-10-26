package np.edu.ku.kurc.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Post {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

    public int id;
    public String title;
    public Date date;
    public Date modified;

    public String slug;
    public String link;

    public String content;
    public String excerpt;

    public Post(int id,String title, Date date, Date modified, String slug, String link, String content, String excerpt) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.modified = modified;
        this.slug = slug;
        this.link = link;
        this.content = content;
        this.excerpt = excerpt;
    }

    public String getDateString() {
        return dateFormat.format(this.date);
    }

    public String getModifiedDateString() {
        return dateFormat.format(this.modified);
    }
}
