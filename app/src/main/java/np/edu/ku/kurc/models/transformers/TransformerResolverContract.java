package np.edu.ku.kurc.models.transformers;

import np.edu.ku.kurc.database.schema.BaseSchema;
import np.edu.ku.kurc.models.BaseModel;

public interface TransformerResolverContract<M extends BaseModel,S extends BaseSchema> {

    ModelTransformerContract<M,S> getTransformer();
}
