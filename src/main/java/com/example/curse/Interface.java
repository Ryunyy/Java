package com.example.curse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interface extends Element{

    private final String cmd_1 = "ifconfig";
    private String regex = ".+"; //регулярка для получения всего вывода
    private String regexName = ".+" + "<UP,"+"(.+)+"+">"+"(.+)"; //регулятка для получения строки с названием интерфейса
    private String regexRX = "(.+)"+"RX packets"+"(.+)"; //регулярка для получения полученных ( хех )  байтов
    private String regexTX = "(.+)"+"TX packets"+"(.+)"; //регулярка для получения отправленных байтов
    ArrayList<Intfc_info> int_info = new ArrayList<>(); //массив объектов класса интерфейса, хранящих имя интерфейса и rx/tx

    public Interface(){
        this.setCmd(cmd_1);
        this.setRegex(regex);
        try {
            this.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setMeasure("Bytes");
        this.setName("Interfaces TX/RX");
    }

    @Override
    public void grab() {
        int_info.clear();
        int line_index = 0; //индекс текущей строки
        String temp; //строка для разбиения
        String[] parts; //массив для частей строки
        ArrayList<String> lines; //массив строк вывода
        lines = this.getResult(); //копирование вывода

        while(line_index < lines.size()){ //пока не конец вывода
            temp = lines.get(line_index); //копируем текущую строку

            Pattern pattern = Pattern.compile(regexName); //если текущая строка содержит имя интерфейса
            Matcher matcher = pattern.matcher(temp);
            if(matcher.find()){
                parts = temp.split(":"); //разбиваем строку по :
                int_info.add(new Intfc_info(parts[0])); //создаем объекст класса с именем, которае лежив в частях строки
            }


            pattern = Pattern.compile(regexRX); //если строка содержит rx
            matcher = pattern.matcher(temp);
            if(matcher.find()){
                parts = temp.split(" "); //разбивем по пробелу
                for(int i = 0; i < parts.length; i++){
                    if(parts[i].contains("bytes")){ //ищем среди частей слово
                        int_info.get(int_info.size()-1).setRxAll(Integer.valueOf(parts[i+1])); //добавляем в последний созданый экземпляр класса интерфейсов значение перед найденным словом, приведенное к int
                    }
                }
            }

            pattern = Pattern.compile(regexTX); //если строка содержит tx
            matcher = pattern.matcher(temp);
            if(matcher.find()){
                parts = temp.split(" "); //разбивем по пробелу
                for(int i = 0; i < parts.length; i++){
                    if(parts[i].contains("bytes")){ //ищем среди частей слово
                        int_info.get(int_info.size()-1).setTxAll(Integer.valueOf(parts[i+1])); //добавляем в последний созданый экземпляр класса интерфейсов значение перед найденным словом, приведенное к int
                    }
                }
            }
            line_index++; //индекс следующей строки
        }
        this.recordInDB();
    }

    public int getIndexOf(String name){
        int index = -1;
        for (int i = 0; i < int_info.size(); i++){
            if(int_info.get(i).getName().equals(name))
                index = i;
        }
        return index;
    }

    @Override
    public void show(){
        System.out.print("\n"+this.getName() + "\n"); //вывод имени для веба, возможно
        for(int i = 0; i < int_info.size(); i++){
            System.out.print("Name of interface: " + int_info.get(i).getName()); //имя интерфейса
            System.out.print(" | RX_all: "+int_info.get(i).getRxAll() + " " + this.getMeasure()); //количество принятых байтов
            System.out.print(" | TX_all: "+int_info.get(i).getTxAll() + " " + this.getMeasure()); //кол-во откравленных байтов
            System.out.print(" | RX_new: "+int_info.get(i).getRxNew() + " " + this.getMeasure()); //кол-во новых байтов по сравнению с предыдущим замером
            System.out.print(" | TX_new: "+int_info.get(i).getTxNew() + " " + this.getMeasure());
            System.out.print(" [" + this.getDate() + "]"); //дата
            System.out.println();
        }
    }

    @Override
    public boolean recordInDB(){ //запись в бд
        boolean result = false;
        String table_name = "intfc_info", insert, checker = "select * from " + table_name;
        int group = 0, diff = 0, last_group_index = 0, counter = 0, first_group_index = 0;
        try {
            Connection c = DriverManager.getConnection(this.getUrl(), this.getUser(), this.getPassword());

            String create_table = "create table " + table_name +
                    " ( id serial primary key," +
                    "group_num int not null," +
                    "name varchar(10) not null," +
                    "received_all int not null," +
                    "transferred_all int not null," +
                    "received_new int not null," +
                    "transferred_new int not null, " +
                    "measure varchar(5) not null," +
                    "date varchar(20) not null )";

            String check_table = "select count(*) from information_schema.tables where table_name='" + table_name + "'";

            try {
                Class.forName("org.postgresql.Driver");
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(check_table);
                rs.next();
                if(rs.getInt(1) == 0){
                    stmt.executeUpdate(create_table);
                    //System.out.println("Create table");
                    group = 1;
                }
                //System.out.println("table already exist");
                if(group == 0){
                    int temp = 0;
                    rs = stmt.executeQuery("select * from " + table_name);
                    while(rs.next()){
                        if(rs.getInt(2) > temp)
                            temp = rs.getInt(2);
                    }
                    group = temp+1;
                    //get new group num
                }
                if(group > 1) {
                    rs = stmt.executeQuery("select * from " + table_name + " where group_num = " + (group - 1));
                    int index;
                    while(rs.next()) {
                        /*System.out.println(rs.getInt("id") + "-" + rs.getInt("group_num") + " : " + rs.getString("name") + " " + rs.getInt("received_all") +
                                " " + rs.getInt("transferred_all") + " " + rs.getInt("received_new") + " " + rs.getInt("transferred_new") + " " + rs.getString("measure") +
                                " " + rs.getString("date"));*/
                        index = getIndexOf(rs.getString("name"));
                        //System.out.println("searching name - " + rs.getString("name"));
                        if(index != -1){
                            //System.out.println("name found");
                            int res = int_info.get(index).getRxAll() - rs.getInt("received_all");
                            if(res < 0)
                                int_info.get(index).setRxNew(int_info.get(index).getRxAll());
                            else
                                int_info.get(index).setRxNew(res);

                            res = int_info.get(index).getTxAll() - rs.getInt("transferred_all");
                            if(res < 0)
                                int_info.get(index).setTxNew(int_info.get(index).getTxAll());
                            else
                                int_info.get(index).setTxNew(res);
                        }
                        else{
                            //System.out.println("name not found");
                            int_info.add(new Intfc_info(rs.getString("name")));
                            int_info.get(int_info.size()-1).setRxAll(rs.getInt("received_all"));
                            int_info.get(int_info.size()-1).setTxAll(rs.getInt("transferred_all"));
                            int_info.get(int_info.size()-1).setRxNew(0);
                            int_info.get(int_info.size()-1).setTxNew(0);
                        }
                    }
                }
                else{
                    for(int i = 0; i < int_info.size(); i++) {
                        int_info.get(i).setRxNew(0);
                        int_info.get(i).setTxNew(0);
                    }
                }
                for(int i = 0; i < int_info.size(); i++) {
                    insert = "insert into " + table_name + " (group_num, name, received_all, transferred_all, received_new, transferred_new,  measure, date) values ('" + group + "','" + int_info.get(i).getName() + "','" +
                        int_info.get(i).getRxAll() + "','" + int_info.get(i).getTxAll() + "','" + int_info.get(i).getRxNew() + "','" + int_info.get(i).getTxNew() + "','" + this.getMeasure() + "','" +
                        this.getDate() + "')";
                    stmt.executeUpdate(insert);
                }
                rs = stmt.executeQuery(checker);
                while(rs.next()) {
                    last_group_index = rs.getInt("group_num");
                    counter++;
                    if(counter == 1){
                        first_group_index = last_group_index;
                    }
                }
                if(first_group_index > last_group_index){
                    stmt.executeUpdate("delete from " + table_name + " where id > " + 0 );
                }
                //System.out.println("last_group_id = " + last_group_index);
                if(last_group_index > this.getMaxCount()){
                    diff = last_group_index - this.getMaxCount();
                    //System.out.println("del id < " + (diff+1));
                    stmt.executeUpdate("delete from " + table_name + " where group_num < " + (diff + 1));
                }
                stmt.close();
                //System.out.println("Opened database successfully");
                result = true;
            }
            finally {
                c.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
            result = false;
        }
        return result;
    }
}
