package in.goutamstark.sqliteexample;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private EditText mEdittextName;
    private TextView mTextViewAmount;
    private int mAmount = 0;
    private GroceryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GroceryDBHelper helper = new GroceryDBHelper(this);
        mDatabase = helper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GroceryAdapter(this, getAllCursor());
        recyclerView.setAdapter(mAdapter);

        //For Swip Functionality
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);


        mEdittextName = findViewById(R.id.edittext_name);
        mTextViewAmount = findViewById(R.id.textview_amount);

        Button buttonIncreased = findViewById(R.id.button_increase);
        Button buttonDecreased = findViewById(R.id.button_decrease);
        Button buttonAdd = findViewById(R.id.button_add);

        buttonIncreased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bIncreased();
            }
        });

        buttonDecreased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bDecreased();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bAdd();
            }
        });
    }

    private void removeItem(long id){
        mDatabase.delete(GroceryContract.GroceryEntry.TABLE_NAME, GroceryContract.GroceryEntry._ID +"="+id,null);
        mAdapter.swapCursor(getAllCursor());
    }

    private void bIncreased() {
        mAmount++;
        mTextViewAmount.setText(String.valueOf(mAmount));
    }

    private void bDecreased() {
        if (mAmount > 0) {
            mAmount--;
            mTextViewAmount.setText(String.valueOf(mAmount));
        }
    }

    private void bAdd() {
        if (mEdittextName.getText().toString().trim().length() == 0 || mAmount == 0) {
            return;
        }

        String name = mEdittextName.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, name);
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, mAmount);

        mDatabase.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllCursor());
        mEdittextName.getText().clear();
    }

    private Cursor getAllCursor() {
        return mDatabase.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"

                );
    }
}
