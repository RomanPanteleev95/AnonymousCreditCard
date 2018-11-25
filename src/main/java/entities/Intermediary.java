package entities;

import constants.Constant;
import entities.banks.Bank;
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

    private static Intermediary intermediary;

    private Intermediary() throws SQLException, ClassNotFoundException {
        Statement statement = Utils.getStatement();
        ResultSet resultSet = statement.executeQuery(Constant.SqlQuery.GET_INTEMEDIARY_PRIVATE_KEY);
        if (resultSet.next()) {
            privateKey = resultSet.getString("private_key");
        } else {
            privateKey = UUID.randomUUID().toString().replaceAll("-", "");
            PreparedStatement preparedStatement = Utils.getPreparedStatement(Constant.SqlQuery.CREATE_INTERMEDIARY_PRIVATE_KEY);
            preparedStatement.setString(1, privateKey);
            preparedStatement.executeUpdate();
        }
    }

    public static Intermediary getIntermediary() throws SQLException, ClassNotFoundException {
        if (intermediary == null) {
            intermediary = new Intermediary();
        }
        return intermediary;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getBankSharedKey(String bankName) throws SQLException, ClassNotFoundException {
        Bank bank = DataBaseUtils.getBankByName(bankName);
        return DataBaseUtils.getIntermediaryKeySharedWithBank(bank.getId());
    }

    public String getLocationSharedKey(int locationId) throws SQLException, ClassNotFoundException {
        return DataBaseUtils.getIntermediaryKeySharedWithLocation(locationId);
    }

}

