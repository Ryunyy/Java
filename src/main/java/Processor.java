import java.io.IOException;

public class Processor extends Element{

    private String cmd = "top -bn 1 ";
    public String result = "";

    public Processor(){
        this.setCmd(cmd);
    }

    @Override
    public boolean recordInDB(){
        return false;
    }
}
