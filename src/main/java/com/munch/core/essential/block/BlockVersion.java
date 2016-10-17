package com.munch.core.essential.block;

/**
 * Created by: Fuxing
 * Date: 15/9/2016
 * Time: 2:20 AM
 * Project: struct
 */
public abstract class BlockVersion {

    public static final int VERSION_FIRST = 1;

    private int version;

    /**
     * All data should be named with the version that is introduced
     * For data not named, it is there since VERSION_FIRST
     *
     * @param version version of block
     */
    public BlockVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
