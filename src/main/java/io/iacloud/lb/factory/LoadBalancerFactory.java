/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
/*



 */
package io.iacloud.lb.factory;

import io.iacloud.lb.controller.RoundRobinBroker;
import io.iacloud.lb.controller.AntColonyBroker;
import io.iacloud.lb.controller.MinMinBroker;
import io.iacloud.lb.controller.PALBBroker;
import io.iacloud.lb.controller.HoneyBeeBroker;
import io.iacloud.lb.controller.JoinIdleQueueBroker;
import io.iacloud.lb.controller.MaxMinBroker;
import io.iacloud.lb.controller.ActiveClusteringBroker;
import io.iacloud.lb.controller.DynamicRR;
import io.iacloud.lb.controller.WeightedRoundRobinBroker;
import io.iacloud.lb.controller.VmSelectionPolicy;
import java.io.IOException;


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
