//package protocols;
//
//import java.io.BufferedInputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
//import java.util.Scanner;
//
//public class Kuznechik {
//    void run() throws FileNotFoundException {
//        Scanner scan = new Scanner(System.in);
//        Scanner scanET = new Scanner(new BufferedInputStream(new FileInputStream("EncryptionText.txt")));
//        PrintWriter outDecrypt = new PrintWriter("DecryptionText.txt");
//        System.out.println("Enter number of operation: \n 1)Encrypt \n 2)Decrypt");
//        String POLYNOMIAL = "111000011";
//        String operation = scan.nextLine();
//        System.out.println("Eneter your key: ");
//        String key = scan.nextLine();
//        String K1 = openTextToHexString(key.substring(0, 16));
//        String K2 = openTextToHexString(key.substring(16));
//        if (operation.equals("1")) {
//            Scanner scanOT = new Scanner(new BufferedInputStream(new FileInputStream("OpenText.txt")));
//            PrintWriter outEncrypt = new PrintWriter("EncryptionText.txt");
//            while (scanOT.hasNext()) {
//                String openText = "";
//                openText = scanOT.nextLine();
//                for (int i = 0; i < openText.length(); i++) {
//                    if (!ALPH.contains("" + openText.charAt(i))) {
//                        openText = removeChar(openText, i);
//                        i--;
//                    }
//                }
//                while (openText.length() % 16 != 0)
//                    openText += 'a';
//                for (int i = 0; i < openText.length(); i += 16)
//                    outEncrypt.print(Encryption(openText.substring(i, i + 16)));
//                outEncrypt.println();
//            }
//            outEncrypt.close();
//            return;
//        } else {
//            scanET = new Scanner(new BufferedInputStream(new FileInputStream("EncryptionText.txt")));
//            outDecrypt = new PrintWriter("DecryptionText.txt");
//            while (scanET.hasNext()) {
//                String openText = "";
//                openText = scanET.nextLine();
//                for (int i = 0; i < openText.length(); i += 16)
//                    outDecrypt.print(Decryption(openText.substring(i, i + 16)));
//                outDecrypt.println();
//            }
//            outDecrypt.close();
//            return;
//        }
//    }
//    String openTextToHexString(String s){
//        String hexString = "";
//        for (int i = 0; i < s.length(); i++){
//            int temp = ALPH.indexOf(s.charAt(i));
//            String tmp = Integer.toBinaryString(temp);
//            while (tmp.length() < 8)
//                tmp = "0" + tmp;
//            hexString += Integer.toHexString(Integer.parseInt(tmp.substring(0,4),2));
//            hexString += Integer.toHexString(Integer.parseInt(tmp.substring(4),2));
//        }
//        return hexString;
//    }
//    String hexStringToOpenText(String s){
//        String openText = "";
//        String binaryString = "";
//        for (int i = 0; i < s.length(); i++){
//            String tmp = "";
//            tmp += Integer.toBinaryString(Integer.parseInt("" + s.charAt(i), 16));
//            while (tmp.length() < 4)
//                tmp = "0" + tmp;
//            binaryString += tmp;
//        }
//
//        for (int i = 0; i < binaryString.length(); i+= 8)
//            openText += ALPH.charAt(Integer.parseInt(binaryString.substring(i, i+8),2));
//        return openText;
//    }
//
//    String Encryption(String s) throws FileNotFoundException {
//        Init();
//        s = openTextToHexString(s);
//
//        s = LSXTransform(s, KeyList.get(0));
//
//        s = LSXTransform(s, KeyList.get(1));
//
//        s = LSXTransform(s, KeyList.get(2));
//
//        s = LSXTransform(s, KeyList.get(3));
//
//        s = LSXTransform(s, KeyList.get(4));
//
//        s = LSXTransform(s, KeyList.get(5));
//
//        s = LSXTransform(s, KeyList.get(6));
//
//        s = LSXTransform(s, KeyList.get(7));
//
//        s = LSXTransform(s, KeyList.get(8));
//
//        s = XTransform(s, KeyList.get(9));
//        s = hexStringToOpenText(s);
//        return s;
//    }
//    String Decryption(String s) throws FileNotFoundException {
//        Init();
//        s = openTextToHexString(s);
//        s = SLXTransform(s, KeyList.get(9));
//        s = SLXTransform(s, KeyList.get(8));
//        s = SLXTransform(s, KeyList.get(7));
//        s = SLXTransform(s, KeyList.get(6));
//        s = SLXTransform(s, KeyList.get(5));
//        s = SLXTransform(s, KeyList.get(4));
//        s = SLXTransform(s, KeyList.get(3));
//        s = SLXTransform(s, KeyList.get(2));
//        s = SLXTransform(s, KeyList.get(1));
//        s = XTransform(s, KeyList.get(0));
//        s = hexStringToOpenText(s);
//        return s;
//    }
//    void Init() throws FileNotFoundException {
//        scanBlock = new Scanner(new BufferedInputStream(new FileInputStream("Sblock.txt")));
//        while (scanBlock.hasNext())
//            Sblock.add(scanBlock.nextInt());
//        Rblock.add(148); Rblock.add(32); Rblock.add(133); Rblock.add(16); Rblock.add(194); Rblock.add(192); Rblock.add(1); Rblock.add(251); Rblock.add(1);
//        Rblock.add(192); Rblock.add(194); Rblock.add(16); Rblock.add(133); Rblock.add(32); Rblock.add(148); Rblock.add(1);
//
//        Constants.add("6ea276726c487ab85d27bd10dd849401");
//        Constants.add("dc87ece4d890f4b3ba4eb92079cbeb02");
//        Constants.add("b2259a96b4d88e0be7690430a44f7f03");
//        Constants.add("7bcd1b0b73e32ba5b79cb140f2551504");
//        Constants.add("156f6d791fab511deabb0c502fd18105");
//        Constants.add("a74af7efab73df160dd208608b9efe06");
//        Constants.add("c9e8819dc73ba5ae50f5b570561a6a07");
//        Constants.add("f6593616e6055689adfba18027aa2a08");
//        Constants.add("98fb40648a4d2c31f0dc1c90fa2ebe09");
//        Constants.add("2adedaf23e95a23a17b518a05e61c10a");
//        Constants.add("447cac8052ddd8824a92a5b083e5550b");
//        Constants.add("8d942d1d95e67d2c1a6710c0d5ff3f0c");
//        Constants.add("e3365b6ff9ae07944740add0087bab0d");
//        Constants.add("5113c1f94d76899fa029a9e0ac34d40e");
//        Constants.add("3fb1b78b213ef327fd0e14f071b0400f");
//        Constants.add("2fb26c2c0f0aacd1993581c34e975410");
//        Constants.add("41101a5e6342d669c4123cd39313c011");
//        Constants.add("f33580c8d79a5862237b38e3375cbf12");
//        Constants.add("9d97f6babbd222da7e5c85f3ead82b13");
//        Constants.add("547f77277ce987742ea93083bcc24114");
//        Constants.add("3add015510a1fdcc738e8d936146d515");
//        Constants.add("88f89bc3a47973c794e789a3c509aa16");
//        Constants.add("e65aedb1c831097fc9c034b3188d3e17");
//        Constants.add("d9eb5a3ae90ffa5834ce2043693d7e18");
//        Constants.add("b7492c48854780e069e99d53b4b9ea19");
//        Constants.add("056cb6de319f0eeb8e80996310f6951a");
//        Constants.add("6bcec0ac5dd77453d3a72473cd72011b");
//        Constants.add("a22641319aecd1fd835291039b686b1c");
//        Constants.add("cc843743f6a4ab45de752c1346ecff1d");
//        Constants.add("7ea1add5427c254e391c2823e2a3801e");
//        Constants.add("1003dba72e345ff6643b95333f27141f");
//        Constants.add("5ea7d8581e149b61f16ac1459ceda820");
//        KeyList.add(K1);
//        KeyList.add(K2);
//        KeyGenerator(0);
//        KeyList.add(K1);
//        KeyList.add(K2);
//        KeyGenerator(8);
//        KeyList.add(K1);
//        KeyList.add(K2);
//        KeyGenerator(16);
//        KeyList.add(K1);
//        KeyList.add(K2);
//        KeyGenerator(24);
//        KeyList.add(K1);
//        KeyList.add(K2);
//    }
//    String XTransform(String s1, String s2){
//        String out = "";
//        for (int i = 0; i < s2.length(); i++){
//            int k = Integer.parseInt(s1.substring(i, i + 1), 16);
//            int c = Integer.parseInt(s2.substring(i, i + 1), 16);
//            out += Integer.toHexString(k^c);
//        }
//        return out;
//    }
//    String STransform(String s){
//        String out = "";
//        for (int i = 0; i < s.length(); i+= 2) {
//            int n = Sblock.get(Integer.parseInt(s.substring(i, i + 2), 16));
//            String tmp = Integer.toHexString(n);
//            while (tmp.length() < 2)
//                tmp = "0" + tmp;
//            out += tmp;
//        }
//        return out;
//    }
//    String subSTransform(String s){
//        String out = "";
//        for (int i = 0; i < s.length(); i+= 2) {
//            int n = Sblock.indexOf(Integer.parseInt(s.substring(i, i + 2), 16));
//            String tmp = Integer.toHexString(n);
//            while (tmp.length() < 2)
//                tmp = "0" + tmp;
//            out += tmp;
//        }
//        return out;
//    }
//    String RTrasform (String s){
//        String out = "";
//        int k1 = Integer.parseInt(s.substring(0,2), 16);
//        String k2= Integer.toBinaryString(k1);
//        String k3 = multiplayPol(k2, Integer.toBinaryString(Rblock.get(0)));
//        String k4 = divPol(k3, POLYNOMIAL);
//        int k = Integer.parseInt(k4, 2);
//        for (int i = 2, j = 1; i < s.length(); i += 2, j++) {
//            String temp = s.substring(i, i + 2);
//            if (!s.substring(i, i + 2).equals("00"))
//                k ^= Integer.parseInt(divPol(multiplayPol(Integer.toBinaryString(Integer.parseInt(s.substring(i, i + 2), 16)), Integer.toBinaryString(Rblock.get(j))), POLYNOMIAL), 2);
//        }
//        out = Integer.toHexString(k) + s.substring(0, s.length() - 2);
//        while (out.length() < 32)
//            out = "0" + out;
//        return out;
//    }
//    String subRTransform(String s){
//        String out = "";
//        int k = Integer.parseInt(s.substring(0,2), 16);
//        for (int i = 2, j = 1; i < s.length(); i += 2, j++) {
//            String temp = s.substring(i, i + 2);
//            if (!s.substring(i, i + 2).equals("00"))
//                k ^= Integer.parseInt(divPol(multiplayPol(Integer.toBinaryString(Integer.parseInt(s.substring(i, i + 2), 16)), Integer.toBinaryString(Rblock.get(j - 1))), POLYNOMIAL), 2);
//        }
//        String tmp = Integer.toHexString(k);
//        while (tmp.length() < 2)
//            tmp = "0" + tmp;
//        out = s.substring(2) + tmp;
//        return out;
//    }
//    String LTransform (String s){
//        for (int i = 0; i < 16; i++){
//            s = RTrasform(s);
//        }
//        return s;
//    }
//    String subLTransform(String s){
//        for (int i = 0; i < 16; i++){
//            s = subRTransform(s);
//        }
//        return s;
//    }
//
//    String LSXTransform(String s1, String s2){
//        return LTransform(STransform(XTransform(s1, s2)));
//    }
//    String SLXTransform(String s1, String s2){
//        return subSTransform(subLTransform(XTransform(s1, s2)));
//    }
//    void KeyGenerator(int idx){
//        for (int i = idx; i < idx + 8; i++){
//            String tmp = K2;
//            K2 = K1;
//            K1 = XTransform(LSXTransform(Constants.get(i),K1),tmp);
//        }
//    }
//
//    String multiplayPol(String s1, String s2){
//        while (s1.length() < 8)
//            s1 = "0" + s1;
//        int m = s1.length() - 1;
//        int[] c = new int[s1.length()];
//        for (int i = m; i >= 0; i--) {
//            String local = "";
//            local += s1.charAt(m - i);
//            c[i] = Integer.parseInt(local);
//        }
//        while (s2.length() < 8)
//            s2 = "0" + s2;
//        int n = s2.length() - 1;
//        int[] d = new int[s2.length()];
//        for (int i = n; i >= 0; i--) {
//            String local = "";
//            local += s2.charAt(n - i);
//            d[i] = Integer.parseInt(local);
//        }
//        int[] r = new int[n + m + 1];
//        for (int i = m; i >= 0; i--){
//            for (int j = n; j >= 0; j--){
//                if (c[i] == 1 && d[j] == 1){
//                    if (r[i + j] == 0)
//                        r[i + j] = 1;
//                    else
//                        r[i + j] = 0;
//                }
//            }
//        }
//        String res = "";
//        for (int i = r.length - 1; i>= 0; i--)
//            res += r[i];
//        return res;
//    }
//    String divPol(String s1, String s2){
//        int m = s1.length() - 1;
//        int[] c = new int[s1.length()];
//        for (int i = m; i >= 0; i--) {
//            String local = "";
//            local += s1.charAt(m - i);
//            c[i] = Integer.parseInt(local);
//        }
//        int n = s2.length() - 1;
//        int[] d = new int[s2.length()];
//        for (int i = n; i >= 0; i--) {
//            String local = "";
//            local += s2.charAt(n - i);
//            d[i] = Integer.parseInt(local);
//        }
//        int[] q = new int[m + 1];
//        for (int k = m - n; k >= 0; k--){
//            q[k] = c[n + k] / d[n];
//            for (int j = n + k - 1; j >= k; j--) {
//                if (q[k] * d[j - k] == 1)
//                    c[j] = Math.abs(c[j] - 1);
//            }
//        }
//        String Q = "";
//        for (int i = m - n; i >= 0; i--)
//            Q += q[i];
//        String R = "";
//        for (int i = n - 1; i >= 0; i--)
//            R += c[i];
//        return R;
//    }
//    String removeChar(String s, int pos) {
//        return s.substring(0, pos) + s.substring(pos + 1, s.length());
//    }
//
//
//}
