package munch.api.search.filter;

/**
 * Truncated version of munch.data.tag.Tag
 * <p>
 * Created by: Fuxing
 * Date: 28/11/18
 * Time: 7:48 AM
 * Project: munch-core
 */
public final class FilterTag {
    private String tagId;

    private munch.data.tag.Tag.Type type;
    private String name;
    private long count;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public munch.data.tag.Tag.Type getType() {
        return type;
    }

    public void setType(munch.data.tag.Tag.Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public static FilterTag from(munch.data.tag.Tag tag, long count) {
        FilterTag tag1 = new FilterTag();
        tag1.setTagId(tag.getTagId());
        tag1.setType(tag.getType());
        tag1.setName(tag.getName());
        tag1.setCount(count);
        return tag1;
    }
}
