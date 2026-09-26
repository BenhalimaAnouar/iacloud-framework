/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.iacloud.lb.loadbalancing;

import io.iacloud.lb.controller.VmSelectionPolicy;


/**
 * Functional interface used by {@link LoadBalancerFactory}
 * to create new instances of VM selection policies.
 *
 * <p>This allows the framework to register built-in policies and
 * user-defined policies dynamically.</p>
 *
 * Example:
 *
 * <pre>
 * LoadBalancerFactory.register("MyPolicy", MyPolicy::new);
 * </pre>
 *
 * @author Anouar Benhalima
 * @version 1.0.0
 */
@FunctionalInterface
public interface PolicySupplier {

    /**
     * Creates a new VM selection policy instance.
     *
     * @return a new policy instance
     */
    VmSelectionPolicy create();
}
