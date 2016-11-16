package np.edu.ku.kurc.models.collection;

import java.util.List;

import np.edu.ku.kurc.models.Author;

public class AuthorCollection extends BaseCollection<Author> {

    public AuthorCollection() {
        super();
    }

    public AuthorCollection(List<Author> list) {
        super(list);
    }
}
