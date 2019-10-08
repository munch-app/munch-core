package app.munch.controller;

import app.munch.model.ChangeGroup;
import app.munch.model.Profile;

import javax.inject.Singleton;
import java.sql.Timestamp;
import java.util.function.Consumer;

/**
 * Created by: Fuxing
 * Date: 4/9/19
 * Time: 2:16 pm
 */
@Singleton
public final class ChangeController extends Controller {

    public void newGroup(String profileUid, String name, String description, Consumer<ChangeGroup> consumer) {
        Profile profile = new Profile();
        profile.setUid(profileUid);

        ChangeGroup group = new ChangeGroup();
        group.setCreatedBy(profile);
        group.setName(name);
        group.setDescription(description);
        newGroup(group, consumer);
    }

    public void newGroup(ChangeGroup group, Consumer<ChangeGroup> consumer) {
        provider.reduce(entityManager -> {
            if (group.getCreatedBy() != null) {
                group.setCreatedBy(entityManager.find(Profile.class, group.getCreatedBy().getUid()));
            }

            entityManager.persist(group);
            return group;
        });

        consumer.accept(group);

        provider.with(entityManager -> {
            entityManager.merge(group);
            group.setCompletedAt(new Timestamp(System.currentTimeMillis()));
            entityManager.persist(group);
        });
    }
}
