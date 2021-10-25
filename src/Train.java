import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Train implements Serializable {
    public static String sPo = "22-01 10:15:55";
    public static String sDo = "22-01 14:15:55";

    private int id;
    private String punct;
    private Date otpr;
    private Date prib;

    public Train(int id, String punct, Date otpr, Date prib) throws ParseException {
        this.id = id;
        this.otpr = otpr;
        this.prib = prib;
        this.punct = punct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPunct(){
        return punct;
    }

    public void setPunct(String punct){
        this.punct = punct;
    }

    public Date getOtpr(){
        return otpr;
    }

    public void setOtpr(){
        this.otpr = otpr;
    }

    public Date getPrib(){
        return prib;
    }

    public void setPrib(Date prib){
        this.prib = prib;
    }


public boolean isDebt() throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("dd-M HH:mm:ss", Locale.ENGLISH);
    formatter.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
    Date date1 = formatter.parse(sPo);
    Date date2 = formatter.parse(sDo);

    if (otpr.compareTo(date1) == 1 & otpr.compareTo(date2) == -1){
        return true;
    } else return false;
}

@Override
public String toString() {
    StringBuffer result = new StringBuffer();
    result.append(id);
    result.append(" ");
    result.append(punct);
    result.append(" ");
    result.append(otpr);
    result.append(" ");
    result.append(prib);
    return result.toString();
}
}
