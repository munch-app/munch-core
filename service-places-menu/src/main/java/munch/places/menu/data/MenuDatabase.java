package munch.places.menu.data;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.ConstraintViolationException;

import javax.annotation.Nullable;
import javax.persistence.RollbackException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 9:05 PM
 * Project: munch-core
 */
@Singleton
public class MenuDatabase {

    private final TransactionProvider provider;

    @Inject
    public MenuDatabase(TransactionProvider provider) {
        this.provider = provider;
    }

    /**
     * @param placeId place id for each to list all menu
     * @param from    start from
     * @param size    size of each
     * @return List of Menu
     */
    public List<Menu> list(String placeId, int from, int size) {
        return provider.reduce(em -> em
                .createQuery("FROM PostgresMenu WHERE placeId = :placeId ORDER BY createdDate DESC", PostgresMenu.class)
                .setParameter("placeId", placeId)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList())
                .stream()
                .map(PostgresMenu::getMenu)
                .collect(Collectors.toList());
    }

    /**
     * @param menu menu to put
     */
    public void put(Menu menu) {
        PostgresMenu data = new PostgresMenu();
        data.setPlaceId(menu.getPlaceId());
        data.setMenuId(menu.getMenuId());
        data.setCreatedDate(menu.getCreatedDate());
        data.setMenu(menu);

        try {
            provider.with(em -> em.persist(data));
        } catch (RollbackException e) {
            ExceptionUtils.getThrowableList(e).stream()
                    .filter(t -> t instanceof ConstraintViolationException)
                    .map(t -> (ConstraintViolationException) t)
                    .filter(c -> c.getConstraintName().equals(PostgresMenu.UNIQUE_CONSTRAINT_MENU_ID))
                    .findAny()
                    .orElseThrow(() -> e);

            // If reach here means menu already exist in database
            throw new MenuExistException();
        }
    }

    /**
     * @param menuId id of menu to get
     * @return Menu or null
     */
    @Nullable
    public Menu get(String menuId) {
        return provider.optional(em -> em
                .createQuery("FROM PostgresMenu WHERE menuId = :menuId", PostgresMenu.class)
                .setParameter("menuId", menuId)
                .getSingleResult())
                .map(PostgresMenu::getMenu)
                .orElse(null);
    }

    /**
     * @param menuId id of menu to delete
     */
    public void delete(String menuId) {
        provider.with(em -> em
                .createQuery("DELETE FROM PostgresMenu WHERE menuId = :menuId")
                .setParameter("menuId", menuId)
                .executeUpdate());
    }

}
