package np.edu.ku.kurc.database.schema.contracts;

public interface SchemaContract {

    /**
     * Returns table name of schema.
     *
      * @return Table name of schema.
     */
    String getTableName();

    /**
     * Returns the primary key of schema.
     *
     * @return  Primary key column of schema.
     */
    String getPrimaryKey();
}
