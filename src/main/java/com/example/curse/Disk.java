package com.example.curse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Disk extends Element{

    private final String cmd_1 = "df --output=size,used,avail /";
    private String regex = "(.*)"+"(\\d+\\D+){2}"+"(\\d)";
    private double total_rom, used_rom;

    public Disk(){
        this.setCmd(cmd_1);
        this.setRegex(regex);
        try {
            this.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setMeasure("GB");
        this.setName("ROM occupancy");
    }

    public double getTotal_rom(){ //геттер для итогового размера диска (монт.директории)
        return this.total_rom;
    }

    public double getUsed_rom(){ //геттер для используемого места на диске
        return this.used_rom;
    }

    @Override
    public void show(){ //вывод в консоль информации
        System.out.print(this.getName() + ": " + this.getUsed_rom() + " " + this.getMeasure() + " | Total: " + this.getTotal_rom() + " " + this.getMeasure() + " [" + this.getDate() + "]\n\n" );
    }

    @Override
    public void grab(){
        double temp1, temp2; //переменные для округления
        ArrayList<String> parse_res; //массмив строк для копирования результата (даже если результат - одна строка, нужен массив, ибо возвращается массив строк вывода. может быть одна, может несколько)
        parse_res = this.getResult(); //копирование строк
        String temp = ""; //строка для разбиения
        String[] parts = parse_res.get(0).split(" "); //разбиение строки по пробелу
        for(int i = 0;i < parts.length; i++) { //пока не конец массива с частями строки
            if(parts[i].length() > 1) { //если длина элемента более одного (необходимость из-за разного вывода в wsl и ubuntu server 18.04)
                temp1 = Double.valueOf(parts[i]) / 1024 / 1024; //преобразование нужного элементка к double и перевод его значения в ГБ
                temp2 = Double.valueOf(parts[i+1]) / 1024 / 1024; //преобразование нужного элементка к double и перевод его значения в ГБ
                temp = String.format("%.2f", temp1); //округление
                this.total_rom = Double.valueOf(temp); //установка значения
                temp = String.format("%.2f", temp2); //округление
                this.used_rom = Double.valueOf(temp); //установка значения
                i = parts.length; //прерывание цикла, т.к. нам нужно только два значения
            }
        }
        this.recordInDB();
    }

    @Override
    public boolean recordInDB(){ //запись в бд
        boolean result = false;
        String table_name = "disk_info", checker = "select * from " + table_name;
        int diff = 0, last_index = 0;
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
                String insert = "insert into " + table_name + " (used_memory, total_memory, measure, date) values ('" + this.getUsed_rom() + "','" + this.getTotal_rom() + "','" + this.getMeasure() + "','" + this.getDate() +"')";
                stmt.executeUpdate(insert);
                rs = stmt.executeQuery(checker);
                while(rs.next()) {
                    last_index = rs.getInt("id");
                }
                System.out.println("last_id = " + last_index);
                if(last_index > this.getMaxCount()){
                    diff = last_index - this.getMaxCount();
                    System.out.println("del id < " + (diff+1));
                    stmt.executeUpdate("delete from " + table_name + " where id < " + (diff + 1));
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
