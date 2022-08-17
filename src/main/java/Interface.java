import java.io.IOException;
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
                        int_info.get(int_info.size()-1).setRx(Integer.valueOf(parts[i+1])); //добавляем в последний созданый экземпляр класса интерфейсов значение перед найденным словом, приведенное к int
                    }
                }
            }

            pattern = Pattern.compile(regexTX); //если строка содержит tx
            matcher = pattern.matcher(temp);
            if(matcher.find()){
                parts = temp.split(" "); //разбивем по пробелу
                for(int i = 0; i < parts.length; i++){
                    if(parts[i].contains("bytes")){ //ищем среди частей слово
                        int_info.get(int_info.size()-1).setTx(Integer.valueOf(parts[i+1])); //добавляем в последний созданый экземпляр класса интерфейсов значение перед найденным словом, приведенное к int
                    }
                }
            }
            line_index++; //индекс следующей строки
        }
    }

    @Override
    public void show(){
        System.out.print(this.getName() + "\n"); //вывод имени для веба, возможно
        for(int i = 0; i < int_info.size(); i++){
            System.out.print("Name of interface: " + int_info.get(i).getName()); //имя интерфейса
            System.out.print(" | RX: "+int_info.get(i).getRx() + " " + this.getMeasure()); //количество принятых байтов
            System.out.print(" | TX: "+int_info.get(i).getTx() + " " + this.getMeasure()); //кол-во откравленных байтов
            System.out.print(" [" + this.getDate() + "]"); //дата
            System.out.println();
        }
    }

    @Override
    public boolean recordInDB(){ //запись в бд
        return false;
    }


}
