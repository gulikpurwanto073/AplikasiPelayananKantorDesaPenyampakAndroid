package com.sona.ktp.activity.kritiksaran;

import com.google.gson.annotations.SerializedName;

public class SemuakritiksaranItem {

	@SerializedName("kritiksaran")
	private String kritiksaran;

	@SerializedName("id_kritiksaran")
	private String id_kritiksaran;

	@SerializedName("username")
	private String username;

	@SerializedName("waktu")
	private String waktu;

	public String getKritiksaran() {
		return kritiksaran;
	}

	public void setKritiksaran(String kritiksaran) {
		this.kritiksaran = kritiksaran;
	}

	public String getId_kritiksaran() {
		return id_kritiksaran;
	}

	public void setId_kritiksaran(String id_kritiksaran) {
		this.id_kritiksaran = id_kritiksaran;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWaktu() {
		return waktu;
	}

	public void setWaktu(String waktu) {
		this.waktu = waktu;
	}

	@Override
 	public String toString(){
		return 
			"SemuakritiksaranItem{" +
			"kritiksaran = '" + kritiksaran + '\'' +
			",id_kritiksaran = '" + id_kritiksaran + '\'' +
			",username = '" + username + '\'' +
			",waktu = '" + waktu + '\'' +
			"}";
		}
}