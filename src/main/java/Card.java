/**
 * Created by dm13y on 9/8/14.
 */
public class Card {
    private String docId;
    private String name;
    private Long currentPrice;
    private int rate;
    private Long fullPrice;
    private int count;
    private String country;
    private String measure;

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDocId() {
        return docId;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return currentPrice;
    }

    public int getDiscountRate() {
        return rate;
    }

    public Long getDiscountPrice() {
        return fullPrice;
    }

    public int getCount() {
        return count;
    }

    public void setRate(int rate) {
        this.rate = rate;
        setFullPrice();
    }

    public void setFullPrice() {
        fullPrice = (long)(getPrice() / ((100 - (double) rate) / 100));
        fullPrice = (fullPrice / 50) * 50;
    }

    public Long getFullPrice() {
        return fullPrice;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Card(String docId, String name, Long currentPrice, String country, String measure) {
        this.docId = docId;
        this.name = name;
        setMeasure(measure);
        this.country = country;
        this.currentPrice = currentPrice;
        this.rate = Integer.parseInt(Main.getDefaultPropPriceList("discountRate"));
        this.count = Integer.parseInt(Main.getDefaultPropPriceList("countPriceList"));
        setFullPrice();
    }
}
