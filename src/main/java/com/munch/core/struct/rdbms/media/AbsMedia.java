package com.munch.core.struct.rdbms.media;

import com.munch.core.struct.rdbms.abs.AbsAuditData;

/**
 * Created By: Fuxing Loh
 * Date: 25/9/2016
 * Time: 11:12 PM
 * Project: struct
 */
public class AbsMedia extends AbsAuditData {

    public static final int STATUS_PRESIGNED = 100;
    public static final int STATUS_UPLOADED = 200;

    public static final int TYPE_IMAGE = 2000;
    public static final int TYPE_VIDEO = 3000;

}
