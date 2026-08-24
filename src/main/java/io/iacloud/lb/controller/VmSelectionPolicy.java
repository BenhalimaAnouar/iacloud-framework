/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package io.iacloud.lb.controller;

import java.util.List;
import org.cloudsimplus.cloudlets.Cloudlet;
import org.cloudsimplus.vms.Vm;

/**
 * @author Ben Halima Anouar
* @version 1.0.0
 * @since 1.0
 */
public interface VmSelectionPolicy {

  Vm selectVmForCloudlet(Cloudlet cloudlet, List<Vm> vmList);
}
