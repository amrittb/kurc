package np.edu.ku.kurc.database.schema;

import android.database.sqlite.SQLiteDatabase;

public class MediaSchema extends BaseSchema {

    public static final String TABLE_NAME = "media";

    public static final String COLUMN_TITLE = "title";

    /**
     * Creates Table.
     *
     * @param database  Database instance.
     */
    public static void onCreate(SQLiteDatabase database) {
        String createSql = TABLE_CREATE + " " + TABLE_IF_NOT_EXISTS + " " + TABLE_NAME + " (" +
                COLUMN_ID + " " + TYPE_INT + " " + CONSTRAINT_PK + "," +
                COLUMN_TITLE + " " + TYPE_TEXT + ")";

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
