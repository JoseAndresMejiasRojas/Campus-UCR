package cr.ac.ucr.ecci.cql.campus20.IPModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//TODO: Implement CRUD operations using DataAccess helper class.
/**
 * @brief Class that represents a Place database entity.
 * */

public class Place implements Parcelable {

    /*Columns*/
    private int id;
    private String name;
    private String description;
    private String type;
    private int rating;
    private int floor;

    public Place() {
    }

    public Place(int id, String name, String description, String type, int rating, int floor) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.rating = rating;
        this.floor = floor;
    }

    protected Place(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        type = in.readString();
        rating = in.readInt();
        floor = in.readInt();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeInt(rating);
        dest.writeInt(floor);
    }

    /**
     * @param context Current app context.
     * @return List containing all the rows in the table.
     * */
    public static List<Place> getPlacesList(Context context) {
        List<Place> list = new ArrayList<>();
        DataAccess dataAccess = DataAccess.getInstance(context);
        dataAccess.open();
        Cursor cursor = dataAccess.selectAll(DatabaseContract.InterestPoints.PlaceTable.TABLE_NAME);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(new Place(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.InterestPoints.PlaceTable.TABLE_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.InterestPoints.PlaceTable.TABLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.InterestPoints.PlaceTable.TABLE_COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.InterestPoints.PlaceTable.TABLE_COLUMN_TYPE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.InterestPoints.PlaceTable.TABLE_COLUMN_RATING)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.InterestPoints.PlaceTable.TABLE_COLUMN_FLOOR))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        dataAccess.close();
        return list;
    }

    /**
     * Inserts a new row in Place table.
     * @param context Current app context.
     * @return The row ID of the newly inserted row, or -1 if an error occurred.
     * */
    public long insert(Context context){
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.InterestPoints.PlaceTable.TABLE_COLUMN_ID, getId());
        values.put(DatabaseContract.InterestPoints.PlaceTable.TABLE_COLUMN_NAME, getName());
        values.put(DatabaseContract.InterestPoints.PlaceTable.TABLE_COLUMN_DESCRIPTION, getDescription());
        values.put(DatabaseContract.InterestPoints.PlaceTable.TABLE_COLUMN_TYPE, getType());
        values.put(DatabaseContract.InterestPoints.PlaceTable.TABLE_COLUMN_RATING, getRating());
        values.put(DatabaseContract.InterestPoints.PlaceTable.TABLE_COLUMN_FLOOR, getFloor());

        DataAccess dataAccess = DataAccess.getInstance(context);
        dataAccess.open();
        long result = dataAccess.insert(DatabaseContract.InterestPoints.PlaceTable.TABLE_NAME, values);
        dataAccess.close();
        return result;
    }
}
