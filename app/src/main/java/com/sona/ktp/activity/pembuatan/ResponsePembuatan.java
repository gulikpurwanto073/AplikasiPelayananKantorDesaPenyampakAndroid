package com.sona.ktp.activity.pembuatan;

import com.google.gson.annotations.SerializedName;
import com.sona.ktp.activity.pembuatan.SemuapembuatanItem;

import java.util.List;

public class ResponsePembuatan {

	@SerializedName("semuapembuatan")
	private List<SemuapembuatanItem> semuapembuatan;

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private String message;

	public void setSemuaBarang(List<SemuapembuatanItem> semuapembuatan){
		this.semuapembuatan = semuapembuatan;
	}

	public List<SemuapembuatanItem> getSemuapembuatan(){
		return semuapembuatan;
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
			"ResponseBarang{" +
			"semuapembuatan = '" + semuapembuatan + '\'' +
			",error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}