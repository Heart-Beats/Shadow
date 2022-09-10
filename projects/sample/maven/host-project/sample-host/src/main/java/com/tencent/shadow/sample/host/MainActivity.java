package com.tencent.shadow.sample.host;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hl.shadow.Shadow;
import com.hl.shadow.lib.ShadowConstants;
import com.tencent.shadow.dynamic.host.EnterCallback;
import com.tencent.shadow.dynamic.host.PluginManager;

import java.io.File;

public class MainActivity extends Activity {

    public static final int FROM_ID_START_ACTIVITY = 1001;
    public static final int FROM_ID_CALL_SERVICE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        textView.setText("宿主App");

        Button button = new Button(this);
        button.setText("启动 2.2.1 打包插件");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);//防止点击重入

                Bundle bundle = new Bundle();
                //  插件 zip 的路径
                String pluginSavePath =
                        new File(MainActivity.this.getExternalFilesDir(null), "plugins/plugin-release.zip").getAbsolutePath();

                String pluginZipPath =
                        _AssetsUtilKt.copyAssets2Path(MainActivity.this, "plugins/plugin-release.zip", pluginSavePath, true);

                bundle.putString(ShadowConstants.KEY_PLUGIN_ZIP_PATH, pluginZipPath);

                startPlugin(v, bundle);

            }
        });

        Button button2 = new Button(this);
        button2.setText("启动 89a753c5 打包插件");
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);//防止点击重入

                Bundle bundle = new Bundle();
                //  插件 zip 的路径
                String pluginSavePath =
                        new File(MainActivity.this.getExternalFilesDir(null), "plugins/plugin-89a753c5-release.zip").getAbsolutePath();

                String pluginZipPath =
                        _AssetsUtilKt.copyAssets2Path(MainActivity.this, "plugins/plugin-89a753c5-release.zip", pluginSavePath, true);

                bundle.putString(ShadowConstants.KEY_PLUGIN_ZIP_PATH, pluginZipPath);

                startPlugin(v, bundle);

            }
        });

        linearLayout.addView(textView);
        linearLayout.addView(button);
        linearLayout.addView(button2);

        Button callServiceButton = new Button(this);
        callServiceButton.setText("调用插件Service，结果打印到Log");
        callServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);//防止点击重入

                // PluginManager pluginManager = InitApplication.getPluginManager();
                // pluginManager.enter(MainActivity.this, FROM_ID_CALL_SERVICE, null, null);
            }
        });

        linearLayout.addView(callServiceButton);

        setContentView(linearLayout);
    }

    private void startPlugin(View v, Bundle bundle) {
        //启动插件中的对应的 Activity
        bundle.putString(
                ShadowConstants.KEY_CLASSNAME,
                "com.google.samples.apps.sunflower.GardenActivity");

        // partKey 每个插件都有自己的 partKey 用来区分多个插件，需要与插件打包脚本中的 packagePlugin{ partKey xxx} 一致
        bundle.putString(ShadowConstants.KEY_PLUGIN_PART_KEY, "sunflower");
        bundle.putLong(ShadowConstants.KEY_FROM_ID, ShadowConstants.FROM_ID_START_ACTIVITY);


        // PluginManager pluginManager = InitApplication.getPluginManager();
        PluginManager pluginManager = Shadow.INSTANCE.getMyPluginManager(MainActivity.this);

        pluginManager.enter(MainActivity.this, ShadowConstants.FROM_ID_START_ACTIVITY, bundle, new EnterCallback() {
            @Override
            public void onShowLoadingView(View view) {
                // MainActivity.this.setContentView(view);//显示Manager传来的Loading页面
            }

            @Override
            public void onCloseLoadingView() {
                //     MainActivity.this.setContentView(linearLayout);
            }

            @Override
            public void onEnterComplete() {
                v.setEnabled(true);
            }
        });
    }
}