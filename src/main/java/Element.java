import java.util.regex.Pattern;

public abstract class Element {
    private String name;
    private int value;
    private String measure;
    private String date = "no date";

    public Element(){

    }

    public void setName(String new_name){
        if(new_name.length() > 0)
            this.name = new_name;
    }

    public String getName(){
        return this.name;
    }

    public void setValue(int new_value){
        if(new_value >= 0)
            this.value = new_value;
    }

    public int getValue(){
        return this.value;
    }

    public void setMeasure(String new_measure){
        if(new_measure.length() > 0)
            this.measure = new_measure;
    }

    public String getMeasure(){
        return this.measure;
    }

    public void setDate(String new_date){
        if(date_check(new_date))
            this.date = new_date;
    }

    public String getDate(){
        return this.date;
    }

    public abstract void show_info();

    public boolean date_check(String name){
        String regex = "(0[1-9]|[1-2]\\d|3[01])_(0[1-9]|1[0-2])_(\\d{4})_([01][0-9]|2[0-3])_([0-5][0-9])_([0-5][0-9])";
        return Pattern.matches(regex, name);
    }
}
