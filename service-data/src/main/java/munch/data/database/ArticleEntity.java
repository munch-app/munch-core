package munch.data.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import munch.data.Article;
import munch.data.database.hibernate.PojoUserType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

/**
 * Created by: Fuxing
 * Date: 18/8/2017
 * Time: 1:20 AM
 * Project: munch-core
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeDefs(value = {
        @TypeDef(name = "data", typeClass = ArticleEntity.ArticleUserType.class)
})
@Table(indexes = {
        // Cluster Index: placeId, sortKey desc
        @Index(name = "index_munch_article_entity_cycle_no", columnList = "cycleNo"),
})
public final class ArticleEntity extends AbstractEntity<Article> {

    private String placeId;
    private String articleId;
    private String sortKey;

    @Id
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Column(nullable = false, length = 255)
    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Column(nullable = false)
    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public final static class ArticleUserType extends PojoUserType<Article> {
        public ArticleUserType() {
            super(Article.class);
        }
    }
}
