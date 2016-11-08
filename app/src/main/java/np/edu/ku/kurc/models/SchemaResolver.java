package np.edu.ku.kurc.models;

import np.edu.ku.kurc.database.schema.BaseSchema;

public interface SchemaResolver<T extends BaseSchema> {

    T getSchema();
}
