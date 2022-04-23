package pan.bo.yu.my_2022_04_20_test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import pan.bo.yu.my_2022_04_20_test.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {
    MyViewModel myViewModel;
    ActivityMain2Binding mbinding;
    FirebaseUser user;
    //授權
    FirebaseAuth mAuth;
    String otpid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_main2);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        mbinding.setMyViewModelXML(myViewModel);
        mbinding.setLifecycleOwner(this);

        mAuth=FirebaseAuth.getInstance();

        mbinding.bget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNumber();
            }
        });

    }

    //取得手機號碼並傳送認證碼
    public void getNumber(){
        String sPhone ="+886"+mbinding.editA.getText().toString();

        ///第一code版塊  發送手機號碼
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(sPhone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        ///第二code版塊
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                          //這個會回傳 官方所設定的認證碼~ 就是解答的意思
                                          @Override
                                          public void onCodeSent(@NonNull String verificationId,
                                                                 @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                              otpid = verificationId;
                                              //mResendToken = token;
                                          }

                                          @Override
                                          public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                          }
                                          //發送成功
                                          @Override
                                          public void onVerificationFailed(@NonNull FirebaseException e) {
                                              //發送失敗
                                              Toast.makeText(getApplicationContext(), "發送失敗", Toast.LENGTH_SHORT).show();
                                          }

                                      }
                        )          // OnVerificationStateChangedCallbacks
                        .build();

        //主要執行入口 上面只是設定配置
        PhoneAuthProvider.verifyPhoneNumber(options);



    }

    //核對OTPID
    public void checkOtpid(View view){
        //參數是otpid 跟 edittext 對比
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid,mbinding.editB.getText().toString());
        //再來登入
        signInWithPhoneAuthCredential(credential);
    }
    //開發者跳轉
    public void JumpTurn(View view) {
        startActivity(new Intent().setClass(MainActivity2.this, MainActivity3.class));
    }

    //返回鍵離開
    public boolean onKeyDown(int keyCode, KeyEvent event) {
// 按下鍵盤上返回按鈕
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setMessage("確定退出系統嗎？")
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                }
                            })
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    //finish(); 關閉單個action
                                    finishAffinity(); //關閉全部APP
                                }
                            }).show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                             user = task.getResult().getUser();

                            Toast.makeText(getApplicationContext(), "登入成功", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent().setClass(MainActivity2.this, MainActivity3.class));

                            // Update UI
                        } else {
                            Toast.makeText(getApplicationContext(), "認證失敗", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}