import java.util.ArrayList;

public class ErrorBean {
    private ArrayList<String> Errores = new ArrayList<String>();
    private ArrayList<String> FichError = new ArrayList<String>();

    public ErrorBean(){
        Errores = null;
        FichError = null;
    }
    public ArrayList<String> getErrores() {
        return Errores;
    }
    public ArrayList<String> getFichError() {
        return FichError;
    }
    public void setErrores(ArrayList<String> errores) {
        this.Errores = errores;
    }
    public void setFichError(ArrayList<String> fichError) {
        this.FichError = fichError;
    }
}
