package fqcheng220.com.croutersample.route;

import com.fqcheng220.crouterannotation.Host;
import com.fqcheng220.crouterannotation.Path;
import com.fqcheng220.crouterannotation.Query;
import com.fqcheng220.crouterapi.PostCard;
import com.fqcheng220.crouterapi.Response;
import io.reactivex.Observable;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: (用一句话描述该文件做什么)
 * @date 2019/12/24 14:36
 */
@Host(OtherRouteConstants.MODULE_NAME)
public interface OtherRouteService {
  @Path(OtherRouteConstants.PATH1) Observable<Response> toPath1(PostCard postCard);

  @Path(OtherRouteConstants.PATH2)
  Observable<Response> toPath2(PostCard postCard, @Query("chaxun1") String query1);

  @Path(OtherRouteConstants.PATH3)
  void toPath3(PostCard postCard);
}
