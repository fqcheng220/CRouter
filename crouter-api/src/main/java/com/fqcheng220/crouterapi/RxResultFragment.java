package com.fqcheng220.crouterapi;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import io.reactivex.annotations.Nullable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author fqcheng220
 * @version V1.0
 * @Description: ${todo}(用一句话描述该文件做什么)
 * @date 2019/10/22 10:47
 */
public class RxResultFragment extends Fragment {
  private static final String TAG = RxResultFragment.class.getSimpleName();
  private static final int REQUEST_CODE = 0;
  private BehaviorSubject<Response> mBehaviorSubject;

  private RxResultFragment(){
    mBehaviorSubject = BehaviorSubject.create();
  }
  public static RxResultFragment create(FragmentManager fragmentManager){
    RxResultFragment rxResultFragment = findFragmentByTag(fragmentManager);
    if(rxResultFragment == null){
      if(fragmentManager != null){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        rxResultFragment = new RxResultFragment();
        fragmentTransaction.add(rxResultFragment,TAG);
        fragmentTransaction.commitNowAllowingStateLoss();
      }
    }
    return rxResultFragment;
  }

  private static RxResultFragment findFragmentByTag(FragmentManager fragmentManager){
    RxResultFragment rxResultFragment = null;
    if(fragmentManager != null){
      Fragment fragment = fragmentManager.findFragmentByTag(TAG);
      if(fragment instanceof RxResultFragment){
        rxResultFragment = (RxResultFragment)fragment;
      }
    }
    return rxResultFragment;
  }

  public BehaviorSubject<Response> start(Intent intent){
    startActivityForResult(intent,REQUEST_CODE);
    return mBehaviorSubject;
  }

  @Override public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == REQUEST_CODE){
      mBehaviorSubject.onNext(new Response(resultCode,data));
      mBehaviorSubject.onComplete();
    }
  }
}
