package app.munch.username;

import com.google.common.io.Resources;
import dev.fuxing.utils.KeyUtils;
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
    private final Set<String> contains;
    private final Set<String> matches;

    @Inject
    UsernameValidator() throws IOException {
        this.contains = getResource("username/contains.txt");
        this.matches = getResource("username/matches.txt");
    }

    public boolean isValid(EntityManager entityManager, String username) {
        username = username.toLowerCase();
        if (matches.contains(username)) {
            return false;
        }

        for (String contain : contains) {
            if (username.contains(contain)) {
                return false;
            }
        }

        if (KeyUtils.ULID_REGEX.matches(username)) {
            return false;
        }

        List list = entityManager.createQuery("SELECT p.username FROM Profile p " +
                "WHERE p.username = :username")
                .setParameter("username", username)
                .getResultList();

        return list.isEmpty();
    }


    private static Set<String> getResource(String name) throws IOException {
        URL resource = Resources.getResource(name);
        return Resources.readLines(resource, StandardCharsets.UTF_8)
                .stream()
                .map(s -> {
                    s = StringUtils.trim(s);
                    s = StringUtils.lowerCase(s);
                    return s;
                })
                .collect(Collectors.toSet());
    }
}
