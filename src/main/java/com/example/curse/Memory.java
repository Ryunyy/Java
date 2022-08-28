package com.example.curse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Memory extends Element{

    private final String cmd_1 = "top -bn 1 -i -c";
    private String regex = "KiB Mem :";
    private double mb_used, mb_total; //переменные для хранения значений используемой озу и общее значение озу

    public Memory(){
        this.setCmd(cmd_1);
        this.setRegex(regex);
        try {
            this.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setName("RAM usage");
        this.setMeasure("MB");
    }

    public double getMb_used(){ //геттер для используемой памяти
        return this.mb_used;
    }

    public double getMb_total(){ //геттер для полной памяти
        return this.mb_total;
    }

    @Override
    public void show(){ //вывод в консоль информации
        System.out.print(this.getName() + ": " + this.getMb_used() + " " + this.getMeasure() + " | Total: " + this.getMb_total() + " " + this.getMeasure() + " [" + this.getDate() + "]\n\n" );
    }

    public void getVal(String parse){
        double val;
        String parts[] = parse.split(" "); //массив частей строки, разбитых по пробелу
        for(int i = 0; i < parts.length; i++){ //пока не конец вывода
            if(parts[i].contains("used")){ //если строка содержит used, значит она нужная нам
                val = (Double.valueOf(parts[i-1]) / 1024); //получаем значение перед этим словом, приводим его к типу double, сокрощаем до МБ
                this.mb_used = Double.valueOf(val); //устанавливаем его
            }
            if(parts[i].contains("total")){  //тот же алгоритм, как и с used
                val = (Double.valueOf(parts[i-1]) / 1024);
                this.mb_total = Double.valueOf(val);
            }
        }
    }

    @Override
    public void grab(boolean record){//вывод команды - одна строка, поэтому ищем циклом сразу в массиве частей
        ArrayList<String> parse_res; //массив строк для копирования результатов команды
        parse_res = this.getResult(); //копирование вывода команды
        getVal(parse_res.get(0));

        String temp = String.format("%.2f", this.mb_total); //округление
        this.mb_total = Double.valueOf(temp);

        temp = String.format("%.2f", this.mb_used); //округление
        this.mb_used = Double.valueOf(temp);

        if(record)
            this.recordInDB();
    }

    public String cleanAll(int first_index, int last_index, String table_name){
        if(first_index > last_index){
            return "delete from " + table_name + " where id > " + 0;
        }
        return "";
    }

    public String cleanOne(int last_index, String table_name){
        int diff = 0;
        if(last_index > this.getMaxCount()){
            diff = last_index - this.getMaxCount();
            //System.out.println("del id < " + (diff+1));
            return "delete from " + table_name + " where id < " + (diff + 1);
        }
        return "";
    }

    @Override
    public boolean recordInDB(){ //запись в бд
        boolean result = false;
        String table_name = "mem_info", checker = "select * from " + table_name;
        int diff = 0, last_index = 0, counter = 0, first_index = 0;
        try {
            Connection c = DriverManager.getConnection(this.getUrl(), this.getUser(), this.getPassword());

            String create_table = "create table " + table_name +
                    " ( id serial primary key," +
                    "used_memory real not null," +
                    "total_memory real not null," +
                    "measure varchar(2) not null," +
                    "date varchar(20) not null )";

            String check_table = "select count(*) from information_schema.tables where table_name='" + table_name + "'";

            try {
                Class.forName("org.postgresql.Driver");
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(check_table);
                rs.next();
                if(rs.getInt(1) == 0){
                    stmt.executeUpdate(create_table);
                }
                //System.out.println("table already exist");
                String insert = "insert into " + table_name + " (used_memory, total_memory, measure, date) values ('" + this.getMb_used() + "','" + this.getMb_total() + "','" + this.getMeasure() + "','" + this.getDate() +"')";
                stmt.executeUpdate(insert);
                rs = stmt.executeQuery(checker);
                while(rs.next()) {
                    last_index = rs.getInt("id");
                    counter++;
                    if(counter == 1){
                        first_index = last_index;
                    }
                }
                stmt.executeUpdate(cleanAll(first_index,last_index,table_name) );
                //System.out.println("last_id = " + last_index);
                stmt.executeUpdate(cleanOne(last_index,table_name));
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
