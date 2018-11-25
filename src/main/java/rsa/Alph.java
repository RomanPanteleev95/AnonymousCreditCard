package rsa;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dasha on 19.04.2017.
 */
public class Alph {
    Map<Character, Integer> alph = new HashMap<>();
    Map<Integer, Character> revAlph = new HashMap<>();

    public void getAlph() throws IOException {

        int code = 100;
        for (char i = 'А'; i <= 'Я'; i++) {
            alph.put(i, code);
            revAlph.put(code, i);
            code++;
        }
        for (char i = 'а'; i <= 'я'; i++) {
            alph.put(i, code);
            revAlph.put(code, i);
            code++;
        }
        for (char i = 'A'; i <= 'Z'; i++) {
            alph.put(i, code);
            revAlph.put(code, i);
            code++;
        }
        for (char i = 'a'; i <= 'z'; i++) {
            alph.put(i, code);
            revAlph.put(code, i);
            code++;
        }
        for (char i = '0'; i <= '9'; i++) {
            alph.put(i, code);
            revAlph.put(code, i);
            code++;
        }

        String zn = ",;.!?@#$%^&*ё:-—_=+-()\'\"\n\t ";

        for (int i = 0; i < zn.length(); i++) {
            alph.put(zn.charAt(i), code);
            revAlph.put(code, zn.charAt(i));
            code++;
        }

    }

}
