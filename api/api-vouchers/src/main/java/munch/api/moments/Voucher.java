package munch.api.moments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import munch.file.Image;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 2019-03-11
 * Time: 21:23
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Voucher {
    private String voucherId;

    private Image image;
    private String description;
    private List<String> terms;

    private Long remaining;
    private Boolean claimed;

    private String recordId;

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTerms() {
        return terms;
    }

    public void setTerms(List<String> terms) {
        this.terms = terms;
    }

    public Long getRemaining() {
        return remaining;
    }

    public void setRemaining(Long remaining) {
        this.remaining = remaining;
    }

    public Boolean getClaimed() {
        return claimed;
    }

    public void setClaimed(Boolean claimed) {
        this.claimed = claimed;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    static final Image IMAGE = new Image();
    static final String DESCRIPTION = "Simply flash this page to the Munch staff at the Munch booth outside of SMU's Gong Cha to collect your 1-for-1 Brown Sugar Fresh Milk voucher.\n\nThe 1-for-1 voucher is limited to the first 50 customers of the day starting from 10am.";
    static final List<String> TERMS = List.of(
            "Each voucher is valid for two medium-sized Brown Sugar Fresh Milk with pearls only.",
            "This voucher is only valid for redemption at Gong Cha’s Singapore Management University outlet only.",
            "This voucher is to be presented to Munch staff for redemption.",
            "This voucher is valid till 30th March 2019 (inclusive).",
            "This voucher is non-refundable and valid for single transaction only."
    );

    static {
        IMAGE.setImageId("Campaign_GongChaCard");
        IMAGE.setSizes(List.of(
                newSize("https://s3-ap-southeast-1.amazonaws.com/munch-static/Campaign/GongChaBanner%404x.png", 1500, 1024),
                newSize("https://s3-ap-southeast-1.amazonaws.com/munch-static/Campaign/GongChaBanner%403x.png", 1125, 768),
                newSize("https://s3-ap-southeast-1.amazonaws.com/munch-static/Campaign/GongChaBanner%402x.png", 750, 512)
        ));
    }

    private static Image.Size newSize(String url, int width, int height) {
        Image.Size size = new Image.Size();
        size.setUrl(url);
        size.setHeight(height);
        size.setWidth(width);
        return size;
    }

    public static Voucher createGongCha(long remaining, boolean claimed, String recordId) {
        Voucher voucher = new Voucher();
        voucher.setVoucherId("00000000-0000-0000-0000-000000000001");
        voucher.setDescription(DESCRIPTION);
        voucher.setTerms(TERMS);
        voucher.setImage(IMAGE);
        voucher.setRemaining(remaining);
        voucher.setClaimed(claimed);
        voucher.setRecordId(recordId);
        return voucher;
    }
}
