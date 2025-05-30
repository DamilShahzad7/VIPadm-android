package vip.com.vipmeetings.bluno;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import vip.com.vipmeetings.R;

/**
 * Created by macmini on 29/07/16.
 */
public class BlunoBaseActivity extends AppCompatActivity {



    ProgressDialog progressDialog;
    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog=new ProgressDialog(this, R.style.MyTheme);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

    }


    public void hideKeyboard(View v)
    {

        if(v!=null)
        {


            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
        }


    }


    public void showDialog()
    {

        if(progressDialog!=null && !progressDialog.isShowing())
        {
            progressDialog.show();
        }
    }


    public void hideDialog()
    {

        if(progressDialog!=null && progressDialog.isShowing())
            progressDialog.dismiss();

    }
}
