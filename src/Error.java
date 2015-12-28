import java.util.ArrayList;

public class Error {
    private ArrayList<String> errores = new ArrayList<String>();
    private ArrayList<String> fichError = new ArrayList<String>();

    public Error(){
        errores = null;
        fichError = null;
    }
    public ArrayList<String> getErrores() {
        return errores;
    }
    public ArrayList<String> getFichError() {
        return fichError;
    }
    public void setErrores(ArrayList<String> errores) {
        this.errores = errores;
    }
    public void setFichError(ArrayList<String> fichError) {
        this.fichError = fichError;
    }
    public int getNumerrores(){
        return this.errores.size();
    }
}
