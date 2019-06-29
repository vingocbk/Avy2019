package com.app.avy.network

import android.content.Context
import android.util.Log
import es.dmoral.toasty.Toasty
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MyObserver(var context: Context) : Observer<Any> {
    val TAG = MyObserver::class.java.simpleName
    override fun onComplete() {
        Log.e(TAG, "onComplete")
    }

    override fun onSubscribe(d: Disposable) {
        Log.e(TAG, "onSubscribe")
    }

    override fun onNext(t: Any) {
        //do something
        //Toasty.success(context, "Thay đổi thành công.", Toasty.LENGTH_SHORT).show()

    }

    override fun onError(e: Throwable) {
        Log.e(TAG, "onError ${e.message}")
        //Toasty.error(context, "Có lỗi xảy ra.", Toasty.LENGTH_SHORT).show()

    }
}