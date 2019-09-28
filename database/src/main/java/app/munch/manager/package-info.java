/**
 * @deprecated use/migrate to controller instead.
 * <p>
 * Why some entity have their own manager?
 * <p>
 * When there are too many rules and it involve data lifecycle, an EntityManager will be created to help facilitate
 * better understand and modularity of the entity.
 * Manager created here should not accept 'jpa.EntityManager'. The manager itself should procure their own transaction
 * and close them within it's lifecycle.
 *
 * <p>
 * Created by: Fuxing
 * Date: 2019-08-08
 * Time: 20:58
 */
package app.munch.manager;

