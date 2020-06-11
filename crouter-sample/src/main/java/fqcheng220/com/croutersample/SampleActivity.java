package fqcheng220.com.croutersample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.fqcheng220.crouterannotation.generated.CRouter$$OtherRouteServiceImp;
import com.fqcheng220.crouterapi.CRouter;
import com.fqcheng220.crouterapi.Response;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2020/6/11 16:14
 */
public class SampleActivity extends Activity implements View.OnClickListener{
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test0);

    CRouter.init("test");
    new CRouter$$OtherRouteServiceImp().doWhenCRouterInit();

    findViewById(R.id.btn1).setOnClickListener(this);
    findViewById(R.id.btn2).setOnClickListener(this);
    findViewById(R.id.btn3).setOnClickListener(this);
  }

  @Override public void onClick(View v) {
    switch (v.getId()){
      case R.id.btn1:
        CRouter.Builder.create("test://test/otherModule/path2").with("chaxun","hello").build().startObservable(this).subscribe(new Observer<Response>() {
          @Override public void onSubscribe(Disposable d) {

          }

          @Override public void onNext(Response response) {
            Toast.makeText(SampleActivity.this,(response.mResultIntent.getStringExtra("from") + "," +  response.mResultIntent.getStringExtra("chaxun")),Toast.LENGTH_LONG).show();
          }

          @Override public void onError(Throwable e) {
            Toast.makeText(SampleActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
          }

          @Override public void onComplete() {

          }
        });
        break;
      case R.id.btn2:
        CRouter.Builder.create("test://test/otherModule/path3").build().startObservable(this).subscribe(new Observer<Response>() {
          @Override public void onSubscribe(Disposable d) {

          }

          @Override public void onNext(Response response) {
            Toast.makeText(SampleActivity.this,response.toString(),Toast.LENGTH_LONG).show();
          }

          @Override public void onError(Throwable e) {
            Toast.makeText(SampleActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
          }

          @Override public void onComplete() {

          }
        });
        break;
      case R.id.btn3:
        CRouter.Builder.create("test://test/otherModule/pathRandom").build().startObservable(this).subscribe(new Observer<Response>() {
          @Override public void onSubscribe(Disposable d) {

          }

          @Override public void onNext(Response response) {
            Toast.makeText(SampleActivity.this,response.toString(),Toast.LENGTH_LONG).show();
          }

          @Override public void onError(Throwable e) {
            Toast.makeText(SampleActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
          }

          @Override public void onComplete() {

          }
        });
        break;
    }
  }
}
