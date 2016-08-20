package com.munch.core.essential.util.random;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by Fuxing
 * Date: 27/6/2015
 * Time: 12:31 PM
 * Project: PuffinCore
 */
public class RandomFunctionTest {

    int ran = 0;

    @Test
    public void testRandom() throws Exception {
        RandomFunction.random(chance -> {
            ran += 1;
        }, chance1 -> {
            ran += 1;
        });

        assertThat(ran).isEqualTo(1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRandomReduce() {
        String value = RandomFunction.randomReduce(chance -> "", chance -> "");
        assertThat(value).isEqualTo("");
    }

    @Test
    public void testRandomWithProbability() throws Exception {
        RandomFunction.random(FunctionPair.of(1, chance -> ran += 1), FunctionPair.of(5, chance -> ran += 1));
        assertThat(ran).isEqualTo(1);
    }

    @Test
    public void testRandomAlwaysFavor() throws Exception {
        RandomFunction.random(FunctionPair.of(2_000_000_000, chance -> ran += 1), FunctionPair.of(1, chance -> ran += 5));
        assertThat(ran).isEqualTo(1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRandomReduceProbability() {
        String value = RandomFunction.randomReduce(ReduceFunctionPair.of(1, chance -> ""), ReduceFunctionPair.of(5, chance -> ""));
        assertThat(value).isEqualTo("");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRandomReduceAlwaysFavor() throws Exception {
        int v = RandomFunction.randomReduce(ReduceFunctionPair.of(2_000_000_000, chance -> 5), ReduceFunctionPair.of(5, chance -> 6));
        assertThat(v).isEqualTo(5);
    }
}
