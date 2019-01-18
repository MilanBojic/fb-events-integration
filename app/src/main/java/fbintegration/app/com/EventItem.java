package fbintegration.app.com;

public class EventItem {

    String temp;

    public EventItem(String temp) {
        this.temp = temp;
    }

    String name;
    String description;
    fbintegration.app.com.Place place;
    long id;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
