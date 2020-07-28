package org.aidtracker.backend;

import org.junit.jupiter.api.BeforeAll;

/**
 * @author mtage
 * @since 2020/7/28 11:19
 */
public class BaseTest {
    @BeforeAll
    public static void ansiColor() {
        System.setProperty("spring.output.ansi.enabled", "ALWAYS");
    }
}
