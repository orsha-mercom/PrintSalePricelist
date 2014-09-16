import java.util.Date;

/**
 * Created by dm13y on 9/3/14.
 */
public class Document {
    private String date;
    private String number;
    private String name;
    private Long id;

    public Document(Long id, String date, String number, String name) {
        this.date = date;
        this.name = name;
        this.id = id;
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public String getNumber() {
        return number;
    }
}
