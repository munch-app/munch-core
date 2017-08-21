package munch.data.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import munch.data.Article;
import munch.data.ImageMeta;
import munch.data.database.hibernate.ImageMetaUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 18/8/2017
 * Time: 1:20 AM
 * Project: munch-core
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeDefs(value = {
        @TypeDef(name = "thumbnail", typeClass = ImageMetaUserType.class)
})
@Table(indexes = {
        @Index(name = "index_munch_article_entity_cycle_no", columnList = "cycleNo"),
        @Index(name = "index_munch_article_entity_article_id_place_id_created_date", columnList = "articleId, placeId, createdDate"),
})
public final class ArticleEntity extends Article implements CycleEntity {

    private Long cycleNo;

    @Override
    @Column(nullable = false)
    public Long getCycleNo() {
        return cycleNo;
    }

    @Override
    public void setCycleNo(Long cycleNo) {
        this.cycleNo = cycleNo;
    }

    @Override
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    public String getPlaceId() {
        return super.getPlaceId();
    }

    @Id
    @Override
    @Column(nullable = false, length = 128) // SHA512
    public String getArticleId() {
        return super.getArticleId();
    }

    @Override
    @Column(nullable = false, length = 255)
    public String getBrand() {
        return super.getBrand();
    }

    @Override
    @Column(nullable = false, length = 2048)
    public String getUrl() {
        return super.getUrl();
    }

    @Override
    @Column(nullable = false, length = 255)
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    @Column(nullable = false, length = 2048)
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    @Type(type = "thumbnail")
    @Column(nullable = true)
    public ImageMeta getThumbnail() {
        return super.getThumbnail();
    }

    @Override
    @Column(nullable = false)
    public Date getCreatedDate() {
        return super.getCreatedDate();
    }

    @Override
    @Column(nullable = false)
    public Date getUpdatedDate() {
        return super.getUpdatedDate();
    }
}
