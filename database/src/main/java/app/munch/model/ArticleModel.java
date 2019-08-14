package app.munch.model;

import app.munch.model.constraint.ArticlePublishedGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.fuxing.postgres.PojoListUserType;
import dev.fuxing.postgres.PojoSetUserType;
import dev.fuxing.validator.ValidEnum;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Set;

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
@TypeDef(name = "Tags", typeClass = ArticleModel.TagsType.class)
@TypeDef(name = "Content", typeClass = ArticleModel.ContentType.class)
public abstract class ArticleModel {

    @NotBlank(groups = ArticlePublishedGroup.class)
    @Column(length = 100, updatable = true, nullable = true, unique = false)
    private String title;

    @NotBlank(groups = ArticlePublishedGroup.class)
    @Column(length = 250, updatable = true, nullable = true, unique = false)
    private String description;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Image image;

    @Valid
    @NotNull(groups = ArticlePublishedGroup.class)
    @Type(type = "Tags")
    private Set<@NotNull Tag> tags;

    @Valid
    @NotNull
    @Type(type = "Content")
    private List<@NotNull Node> content;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = HeadingNode.class, name = "heading"),
            @JsonSubTypes.Type(value = ParagraphNode.class, name = "paragraph"),
            @JsonSubTypes.Type(value = PlaceNode.class, name = "place"),
            @JsonSubTypes.Type(value = AvatarNode.class, name = "avatar"),
            @JsonSubTypes.Type(value = LineNode.class, name = "line"),
            @JsonSubTypes.Type(value = ImageNode.class, name = "image"),
    })

    public interface Node {
        @ValidEnum
        String getType();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
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
        @NotNull
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
    public static final class LineNode implements Node {
        @Override
        public String getType() {
            return "line";
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
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

            @NotNull
            private Place place;

            @Valid
            private Options options;

            public Place getPlace() {
                return place;
            }

            public void setPlace(Place place) {
                this.place = place;
            }

            public Options getOptions() {
                return options;
            }

            public void setOptions(Options options) {
                this.options = options;
            }

            /**
             * Options used to contains image and name.
             * However now that Place is an detached entity.
             * It will just use the detached entity data.
             */
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static final class Options {

                private Boolean autoPublish;

                private Boolean autoUpdate;

                /**
                 * @return auto publish, whether PlaceNode data get automatically published to PlaceRevision
                 */
                public Boolean getAutoPublish() {
                    return autoPublish;
                }

                public void setAutoPublish(Boolean autoPublish) {
                    this.autoPublish = autoPublish;
                }

                /**
                 * @return auto update, whether PlaceNode data get automatically updated from new PlaceRevision changes
                 */
                public Boolean getAutoUpdate() {
                    return autoUpdate;
                }

                public void setAutoUpdate(Boolean autoUpdate) {
                    this.autoUpdate = autoUpdate;
                }
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
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
            private Image image;

            private String line1;

            private String line2;

            public Image getImage() {
                return image;
            }

            public void setImage(Image image) {
                this.image = image;
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
}
