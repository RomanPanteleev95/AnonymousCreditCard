package entities;

import constants.Constant;
import utils.ConnectionUtils;
import utils.DataBaseUtils;
import utils.Utils;

import javax.rmi.CORBA.Util;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Intermediary {
    private String privateKey;
    private Map<String, String> banksSharedKey = new HashMap<>(); //name банка -> общий ключ с банком
    private Map<Integer, String> locationSharedKey = new HashMap<>();

    private static Intermediary intermediary;

    private Intermediary() throws SQLException, ClassNotFoundException {
        Statement statement = Utils.getStatement();
        ResultSet resultSet = statement.executeQuery(Constant.SqlQuery.GET_INTEMEDIARY_PRIVATE_KEY);
        if (resultSet.next()){
            privateKey = resultSet.getString("private_key");
        }else {
            privateKey = UUID.randomUUID().toString().replaceAll("-", "");
            PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.CREATE_INTERMEDIARY_PRIVATE_KEY);
            preparedStatement.setString(1, privateKey);
            preparedStatement.executeUpdate();
        }
    }

    public static Intermediary getIntermediary() throws SQLException, ClassNotFoundException {
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

    public String getLocationSharedKey(int locationId){
        return locationSharedKey.get(locationId);
    }

    public void addLocationSharedKey(int locationId, String sharedKey){
        locationSharedKey.put(locationId, sharedKey);
    }
}
