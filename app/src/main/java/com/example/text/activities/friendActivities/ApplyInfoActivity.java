package com.example.text.activities.friendActivities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.text.R;
import com.example.text.activities.profileActivities.ProfileActivity;
import com.example.text.adapters.ApplyInfoAdapter;
import com.example.text.apis.ApiService;
import com.example.text.dataModel.ApplyInfoItem;
import com.example.text.dataModel.request.DealWithApplyRequest;
import com.example.text.dataModel.request.UserIdRequest;
import com.example.text.dataModel.response.AvatarResponse;
import com.example.text.dataModel.response.FetchApplyInfoResponse;
import com.example.text.database.AvatarModel;
import com.example.text.retrofits.RetrofitClient;
import com.example.text.utils.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyInfoActivity extends AppCompatActivity implements ApplyInfoAdapter.OnActionClickListener {
    private ImageButton btnBack;
    private RecyclerView recyclerView;
    private ApplyInfoAdapter adapter;
    private List<ApplyInfoItem> requests;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_list);

        // 初始化数据（模拟数据）
        requests = new ArrayList<>();
//        requests.add(new ApplyInfoItem("123","","m","fuck",0));

        btnBack=findViewById(R.id.btn_back);

        // 配置 RecyclerView
        recyclerView = findViewById(R.id.apply_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ApplyInfoAdapter(requests);
        adapter.setOnActionClickListener(this);
        adapter.setOnItemClickListener(position -> {
            ApplyInfoItem item = requests.get(position);
            showApplyDetailDialog(item);
        });
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL
        ));

        fetchApplyInfo();

        btnBack.setOnClickListener(v->finish());
    }

    private void fetchApplyInfo(){
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<FetchApplyInfoResponse> call = apiService.searchApply(new UserIdRequest(Store.getInstance(getApplicationContext()).getUserId()),Store.getInstance(getApplicationContext()).getData("token"));

        call.enqueue(new Callback<FetchApplyInfoResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<FetchApplyInfoResponse> call, @NonNull Response<FetchApplyInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    requests.clear();
                    requests.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ApplyInfoActivity.this, "上传失败: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FetchApplyInfoResponse> call, @NonNull Throwable t) {
                Toast.makeText(ApplyInfoActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 在 Activity/Fragment 中显示对话框
    private void showApplyDetailDialog(ApplyInfoItem message) {
        ApplyDetailDialogFragment dialog = ApplyDetailDialogFragment.newInstance(message);
        dialog.setOnActionClickListener(new ApplyDetailDialogFragment.OnActionClickListener() {
            @Override
            public void onAgree() {
                ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
                Call<Void> call = apiService.dealWithApply(new DealWithApplyRequest(message.getApplyId(),"1"), Store.getInstance(getApplicationContext()).getData("token"));

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        adapter.updateRequestStatus(findPosition(message), 1);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(ApplyInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onReject() {
                ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
                Call<Void> call = apiService.dealWithApply(new DealWithApplyRequest(message.getApplyId(),"2"), Store.getInstance(getApplicationContext()).getData("token"));

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        adapter.updateRequestStatus(findPosition(message), -1);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Toast.makeText(ApplyInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show(getSupportFragmentManager(), "申请详情");
    }

    @Override
    public void onAcceptClick(int position) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Void> call = apiService.dealWithApply(new DealWithApplyRequest(requests.get(position).getApplyId(),"1"), Store.getInstance(getApplicationContext()).getData("token"));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                adapter.updateRequestStatus(position, 1);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(ApplyInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRejectClick(int position) {
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<Void> call = apiService.dealWithApply(new DealWithApplyRequest(requests.get(position).getApplyId(),"2"), Store.getInstance(getApplicationContext()).getData("token"));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                adapter.updateRequestStatus(position, -1);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(ApplyInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int findPosition(ApplyInfoItem applyInfoItem){
        for(int i=0;i<requests.size();++i){
            if(Objects.equals(requests.get(i).getApplyId(), applyInfoItem.getApplyId())){
                return i;
            }
        }
        return -1;
    }
}