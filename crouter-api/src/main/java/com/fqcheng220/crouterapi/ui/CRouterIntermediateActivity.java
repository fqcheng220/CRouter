package com.fqcheng220.crouterapi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.fqcheng220.crouterapi.CRouter;
import com.fqcheng220.crouterapi.Response;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: ${todo}(用一句话描述该文件做什么)
 * @date 2019/10/24 17:21
 */
public class CRouterIntermediateActivity extends FragmentActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Toast.makeText(this,"this is CRouterIntermediateActivity",Toast.LENGTH_LONG).show();
    CRouter.Builder.create(getIntent().getDataString()).build().startObservable(this).subscribe(new Observer<Response>() {
      @Override public void onSubscribe(Disposable d) {

      }

      @Override public void onNext(Response response) {

      }

      @Override public void onError(Throwable e) {
        //不需要返回任何结果
        CRouterIntermediateActivity.this.finish();
      }

      @Override public void onComplete() {
        //不需要返回任何结果
        CRouterIntermediateActivity.this.finish();
      }
    });
  }
}
