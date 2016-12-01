package crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBManager {
    private Mapper mapper;
    private List<String> dbQuery;
    private Statement st;
    private ResultSet rs;

    private static final String[] field = {"TypeS", "Url", "Title", "Refer", "BidNo", "Bstart", "Bexpire", "PDate", "Dept", "Charge"};

    /*
     * Type Number 정보
     * From 0 to 3 : 입찰정보  물품0 공사1 용역2 미분류3
     * From 4 to 7 : 계약정보  물품4 공사5 용역6 미분류7
     */
    private Connection conn;

    private String userName;
    private String userPassword;
    private String dbms;
    private String server;
    private String portNumber;
    private String dbName;

    private String queryForm;

    private int id; // primary key
    private int Type; // 입찰종류
    private String Url;
    private String Title; // 공고명
    private String Refer; // 자료출처
    private String BidNo; // 공고번호
    private String Bstart; // 게시일
    private String Bexpire; // 마감일
    private String PDate; // 게시일자
    private String Dept; // 담당부서
    private String Charge; // 담당자
    private String Date; // 갱신일자

    private boolean[] smartField;
    private String[] category;
    private ArrayList<BidList> bidList;

    public DBManager(String id, String pwd){
        userName = id;
        userPassword = pwd;
    }

    public void updateDatabase() throws SQLException{
        st = conn.createStatement();
        st.executeUpdate("use bidinfo;");
        st.executeUpdate("delete from Bidinfo_bidlist where Date < DATE_SUB(NOW(), INTERVAL 20 DAY);");
        st.executeUpdate("delete from Bidinfo_comment where Date < DATE_SUB(NOW(), INTERVAL 20 DAY);");
    }

    public void setDBManager(Mapper mappedData,String db){
        mapper = mappedData;
        dbms = db;
        queryForm = "INSERT INTO Bidinfo_bidlist";
        bidList = mapper.mapping();
        category = new String[mapper.getCategory().size()];
        for(int i = 0; i < mapper.getCategory().size(); i++){
            category[i] = mapper.getCategory().get(i);
        }
        dbQuery = new ArrayList<String>();
    }

    public void setUserInfo(String id, String pwd){
        userName = id;
        userPassword = pwd;
    }

    public void close() throws SQLException{
        conn.close();
    }

    public void setDbms(String db){
        dbms = db;
    }

    private void setSQLStyle(){
        server = "localhost";
        portNumber = "3306";
    }

    private void setDerbyStyle(){
        server = "derby";
        dbName = "testdb";
    }

    public void makeDbConnection() throws SQLException{
        conn = getConnection();
    }

    public String[] getUrlList(){
        String[] urls;
        try {
            st = conn.createStatement();
            st.executeUpdate("use bidinfo;");
            rs = st.executeQuery("SELECT COUNT(*) FROM Bidinfo_crawler;");
        } catch (SQLException error) {
            System.out.println("Database Error");
        }
        int rowCount = 0;
        try {
            if(rs.next()){
                rowCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        urls = new String[rowCount];
        String query = "select url from Bidinfo_crawler";
        try {
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int i = 0;
        try {
            while(rs.next()){
                urls[i] = rs.getString("url");
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return urls;
    }

    public int[] getStatusList() throws SQLException{
        int[] statusList;
        st = conn.createStatement();
        st.executeUpdate("use bidinfo;");
        rs = st.executeQuery("SELECT COUNT(*) FROM Bidinfo_crawler;");

        int rowCount = 0;
        if(rs.next()){
            rowCount = rs.getInt(1);
        }

        statusList = new int[rowCount];
        rs = st.executeQuery("select status from Bidinfo_crawler");
        int i = 0;

        while(rs.next()){
            statusList[i] = rs.getInt("status");
            i++;
        }

        return statusList;
    }

    public void addFieldQueries(){
        String fieldVal = "(";
        int count = 0;
        for(int i = 0; i < 10; i++){
            if(smartField[i] && count < category.length - 1){
                if(i == 0){
                    fieldVal += "Type" + ",";
                    count++;
                }else{
                    fieldVal += field[i] + ",";
                    count++;
                }
            }else if(smartField[i]){
                fieldVal += field[i];
                count++;
            }
        }
        if(count == category.length){
            fieldVal += ")";
            queryForm += fieldVal;
        }
    }

    private void addData(){
        int columnCount = category.length;
        String values = "values(";

        String temp;
        int count = 0;

        for(int i = 0 ; i < bidList.size(); i++){
            temp = queryForm;
            count = 0;
            values = "select ";
            for(int j = 0; j < field.length; j++){
                if(smartField[j] && j == 0){
                    values += Integer.toString(bidList.get(i).getType()) + ",";
                    count++;
                }else if(smartField[j] && count != columnCount - 1){
                    values += "\'" +bidList.get(i).getIndex(j) + "\',";
                    count++;
                }else if(smartField[j] && count == columnCount - 1){
                    values += "\'" + bidList.get(i).getIndex(j) + "\'";
                    count++;
                }
            }
            if(count == category.length){
                values += "from DUAL where not exists (select * from Bidinfo_bidlist where Title = '" + bidList.get(i).getTitle() + "');";
                temp += values;
            }
            dbQuery.add(temp);
        }
    }

    public void execute(int num){
        int i = num;
        int number = i;

        try {
            if(st == null)
                st = conn.createStatement();
            st.executeUpdate("use bidinfo;");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(num);

        for(; i < dbQuery.size(); i++){
            number = i;
            try {
                st.executeUpdate(dbQuery.get(i));
            } catch (SQLException e) {
                System.out.println("A Query Failed");
            }finally{
                execute(number + 1);
            }
        }
    }

    public void execute(){
        try {
            makeDbConnection();
        } catch (SQLException e) {
        }
        smartFieldFinder();
        addFieldQueries();
        addData();
        int errorNum = 0;

        System.out.println(dbQuery.size());

        try {
            st = conn.createStatement();
            st.executeUpdate("use bidinfo;");
            for(int i = 0; i < dbQuery.size(); i++){
                System.out.println(dbQuery.get(i));
            }
            for(int i = 0; i < dbQuery.size(); i++){
                errorNum = i;
                st.executeUpdate(dbQuery.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            execute(errorNum + 1);
        }
    }

    private void smartFieldFinder(){
        smartField = new boolean[10];
        for(int i = 0; i < 10 ; i++){
            smartField[i] = false;
        }
        String fieldS;
        for(int i = 0; i < category.length ; i++){
            fieldS = category[i];
            for(int j = 0 ; j < 10 ; j++){
                if(fieldS.equals(field[j])){
                    smartField[j] = true;
                }else
                    continue;
            }
        }
    }

    private Connection getConnection() throws SQLException{
        Connection connection = null;
        Properties prop = new Properties();

        prop.put("user", userName);
        prop.put("password", userPassword);

        if(dbms.equals("mysql")){
            try {
                setSQLStyle();
                connection = DriverManager.getConnection("jdbc:" + dbms + "://" + server + ":" + portNumber + "/?autoReconnect=true&useSSL=false", prop);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(dbms.equals("derby")){
            try {
                setDerbyStyle();
                connection = DriverManager.getConnection("jdbc:" + dbms + ":" + dbName + ";create=true", prop);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Connection Success");

        return connection;
    }
}
