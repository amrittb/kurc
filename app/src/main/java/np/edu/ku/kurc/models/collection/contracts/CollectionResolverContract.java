package np.edu.ku.kurc.models.collection.contracts;

import java.util.List;

import np.edu.ku.kurc.models.BaseModel;
import np.edu.ku.kurc.models.collection.BaseCollection;

public interface CollectionResolverContract<M extends BaseModel> {

    /**
     * Returns collection for given model.
     *
     * @param list  List from which the collection is to be populated.
     * @return      Populated Collection.
     */
    BaseCollection<M> getCollection(List<M> list);
}
