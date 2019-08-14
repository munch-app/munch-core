package app.munch.username;

import com.google.common.io.Resources;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 14/8/19
 * Time: 3:13 pm
 */
@SuppressWarnings("UnstableApiUsage")
@Singleton
public class UsernameValidator {
    private final Set<String> blacklist;

    @Inject
    UsernameValidator() throws IOException {
        URL resource = Resources.getResource("username-blacklist.txt");
        this.blacklist = Resources.readLines(resource, StandardCharsets.UTF_8)
                .stream()
                .map(s -> {
                    s = StringUtils.trim(s);
                    s = StringUtils.lowerCase(s);
                    return s;
                })
                .collect(Collectors.toSet());
    }

    public boolean isValid(EntityManager entityManager, String username) {
        if (blacklist.contains(username)) {
            return false;
        }

        List list = entityManager.createQuery("SELECT username from Profile " +
                "WHERE username = :username")
                .setParameter("username", username)
                .getResultList();

        return list.isEmpty();
    }
}
