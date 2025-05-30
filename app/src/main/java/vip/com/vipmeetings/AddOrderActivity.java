package vip.com.vipmeetings;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vip.com.vipmeetings.body.AddOrderRequestBody;
import vip.com.vipmeetings.body.AddOrderResponseBody;
import vip.com.vipmeetings.body.GetProductsRequestBody;
import vip.com.vipmeetings.body.GetProductsResponseBody;
import vip.com.vipmeetings.envelope.SoapAddOrderRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapAddOrderResponseEnvelope;
import vip.com.vipmeetings.envelope.SoapGetProductsRequestEnvelope;
import vip.com.vipmeetings.envelope.SoapGetProductsResponseEnvelope;
import vip.com.vipmeetings.interfaces.MainApi;
import vip.com.vipmeetings.models.Product;
import vip.com.vipmeetings.models.Products;
import vip.com.vipmeetings.models.Status;
import vip.com.vipmeetings.request.AddOrder;
import vip.com.vipmeetings.request.GetProducts;
import vip.com.vipmeetings.utilities.Constants;

/**
 * Created by Srinath on 24/06/17.
 */

public class AddOrderActivity extends BaseActivity {


    @BindView(R.id.rvorder)
    RecyclerView rvorder;


    @BindView(R.id.tvsendorder)
    TextView tvsendorder;

    @BindView(R.id.tvtotal)
    TextView tvtotal;


    List<Product> productList;
    ProductAdapter productAdapter;
    float total = 0f;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addorder);
        ButterKnife.bind(this);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter();
        rvorder.setLayoutManager(new LinearLayoutManager(this));
        rvorder.setAdapter(productAdapter);
        setRetrofit(getApp().getTcpIp());
        onTokenRefresh();
    }

    private void onTokenRefresh() {

        getOrders();
    }

    @OnClick(R.id.ivhome)
    public void onHome(View v) {

        gotoHome();

    }


    @OnClick(R.id.tvback)
    public void onBack(View v) {
        onBackPressed();
    }

    private void getOrders() {

        productList.clear();
        productAdapter.notifyDataSetChanged();
        showPD();
        mainApi = retrofit.create(MainApi.class);

        SoapGetProductsRequestEnvelope soapGetProductsRequestEnvelope =
                new SoapGetProductsRequestEnvelope();

        GetProductsRequestBody getProductsRequestBody = new GetProductsRequestBody();

        GetProducts getProducts = new GetProducts();
        getProducts.setAuthToken(getAuthTokenPref_Mobile());
        getProducts.setA_sCanteenId("");
        getProducts.setA_sMeetingId(getMeetingId());
        getProducts.setA_sCategoryId("");
        getProductsRequestBody.setGetProducts(getProducts);
        soapGetProductsRequestEnvelope.setBody(getProductsRequestBody);

        Observable<SoapGetProductsResponseEnvelope> lClientDetailsObservable =
                mainApi.getProducts(soapGetProductsRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .map(this::onSoapGetProducts)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onProducts, this::onError);
    }

    private Products onSoapGetProducts(SoapGetProductsResponseEnvelope soapGetProductsResponseEnvelope) {


        GetProductsResponseBody getProductsResponseBody = soapGetProductsResponseEnvelope.getBody();
        return getProductsResponseBody.getGetProductsResponse().getGetProductsResult().getProducts();
    }

    private void onProducts(Products products) {


        hidePD();
        if (products != null && products.getProduct() != null) {


            productList.clear();
            productList.addAll(products.getProduct());
            productAdapter.notifyDataSetChanged();

        }

    }


    @OnClick(R.id.tvsendorder)
    public void onSendOrder(View v) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Product product : productList) {

            try {
                if (product != null && product.getQuantity().length() > 0) {


                    try {

                        Integer.parseInt(product.getQuantity());
                        stringBuilder.append("ProductId:=" + product.getProductId().trim() +
                                ":-" + "ProductName:=" + product.getDescription().trim() +
                                ":-" + "CanteenName:=" + product.getCanteenName().trim() +
                                ":-" + "ProductPrice:=" + product.getPrice().trim() +
                                ":-" + "PructCurrency:=" + product.getCurrency().trim()
                                + ":-" + "ProductQuantity:=" + product.getQuantity().trim()
                                + ":-" + "ServeTime:=" + getStarttime().trim());
                        stringBuilder.append(":|");
                    } catch (Exception e) {

                    }
                }

            } catch (Exception e) {

                Constants.e("err", e.toString());
            }
        }
        if (stringBuilder != null && stringBuilder.length() > 0) {

            sendOrder(stringBuilder.substring(0, stringBuilder.length() - 2));
        } else {
            showToast(getString(R.string.someerror));
        }

    }


    private void sendOrder(String text) {

        showPD();
        Constants.e("text" + getMeetingId(), text);
        mainApi = retrofit.create(MainApi.class);

        SoapAddOrderRequestEnvelope soapAddOrderRequestEnvelope =
                new SoapAddOrderRequestEnvelope();

        AddOrderRequestBody addOrderRequestBody = new AddOrderRequestBody();

        AddOrder addOrder = new AddOrder();
        addOrder.setAuthToken(getAuthTokenPref_Mobile());
        addOrder.setA_sMeetingId(getMeetingId());
        addOrder.setA_sProductData(text);

        addOrderRequestBody.setAddOrder(addOrder);
        soapAddOrderRequestEnvelope.setBody(addOrderRequestBody);

        Observable<SoapAddOrderResponseEnvelope> lClientDetailsObservable =
                mainApi.addOrder(soapAddOrderRequestEnvelope);

        lClientDetailsObservable.
                subscribeOn(Schedulers.newThread())
                .map(this::onSoapAddOrder)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAddOrder, this::onError);
    }

    private Status onSoapAddOrder(SoapAddOrderResponseEnvelope soapAddOrderResponseEnvelope) {

        AddOrderResponseBody addOrderResponseBody = soapAddOrderResponseEnvelope.getBody();

        return addOrderResponseBody.getAddOrderResponse().getAddOrderResult().getStatus();
    }

    private void onAddOrder(Status status) {

        hidePD();
        if (status != null) {
            if (status.get_status().equalsIgnoreCase("SUCCESS")) {

                Toast.makeText(this, "Order booked successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SkipRoomActivity.class);
                intent.putExtra(Constants.DATETIMEMODEL, getIntent().getStringExtra(Constants.DATETIMEMODEL));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            } else {

                showToast(getString(R.string.someerror));

            }
        } else {
            showToast(getString(R.string.someerror));

        }
    }


    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {


        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.inflate_addorder, parent,false));
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {


            Product product = productList.get(position);

            holder.tvdes.setText(product.getDescription());

            holder.tvcategory.setText(product.getCategory());
            holder.tvprice.setText(product.getCurrency() + "." + product.getPrice());

            if (product.getQuantity() != null && product.getQuantity().trim().length() > 0) {


                try {

                    if (Integer.parseInt(product.getQuantity()) > 0) {

                        holder.etquantity.setText(product.getQuantity());
                        holder.etsum.setText(String.valueOf(Integer.parseInt(product.getQuantity())
                                * Float.valueOf(product.getPrice())));
                    } else {
                        holder.etquantity.setText("");
                        holder.etsum.setText("");
                    }

                } catch (Exception e) {

                    Constants.e("err", e.toString());
                    holder.etquantity.setText("");
                    holder.etsum.setText("");
                }

            } else {

                holder.etquantity.setText("");
                holder.etsum.setText("");
            }

//
//            holder.etquantity.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//
//                    if(editable.length()>0)
//                    {
//                        productList.get(holder.getAdapterPosition()).setQuantity(
//                                holder.etquantity.getText().toString().trim());
//                        hideKeyboard(holder.etquantity);
//
//                    }else
//                    {
//                        productList.get(holder.getAdapterPosition()).setQuantity("0");
//                    }
//                    getTotalPrice();
//                    notifyDataSetChanged();
//
//                }
//            });


            holder.etquantity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {


                    if (i == EditorInfo.IME_ACTION_DONE) {


                        productList.get(holder.getAdapterPosition()).setQuantity(
                                holder.etquantity.getText().toString().trim());

                        hideKeyboard(holder.etquantity);
                        getTotalPrice();
                        notifyDataSetChanged();
                        return true;
                    }

                    return false;
                }
            });


        }

        public class VH extends RecyclerView.ViewHolder implements TextWatcher {


            @BindView(R.id.tvdes)
            TextView tvdes;

            @BindView(R.id.tvcategory)
            TextView tvcategory;

            @BindView(R.id.tvprice)
            TextView tvprice;

            @BindView(R.id.etquantity)
            EditText etquantity;

            @BindView(R.id.etsum)
            TextView etsum;

            public VH(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                //etquantity.addTextChangedListener(this);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        }


    }

    private void getTotalPrice() {


        total = 0f;
        for (Product product : productList) {

            try {

                if (product != null && product.getQuantity().length() > 0)
                    total += Integer.parseInt(product.getQuantity())
                            * Float.valueOf(product.getPrice());
            } catch (Exception e) {

            }
        }

        tvtotal.setText("Total : " + String.valueOf(total));
    }


}
