/**
 * app.munch.model are models that are unique and mostly persisted in munch sql database.
 * Model that are shared, should be in app.munch.model package, while model that are only used inside a
 * Model should be inside a model.
 * Primary Key handling:
 * - All top level entity must be given L13-ID = (L12-ID + single char postfix)
 * - Column 'id' is usually used for entity with public facing entities
 * - Column 'uid' is usually used for entity with many entities (e.g. PlaceImage, Place being the main entity hence 'uid' shall be used for PlaceImage for its primary key.)
 * - When 'uid' is used, KeyUtils.nextULID() should be used to generated it. ULID allow it to be sorted and placed in TransportCursor
 *
 *
 * <p>
 * Created by: Fuxing
 * Date: 2019-08-08
 * Time: 20:58
 */
package app.munch.model;

