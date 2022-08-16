import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processor extends Element{

    private String cmd_1 = "top -bn 1 ";
    private String regex = "PID";
    private double sum_occupancy = 0;

    public Processor(){
        this.setCmd(cmd_1);
        this.setRegex(regex);
    }

    @Override
    public boolean recordInDB(){
        return false;
    }

}
