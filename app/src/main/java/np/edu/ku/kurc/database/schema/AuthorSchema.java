package np.edu.ku.kurc.database.schema;

import android.database.sqlite.SQLiteDatabase;

public abstract class AuthorSchema extends BaseSchema {

    public static final String TABLE_NAME = "authors";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SLUG = "slug";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_AVATAR = "avatar";

    /**
     * Creates Table.
     *
     * @param database  Database instance.
     */
    public static void onCreate(SQLiteDatabase database) {
        String createSql = TABLE_CREATE + " " + TABLE_IF_NOT_EXISTS + " " + TABLE_NAME + " (" +
                            COLUMN_ID + " " + TYPE_INT + " " + CONSTRAINT_PK + "," +
                            COLUMN_NAME + " " + TYPE_TEXT + "," +
                            COLUMN_SLUG + " " + TYPE_TEXT + "," +
                            COLUMN_LINK + " " + TYPE_TEXT + "," +
                            COLUMN_AVATAR + " " + TYPE_TEXT + " )";

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
