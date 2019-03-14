package munch.api;

import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 2019-03-11
 * Time: 01:00
 * Project: munch-core
 */
public final class MunchTeam {
    private static final Set<String> ALL = Set.of(
            "GoNd1yY0uVcA8pHBUrZzd84C4Dg1", // Earnest Lim
            "oNOfWjsL49giM0f3ANzAkQY9AoG3", // Ying Zhong Ng
            "CM8wAOSdenMD8dQUUjniK5f5rzL2", // Joel-David Wong
            "0aMrslcgMyW3xbWnfdyC4C6Tx6r1", // Samuel Tan
            "sGtVZuFJwYhf5OQ6KU5DK6Ts0Z13" // Fuxing Loh
    );

    /**
     * @param userId to check if is part if the munch team
     * @return if it is part of munch team
     */
    public static boolean is(String userId) {
        return ALL.contains(userId);
    }
}
