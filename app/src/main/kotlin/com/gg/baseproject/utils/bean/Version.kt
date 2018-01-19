package com.gg.baseproject.utils.bean
import com.google.gson.annotations.SerializedName


/**
 *  Creator : GG
 *  Time    : 2017/12/19
 *  Mail    : gg.jin.yu@gmail.com
 *  Explain :
 */

data class Version(
		@SerializedName("apkForce") var apkForce: Int? = 0, //0
		@SerializedName("apkId") var apkId: String? = "", //1
		@SerializedName("apkKey") var apkKey: String? = "", //anpxdAndroidErpApp
		@SerializedName("apkName") var apkName: String? = "", //启辕汽车
		@SerializedName("apkRemark") var apkRemark: String? = "", //启辕汽车（安卓）
		@SerializedName("apkSeq") var apkSeq: Int? = 0, //1
		@SerializedName("apkState") var apkState: Int? = 0, //1
		@SerializedName("apkUrl") var apkUrl: String? = "", //http://
		@SerializedName("apkVersion") var apkVersion: String? = "" //1.0.0
)