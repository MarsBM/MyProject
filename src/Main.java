import com.opencsv.CSVReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Мар'ян on 01.05.2016.
 */
public class Main {
    private static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("spring.xml");
    public static void main(String[] args) throws Exception {

        //FileReader reader = (FileReader) CONTEXT.getBean("FileReader");
        CSVReader csvReader = (CSVReader) CONTEXT.getBean("CSVReader");
        String url = (String) CONTEXT.getBean("connectURL");
        String login = (String) CONTEXT.getBean("connectLogin");
        String password = (String) CONTEXT.getBean("connectPassword");
        String tableName = (String) CONTEXT.getBean("tableName");

        Connection connection = DriverManager.getConnection(url, login, password);
        Statement statement = connection.createStatement();

        List<String[]> table = csvReader.readAll();
        String[] header = table.get(0);

        DBHelper.createTable(tableName, header, statement);
        DBHelper.insertData(tableName, table, statement, true);

        //reader.close();
        connection.close();
    }
}
