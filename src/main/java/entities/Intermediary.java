package entities;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Intermediary {
    private String privateKey = UUID.randomUUID().toString().replaceAll("-", "");
    private Map<String, String> banksSharedKey = new HashMap<>(); //name банка -> общий ключ с банком
    private Map<Integer, String> locationSharedKey = new HashMap<>();

    private static Intermediary intermediary;

    private Intermediary(){

    }

    public static Intermediary getIntermediary(){
        if (intermediary == null){
            intermediary = new Intermediary();
        }
        return intermediary;
    }

    public void addBanksSharedKey(String bankName, String sharedKey){
        banksSharedKey.put(bankName, sharedKey);
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getBankSharedKey(String banckName){
        return banksSharedKey.get(banckName);
    }

    public String getLocationSharedKey(String locationId){
        return locationSharedKey.get(locationId);
    }

    public void addLocationSharedKey(int locationId, String sharedKey){
        locationSharedKey.put(locationId, sharedKey);
    }
}
