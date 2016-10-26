package np.edu.ku.kurc.models;

import android.content.Context;
import android.text.format.DateUtils;

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

    public String getDateString(Context context) {
        return (String) DateUtils.getRelativeDateTimeString(context, date.getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
    }

    public String getModifiedDateString(Context context) {
        return (String) DateUtils.getRelativeDateTimeString(context, modified.getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);

    }
}
