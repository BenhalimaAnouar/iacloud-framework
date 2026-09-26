/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
/*



 */
package io.iacloud.lb.factory;

import io.iacloud.lb.controller.RoundRobinBroker;

import io.iacloud.lb.controller.VmSelectionPolicy;
import  io.iacloud.lb.controller.JoinIdleQueueBroker;
import io.iacloud.lb.loadbalancing.PolicySupplier;



import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Factory and registry for IA-Cloud VM selection policies.
 *
 * <p>The factory stores all built-in load balancing policies and also allows
 * developers to register their own custom policies without modifying
 * the IA-Cloud framework source code.</p>
 *
 * Built-in usage:
 *
 * <pre>
 * VmSelectionPolicy policy =
 *     LoadBalancerFactory.create(LoadBalancingPolicy.ROUND_ROBIN);
 * </pre>
 *
 * Custom usage:
 *
 * <pre>
 * LoadBalancerFactory.register("MyPolicy", MyPolicy::new);
 * VmSelectionPolicy policy =
 *     LoadBalancerFactory.create("MyPolicy");
 * </pre>
 *
 * @author Anouar Benhalima
 * @version 1.0.0
 */
public final class LoadBalancerFactory {

    /**
     * Registry containing policy names and their suppliers.
     */
    private static final Map<String, PolicySupplier> POLICIES =
            new HashMap<>();

    static {
        registerBuiltInPolicies();
    }

    private LoadBalancerFactory() {
        throw new UnsupportedOperationException(
                "LoadBalancerFactory cannot be instantiated."
        );
    }

    /**
     * Registers all built-in IA-Cloud policies.
     */
    private static void registerBuiltInPolicies() {

        register(
                LoadBalancingPolicy.ROUND_ROBIN,
                RoundRobinBroker::new
        );

        register(
                LoadBalancingPolicy.JIQ,
                JoinIdleQueueBroker::new
        );



        /*
         * Add your other built-in policies here.
         *
         * Example:
         *
         * register(
         *      LoadBalancingPolicy.ACTIVE_CLUSTERING,
         *      ActiveClusteringPolicy::new
         * );
         */
    }

    /**
     * Registers a new policy.
     *
     * <p>This method can be used by framework developers to register
     * built-in algorithms and by external users to register their
     * own custom algorithms.</p>
     *
     * @param name the unique policy name
     * @param supplier the policy supplier
     */
    public static void register(
            String name,
            PolicySupplier supplier
    ) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Policy name cannot be null or empty."
            );
        }

        if (supplier == null) {
            throw new IllegalArgumentException(
                    "Policy supplier cannot be null."
            );
        }

        POLICIES.put(
                normalize(name),
                supplier
        );
    }

    /**
     * Creates a policy instance from its registered name.
     *
     * @param name the policy name
     * @return a new policy instance
     */
    public static VmSelectionPolicy create(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Policy name cannot be null or empty."
            );
        }

        PolicySupplier supplier =
                POLICIES.get(normalize(name));

        if (supplier == null) {
            throw new IllegalArgumentException(
                    "Unknown load balancing policy: " + name
                            + ". Registered policies: "
                            + getRegisteredPolicyNames()
            );
        }

        return supplier.create();
    }

    /**
     * Checks whether a policy exists in the registry.
     *
     * @param name the policy name
     * @return true if the policy is registered
     */
    public static boolean exists(String name) {

        if (name == null || name.isBlank()) {
            return false;
        }

        return POLICIES.containsKey(
                normalize(name)
        );
    }

    /**
     * Returns the registered policy names.
     *
     * @return unmodifiable set of policy names
     */
    public static Set<String> getRegisteredPolicyNames() {
        return Collections.unmodifiableSet(
                POLICIES.keySet()
        );
    }

    /**
     * Normalizes policy names to avoid case mismatch.
     *
     * @param name policy name
     * @return normalized name
     */
    private static String normalize(String name) {
        return name.trim().toLowerCase();
    }
}


/*

public class LoadBalancerFactory {

  public static VmSelectionPolicy getFactoryPolicy(int policy) {

    switch (policy) {
      case LoadBalancingPolicy.ROUND_ROBIN -> {
        return new RoundRobinBroker();
      }
      case LoadBalancingPolicy.DYNAMIC_ROUND_ROBIN -> {
        return new DynamicRR();
      }
      case LoadBalancingPolicy.MAX_MIN -> {
        return new MaxMinBroker();
      }
      case LoadBalancingPolicy.MIN_MIN -> {
        return new MinMinBroker();
      }

      case LoadBalancingPolicy.JOIN_IDLE_QUEUE -> {
        return new JoinIdleQueueBroker();
      }

      case LoadBalancingPolicy.W_ROUND_ROBIN -> {
        return new WeightedRoundRobinBroker(null);
      }

      case LoadBalancingPolicy.HONEYBEE -> {
        return new HoneyBeeBroker(0);
      }

      case LoadBalancingPolicy.ANT_COLONY -> {
        return new AntColonyBroker(0);
      }

      case LoadBalancingPolicy.ACTIVE_CLUSTERING -> {
        return new ActiveClusteringBroker();
      }

      case LoadBalancingPolicy.PALB -> {
        return new PALBBroker(0);
      }

      default -> throw new IllegalArgumentException("Umplemented Policy: " + policy);
    }
  }

  public static String autoSelect() {

    String tmp = null;
    try {
      ConfigManager ctx = new ConfigManager("");

      ctx.getString("auto");

    } catch (IOException ex) {
      System.err.println("");
    }

    return tmp;
  }








}
*/

