package crawler;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        int i;
        int[] status = null;

        DBManager dbmng = new DBManager("root","lelab2016");
        dbmng.setDbms("mysql");
        try {
            dbmng.makeDbConnection();
            dbmng.updateDatabase();
        }catch (SQLException e){
            System.out.println("################ SQL Exception occured");
            e.printStackTrace();
        }
            String[] urls = dbmng.getUrlList();
            Parser[] parser = new Parser[urls.length];
            Mapper mapper = new Mapper();
        try {
            status = dbmng.getStatusList();
            dbmng.close();
        }catch (SQLException e){
            System.out.println("################ SQL Exception occured");
            e.printStackTrace();
        }

        if(status == null){
            System.out.println("################ Parser state error");
        }
        for(i = 0; i < urls.length; i++){
            parser[i] = new Parser(urls[i]);
            parser[i].setType(status[i]);
        }

        for(i = 0; i < urls.length; i++){
            try {
                parser[i].findLocation();
                mapper.setMapper(parser[i]);
                dbmng.setDBManager(mapper, "mysql");
                dbmng.execute();
            }catch (IOException e){
                System.out.println("################ Execution Failed - Continuing");
                e.printStackTrace();
            }
        }
    }
}
