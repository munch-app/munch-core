package munch.api.search.cards;

import munch.file.Image;

/**
 * Created by: Fuxing
 * Date: 2019-03-11
 * Time: 03:51
 * Project: munch-core
 */
public final class SearchClaimVoucherCard implements SearchCard {
    private final String voucherId;
    private final Image image;
    private final String title;
    private final String button;

    public SearchClaimVoucherCard(String voucherId, Image image, String title, String button) {
        this.voucherId = voucherId;
        this.image = image;
        this.title = title;
        this.button = button;
    }

    @Override
    public String getCardId() {
        return "ClaimVoucher_2019-03-11";
    }

    @Override
    public String getUniqueId() {
        return voucherId;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public Image getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getButton() {
        return button;
    }
}
