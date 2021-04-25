package com.gersy.smartled.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.gersy.smartled.R;

import java.util.List;

public class FragmentHelper {

	public static void addStackChangedListener(FragmentActivity activity, FragmentManager.OnBackStackChangedListener listener) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		fragmentManager.addOnBackStackChangedListener(listener);
	}

	public static void removeStackChangedListener(FragmentActivity activity, FragmentManager.OnBackStackChangedListener listener) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		fragmentManager.removeOnBackStackChangedListener(listener);
	}

	public static void addFragment(FragmentActivity activity, int layoutId, Fragment fragment, String tag) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
		fragmentTransaction.add(layoutId, fragment, tag);
		fragmentTransaction.commit(); // 提交事务
	}

	public static void addAndShowFragment(FragmentActivity activity, int layoutId, Fragment fragment, String tag) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.add(layoutId, fragment, tag);
		fragmentTransaction.show(fragment);
		fragmentTransaction.commit(); // 提交事务
	}

	public static Fragment getCurrFragment(FragmentActivity activity, int layoutId) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(layoutId);
		return fragment;
	}

	/**
	 * @description 替换容器中的fragment
	 * @param fragment
	 *            要替换成的fragment
	 * */
	public static void replaceFragment(FragmentActivity activity, int layoutId, Fragment fragment, String tag) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.addToBackStack(tag);
		fragmentTransaction.replace(layoutId, fragment, tag);
		fragmentTransaction.commitAllowingStateLoss();
	}

	public static void replaceFragmentNotStack(FragmentActivity activity, int layoutId, Fragment fragment, String tag) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out);
		fragmentTransaction.replace(layoutId, fragment, tag);
		// fragmentTransaction.addToBackStack(null);
		// fragmentTransaction.commit();
		fragmentTransaction.commitAllowingStateLoss();
	}

	public static Fragment findFragmentByTag(FragmentActivity activity, String tag) {
		FragmentManager frgtManager = activity.getSupportFragmentManager();
		return frgtManager.findFragmentByTag(tag);
	}

	public static void hideFragment(FragmentActivity activity, Fragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.hide(fragment);
		// fragmentTransaction.commit();
		fragmentTransaction.commitAllowingStateLoss();
	}

	public static void hideFragment(FragmentActivity activity, Fragment fragment, int enterAnim, int outAnim) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.setCustomAnimations(enterAnim, outAnim);
		fragmentTransaction.hide(fragment);
		// fragmentTransaction.commit();
		fragmentTransaction.commitAllowingStateLoss();
	}

	public static void showFragment(FragmentActivity activity, Fragment fragment, int enterAnim, int outAnim) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.setCustomAnimations(enterAnim, outAnim);
		fragmentTransaction.show(fragment);
		// fragmentTransaction.commit();
		fragmentTransaction.commitAllowingStateLoss();
	}

	public static void showFragment(FragmentActivity activity, Fragment fragment) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.show(fragment);
		// fragmentTransaction.commit();
		fragmentTransaction.commitAllowingStateLoss();
	}

	public static void removeFragment(FragmentActivity activity, Fragment fragment, String tag) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (activity.isFinishing()) {
			return;
		}
		fragmentTransaction.remove(fragment);
		// fragmentTransaction.commit();
		fragmentTransaction.commitAllowingStateLoss();
		fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	public static boolean isHaveFragments(FragmentActivity activity) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		int count = fragmentManager.getBackStackEntryCount();
		return count > 0 ? false : true;
	}

	public static List<Fragment> getFragments(FragmentActivity activity) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		List<Fragment> fragments = fragmentManager.getFragments();
		return fragments;
	}

	public static void switchFragment(FragmentActivity fActivity, int resId, Fragment from, Fragment to, String tag) {
		if (to.isAdded()) {
			getFTransaction(fActivity).hide(from).show(to).commit();
			return;
		}
		// getFTransaction(fActivity).add(to, tag).hide(from).show(to).commit();
		getFTransaction(fActivity).add(resId, to, tag).hide(from).show(to).commit();
	}

	private static FragmentTransaction getFTransaction(FragmentActivity fActivity) {
		return fActivity.getSupportFragmentManager().beginTransaction();
	}
}
