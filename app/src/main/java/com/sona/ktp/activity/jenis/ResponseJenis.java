package com.sona.ktp.activity.jenis;

import com.google.gson.annotations.SerializedName;
import com.sona.ktp.activity.jenis.SemuajenisItem;

import java.util.List;

public class ResponseJenis {

	@SerializedName("semuajenis")
	private List<SemuajenisItem> semuajenis;

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private String message;

	public void setSemuaJenis(List<SemuajenisItem> semuajenis){
		this.semuajenis = semuajenis;
	}

	public List<SemuajenisItem> getSemuajenis(){
		return semuajenis;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return 
			"ResponseJenis{" +
			"semuajenis = '" + semuajenis + '\'' +
			",error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}