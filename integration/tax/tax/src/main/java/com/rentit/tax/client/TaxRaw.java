package com.rentit.tax.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class TaxRaw {

  private String vunp;
  private String vnaimp;
  private String vnaimk;
  private String vpadres;
  private String dreg;
  private String nmns;
  private String vmns;
  private String ckodsost;
  private String vkods;
  private String dlikv;
  private String vlikv;

  public String getVunp() {
    return vunp;
  }

  public void setVunp(String vunp) {
    this.vunp = vunp;
  }

  public String getVnaimp() {
    return vnaimp;
  }

  public void setVnaimp(String vnaimp) {
    this.vnaimp = vnaimp;
  }

  public String getVnaimk() {
    return vnaimk;
  }

  public void setVnaimk(String vnaimk) {
    this.vnaimk = vnaimk;
  }

  public String getVpadres() {
    return vpadres;
  }

  public void setVpadres(String vpadres) {
    this.vpadres = vpadres;
  }

  public String getDreg() {
    return dreg;
  }

  public void setDreg(String dreg) {
    this.dreg = dreg;
  }

  public String getNmns() {
    return nmns;
  }

  public void setNmns(String nmns) {
    this.nmns = nmns;
  }

  public String getVmns() {
    return vmns;
  }

  public void setVmns(String vmns) {
    this.vmns = vmns;
  }

  public String getCkodsost() {
    return ckodsost;
  }

  public void setCkodsost(String ckodsost) {
    this.ckodsost = ckodsost;
  }

  public String getVkods() {
    return vkods;
  }

  public void setVkods(String vkods) {
    this.vkods = vkods;
  }

  public String getDlikv() {
    return dlikv;
  }

  public void setDlikv(String dlikv) {
    this.dlikv = dlikv;
  }

  public String getVlikv() {
    return vlikv;
  }

  public void setVlikv(String vlikv) {
    this.vlikv = vlikv;
  }
}