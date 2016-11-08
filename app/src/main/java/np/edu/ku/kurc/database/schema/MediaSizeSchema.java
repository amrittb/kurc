package np.edu.ku.kurc.database.schema;

import android.database.sqlite.SQLiteDatabase;

public class MediaSizeSchema extends BaseSchema {

    public static final String TABLE_NAME = "media_sizes";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FILE_NAME = "file_name";
    public static final String COLUMN_WIDTH = "width";
    public static final String COLUMN_HEIGHT = "height";
    public static final String COLUMN_MIME_TYPE = "mime_type";
    public static final String COLUMN_SOURCE_URL = "source_url";
    public static final String COLUMN_MEDIA_ID = "media_id";

    /**
     * Creates Table.
     *
     * @param database  Database instance.
     */
    public static void onCreate(SQLiteDatabase database) {
        String createSql = TABLE_CREATE + " " + TABLE_IF_NOT_EXISTS + " " + TABLE_NAME + " (" +
                COLUMN_ID + " " + TYPE_INT + " " + CONSTRAINT_PK + "," +
                COLUMN_NAME + " " + TYPE_TEXT + "," +
                COLUMN_FILE_NAME + " " + TYPE_TEXT + "," +
                COLUMN_WIDTH + " " + TYPE_INT + "," +
                COLUMN_HEIGHT + " " + TYPE_INT + "," +
                COLUMN_MIME_TYPE + " " + TYPE_TEXT + "," +
                COLUMN_SOURCE_URL + " " + TYPE_TEXT + "," +
                COLUMN_MEDIA_ID + " " + TYPE_INT + "," +
                CONSTRAINT_FK + "(" + COLUMN_MEDIA_ID + ") " +
                CONSTRAINT_REFERENCES + " " + MediaSchema.TABLE_NAME + "(" + MediaSchema.COLUMN_ID + ") )";

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
