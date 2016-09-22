package com.munch.core.struct.block.source;

import com.munch.core.struct.block.BlockVersion;

/**
 * Created By: Fuxing Loh
 * Date: 22/9/2016
 * Time: 8:00 PM
 * Project: struct
 */
public class SourceTag extends BlockVersion {

    private String name;
    private int count;

    public SourceTag() {
        super(VERSION_FIRST);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return obj.hashCode() == this.hashCode();
    }
}
