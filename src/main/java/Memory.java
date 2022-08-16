public class Memory extends Element{

    private final String cmd_1 = "top -bn 1 -i -c";

    public Memory(){
        this.setCmd(cmd_1);

    }

    @Override
    public boolean recordInDB(){
        return false;
    }

}
