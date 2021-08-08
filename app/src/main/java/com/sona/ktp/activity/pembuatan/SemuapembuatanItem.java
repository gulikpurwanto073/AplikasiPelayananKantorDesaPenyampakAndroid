package com.sona.ktp.activity.pembuatan;

import com.google.gson.annotations.SerializedName;

public class SemuapembuatanItem {

	@SerializedName("nama")
	private String nama;

	@SerializedName("id_pembuatan")
	private String id_pembuatan;

	@SerializedName("tempat_lahir")
	private String tempat_lahir;

	@SerializedName("tanggal_lahir")
	private String tanggal_lahir;

	@SerializedName("jenis_kelamin")
	private String jenis_kelamin;

	@SerializedName("alamat")
	private String alamat;

	@SerializedName("rtrw")
	private String rtrw;

	@SerializedName("keldesa")
	private String keldesa;

	@SerializedName("kecamatan")
	private String kecamatan;

	@SerializedName("agama")
	private String agama;

	@SerializedName("status_pekerjaan")
	private String status_pekerjaan;

	@SerializedName("status_pernikahan")
	private String status_pernikahan;

	@SerializedName("kewarganegaraan")
	private String kewarganegaraan;

	@SerializedName("berlaku_hingga")
	private String berlaku_hingga;

	@SerializedName("nik")
	private String nik;

	@SerializedName("username")
	private String username;

	@SerializedName("waktu")
	private String waktu;

	@SerializedName("status_pembuatan")
	private String status_pembuatan;

	@SerializedName("catatan")
	private String catatan;

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getId_pembuatan() {
		return id_pembuatan;
	}

	public void setId_pembuatan(String id_pembuatan) {
		this.id_pembuatan = id_pembuatan;
	}

	public String getTempat_lahir() {
		return tempat_lahir;
	}

	public void setTempat_lahir(String tempat_lahir) {
		this.tempat_lahir = tempat_lahir;
	}

	public String getTanggal_lahir() {
		return tanggal_lahir;
	}

	public void setTanggal_lahir(String tanggal_lahir) {
		this.tanggal_lahir = tanggal_lahir;
	}

	public String getJenis_kelamin() {
		return jenis_kelamin;
	}

	public void setJenis_kelamin(String jenis_kelamin) {
		this.jenis_kelamin = jenis_kelamin;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getRtrw() {
		return rtrw;
	}

	public void setRtrw(String rtrw) {
		this.rtrw = rtrw;
	}

	public String getKeldesa() {
		return keldesa;
	}

	public void setKeldesa(String keldesa) {
		this.keldesa = keldesa;
	}

	public String getKecamatan() {
		return kecamatan;
	}

	public void setKecamatan(String kecamatan) {
		this.kecamatan = kecamatan;
	}

	public String getAgama() {
		return agama;
	}

	public void setAgama(String agama) {
		this.agama = agama;
	}

	public String getStatus_pekerjaan() {
		return status_pekerjaan;
	}

	public void setStatus_pekerjaan(String status_pekerjaan) {
		this.status_pekerjaan = status_pekerjaan;
	}

	public String getStatus_pernikahan() {
		return status_pernikahan;
	}

	public void setStatus_pernikahan(String status_pernikahan) {
		this.status_pernikahan = status_pernikahan;
	}

	public String getKewarganegaraan() {
		return kewarganegaraan;
	}

	public void setKewarganegaraan(String kewarganegaraan) {
		this.kewarganegaraan = kewarganegaraan;
	}

	public String getBerlaku_hingga() {
		return berlaku_hingga;
	}

	public void setBerlaku_hingga(String berlaku_hingga) {
		this.berlaku_hingga = berlaku_hingga;
	}

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
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

	public String getStatus_pembuatan() {
		return status_pembuatan;
	}

	public void setStatus_pembuatan(String status_pembuatan) {
		this.status_pembuatan = status_pembuatan;
	}

	public String getCatatan() {
		return catatan;
	}

	public void setCatatan(String catatan) {
		this.catatan = catatan;
	}

	@Override
 	public String toString(){
		return 
			"SemuapembuatanItem{" +
			"nama = '" + nama + '\'' +
			",id_pembuatan = '" + id_pembuatan + '\'' +
			",tempat_lahir = '" + tempat_lahir + '\'' +
			",tanggal_lahir = '" + tanggal_lahir + '\'' +
			",jenis_kelamin = '" + jenis_kelamin + '\'' +
			",alamat = '" + alamat + '\'' +
			",rtrw = '" + rtrw + '\'' +
			",keldesa = '" + keldesa + '\'' +
			",kecamatan = '" + kecamatan + '\'' +
			",agama = '" + agama + '\'' +
			",status_pekerjaan = '" + status_pekerjaan + '\'' +
			",status_pernikahan = '" + status_pernikahan + '\'' +
			",kewarganegaraan = '" + kewarganegaraan + '\'' +
			",berlaku_hingga = '" + berlaku_hingga + '\'' +
			",nik = '" + nik + '\'' +
			",username = '" + username + '\'' +
			",waktu = '" + waktu + '\'' +
			",status_pembuatan = '" + status_pembuatan + '\'' +
			",catatan = '" + catatan + '\'' +
			"}";
		}
}