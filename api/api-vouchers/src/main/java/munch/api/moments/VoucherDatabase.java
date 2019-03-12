package munch.api.moments;

import catalyst.airtable.AirtableApi;
import catalyst.airtable.AirtableRecord;
import munch.restful.core.JsonUtils;
import munch.restful.core.exception.ForbiddenException;
import munch.user.client.UserProfileClient;
import munch.user.data.UserProfile;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 2019-03-11
 * Time: 00:42
 * Project: munch-core
 */
@Singleton
public final class VoucherDatabase {
    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final AirtableApi.Table quotaTable;
    private final AirtableApi.Table claimTable;
    private final UserProfileClient profileClient;

    @Inject
    public VoucherDatabase(AirtableApi airtableApi, UserProfileClient profileClient) {
        this.profileClient = profileClient;
        AirtableApi.Base base = airtableApi.base("appcWQ1BV40yYneHV");
        this.quotaTable = base.table("Voucher Quota");
        this.claimTable = base.table("Claimed Voucher");
    }


    public Voucher get(String voucherId, String userId, LocalDateTime dateTime) {
        AirtableRecord quota = getQuota(getQuotaId(dateTime));
        boolean userClaimed = getClaim(userId) != null;

        if (quota == null) return Voucher.createGongCha(0, userClaimed, null);

        int quantity = Objects.requireNonNull(quota.getFieldInteger("Quantity"));
        int claimed = Objects.requireNonNull(quota.getFieldInteger("Claimed"));
        long remain = quantity - claimed;
        if (remain < 0) remain = 0;

        return Voucher.createGongCha(remain, userClaimed, quota.getId());
    }

    public Voucher claim(String voucherId, String userId, String passcode, LocalDateTime dateTime) throws ForbiddenException {
        if (!passcode.equals("8763")) throw new ForbiddenException("Passcode Invalid");

        Voucher voucher = get(voucherId, userId, dateTime);
        if (voucher.getRemaining() <= 0) throw new ForbiddenException("Fully Claimed");
        if (voucher.getClaimed()) throw new ForbiddenException("Already Claimed");

        claimTable.post(create(voucher.getRecordId(), userId));

        voucher.setRemaining(voucher.getRemaining() - 1);
        voucher.setClaimed(true);
        return voucher;
    }

    @Nullable
    private AirtableRecord getQuota(String quotaId) {
        List<AirtableRecord> records = quotaTable.find("QuotaId", quotaId);
        if (records.isEmpty()) return null;
        return records.get(0);
    }

    @Nullable
    private AirtableRecord getClaim(String userId) {
        List<AirtableRecord> records = claimTable.find("Profile.userId", userId);
        if (records.isEmpty()) return null;
        return records.get(0);
    }

    private AirtableRecord create(String recordId, String userId) {
        UserProfile profile = profileClient.get(userId);
        if (profile == null) throw new ForbiddenException("User Don't Exist");

        AirtableRecord record = new AirtableRecord();
        record.putField("Profile.name", profile.getName());
        record.putField("Profile.userId", profile.getUserId());
        record.putField("Profile.email", profile.getEmail());
        record.putField("CreatedAt", new Date());

        record.putField("Voucher Quota", JsonUtils.createArrayNode().add(recordId));
        return record;
    }

    private static String getQuotaId(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.of(2019, Month.MARCH, 18, 10, 0))) {
            return "Before";
        }

        return "GC_" + dateTime.format(PATTERN);
    }
}
