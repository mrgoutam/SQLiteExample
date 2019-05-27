package in.goutamstark.sqliteexample;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    public GroceryAdapter(Context context, Cursor cursor){
            mContext = context;
            mCursor = cursor;
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView, countTextView;
        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textview_name_item);
            countTextView = itemView.findViewById(R.id.textview_amount_item);
        }
    }
    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grocery_item, viewGroup, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int i) {
        if (!mCursor.moveToPosition(i)){
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_NAME));
        String amount = mCursor.getString(mCursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_AMOUNT));



        holder.nameTextView.setText(name);
        holder.countTextView.setText(String.valueOf(amount));

        //For swap functionality and sending to MainActivity
        long id = mCursor.getLong(mCursor.getColumnIndex(GroceryContract.GroceryEntry._ID));
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if (mCursor != null){
            mCursor.close();
        }
        mCursor = newCursor;
        if (newCursor != null){
            notifyDataSetChanged();
        }
    }

}
