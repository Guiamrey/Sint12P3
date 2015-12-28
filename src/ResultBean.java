import java.util.ArrayList;

public class ResultBean {
    private ArrayList<String> data = new ArrayList<String>();

    public ResultBean(){
        data = null;
    }
    public ArrayList<String> getData() {
        return data;
    }
    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
