import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parser {
    
    public Parser(){
        
    }
    
    public String getInfo(String cmd) throws IOException {
        String output = "", line, s;
        Process p;

        System.out.println("\ncommand = " + cmd);

        try {
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                output += ("\n" + s);
            p.waitFor();
            p.destroy();
        } catch (Exception e) {}
        return output;
    }
}
