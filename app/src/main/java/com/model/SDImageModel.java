package com.model;

//照片 的基本信息
public class SDImageModel{
	public String sdImage;//照片的路径 例如： /sd/20170806/80003016_20170806_105001.jpg, /sd/20170806/80003016_20170806_104950.jpg,
	public boolean checked; //判断照片是否被选中
	public SDImageModel()
	{
		checked = false;
	}
	public String getSdImageUrl(DevModel model){
		return "http://"+model.ip+":"+model.httpport+""+sdImage;
	}
	public String getSdImageName(){
		String[] array = sdImage.split("/");
		return array[array.length-1];
	}
	//判断照片是否是jpg格式
	public boolean isSDImage(){
		return sdImage.contains("jpg");
	}
}