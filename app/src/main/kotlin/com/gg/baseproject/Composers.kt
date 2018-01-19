package com.gg.baseproject

import com.anpxd.baselibrary.utils.AppManager
import com.anpxd.ewalker.utils.GsonUtil
import com.anpxd.framelibrary.utils.DelegatesExt
import com.anpxd.net.exception.ApiException
import com.anpxd.net.exception.ExceptionFactory
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 *  Creator : GG
 *  Time    : 2017/11/13
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */
object Composers {
    private val TAG = "Composers"

    fun <T> handleError(): ObservableTransformer<Response<T>, T> {
        return ObservableTransformer { observable ->
            observable.map { responseModel: Response<T>? ->
                if (responseModel != null && responseModel.code == 1 && responseModel.result != null) {
                    responseModel.result!!
                } else {
                    throw ExceptionFactory.ServerException(responseModel?.code ?: -1, responseModel?.msg ?: "网络不给力")
                }
            }
                    .onErrorResumeNext { throwable: Throwable ->
                        Observable.error(ExceptionFactory.create(throwable))
                    }
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { e: Throwable ->
                        (e as? ApiException)?.apply {
                            // toast 前， 确保在 UI 线程 !!!
//                            Toast.makeText(App.instance, this.errorMsg, Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    /**
     * 普通的 ResponseBody 格式(非json), 处理 404 等 errorCode
     */
    fun <T> composeWithoutResponse(): ObservableTransformer<T, T> {
        return ObservableTransformer { observable ->
            observable.onErrorResumeNext { throwable: Throwable ->
                Observable.error(ExceptionFactory.create(throwable))
            }
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 接口数据缓存策略:
     *      1.检查缓存数据, 若有则先显示缓存
     *      2.回调网络数据, 若成功则覆盖先前的缓存数据
     *      3.拉取数据成功, 缓存数据到本地
     *
     * @param key 接口标识, 如: "接口名" + "请求参数(Json格式)"
     */
    inline fun <reified T> cache(key: String): ObservableTransformer<in T, out T>? {
        return ObservableTransformer { observable ->
            Observable.create<T> { emitter ->
                // 1.检查缓存数据, 若有则先显示缓存
                var localData by DelegatesExt.preference(AppManager.getInstance().currentActivity(), key, "")
//                val localData = CXCacheFileManager.get(CXBaseApplication.INSTANCE).getAsObject(key)
                if (localData != "") {
                    emitter.onNext(GsonUtil.jsonToBean(localData, T::class.java)!!)
                }

                // 2.回调网络数据, 若成功则覆盖先前的缓存数据
                observable.subscribe({ data ->
                    emitter.onNext(data)

                    // 3.拉取数据成功, 缓存数据到本体
                    localData = GsonUtil.toJson(data!!)
//                    CXCacheFileManager.get(CXBaseApplication.INSTANCE).put(key, data as Serializable)

                }, { error ->
                    emitter.onError(error)

                }, {
                    emitter.onComplete()
                })
            }
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}