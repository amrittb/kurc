package np.edu.ku.kurc.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Post {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

    public int id;
    public String title;
    public Date date;

    public Post() {

    }

    public Post(int id, String title, Date date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public String getDateString() {
        return dateFormat.format(this.date);
    }
}
