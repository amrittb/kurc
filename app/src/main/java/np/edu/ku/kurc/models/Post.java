package np.edu.ku.kurc.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import np.edu.ku.kurc.database.DatabaseHelper;
import np.edu.ku.kurc.database.schema.CategoryPostSchema;
import np.edu.ku.kurc.database.schema.PostSchema;
import np.edu.ku.kurc.database.schema.SchemaFactory;
import np.edu.ku.kurc.models.collection.AuthorCollection;
import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.collection.CategoryCollection;
import np.edu.ku.kurc.models.collection.MediaCollection;
import np.edu.ku.kurc.models.collection.PostCollection;
import np.edu.ku.kurc.models.exception.DatabaseErrorException;
import np.edu.ku.kurc.models.transformers.BaseTransformer;
import np.edu.ku.kurc.models.transformers.PostTransformer;
import np.edu.ku.kurc.models.transformers.TransformerFactory;

public class Post extends BaseModel<Post,PostSchema> {

    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("date")
    public Date date;

    @SerializedName("modified")
    public Date modified;

    @SerializedName("slug")
    public String slug;

    @SerializedName("link")
    public String link;

    @SerializedName("content")
    public String content;

    @SerializedName("excerpt")
    public String excerpt;

    @SerializedName("sticky")
    public boolean isSticky;

    @SerializedName("_embedded")
    public Embedded embedded;

    @SerializedName("author")
    public int authorId;

    @SerializedName("featured_media")
    public int featuredMediaId;

    @SerializedName("categories")
    public List<Integer> categoryIds;

    private Author attachedAuthor;
    private FeaturedMedia attachedMedia;

    public Post() {}

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

    /**
     * Checks if the post has content.
     *
     * @return  Flag to determine if the post has content.
     */
    public boolean hasContent() {
        return ! ((content == null) || (content.isEmpty()));
    }

    /**
     * Checks if the post has featured attachedMedia.
     *
     * @return Flag to check if featured attachedMedia exists.
     */
    public boolean hasFeaturedMedia() {
        return (attachedMedia != null) || (embedded != null && embedded.featured != null && (! embedded.featured.isEmpty() && embedded.featured.get(0).hasImageData()));
    }

    /**
     * Returns featured attachedMedia.
     *
     * @return Featured attachedMedia instance.
     */
    public FeaturedMedia getFeaturedMedia() {
        if(attachedMedia != null) {
            return attachedMedia;
        } else if(embedded != null && (!embedded.featured.isEmpty())) {
            return embedded.featured.get(0);
        }

        return null;
    }

    /**
     * Returns Author of the post.
     *
     * @return Author of post.
     */
    public Author getAuthor() {
        if(attachedAuthor != null) {
            return attachedAuthor;
        } else if(embedded != null) {
            return embedded.authors.get(0);
        }

        return null;
    }

    /**
     * Returns categories from array of terms.
     *
     * @return CategoryCollection.
     */
    public CategoryCollection getCategories() {
        return new CategoryCollection(Arrays.asList(embedded.terms[0]));
    }

    /**
     * Returns latest latestPaginated posts.
     *
     * @param context   Application Context.
     * @param perPage   Posts per page.
     * @param page      Page of which posts are to be fetched.
     * @return          Collection of posts.
     */
    public PostCollection latestPaginated(Context context, int perPage, int page) {
        int offset = (perPage * (page - 1));

        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(getSchema().getTableName(),
                null,
                null,
                null,
                null,
                null,
                PostSchema.COLUMN_CREATED_AT + " DESC",
                Integer.toString(offset) + "," + Integer.toString(perPage));

        return (PostCollection) getCollection(cursor);
    }

    /**
     * Returns latest pinned post.
     *
     * @param context   Application Context.
     * @return          Latest Pinned post.
     */
    public Post getLatestPinned(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(getSchema().getTableName(),
                null,
                PostSchema.COLUMN_STICKY + "=1",
                null,
                null,
                null,
                "created_at DESC",
                "1");

        PostCollection posts = (PostCollection) getCollection(cursor);

        if ( ! posts.isEmpty()) {

            posts.loadMetadata(context);

            return posts.get(0);
        }

        return null;
    }

    /**
     * Gets Latest paginated posts which includes author and featured media.
     *
     * @param context               Application Context.
     * @param perPage               Posts per page.
     * @param page                  Page of posts.
     * @param includeAuthor         Flag which determines if authors are to be included.
     * @param includeFeaturedMedia  Flag which determines if media are to be included.
     * @return                      Post Collection of fetched posts.
     */
    public PostCollection latestPaginated(Context context, int perPage, int page, boolean includeAuthor, boolean includeFeaturedMedia) {
        PostCollection posts = latestPaginated(context,perPage,page);

        posts.loadMetadata(context);

        return posts;
    }



    @Override
    public PostSchema getSchema() {
        return SchemaFactory.getInstance(PostSchema.class);
    }

    @Override
    public BaseCollection<Post> getCollection(List<Post> list) {
        return new PostCollection(list);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public BaseTransformer<Post> getTransformer() {
        return TransformerFactory.getInstance(PostTransformer.class);
    }

    /**
     * Attaches author from author collection.
     *
     * @param authors   Collection to attach author from.
     */
    public void attachAuthors(AuthorCollection authors) {
        for(Author a: authors) {
            if(a.id == authorId) {
                attachedAuthor = a;
                break;
            }
        }
    }

    /**
     * Attaches media from media collection.
     *
     * @param mediaCollection   Collection to attach media from.
     */
    public void attachMedia(MediaCollection mediaCollection) {
        for(FeaturedMedia m: mediaCollection) {
            if(m.id == featuredMediaId) {
                attachedMedia = m;
                break;
            }
        }
    }

    @Override
    public long save(SQLiteDatabase db) throws DatabaseErrorException {
        Author author = getAuthor();

        if(author != null) {
            author.save(db);
        }

        FeaturedMedia media = getFeaturedMedia();

        if(featuredMediaId != 0 && media != null) {
            media.save(db);
        }

        long savedId = super.save(db);

        saveCategories(db, savedId);

        return savedId;
    }

    /**
     * Saves categories to database.
     *
     * @param db        Database Instance.
     * @param savedId   Saved Post Id.
     */
    private void saveCategories(SQLiteDatabase db, long savedId) {
        try {
            db.beginTransaction();

            ContentValues values = new ContentValues();

            for(Integer categoryId: categoryIds) {
                values.put(CategoryPostSchema.COLUMN_POST_ID,savedId);
                values.put(CategoryPostSchema.COLUMN_CATEGORY_ID,categoryId);

                db.insertWithOnConflict(CategoryPostSchema.TABLE_NAME,null,values,SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Post findById(Context context, int id) {
        Post post = super.findById(context, id);

        if(post == null) {
            return null;
        }

        PostCollection posts = new PostCollection();
        posts.add(post);
        posts.loadMetadata(context);

        return posts.get(0);
    }

    /**
     * Returns newest published date.
     *
     * @param context   Application Context.
     * @return          Newest published date.
     */
    public String getNewestPublishedDate(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        String sql = "SELECT max(created_at) as after FROM posts";

        String after = null;

        Cursor c = db.rawQuery(sql,null);

        if(c.moveToFirst()) {
            after = c.getString(0);
        }

        c.close();

        return after;
    }
}
