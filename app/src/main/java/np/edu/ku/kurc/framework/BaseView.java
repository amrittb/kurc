package np.edu.ku.kurc.framework;

public interface BaseView<T> {

    /**
     * Sets presenter instance.
     *
     * @param presenter Presenter instance to be set in view.
     */
    void setPresenter(T presenter);
}
