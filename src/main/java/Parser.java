import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parser {
    
    public Parser(){
        
    }
    
    public String getInfo(String cmd) throws IOException {
        String output = "", line;
        ProcessBuilder builder = new ProcessBuilder(cmd, output);
        builder.redirectErrorStream(true);
        Process process = builder.start(); //запуск команды
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream())); //получение результата после запуска

        while (true) {
            line = bufferedReader.readLine();
            if (line != null)
                output += line ;
            else
                break;
        }
        return output;
    }
}
