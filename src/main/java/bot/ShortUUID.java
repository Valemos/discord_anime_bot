package bot;

import org.apache.commons.lang3.RandomStringUtils;

public class ShortUUID {
    public static String generate() {
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
