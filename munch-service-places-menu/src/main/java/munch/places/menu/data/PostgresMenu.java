package munch.places.menu.data;

import munch.places.menu.data.hibernate.MenuUserType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 22/4/2017
 * Time: 3:43 PM
 * Project: munch-core
 */
@Entity
@TypeDef(name = "menu", typeClass = MenuUserType.class)
@Table(uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"menuId"},
                name = PostgresMenu.UNIQUE_CONSTRAINT_MENU_ID
        )
}, indexes = {
        @Index(name = "INDEX_MENU_PLACE_MENU_CREATED", columnList = "placeId, menuId, createdDate")
})
public final class PostgresMenu {
    public static final String UNIQUE_CONSTRAINT_MENU_ID = "UK_PLACE_MENU_MENU_ID";

    private String id;
    private String placeId;
    private String menuId;
    private Date createdDate;

    private Menu menu;

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)", nullable = false)
    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(nullable = false, updatable = false, length = 255)
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Column(nullable = false, updatable = false, length = 255)
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    @Column(nullable = false, updatable = false)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Type(type = "menu")
    @Column(nullable = false)
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
