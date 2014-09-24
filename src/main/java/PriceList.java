/**
 * Contains fields for printing price tags
 */
public class PriceList {
    private Card card;
    private Document doc;

    public PriceList(Card card, Document doc) {
        this.card = card;
        this.doc = doc;
    }

    public String getMeasure() {
        return card.getMeasure();
    }

    public PriceList(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public int getCount() {
        return card.getCount();
    }

    public String getCountry() {
        return card.getCountry();
    }
//
    public String getName() {
        return card.getName();
    }
//
    public Long getPrice() {
        return card.getPrice();
    }
//
    public int getRate() {
        return card.getDiscountRate();
    }
//
    public Long getFullPrice() {
        return card.getFullPrice();
    }
//
    public String getDocument() {
        return doc.getName() + " ТТН №" + doc.getNumber() + " от " + doc.getDate();
    }
}
