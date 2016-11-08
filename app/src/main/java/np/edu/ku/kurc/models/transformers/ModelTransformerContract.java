package np.edu.ku.kurc.models.transformers;

import android.content.ContentValues;

public interface ModelTransformerContract<M,S> {

    ContentValues transform(M model, S schema);
}
