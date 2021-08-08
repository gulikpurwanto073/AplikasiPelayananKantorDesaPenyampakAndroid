package com.sona.ktp.activity.kritiksaran;

import com.google.gson.annotations.SerializedName;
import com.sona.ktp.activity.kritiksaran.SemuakritiksaranItem;

import java.util.List;

public class ResponseKritiksaran {

	@SerializedName("semuakritiksaran")
	private List<SemuakritiksaranItem> semuakritiksaran;

	@SerializedName("error")
	private boolean error;

	@SerializedName("message")
	private String message;

	public void setSemuaKritiksaran(List<SemuakritiksaranItem> semuakritiksaran){
		this.semuakritiksaran = semuakritiksaran;
	}

	public List<SemuakritiksaranItem> getSemuakritiksaran(){
		return semuakritiksaran;
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
			"semuakritiksaran = '" + semuakritiksaran + '\'' +
			",error = '" + error + '\'' + 
			",message = '" + message + '\'' + 
			"}";
		}
}