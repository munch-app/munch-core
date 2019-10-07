package app.munch.model;

/**
 * To house all Groups for validation into one interface.
 *
 * @author Fuxing Loh
 * @since 6/10/19 at 11:35 am
 */
public interface Groups {

    /**
     * Tag default validation.
     * Tag exists in multiple models, some models don't require as strict validation.
     *
     * @author Fuxing Loh
     * @since 30/8/19 at 9:58 PM
     */
    interface TagDefault {
    }

    /**
     * Image default validation.
     *
     * @author Fuxing Loh
     * @since 20/8/19 at 7:03 pm
     */
    interface ImageDefault {
    }
}
