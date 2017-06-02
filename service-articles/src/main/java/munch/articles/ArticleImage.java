package munch.articles;

import munch.articles.hibernate.PojoUserType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

/**
 * Created By: Fuxing Loh
 * Date: 2/6/2017
 * Time: 4:10 PM
 * Project: munch-core
 */
@Entity
@TypeDefs(value = {
        @TypeDef(name = "types", typeClass = ArticleImage.UserType.Types.class)
})
public final class ArticleImage {
    private String url;
    private String key;
    private Type[] types;

    private Article article;

    @Id
    @Column(nullable = false, length = 2048)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(nullable = false, length = 255)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @org.hibernate.annotations.Type(type = "types")
    @Column(nullable = false)
    public Type[] getTypes() {
        return types;
    }

    public void setTypes(Type[] types) {
        this.types = types;
    }

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public final static class Type {
        private String type;
        private String url;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    static class UserType {
        static class Types extends PojoUserType<Type[]> {
            Types() {
                super(Type[].class);
            }
        }
    }
}