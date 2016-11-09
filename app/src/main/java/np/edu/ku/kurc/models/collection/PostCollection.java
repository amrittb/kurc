package np.edu.ku.kurc.models.collection;

import java.util.List;

import np.edu.ku.kurc.models.Post;

public class PostCollection extends BaseCollection<Post> {

    public PostCollection() {
        super();
    }

    public PostCollection(List<Post> list) {
        super(list);
    }
}
