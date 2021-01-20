package game.cards;

import java.math.BigInteger;
import java.util.UUID;

public class ShortUUID {
    public static String generate() {
        String uuid_string = UUID.randomUUID().toString().replaceAll("-","");
        BigInteger big = new BigInteger(uuid_string, 16);
        return big.toString(36);
    }
}
