import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Мар'ян on 03.05.2016.
 */
public class DBHelper {
    public static void createTable(String tableName, String[] columnHeader, Statement statement) throws SQLException {
        StringBuilder builder = new StringBuilder();
        String createRequest = "CREATE TABLE %s (%s);";

        for (String element : columnHeader) {
            builder.append(element).append(" TEXT,");
        }
        builder.deleteCharAt(builder.length() - 1);
        statement.execute(String.format(createRequest, tableName, builder.toString()));
    }

    public static void insertData(String tableName, List<String []> data, Statement statement, boolean analyzeDataTypes ) throws SQLException {
        String intRegular = "-?\\d+";
        String doubleRegular = "-?\\d+\\.\\d+";
        String insertRequest = "INSERT INTO %s VALUES (%s);";
        String modifyRequest = "ALTER TABLE %s MODIFY %s %s;";
        String[] header = data.get(0);
        int[] types = new int[data.get(0).length];

        for (int j = 1; j < data.size(); j++) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < data.get(j).length; i++) {
                //заміна коми на точку, щоб база аних сприйняла це як число
                String element = data.get(j)[i].replaceAll(",", ".");
                builder.append("\'").append(element).append("\',");
                //дані для аналізу типів даних
                if (element.matches(intRegular)) {
                    types[i] = types[i] < 10 ? 10 : types[i];
                } else if (element.matches(doubleRegular)) {
                    types[i] = types[i] < 20 ? 20 : types[i];
                } else types[i] = types[i] < 30 ? 30 : types[i];
            }
            builder.deleteCharAt(builder.length()-1);
            statement.execute(String.format(insertRequest, tableName, builder.toString()));
        }
        // міняє тип стовпчика таблиці в залежності від типу даних
        if (analyzeDataTypes) {
            for (int i = 0; i < header.length; i++) {
                if (types[i] == 10) {
                    statement.execute(String.format(modifyRequest, tableName, header[i], "INT"));
                }
                if (types[i] == 20) {
                    statement.execute(String.format(modifyRequest, tableName, header[i], "DOUBLE"));
                }
            }
        }
    }
}
