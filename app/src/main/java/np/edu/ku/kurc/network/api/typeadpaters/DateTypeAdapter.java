package     np.edu.ku.kurc.network.api.typeadpaters;

import android.support.annotation.Nullable;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import np.edu.ku.kurc.utils.DateUtils;

public class DateTypeAdapter extends TypeAdapter<Date> {

    /**
     * Date Format for type adapter.
     */
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        out.value(DateUtils.toString(value,sdf));
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        String dateString = in.nextString();

        return DateUtils.fromString(dateString,sdf);
    }
}
