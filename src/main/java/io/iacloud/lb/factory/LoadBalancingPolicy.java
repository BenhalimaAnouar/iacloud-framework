/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package io.iacloud.lb.factory;

/**
 * @author Ben Halima Anouar
* @version 1.0.0
 * @since 1.0
 */
public class LoadBalancingPolicy {

  public static String API = "";
  public static  String API_KEY = "";
  public static final int NOT_IMPLEMENTED_YET = 0;
  public static String CUSTOMIZED_API;



public static final String ROUND_ROBIN = "RR";

    public static final String PALB = "PALB";

    public static final String CARTON = "CARTON";

    public static final String ACTIVE_CLUSTERING = "ActiveClustering";

    public static final String MIN_MIN = "MinMin";

    public static final String MAX_MIN = "MaxMin";

    public static final String JIQ = "JoinIdleQueue";

    public static final String CLBVM = "CLBVM";

    public static final String FAMLB = "FAMLB";

    public static final String THROTTLED = "Throttled";

    public static final String HONEY_BEE = "HoneyBeeForaging";

    public static final String ANT_COLONY = "AntColony";

    /**
     * Default fallback policy used when IA-Cloud API prediction fails.
     */
    public static final String DEFAULT_FALLBACK = ROUND_ROBIN;

}
