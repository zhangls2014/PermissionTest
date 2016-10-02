package cn.edu.cwnu.zhangls.permission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE_1 = 10011;

    AppCompatSpinner mSpinner;
    Button mButton;

    String[] permissionStrArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner = (AppCompatSpinner) findViewById(R.id.spinner_permission);
        mButton = (Button) findViewById(R.id.btn_get_permission);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "---" + position + "---", Toast.LENGTH_SHORT).show();
                permissionStrArr = new String[1];
                permissionStrArr[0] = "android.permission." +
                        getResources().getStringArray(R.array.permission_name)[position];
//                permissionStrArr[0] = "android.permission." + mSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MainActivity.this, "---0.0---", Toast.LENGTH_SHORT).show();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermission(permissionStrArr);
            }
        });
    }

    private void getPermission(String[] permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            switch (checkSelfPermission(Manifest.permission.READ_CONTACTS)) {
                case PackageManager.PERMISSION_GRANTED:
                    // 已有授权
                    Toast.makeText(MainActivity.this, "您已经拥有该权限", Toast.LENGTH_SHORT).show();
                    break;
                case PackageManager.PERMISSION_DENIED:
                    // 没有权限：尚未请求过权限，或者请求授权被拒绝，或者曾经授权过，
                    // 但被用户在设置中禁用权限
                    requestPermissions(permission, PERMISSION_REQUEST_CODE_1);
                    break;
                default:
                    // 其实只会返回上述两种情况
                    break;
            }
        } else {
            Toast.makeText(MainActivity.this, "您的手机系统版本过低，不能获取运行时权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE_1) {
            if (permissions.length == 1 &&
                    permissions[0].equals(permissionStrArr[0]) &&
                    grantResults.length == 1) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权请求被通过，读取通讯录
                    Toast.makeText(MainActivity.this, "您的授权请求已经通过", Toast.LENGTH_SHORT).show();
                } else {
                    // 授权请求被拒绝
                    Toast.makeText(MainActivity.this, "您的授权请求被拒绝", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 其他情况
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }
}
