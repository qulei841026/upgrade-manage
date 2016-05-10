package carsmart.upgrade.manager.sample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import carsmart.upgrade.manager.OnConfirmUpgradeListener;
import carsmart.upgrade.manager.Upgrade;
import carsmart.upgrade.manager.UpgradeConfig;
import carsmart.upgrade.manager.UpgradeManager;
import carsmart.upgrade.manager.interceptor.DialogInterceptor;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upgradeVersion();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void upgradeVersion() {
        UpgradeManager upgradeManager = new UpgradeManager(new UpgradeConfig.Builder()
                .setDialogInterceptor(new DialogInterceptor() {
                    @Override
                    public Dialog intercept(String prompt, boolean isForce, final OnConfirmUpgradeListener listener) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setMessage("自定义升级对话框")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        listener.onUpgrade();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        return builder.create();
                    }
                }).setContext(MainActivity.this).create());

        Upgrade upgrade = new Upgrade();

        upgrade.isForce = true;
        upgrade.prompt = "重要升级";

        upgradeManager.setUpgrade(upgrade);

    }
}
