/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.iacloud.lb.display;

import org.cloudsimplus.cloudlets.CloudletSimple;

/**
 * @author Ben Halima Anouar
* @version 1.0.0
 * @since 1.0
 */
public class CustomCloudlet extends CloudletSimple {

  private String policy;

  public String getPolicy() {
    return policy;
  }

  public void setPolicy(String policy) {
    this.policy = policy;
  }

  public CustomCloudlet(long length, int pesNumber) {
    super(length, pesNumber);
  }
}
