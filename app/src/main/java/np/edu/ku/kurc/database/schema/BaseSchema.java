package np.edu.ku.kurc.database.schema;

import np.edu.ku.kurc.database.schema.contracts.SchemaContract;

public abstract class BaseSchema implements SchemaContract {

    public static final String TABLE_CREATE = "CREATE TABLE";
    public static final String TABLE_DROP = "DROP TABLE";
    public static final String TABLE_IF_EXISTS = "IF EXISTS";
    public static final String TABLE_IF_NOT_EXISTS = "IF NOT EXISTS";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CREATED_AT = "created_at";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    public static final String TYPE_NONE = "NONE";
    public static final String TYPE_TEXT = "TEXT";
    public static final String TYPE_DOUBLE = "REAL";
    public static final String TYPE_INT = "INTEGER";

    public static final String CONSTRAINT_UNIQUE = "UNIQUE";
    public static final String CONSTRAINT_DEFAULT = "DEFAULT";
    public static final String CONSTRAINT_NOT_NULL = "NOT NULL";

    public static final String CONSTRAINT_CHECK = "CHECK";
    public static final String CONSTRAINT_PK = "PRIMARY KEY";
    public static final String CONSTRAINT_FK = "FOREIGN KEY";
    public static final String CONSTRAINT_REFERENCES = "REFERENCES";

    @Override
    public String getPrimaryKey() {
        return COLUMN_ID;
    }
}
