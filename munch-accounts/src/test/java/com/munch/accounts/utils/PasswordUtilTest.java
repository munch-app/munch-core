package com.munch.accounts.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by: Fuxing
 * Date: 19/11/2016
 * Time: 2:21 AM
 * Project: munch-core
 */
public class PasswordUtilTest {
    private Pair[] presetPassword = new Pair[]{Pair.of("neiH5nANZcXM5n2OabbF5kiSJkBLHa", "$2a$10$4pQsta6VrO.AMtxmK8ddu.XW9GVBBkNE1RqTb5RWZ9m5.GaSM3Ile"), Pair.of("7xfeJoITyYvaaO5hF5yx5a6Del8Phk", "$2a$10$9q3QzcAxC9IKXEgnmbbvVupGXksgYnRmJgcugHmB/3p9xPd181DhG"), Pair.of("WY770NlKDrrJ3VD46M4WfiOtZVaPGe", "$2a$10$A0MaA3qaOh2PLK.7AJUn2ukH8rz7ud8.OCZw5zxMFZZTdbVZfkSlW"), Pair.of("dZNL65j0H0dte6taRMvUFgaj58pipP", "$2a$10$iM1eBU.th9LiM/WH61pE..xDeeiX1rTpIrm3A0GWPa4T9brSzj0yO"), Pair.of("zAjO96tUpC5iyPuPk9Dd18MQOqzeSC", "$2a$10$aKpOYIyXMLbR4Jrlt6t1Xet2lNOZD6m1wAev84fHY5YB.kVxs/G86"), Pair.of("igryCQ4FcpRcVMeSGgzT4YkeW5Faok", "$2a$10$eC/ShUH8ucKySDEqQt1vMOQRL1iuFe3SArH4X.oZ442sXIF8I3Tq."), Pair.of("iCzdDWgv1WA771AS2ix6rYYci77LR7", "$2a$10$xD6HohSisOb1QLMQ/GUzFeXMSoMpTgOfp4BxGdO9RN.K0SHnyvLci"), Pair.of("tY0cl7VFOTDwoLfYn0h9MLiDFch9LG", "$2a$10$D2wFGPhQJ4GYZMvePcFweOvVjnNmcQfBHL0r7ULwrkyL7R01/.Hnq"), Pair.of("NRHf3fvTJEkD0KBzzA0yZ3FU0dodMv", "$2a$10$MWLD3BBWhEH9TVECv35YxeVxL80XUzjKAUzcJAgo8UjNqZqkekbhy"), Pair.of("iu4lCY63qercGvvCYvZa7Lfd5BJxQ9", "$2a$10$MV5Up9FYJrUzjoDhAdcyouvbUsJoot32azgpvy9.8K2UHLLzNP6zC")};
    private PasswordUtil passwordUtil;

    @BeforeEach
    public void setUp() throws Exception {
        passwordUtil = new PasswordUtil();
    }

    @Test
    public void testProperties() {
        String plainPassword = RandomStringUtils.randomAlphanumeric(30);
        String hashed = passwordUtil.hashPassword(plainPassword);

        System.out.println("Plain Password:" + plainPassword);
        System.out.println("Hashed:" + hashed);
        System.out.println("Hashed Size:" + hashed.length());
    }

    @Test
    public void testSingle() {
        String plainPassword = RandomStringUtils.randomAlphanumeric(30);
        System.out.println(plainPassword);
        String hashedPassword = passwordUtil.hashPassword(plainPassword);
        System.out.println(hashedPassword);

        boolean matches = passwordUtil.check(plainPassword, hashedPassword);
        System.out.println(matches);
        assertThat(matches).isTrue();
    }

    @Test
    public void testAllChar() throws Exception {
        for (int i = 0; i < 5; i++) {
            String plainPassword = RandomStringUtils.random(30);
            String hashed = passwordUtil.hashPassword(plainPassword);
            assertThat(passwordUtil.check(plainPassword, hashed)).isTrue();
            System.out.println(hashed);
        }
    }

    @Test
    public void testPresetDuration() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (Pair pair : presetPassword) {
            String plainPassword = (String) pair.getLeft();
            String hashed = (String) pair.getRight();
            assertThat(passwordUtil.check(plainPassword, hashed)).isTrue();
        }

        stopWatch.stop();
        System.out.println("Time per check (Millis): " + stopWatch.getTime() / presetPassword.length);
    }

    @Test
    public void testHashDuration() {
        StopWatch stopWatch = new StopWatch();

        final int cycle = 10;
        String[] plains = new String[cycle];
        for (int i = 0; i < cycle; i++) {
            plains[i] = RandomStringUtils.randomAlphabetic(33);
        }

        stopWatch.start();
        for (String plain : plains) {
            passwordUtil.hashPassword(plain);
        }
        stopWatch.stop();

        System.out.println("Time per hash (Millis): " + stopWatch.getTime() / 10);
    }
}