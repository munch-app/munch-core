package com.munch.core.essential.block;

import com.google.gson.JsonObject;

/**
 * Created by: Fuxing
 * Date: 17/10/2016
 * Time: 8:31 AM
 * Project: essential
 */
public abstract class BlockMigration<B extends BlockVersion> {

    private final int latestVersion;

    /**
     * @param latestVersion latest version of the block
     */
    public BlockMigration(int latestVersion) {
        this.latestVersion = latestVersion;
    }

    /**
     * Helper initializer that uses that latest block to init
     *
     * @param block new Block()
     */
    public BlockMigration(B block) {
        this(block.getVersion());
    }

    /**
     * @param block check if block needs to update
     */
    protected boolean isUpdate(JsonObject block) {
        return getVersion(block) < latestVersion;
    }

    /**
     * @return block version
     */
    protected int getVersion(JsonObject block) {
        return block.get("version").getAsInt();
    }

    protected void incrementalUpdate(JsonObject block) {
        if (isUpdate(block)) {
            update(block);
            // Incrementally update util the latest version
            incrementalUpdate(block);
        }
    }

    /**
     * Remember to constantly update the version at each update
     * e.g.
     *  switch (getVersion(block)){
     *      case BlockVersion.VERSION_FIRST:
     *          update logic
     *          block.addProperty("version", 2);
     *      case 2:
     *          block.addProperty("version", 3);
     *      case 3:
     *          block.addProperty("version", 4);
     *      case 4:
     *          block.addProperty("version", 5);
     *      case 5:
     *          block.addProperty("version", 6);
     *      case 6:
     *          block.addProperty("version", 7);
     * }
     *
     * @param block block to update
     */
    protected abstract void update(JsonObject block);

}
