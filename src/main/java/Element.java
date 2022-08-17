import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Element {
    private String name = "", measure = "", cmd = "", regex = "", date = "no date";
    private double value = 0;
    private ArrayList<String> result;

    public Element(){

    }

    public void setCmd(String new_cmd){
        if(new_cmd.length()>0)
            this.cmd = new_cmd;
    }

    public String getCmd(){
        return this.cmd;
    }

    public void setRegex(String new_reg){
        if(new_reg.length() > 0)
            this.regex = new_reg;
    }

    public String getRegex(){
        return regex;
    }

    public void setName(String new_name){
        if(new_name.length() > 0)
            this.name = new_name;
    }

    public String getName(){
        return this.name;
    }

    public void setValue(double new_value){
        if(new_value >= 0)
            this.value = new_value;
    }

    public double getValue(){
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

    protected abstract void show();

    public void setResult(ArrayList<String> new_res){
        if(new_res.size() >0)
            this.result = new_res;
    }

    public ArrayList<String> getResult(){
        return this.result;
    }

    public void parse() throws IOException {
        Parser parser = new Parser();
        try {
            this.result = parser.getInfo(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("parse unsuccessful");
        }
        filter();
    }

    public void filter(){
        ArrayList<String> res = new ArrayList<>();
        int i = 0;
        while(i < result.size()){
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(result.get(i));
            //System.out.print("LINE: " + result.get(i));
            if (matcher.find()) {
                //System.out.print("\ncorrect\n\n");
                res.add(result.get(i));
            }
            else{
                //System.out.print("\nincorrect\n\n");
            }
            i++;
        }
        this.setResult(res);
    }

    public boolean date_check(String name){
        String regex = "(0[1-9]|[1-2]\\d|3[01])_(0[1-9]|1[0-2])_(\\d{4})_([01][0-9]|2[0-3])_([0-5][0-9])_([0-5][0-9])";
        return Pattern.matches(regex, name);
    }

    public abstract boolean recordInDB();

    public abstract void grab();
}
