package models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Review implements Comparable <Review>{
    private String content;
    private String writtenBy;
    private int rating;
    private int id;
    private int restaurantId; //will be used to connect Restaurant to Review (one-to-many)
    private long createdat;
    private String formattedCreatedAt;

    //getters
    public String getContent() {return content;}
    public String getWrittenBy() {return writtenBy;}
    public int getRating() {return rating;}
    public int getId() {return id;}
    public int getRestaurantId() {return restaurantId;}
    public long getCreatedat() {return createdat;}
    public String getFormattedCreatedAt() {
        return new SimpleDateFormat("MM/dd/yyyy @ K:mm a").format(createdat);
    }

    //setters
    public void setContent(String content) {this.content = content;}
    public void setWrittenBy(String writtenBy) {this.writtenBy = writtenBy;}
    public void setRating(int rating) {this.rating = rating;}
    public void setId(int id) {this.id = id;}
    public void setRestaurantId(int restaurantId) {this.restaurantId = restaurantId;}
    public void setCreatedat() {this.createdat = System.currentTimeMillis();}
    public void setFormattedCreatedAt() {
        this.formattedCreatedAt = new SimpleDateFormat("MM/dd/yyyy @ K:mm a").format(this.createdat);
    }


    public Review(String content, String writtenBy, int rating, int restaurantId) {
        this.content = content;
        this.writtenBy = writtenBy;
        this.rating = rating;
        this.restaurantId = restaurantId;
        this.createdat = System.currentTimeMillis();
        setFormattedCreatedAt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return rating == review.rating && id == review.id && restaurantId == review.restaurantId && content.equals(review.content) && writtenBy.equals(review.writtenBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, writtenBy, rating, id, restaurantId);
    }

    @Override
    public int compareTo(Review reviewObject) {
        return Long.compare(this.createdat, reviewObject.createdat);
    }
    /*@Override
    public int compareTo(Review reviewObject) {
        if (this.createdat < reviewObject.createdat)
        {
            return -1; //this object was made earlier than the second object.
        }
        else if (this.createdat > reviewObject.createdat){ //this object was made later than the second object
            return 1;
        }
        else {
            return 0; //they were made at the same time, which is very unlikely, but mathematically not impossible.
        }
    }*/
}
