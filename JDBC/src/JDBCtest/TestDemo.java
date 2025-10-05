package JDBCtest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
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
        try (Connection connection = getConnection())
        {
//            System.out.println(connection);
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
            Blob blob = connection.createBlob();
            var off = 0L;
            try (OutputStream outputStream = blob.setBinaryStream(off+1)) {
                BufferedImage image = ImageIO.read(new File("ten.gif"));
                ImageIO.write(image,"gif",outputStream);
            }
            String SQL = """
                    INSERT INTO user(id,username,password,image,create_time,update_time,description)
                    VALUES(?,?,?,?,?,?,?)
                    """;
            PreparedStatement statement = connection.prepareStatement(SQL);
            statement.setLong(1,1);
            statement.setString(2,"赵云");
            statement.setString(3,"A0116659");
            statement.setBlob(4,blob);
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(7,"能否插入成功？");
            statement.executeUpdate();

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
//        获取驱动器，用以驱动数据库
//        Class.forName(drivers);
        if (drivers != null) System.setProperty("db.driver",drivers);
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
