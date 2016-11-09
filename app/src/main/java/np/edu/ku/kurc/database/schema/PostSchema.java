package np.edu.ku.kurc.database.schema;

import android.database.sqlite.SQLiteDatabase;

public class PostSchema extends BaseSchema {

    public static final String TABLE_NAME = "posts";

    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_SLUG = "slug";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_EXCERPT = "excerpt";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_STICKY = "sticky";
    public static final String COLUMN_AUTHOR_ID = "author_id";
    public static final String COLUMN_FEATURED_MEDIA_ID = "featured_media_id";

    /**
     * Creates Table.
     *
     * @param database  Database instance.
     */
    public static void onCreate(SQLiteDatabase database) {
        String createSql = TABLE_CREATE + " " + TABLE_IF_NOT_EXISTS + " " + TABLE_NAME + " (" +
                COLUMN_ID + " " + TYPE_INT + " " + CONSTRAINT_PK + "," +
                COLUMN_TITLE + " " + TYPE_TEXT + "," +
                COLUMN_SLUG + " " + TYPE_TEXT + "," +
                COLUMN_LINK + " " + TYPE_TEXT + "," +
                COLUMN_EXCERPT + " " + TYPE_TEXT + "," +
                COLUMN_CONTENT + " " + TYPE_TEXT + "," +
                COLUMN_STICKY + " " + TYPE_INT + "," +
                COLUMN_CREATED_AT + " " + TYPE_TEXT + "," +
                COLUMN_UPDATED_AT + " " + TYPE_TEXT + "," +
                COLUMN_AUTHOR_ID + " " + TYPE_INT + "," +
                COLUMN_FEATURED_MEDIA_ID + " " + TYPE_INT + "," +
                CONSTRAINT_FK + "(" + COLUMN_AUTHOR_ID + ") " +
                CONSTRAINT_REFERENCES + " " + AuthorSchema.TABLE_NAME + "(" + AuthorSchema.COLUMN_ID + ")," +
                CONSTRAINT_FK + "(" + COLUMN_FEATURED_MEDIA_ID + ") " +
                CONSTRAINT_REFERENCES + " " + MediaSchema.TABLE_NAME + "(" + MediaSchema.COLUMN_ID + ")" +
                ")";

        database.execSQL(createSql);
    }

    /**
     * Upgrades Table.
     *
     * @param database      Database instance.
     * @param oldVersion    Old Version database.
     * @param newVersion    New Version database.
     */
    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(TABLE_DROP + " " + TABLE_IF_EXISTS + " " + TABLE_NAME);
        onCreate(database);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
