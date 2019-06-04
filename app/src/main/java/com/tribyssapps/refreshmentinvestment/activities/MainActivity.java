package com.tribyssapps.refreshmentinvestment.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.tribyssapps.refreshmentinvestment.R;
import com.tribyssapps.refreshmentinvestment.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addNewFragment(new MainFragment());
    }

   public void addNewFragment(Fragment fragment){
        if (fragment instanceof MainFragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commitAllowingStateLoss();}
        else{

            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).addToBackStack("report").commitAllowingStateLoss();}

            }




}
