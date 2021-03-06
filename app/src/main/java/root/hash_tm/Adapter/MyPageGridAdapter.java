package root.hash_tm.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import root.hash_tm.Model.BookModel;
import root.hash_tm.Model.IntentModel;
import root.hash_tm.R;
import root.hash_tm.Activity.PoemtryIndexActivity;
import root.hash_tm.Connect.RetrofitClass;
import root.hash_tm.Util.BaseActivity;
import root.hash_tm.Util.UtilClass;

/**
 * Created by root1 on 2017. 9. 24..
 */

public class MyPageGridAdapter extends RecyclerView.Adapter implements Callback<JsonObject> {

    private List<BookModel> data = new ArrayList<>();
    private BaseActivity activity;

    private boolean isMyPoemtry;

    public MyPageGridAdapter(String title, String cookie, BaseActivity activity) {
        this.activity = activity;

        if(title.equals("출간 완료 시집")){
            isMyPoemtry = true;
            RetrofitClass.getInstance().apiInterface
                    .getMyPoemtry(cookie).enqueue(this);
        }else{
            isMyPoemtry = false;
            RetrofitClass.getInstance().apiInterface
                    .getHeartPomtry(cookie).enqueue(this);
        }
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        if(response.code() == 200){
            JsonElement tempData = response.body().get("data");
            Gson gson = new Gson();
            data = Arrays.asList(gson.fromJson(tempData, BookModel[].class));
            notifyDataSetChanged();
        }else{

        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        t.printStackTrace();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_mypage_grid, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder)holder;
        myViewHolder.titleText.setText(data.get(position).getTitle());
        myViewHolder.id = data.get(position).getId();
        if(isMyPoemtry){
            myViewHolder.writerText.setText(data.get(position).getHearts());
        }else{
            myViewHolder.writerText.setText(data.get(position).getWriter());
        }

        UtilClass.getInstance().setImage(activity, data.get(position).getId()+"", myViewHolder.bookCard);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView bookCard;
        TextView titleText, writerText;
        int id;

        public MyViewHolder(View itemView) {
            super(itemView);
            bookCard = (ImageView)itemView.findViewById(R.id.bookCard);
            bookCard.setClipToOutline(true);
            titleText = (TextView)itemView.findViewById(R.id.titleText);
            writerText = (TextView)itemView.findViewById(R.id.writerText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<IntentModel> arrayList = new ArrayList<IntentModel>();
                    arrayList.add(new IntentModel("bookId", id + ""));
                    activity.goNextActivity(PoemtryIndexActivity.class, arrayList);
                }
            });
        }
    }
}
