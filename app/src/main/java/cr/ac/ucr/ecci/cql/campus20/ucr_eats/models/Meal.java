package cr.ac.ucr.ecci.cql.campus20.ucr_eats.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

@Entity(tableName = "Meal",
        foreignKeys = @ForeignKey(entity = Restaurant.class,
                    parentColumns = "id",
                    childColumns = "restaurant_id",
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE), // Foreign key to Restaurant ID
        indices = {@Index(value={"name"}, unique = true)}) // Unique Meal name
public class Meal implements Parcelable
{
    // Enums workaround since Java's enums have a different nature
    @Ignore
    public static final int BREAKFAST = 0, LUNCH = 1, DINNER = 2;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "restaurant_id")
    private int restaurant_id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "photo")
    private String photo;

    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "price")
    private int price;

    @Ignore
    private int maxServings;

    @Ignore
    private int availableServings;

    public Meal()
    {
        this.setRestaurant_id(0);
        this.setName("");
        this.setPhoto("");
        this.setType(0);
        this.setPrice(0);
        this.setAvailableServings(0);
        this.setMaxServings(0);
    }

    @Ignore
    public Meal(int restaurant_id, String name, String photo, int type, int price)
    {
        this.setRestaurant_id(restaurant_id);
        this.setName(name);
        this.setPhoto(photo);
        this.setType(type);
        this.setPrice(price);
        this.setAvailableServings(0);
        this.setMaxServings(0);
    }

    // Firebase data constructor
    @Ignore
    public Meal(@NonNull DataSnapshot data)
    {
        this.setName(data.child("name").getValue(String.class));
        this.setPhoto(data.child("photo").getValue(String.class));
        this.setPrice(Objects.requireNonNull(data.child("price").getValue(Integer.class)));
        this.setType(Objects.requireNonNull(data.child("type").getValue(Integer.class)));
        this.setAvailableServings(Objects.requireNonNull(data.child("available").getValue(Integer.class)));
        this.setMaxServings(Objects.requireNonNull(data.child("max").getValue(Integer.class)));
    }

    @Ignore
    protected Meal(Parcel in) {
        id = in.readInt();
        restaurant_id = in.readInt();
        name = in.readString();
        photo = in.readString();
        type = in.readInt();
        price = in.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price > 0 ? price : -1;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(restaurant_id);
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeInt(type);
        dest.writeInt(price);
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>()
    {
        @Override
        public Meal createFromParcel(Parcel in)
        {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size)
        {
            return new Meal[size];
        }
    };

    public int getMaxServings() {
        return maxServings;
    }

    public void setMaxServings(int maxServings) {
        this.maxServings = maxServings;
    }

    public int getAvailableServings() {
        return availableServings;
    }

    public void setAvailableServings(int availableServings) {
        this.availableServings = availableServings;
    }
}
