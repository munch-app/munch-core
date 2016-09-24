package com.munch.core.struct.block.source.finals;

import com.munch.core.struct.block.BlockVersion;

/**
 * Created by: Fuxing
 * Date: 25/9/2016
 * Time: 2:17 AM
 * Project: struct
 */
public class FinalReport extends BlockVersion {

    /**
     * All data should be named with the version that is introduced
     * For data not named, it is there since VERSION_FIRST
     *
     * @param version version of block
     */
    public FinalReport() {
        super(VERSION_FIRST);
    }

    public class Tag {

    }
}
