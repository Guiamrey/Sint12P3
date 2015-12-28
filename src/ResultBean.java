import java.util.ArrayList;

public class ResultBean {
    private ArrayList<String> Data = new ArrayList<String>();

    public ResultBean(){
        Data = null;
    }
    public ArrayList<String> getData() {
        return Data;
    }
    public void setData(ArrayList<String> data) {
        this.Data = data;
    }
}
