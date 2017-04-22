package munch.places.menu.data;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;
import org.hibernate.exception.ConstraintViolationException;

import javax.annotation.Nullable;
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

    public List<Menu> list(String placeId, int from, int size) {
        return provider.reduce(em -> em
                .createQuery("FROM PostgresMenu WHERE placeId = :placeId", PostgresMenu.class)
                .setParameter("placeId", placeId)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList())
                .stream()
                .map(PostgresMenu::getMenu)
                .collect(Collectors.toList());
    }

    public void put(Menu menu) {
        PostgresMenu data = new PostgresMenu();
        data.setPlaceId(menu.getPlaceId());
        data.setMenuId(menu.getMenuId());
        data.setCreatedDate(menu.getCreatedDate());
        data.setMenu(menu);

        try {
            provider.with(em -> em.persist(data));
        } catch (ConstraintViolationException e) {
            if (e.getConstraintName().equals(PostgresMenu.UNIQUE_CONSTRAINT_MENU_ID))
                throw new MenuAlreadyExistException();
            throw e;
        }
    }

    @Nullable
    public Menu get(String menuId) {
        return provider.optional(em -> em
                .createQuery("FROM PostgresMenu WHERE menuId = :menuId", PostgresMenu.class)
                .setParameter("menuId", menuId)
                .getSingleResult())
                .map(PostgresMenu::getMenu)
                .orElse(null);
    }

    public void delete(String menuId) {
        provider.with(em -> em
                .createQuery("DELETE FROM PostgresMenu WHERE menuId = :menuId")
                .setParameter("menuId", menuId)
                .executeUpdate());
    }
}
