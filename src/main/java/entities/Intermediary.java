package entities;

import entities.banks.Bank;
import entities.blocks.DoubleBlock;

import java.util.HashMap;
import java.util.Map;

public class Intermediary {
    private String privateKey = "intermediaryPrivateKey";
    private Map<String, String> banksKey = new HashMap<>(); //id банка -> общий ключ с банком
    private Map<String, DoubleBlock> doubleBlocks = new HashMap<>();
    private Map<String, String> locationKey = new HashMap<>();

    private static Intermediary intermediary;

    private Intermediary(){

    }

    public static Intermediary getIntermediary(){
        if (intermediary == null){
            intermediary = new Intermediary();
        }
        return intermediary;
    }

    public void addBankDoubleBlock(Bank bank, DoubleBlock doubleBlock){
        doubleBlocks.put(bank.getId(), doubleBlock);
    }

    public void addBanksSharedKey(Bank bank, String sharedKey){
        banksKey.put(bank.getId(), sharedKey);
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getBankSharedKey(String banckId){
        return banksKey.get(banckId);
    }

    public String getLocationSharedKey(String locationId){
        return locationKey.get(locationId);
    }

    public void addLocationSharedKey(Location location){
        locationKey.put(location.getLocationId(), location.getSharedKeyWithIntermediary());
    }

    public DoubleBlock getDoubleBlock(String banckId){
        return doubleBlocks.get(banckId);
    }

    public String getSharedKeyByDoubleBlock(DoubleBlock doubleBlock){
        for (String key : doubleBlocks.keySet()){
            if (doubleBlock.equals(doubleBlocks.get(key))){
                return key;
            }
        }
        return null;
    }
}
