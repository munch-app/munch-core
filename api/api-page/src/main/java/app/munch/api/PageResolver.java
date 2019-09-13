package app.munch.api;

import app.munch.model.*;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.jpa.TransactionProvider;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 22/8/19
 * Time: 9:35 pm
 */
@Singleton
public final class PageResolver {

    private final TransactionProvider provider;

    @Inject
    public PageResolver(TransactionProvider provider) {
        this.provider = provider;
    }

    public List<Map> resolve(String name) {
        if (StringUtils.equals(name, "home")) {
            return home();
        }

        return List.of();
    }

    @SuppressWarnings("unchecked")
    private List<Map> home() {
        List<Map> mapList = provider.reduce(entityManager -> {
            List<Map> list = new ArrayList<>();
            list.add(getFeatured(entityManager));
            list.addAll(getPublications(entityManager));
            return list;
        });

        mapList.forEach(map -> {
            HibernateUtils.clean(map.get("publication"));
            ((List<Article>) map.get("articles")).forEach(article -> {
                HibernateUtils.clean(article);
                HibernateUtils.clean(article.getProfile());
            });
        });
        return mapList;
    }

    private static Map getFeatured(EntityManager entityManager) {
        return Map.of(
                "articles", selectArticlesFromPublication(entityManager, "3kvgmz1qpnxg5", 7)
        );
    }

    private static List<Map> getPublications(EntityManager entityManager) {
        List<Publication> publications = entityManager.createQuery("FROM Publication " +
                "WHERE id != '3kvgmz1qpnxg5'" +
                "ORDER BY updatedAt DESC", Publication.class)
                .getResultList();

        return publications.stream()
                .map(publication -> {
                    return Map.of(
                            "articles", selectArticlesFromPublication(entityManager, publication.getId(), 12),
                            "publication", publication
                    );
                }).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static List<Article> selectArticlesFromPublication(EntityManager entityManager, String id, int size) {
        List<Object[]> articles = entityManager.createQuery("SELECT " +
                "pa.article.id,pa.article.updatedAt,pa.article.createdAt,pa.article.slug," +
                "pa.article.title,pa.article.description,pa.article.image,pa.article.profile,pa.article.tags " +
                "FROM PublicationArticle pa " +
                "WHERE pa.publication.id = :id " +
                "ORDER BY pa.position DESC ", Object[].class)
                .setParameter("id", id)
                .setMaxResults(size)
                .getResultList();

        return articles.stream()
                .map(objects -> {
                    Article article = new Article();
                    article.setId((String) objects[0]);
                    article.setUpdatedAt((Date) objects[1]);
                    article.setCreatedAt((Date) objects[2]);
                    article.setSlug((String) objects[3]);
                    article.setTitle((String) objects[4]);
                    article.setDescription((String) objects[5]);
                    article.setImage((Image) objects[6]);
                    article.setProfile((Profile) objects[7]);
                    article.setTags((Set<Tag>) objects[8]);
                    return article;
                })
                .collect(Collectors.toList());
    }
}
