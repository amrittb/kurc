package np.edu.ku.kurc.models.collection;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import np.edu.ku.kurc.models.Author;
import np.edu.ku.kurc.models.FeaturedMedia;
import np.edu.ku.kurc.models.Post;

public class PostCollection extends BaseCollection<Post> {

    public PostCollection() {
        super();
    }

    public PostCollection(List<Post> list) {
        super(list);
    }

    /**
     * Loads Metadata for posts from database.
     *
     * @param context   Application Context.
     */
    public void loadMetadata(Context context) {
        if(! isEmpty()) {
            lazyLoadAuthors(context);
            lazyLoadFeaturedMedia(context);
        }
    }

    /**
     * Loads Authors for each posts.
     *
     * @param context   Application Context.
     */
    private void lazyLoadAuthors(Context context) {
        List<Integer> authorIds = new ArrayList<>();

        for(Post p: this) {
            if( ! authorIds.contains(p.authorId)) {
                authorIds.add(p.authorId);
            }
        }

        Author a = new Author();

        AuthorCollection authors = (AuthorCollection) a.allIn(context, authorIds);

        for(Post p: this) {
            p.attachAuthors(authors);
        }
    }

    /**
     * Loads Featured Media for each posts.
     *
     * @param context   Application Context.
     */
    private void lazyLoadFeaturedMedia(Context context) {
        List<Integer> mediaIds = new ArrayList<>();

        for(Post p: this) {
            if( ! mediaIds.contains(p.featuredMediaId) && p.featuredMediaId != 0) {
                mediaIds.add(p.featuredMediaId);
            }
        }

        FeaturedMedia media = new FeaturedMedia();

        MediaCollection mediaCollection = (MediaCollection) media.allIn(context, mediaIds);

        for(Post p: this) {
            p.attachMedia(mediaCollection);
        }
    }
}
