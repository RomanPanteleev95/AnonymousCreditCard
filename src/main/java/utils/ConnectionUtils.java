package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtils {
    private static Connection connection;

    private ConnectionUtils() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:postgresql://ec2-54-217-236-201.eu-west-1.compute.amazonaws.com:5432" +
                "/ddh2dcreu9it5p?sslfactory=org.postgresql.ssl.NonValidatingFactory&user=wqdmxutohfcxex&password=78b1c206c96557b79a4b3a0968eea4f4975f8f3462a95c255ae68a7e86c95a66&ssl=true");
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null){
            ConnectionUtils connectionUtils = new ConnectionUtils();
        }
        return connection;
    }
}
