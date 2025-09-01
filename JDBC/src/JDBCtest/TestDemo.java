package JDBCtest;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.Properties;

public class TestDemo {
    public static void main(String[] args) throws ClassNotFoundException {
        try {
            runTest();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void runTest() throws SQLException, IOException, ClassNotFoundException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement())
        {
            System.out.println(connection);
//            String sql = """
//                    create table greeting(message varchar(50))
//                    """;
//            statement.executeUpdate(sql);
//            //想表中插入数据
//            String insertSql = """
//                    insert into greeting(message)
//                    values ('你好世界！')
//                    """;
//            statement.executeUpdate(insertSql);
//
//            //读取并且在终端中打印此文件
//            String query = """
//                    select * from greeting
//                    """;
//            ResultSet resultSet = statement.executeQuery(query);
//            System.out.println(resultSet);

        }

    }

    public static Connection getConnection() throws IOException, SQLException, ClassNotFoundException {
        var props = new Properties();
        try(Reader in = Files.newBufferedReader(Path.of(".//JDBC//mysql.properties")))
        {
            props.load(in);
        }
        String drivers = props.getProperty("db.driver");
//        System.out.println(drivers);
        //获取驱动器，用以驱动数据库
        Class.forName(drivers);
        String url = props.getProperty("db.url");
//        System.out.println(url);
        String username = props.getProperty("db.username");
//        System.out.println(username);
        String password = props.getProperty("db.password");
//        System.out.println(password);
        //获取数据库的链接
        return DriverManager.getConnection(url,username,password);

    }
}
