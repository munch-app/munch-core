/**
 * When there are too many rules and it involve data lifecycle,
 * a Controller should be created to help facilitate and better understand the modularity of the entity.
 * <p>
 * Controller created here should not accept 'jpa.EntityManager'.
 * The manager itself should procure their own transaction and close them within it's lifecycle.
 * <p>
 *
 * @author Fuxing Loh
 * @since 2019-09-28 at 23:23
 */
package app.munch.controller;

