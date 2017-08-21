package munch.data.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import munch.data.ImageMeta;
import munch.data.InstagramMedia;
import munch.data.database.hibernate.ImageMetaUserType;
import munch.data.database.hibernate.PojoUserType;
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
        @TypeDef(name = "image", typeClass = ImageMetaUserType.class),
        @TypeDef(name = "profile", typeClass = InstagramMediaEntity.ProfileUserType.class)
})
@Table(indexes = {
        @Index(name = "index_munch_instagram_media_entity_cycle_no", columnList = "cycleNo"),
        @Index(name = "index_munch_instagram_media_entity_media_id_place_id_created_date", columnList = "mediaId, placeId, createdDate")
})
public final class InstagramMediaEntity extends InstagramMedia implements CycleEntity {

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
    @Column(nullable = false, length = 255)
    public String getMediaId() {
        return super.getMediaId();
    }

    @Override
    @Type(type = "profile")
    @Column(nullable = true)
    public Profile getProfile() {
        return super.getProfile();
    }

    @Override
    @Type(type = "image")
    @Column(nullable = true)
    public ImageMeta getImage() {
        return super.getImage();
    }

    @Override
    @Column(nullable = true, length = 2500)
    public String getCaption() {
        return super.getCaption();
    }

    @Override
    @Column(nullable = true)
    public Date getCreatedDate() {
        return super.getCreatedDate();
    }

    @Override
    @Column(nullable = true)
    public Date getUpdatedDate() {
        return super.getUpdatedDate();
    }

    public final static class ProfileUserType extends PojoUserType<Profile> {
        public ProfileUserType() {
            super(Profile.class);
        }
    }
}
