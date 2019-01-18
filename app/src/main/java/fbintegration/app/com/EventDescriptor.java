package fbintegration.app.com;

import android.graphics.Bitmap;

public class EventDescriptor {

    String name;
    String description;
    String city;
    String country;
    String url;
    Bitmap mBitmap;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void clearUrl() {
        setUrl("");
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(final Bitmap bitmap) {
        mBitmap = bitmap;
    }
}
