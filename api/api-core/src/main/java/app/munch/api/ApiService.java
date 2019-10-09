package app.munch.api;

import app.munch.query.EntityQuery;
import app.munch.model.Profile;
import dev.fuxing.err.ConflictException;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportService;
import org.intellij.lang.annotations.Language;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Fuxing Loh
 * @since 7/10/19 at 7:11 pm
 */
public abstract class ApiService implements TransportService {

    protected TransactionProvider provider;

    @Inject
    void inject(TransactionProvider provider) {
        this.provider = provider;
    }

    protected <T> TransportList queryAuthorized(TransportContext context,
                                                @Language("HQL") String select,
                                                Class<T> clazz,
                                                TriConsumer<Profile, TransportCursor, EntityQuery<T>> queryChaining,
                                                BiConsumer<TransportCursor.Builder, T> cursorBuilder) {
        @NotNull TransportCursor cursor = context.queryCursor();
        String accountId = context.get(ApiRequest.class).getAccountId();

        return provider.reduce(true, entityManager -> {
            Profile profile = Profile.findByAccountId(entityManager, accountId);
            if (profile == null) throw new ForbiddenException("profile not found");

            EntityQuery<T> chain = EntityQuery.select(entityManager, select, clazz);
            queryChaining.accept(profile, cursor, chain);

            return chain.asTransportList((entity, builder) -> {
                builder.putAll(cursor);
                cursorBuilder.accept(builder, entity);
            });
        });
    }

    /**
     * @param context       transport context to read from request
     * @param entityMapper  map to the entity you are getting
     * @param profileMapper entity -> profile mapper, used to check whether current account has access to profile
     * @param <T>           Entity model type
     * @return Entity, if authorized
     * @throws dev.fuxing.err.ForbiddenException if user don't have access to entity
     */
    protected <T> T getAuthorized(TransportContext context,
                                  Function<EntityManager, T> entityMapper,
                                  Function<T, Profile> profileMapper) throws ForbiddenException {
        String accountId = context.get(ApiRequest.class).getAccountId();

        return provider.reduce(true, entityManager -> {
            T entity = entityMapper.apply(entityManager);
            Profile.authorize(entityManager, accountId, profileMapper.apply(entity));
            return entity;
        });
    }

    protected <T> T patch(TransportContext context,
                          Function<EntityManager, T> entityMapper,
                          BiFunction<EntityManager, EntityPatch.JsonBody<T>, T> patcher) throws ForbiddenException {
        return provider.reduce(false, entityManager -> {
            T entity = entityMapper.apply(entityManager);
            if (entity == null) return null;
            return patcher.apply(entityManager, EntityPatch.with(entityManager, entity, context.bodyAsJson()));
        });
    }

    /**
     * @param context       transport context to read from request
     * @param entityMapper  map to the entity you are patching
     * @param profileMapper entity -> profile mapper, used to check whether current account has access to profile
     * @param patcher       patcher provider for you to edit the model in chain like structure
     * @param <T>           Entity model type
     * @return patched Entity
     * @throws dev.fuxing.err.ForbiddenException if user don't have access to entity
     */
    protected <T> T patchAuthorized(TransportContext context,
                                    Function<EntityManager, T> entityMapper,
                                    Function<T, Profile> profileMapper,
                                    Function<EntityPatch.JsonBody<T>, T> patcher) throws ForbiddenException {
        String accountId = context.get(ApiRequest.class).getAccountId();

        return provider.reduce(false, entityManager -> {
            T entity = entityMapper.apply(entityManager);
            if (entity == null) return null;

            Profile.authorize(entityManager, accountId, profileMapper.apply(entity));

            return patcher.apply(EntityPatch.with(entityManager, entity, context.bodyAsJson()));
        });
    }

    protected <T> T postAuthorized(TransportContext context, BiFunction<EntityManager, Profile, T> supplier) {
        String accountId = context.get(ApiRequest.class).getAccountId();

        return provider.reduce(entityManager -> {
            Profile profile = Profile.findByAccountId(entityManager, accountId);
            if (profile == null) throw new ForbiddenException("profile not found");

            return supplier.apply(entityManager, profile);
        });
    }

    public void isAuthorized(EntityManager entityManager, TransportContext context, Profile profile) {
        String accountId = context.get(ApiRequest.class).getAccountId();
        Profile.authorize(entityManager, accountId, profile);
    }

    /**
     * @param entityManager to use
     * @param context       to resolve ApiRequest for Profile
     * @return current logged in use Profile
     * @throws ConflictException  if profile not found
     * @throws ForbiddenException if use not logged in
     */
    public Profile findProfile(EntityManager entityManager, TransportContext context) {
        String accountId = context.get(ApiRequest.class).getAccountId();
        Profile profile = Profile.findByAccountId(entityManager, accountId);
        if (profile != null) {
            return null;
        }
        throw new ConflictException("Profile not found.");
    }

    public interface TriConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }
}
