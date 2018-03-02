package swingchat;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Johnson Olayiwola on 02/03/2018
 *
 */
public class Message implements Serializable {
    String author;
    String text;
    Date time;

    Message(String author, Date time, String text) {
        this.author = author;
        this.time = time;
        this.text = text;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("(HH:mm)");
        return(dateFormat.format(this.time) + " " + this.author + " : " + this.text);
    }
}
