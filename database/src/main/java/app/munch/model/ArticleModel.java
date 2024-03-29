package app.munch.model;

import com.fasterxml.jackson.annotation.*;
import dev.fuxing.postgres.PojoListUserType;
import dev.fuxing.postgres.PojoSetUserType;
import dev.fuxing.postgres.PojoUserType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Reasons why there is ArticleModel because Article contains multiple revision and a live version of the Article.
 * The live version of the Article needs to be a single entity so that it can be placed in entity that reference it.
 * It's rather difficult to design a model that has many version and yet linked to another entity.
 * <p>
 * Created by: Fuxing
 * Date: 2019-08-06
 * Time: 17:01
 */
@MappedSuperclass
@TypeDef(name = "ArticleModel.TagsType", typeClass = ArticleModel.TagsType.class)
@TypeDef(name = "ArticleModel.ContentType", typeClass = ArticleModel.ContentType.class)
@TypeDef(name = "ArticleModel.OptionsType", typeClass = ArticleModel.OptionsType.class)
public abstract class ArticleModel {

    @NotNull
    @Pattern(regexp = "[0-9a-z-]{0,200}")
    @Column(length = 200, updatable = true, nullable = false, unique = false)
    private String slug;

    @NotBlank(groups = Groups.ArticlePublished.class)
    @Length(max = 100)
    @Column(length = 100, updatable = true, nullable = true, unique = false)
    private String title;

    @NotBlank(groups = Groups.ArticlePublished.class)
    @Length(max = 250)
    @Column(length = 250, updatable = true, nullable = true, unique = false)
    private String description;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Image image;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Profile profile;

    @Valid
    @NotNull
    @Size(max = 8)
    @Type(type = "ArticleModel.TagsType")
    private Set<@NotNull Tag> tags;

    @Valid
    @NotNull
    @Size(max = 1000)
    @Type(type = "ArticleModel.ContentType")
    private List<@NotNull Node> content;

    @Valid
    @NotNull
    @Type(type = "ArticleModel.OptionsType")
    private Options options;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Node> getContent() {
        return content;
    }

    public void setContent(List<Node> content) {
        this.content = content;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public static class TagsType extends PojoSetUserType<Tag> {
        public TagsType() {
            super(Tag.class);
        }
    }

    public static class ContentType extends PojoListUserType<Node> {
        public ContentType() {
            super(Node.class);
        }
    }

    public static class OptionsType extends PojoUserType<Options> {
        public OptionsType() {
            super(Options.class);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Options {
        private Boolean map;
        private Boolean ads;
        private Boolean affiliate;

        private Boolean placePublishing;

        /**
         * @return whether to show an embedded map if available.
         */
        public Boolean getMap() {
            return map;
        }

        public void setMap(Boolean map) {
            this.map = map;
        }

        /**
         * @return whether to show ads if available.
         */
        public Boolean getAds() {
            return ads;
        }

        public void setAds(Boolean ads) {
            this.ads = ads;
        }

        /**
         * @return whether to show munch affiliates if available.
         */
        public Boolean getAffiliate() {
            return affiliate;
        }

        public void setAffiliate(Boolean affiliate) {
            this.affiliate = affiliate;
        }

        /**
         * When publishing is enabled, it will sync both direction
         *
         * @return whether to publish place information into a new revision.
         */
        public Boolean getPlacePublishing() {
            return placePublishing;
        }

        public void setPlacePublishing(Boolean placePublishing) {
            this.placePublishing = placePublishing;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(HeadingNode.class),
            @JsonSubTypes.Type(ParagraphNode.class),
            @JsonSubTypes.Type(PlaceNode.class),
            @JsonSubTypes.Type(AvatarNode.class),
            @JsonSubTypes.Type(LineNode.class),
            @JsonSubTypes.Type(ImageNode.class),
    })
    public interface Node {
        @JsonIgnore
        String getType();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeName("heading")
    public static final class HeadingNode implements Node {
        private Attrs attrs;

        private List<TextContent> content;

        @Override
        public String getType() {
            return "heading";
        }

        @Valid
        @NotNull
        public Attrs getAttrs() {
            return attrs;
        }

        public void setAttrs(Attrs attrs) {
            this.attrs = attrs;
        }

        @Valid
        public List<TextContent> getContent() {
            return content;
        }

        public void setContent(List<TextContent> content) {
            this.content = content;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Attrs {

            @NotNull
            @Min(1)
            @Max(2)
            private Integer level;

            public Integer getLevel() {
                return level;
            }

            public void setLevel(Integer level) {
                this.level = level;
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeName("paragraph")
    public static final class ParagraphNode implements Node {

        @Valid
        private List<TextContent> content;

        @Override
        public String getType() {
            return "paragraph";
        }

        public List<TextContent> getContent() {
            return content;
        }

        public void setContent(List<TextContent> content) {
            this.content = content;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeName("line")
    public static final class LineNode implements Node {
        @Override
        public String getType() {
            return "line";
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeName("image")
    public static final class ImageNode implements Node {

        @NotNull
        @Valid
        private Attrs attrs;

        @Override
        public String getType() {
            return "image";
        }

        public Attrs getAttrs() {
            return attrs;
        }

        public void setAttrs(Attrs attrs) {
            this.attrs = attrs;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class Attrs {

            @NotNull
            @Valid
            private Image image;

            @Length(max = 500)
            private String caption;

            public Image getImage() {
                return image;
            }

            public void setImage(Image image) {
                this.image = image;
            }

            public String getCaption() {
                return caption;
            }

            public void setCaption(String caption) {
                this.caption = caption;
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeName("place")
    public static final class PlaceNode implements Node {

        @NotNull
        @Valid
        private Attrs attrs;

        @Override
        public String getType() {
            return "place";
        }

        public Attrs getAttrs() {
            return attrs;
        }

        public void setAttrs(Attrs attrs) {
            this.attrs = attrs;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class Attrs {

            @Valid
            @NotNull
            private Place place;

            public Place getPlace() {
                return place;
            }

            public void setPlace(Place place) {
                this.place = place;
            }

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static final class Place extends PlaceModel {

                /**
                 * Id & Slug can be Null when Place association is not yet created.
                 */
                @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{12}0$")
                private String id;

                /**
                 * @see Place#id
                 */
                @Pattern(regexp = "[0-9a-z-]{0,200}")
                private String slug;

                @Valid
                private Image image;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getSlug() {
                    return slug;
                }

                public void setSlug(String slug) {
                    this.slug = slug;
                }

                public Image getImage() {
                    return image;
                }

                public void setImage(Image image) {
                    this.image = image;
                }
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeName("avatar")
    public static final class AvatarNode implements Node {

        @NotNull
        @Valid
        private Attrs attrs;

        @Override
        public String getType() {
            return "avatar";
        }

        public Attrs getAttrs() {
            return attrs;
        }

        public void setAttrs(Attrs attrs) {
            this.attrs = attrs;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class Attrs {

            @Valid
            @NotNull
            private List<Image> images;

            @NotBlank(groups = {Groups.ArticlePublished.class})
            @Length(max = 100)
            private String line1;

            @NotBlank(groups = {Groups.ArticlePublished.class})
            @Length(max = 100)
            private String line2;

            public List<Image> getImages() {
                return images;
            }

            public void setImages(List<Image> images) {
                this.images = images;
            }

            public String getLine1() {
                return line1;
            }

            public void setLine1(String line1) {
                this.line1 = line1;
            }

            public String getLine2() {
                return line2;
            }

            public void setLine2(String line2) {
                this.line2 = line2;
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class TextContent {

        @NotNull
        @Pattern(regexp = "text|hard_break")
        private String type;

        private String text;

        @Valid
        private List<TextMark> marks;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<TextMark> getMarks() {
            return marks;
        }

        public void setMarks(List<TextMark> marks) {
            this.marks = marks;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class TextMark {

        /**
         * In the future can break it now into their own implemented classes.
         */
        @NotNull
        @Pattern(regexp = "bold|italic|underline|link")
        private String type;

        @Valid
        private Attrs attrs;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Attrs getAttrs() {
            return attrs;
        }

        public void setAttrs(Attrs attrs) {
            this.attrs = attrs;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class Attrs {

            private String href;

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }
        }
    }

    public static final class NodeUtils {
        public static Optional<String> findFirstHeading(List<Node> content) {
            return content.stream()
                    .filter(node -> node instanceof HeadingNode)
                    .map(node -> (HeadingNode) node)
                    .filter(node -> node.getContent() != null)
                    .map(node -> node.getContent().stream()
                            .map(TextContent::getText)
                            .collect(Collectors.joining()))
                    .filter(StringUtils::isNotBlank)
                    .findFirst();
        }

        public static Optional<String> findFirstParagraph(List<Node> content) {
            return content.stream()
                    .filter(node -> node instanceof ParagraphNode)
                    .map(node -> (ParagraphNode) node)
                    .filter(node -> node.getContent() != null)
                    .map(node -> node.getContent().stream()
                            .map(TextContent::getText)
                            .collect(Collectors.joining()))
                    .filter(StringUtils::isNotBlank)
                    .findFirst();
        }
    }
}
