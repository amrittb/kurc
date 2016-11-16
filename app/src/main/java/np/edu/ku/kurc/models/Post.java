package np.edu.ku.kurc.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import np.edu.ku.kurc.common.Const;
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
     * Returns latestPaginated rows.
     *
     * @param context           Application Context.
     * @param perPage           Rows per page.
     * @param after             Posts after this date.
     * @param category          Post category.
     * @param attachMetadata    Flag to determine if we need to attach meta data.
     * @return                  Collection of rows in table.
     */
    public PostCollection getPostsAfter(Context context, int perPage, String after, String category, boolean attachMetadata) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        String sql = getPostsQuery(perPage, null, after, category);

        Cursor cursor = db.rawQuery(sql,null);

        PostCollection posts = (PostCollection) getCollection(cursor);

        attachMetaData(context,attachMetadata,attachMetadata,posts);

        return posts;
    }

    /**
     * Returns latestPaginated rows.
     *
     * @param context           Application Context.
     * @param perPage           Rows per page.
     * @param before            Posts before this date.
     * @param category          Post category.
     * @param attachMetadata    Flag to determine if we need to attach meta data.
     * @return                  Collection of rows in table.
     */
    public PostCollection getPostsBefore(Context context, int perPage, String before, String category, boolean attachMetadata) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        String sql = getPostsQuery(perPage, before, null, category);

        Cursor cursor = db.rawQuery(sql,null);

        PostCollection posts = (PostCollection) getCollection(cursor);

        attachMetaData(context,attachMetadata,attachMetadata,posts);

        return posts;
    }

    /**
     * Returns posts query for given arguments.
     *
     * @param perPage   Posts per page.
     * @param before    Posts before this date.
     * @param after     Posts after this date.
     * @param category  Posts in this category.
     * @return          Returns query string.
     */
    @NonNull
    private String getPostsQuery(int perPage, String before, String after, String category) {
        String sql = "SELECT posts.* " +
                "FROM category_post " +
                "JOIN categories ON categories._id = category_post.category_id " +
                "JOIN posts ON posts._id = category_post.post_id ";

        if(category != null) {
            sql += "WHERE categories.slug = " + category + " ";
        }

        if(before != null) {
            sql += "AND WHERE created_at < " + before + " ";
        }

        if(after != null) {
            sql += "AND WHERE created_at > " + after + " ";
        }

        sql += "ORDER BY created_at DESC ";

        sql += "LIMIT " + Integer.toString(perPage);

        return sql;
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

        if(posts.isEmpty()) {
            posts = latestPaginated(context, 1, 1, false, false);
        }

        if ( ! posts.isEmpty()) {

            attachMetaData(context,true,false,posts);

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

        attachMetaData(context, includeAuthor, includeFeaturedMedia, posts);

        return posts;
    }

    private void attachMetaData(Context context, boolean includeAuthor, boolean includeFeaturedMedia, PostCollection posts) {
        if(includeAuthor && posts.size() != 0) {
            posts.lazyLoadAuthors(context);
        }

        if(includeFeaturedMedia && posts.size() != 0) {
            posts.lazyLoadFeaturedMedia(context);
        }
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
        attachMetaData(context,true,false,posts);

        return posts.get(0);
    }
}
