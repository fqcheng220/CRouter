package com.fqcheng220.crouterapi;

public class RxResult {
    private static final String TAG = RxResult.class.getSimpleName();
    private RxResultFragment mRxResultFragment;

    private RxResult(RxResultFragment rxResultFragment){
        mRxResultFragment = rxResultFragment;
    }

//    public static RxResult create(FragmentManager fragmentManager){
//        if(fragmentManager != null){
//            RxResultFragment rxResultFragment = null;
//            for(Fragment fragment:fragmentManager.getFragments()){
//                if(fragment.getTag() == TAG && fragment.getClass() == RxResultFragment.class){
//                    rxResultFragment = (RxResultFragment)fragment;
//                    break;
//                }
//            }
//            if(rxResultFragment == null){
//                rxResultFragment = RxResultFragment.create();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.add(rxResultFragment,TAG);
//                fragmentTransaction.commitNowAllowingStateLoss();
//            }
//            return new RxResult(rxResultFragment);
//        }
//        return null;
//    }
}
