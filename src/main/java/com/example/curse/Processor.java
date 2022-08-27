package com.example.curse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Processor extends Element{

    private String cmd_1 = "top -bn 1"; //командя для парсинга результатов
    private String regex = "(\\D+\\d+){7}"+"(\\.)?\\d\\D+\\d+(\\.)?\\d"+"(\\D+\\d+){2}"+"(\\.)"+"(\\d+\\D+)"; //регулярка для получения результатов таблицы
    private double sum_occupancy = 0; //итоговая загруженность процессора

    public Processor(){
        this.setCmd(cmd_1); //установка команды для парсинга
        this.setRegex(regex); //установка регулярки
        try {
            this.parse(); //парсинг команды
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setMeasure("%"); //установка ед.измерений
        this.setName("CPU occupancy"); //установка названия (для веба)
    }

    public double getSum(){ //геттер загруженности процессора
        return this.sum_occupancy;
    }

    @Override
    public void grab(){
        int line_index = 0; //номер линии в спаршеном выводе
        String temp; //строка для ее разбиения и поиска нужного параметра
        String[] parts; //массив частей, на которые была разбита строка
        ArrayList<String> lines; //массив строк для поиска среди них нужных параметров
        lines = this.getResult(); //копирование из буффера результатов команды

        while(line_index < lines.size()){ //пока не дошли до конца вывода
            temp = lines.get(line_index); //копируем строку для ее разбиения
            parts = temp.split(" "); //разбиваем строку по пробелам

            if((!parts[parts.length - 7].contains(","))&&(!parts[parts.length - 7].isEmpty()))
                this.sum_occupancy += Double.valueOf(parts[parts.length - 7]); //получаем нужное значение (из столбца) и суммируем его с предыдущими для подсчета общей загруженности CPU
            line_index++; //индекс для следующей строки
        }
        this.setValue(this.getSum()); //установка итоговой суммы
        this.recordInDB();
    }

    

    @Override
    protected void show(){ //вывод информации в консоль
        System.out.print(this.getName() + ": " + this.getValue() + " " + this.getMeasure() + " [" + this.getDate() + "]\n\n" );
    }

    @Override
    public boolean recordInDB(){ //запись в БД
        boolean drop = false, result = false;
        String table_name = "proc_info", checker = "select * from " + table_name;
        int diff = 0, last_index = 0, counter = 0, first_index = 0;
        try {
            Connection c = DriverManager.getConnection(this.getUrl(), this.getUser(), this.getPassword());

            String create_table = "create table " + table_name +
                    " ( id serial primary key," +
                    "occupancy real not null," +
                    "measure varchar(1) not null," +
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
                String insert = "insert into " + table_name + " (occupancy, measure, date) values ('" + this.getValue() + "','" + this.getMeasure() + "','" + this.getDate() +"')";
                stmt.executeUpdate(insert);
                rs = stmt.executeQuery(checker);
                while(rs.next()) {
                    last_index = rs.getInt("id");
                    counter++;
                    if(counter == 1){
                        first_index = last_index;
                    }
                }
                if(first_index > last_index){
                    stmt.executeUpdate("delete from " + table_name + " where id > " + 0 );
                }
                //System.out.println("last_id = " + last_index);
                if(last_index > this.getMaxCount()){
                    diff = last_index - this.getMaxCount();
                    //System.out.println("del id < " + (diff+1));
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
