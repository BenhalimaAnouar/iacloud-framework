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

 /**
     * Selects the most suitable VM for the given Cloudlet.
     *
     * @param cloudlet the Cloudlet that needs to be assigned
     * @param vmList the list of available VMs
     * @return the selected VM
     */
  Vm selectVmForCloudlet(Cloudlet cloudlet, List<Vm> vmList);
}
