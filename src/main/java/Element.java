import java.io.IOException;
import java.util.regex.Pattern;

public abstract class Element {
    private String name, measure, cmd, date = "no date";
    private int value;
    public String result = "";

    public Element(){

    }

    public void setCmd(String new_cmd){
        if(new_cmd.length()>0)
            this.cmd = new_cmd;
    }

    public String getCmd(){
        return this.cmd;
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

    public void show_info(){
        try {
            parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("\n" + this.getName() + ": " + this.getValue() + " " + this.getMeasure() + " [" + this.getDate() + "]" );
        if(result.length() > 0) {
            System.out.print("\nResult of command:\n");
            System.out.println(result);
        }
    }

    private String parse() throws IOException {
        Parser parser = new Parser();
        return parser.getInfo(cmd);
    }

    public boolean date_check(String name){
        String regex = "(0[1-9]|[1-2]\\d|3[01])_(0[1-9]|1[0-2])_(\\d{4})_([01][0-9]|2[0-3])_([0-5][0-9])_([0-5][0-9])";
        return Pattern.matches(regex, name);
    }

    public abstract boolean recordInDB();
}
