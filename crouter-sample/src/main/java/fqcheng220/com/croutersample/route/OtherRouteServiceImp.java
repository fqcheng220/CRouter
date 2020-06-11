package fqcheng220.com.croutersample.route;

import android.content.Intent;
import android.widget.Toast;
import com.fqcheng220.crouterannotation.Host;
import com.fqcheng220.crouterannotation.Path;
import com.fqcheng220.crouterannotation.Query;
import com.fqcheng220.crouterapi.PostCard;
import com.fqcheng220.crouterapi.Response;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2019/12/24 14:43
 */
@Host(OtherRouteConstants.MODULE_NAME)
public class OtherRouteServiceImp implements OtherRouteService {
  @Path(OtherRouteConstants.PATH1)
  @Override public Observable<Response> toPath1(PostCard postCard) {
    return Observable.create(new ObservableOnSubscribe<Response>() {
      @Override public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
        emitter.onNext(new Response(0,new Intent().putExtra("from","toPath1")));
      }
    });
  }

  @Path(OtherRouteConstants.PATH2)
  @Override public Observable<Response> toPath2(PostCard postCard, final @Query("chaxun")String query1) {
    return Observable.create(new ObservableOnSubscribe<Response>() {
      @Override public void subscribe(ObservableEmitter<Response> emitter) throws Exception {
        emitter.onNext(new Response(0,new Intent().putExtra("from","toPath2").putExtra("chaxun",query1)));
      }
    });
  }

  @Path(OtherRouteConstants.PATH3)
  @Override public void toPath3(PostCard postCard) {
    Toast.makeText(postCard.mCtx,"toPath3",Toast.LENGTH_LONG).show();
  }
}
