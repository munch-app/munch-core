/**
 * When there are too many rules and it involve data lifecycle,
 * a Controller should be created to help facilitate and better understand the modularity of the entity.
 * <p>
 * Controller created here should not accept 'jpa.EntityManager'.
 * The manager itself should procure their own transaction and close them within it's lifecycle.
 * <p>
 * Date: 2019-09-28
 * Time: 23:23
 *
 * @author Fuxing Loh
 */
package app.munch.controller;

