package np.edu.ku.kurc.models.collection;

import java.util.List;

import np.edu.ku.kurc.models.FeaturedMedia;

public class MediaCollection extends BaseCollection<FeaturedMedia> {

    public MediaCollection() {
        super();
    }

    public MediaCollection(List<FeaturedMedia> list) {
        super(list);
    }
}
