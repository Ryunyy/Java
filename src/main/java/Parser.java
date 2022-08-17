import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Parser {
    
    public Parser(){
        
    }
    
    public ArrayList<String> getInfo(String cmd) throws IOException {
        String s;
        ArrayList<String> output = new ArrayList<>();
        Process p;

        try {
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                output.add(s);
            p.waitFor();
            p.destroy();
        } catch (Exception e) {}
        return output;
    }
}
