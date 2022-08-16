import java.io.IOException;
import java.util.ArrayList;

public class Memory extends Element{

    private final String cmd_1 = "top -bn 1 -i -c";
    private String regex = "KiB Mem :";
    private double mb;

    public Memory(){
        this.setCmd(cmd_1);
        this.setRegex(regex);
        try {
            this.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getMb(){
        return this.mb;
    }

    public void grab(){
        ArrayList<String> parse_res = new ArrayList<>();
        parse_res = this.getResult();
        String temp = parse_res.get(0);
        String parts[] = temp.split(" ");
        for(int i = 0; i < parts.length; i++){
            System.out.print(i + ": " + parts[i] + "\n");
        }
    }

    @Override
    public boolean recordInDB(){
        return false;
    }

}
