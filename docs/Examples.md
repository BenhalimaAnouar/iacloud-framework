
import java.io.IOException;
import java.util.List;
import loadbalancig.AdaptiveBroker;
import loadbalancig.CustomCloudlet;
import loadbalancig.CustomCloudletsTableBuilder;
import org.cloudsimplus.core.CloudSimPlus;
import org.cloudsimplus.datacenters.DatacenterSimple;
import org.cloudsimplus.hosts.HostSimple;
import org.cloudsimplus.resources.PeSimple;
import org.cloudsimplus.utilizationmodels.UtilizationModelDynamic;
import org.cloudsimplus.vms.VmSimple;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/**
 *
 * @author aaaaa
 */
public class Test {

    private static AdaptiveBroker broker0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        // TODO code application logic here

//Creates a CloudSimPlus object to initialize the simulation.
        var simulation = new CloudSimPlus();

        broker0 = new AdaptiveBroker(simulation,null);
//Creates a Broker that will act on behalf of a cloud user (customer).

        long ram = 1000000; //in Megabytes
        long storage = 100000; //in Megabytes
        long bw = 100000; //in Megabits/s

//Creates one host with a specific list of CPU cores (PEs).
//Uses a PeProvisionerSimple by default to provision PEs for VMs
//Uses ResourceProvisionerSimple by default for RAM and BW provisioning
//Uses VmSchedulerSpaceShared by default for VM scheduling
        var host0 = new HostSimple(ram, bw, storage, List.of(new PeSimple(10000)));
        var host1 = new HostSimple(ram, bw, storage, List.of(new PeSimple(10000)));
//Creates a Datacenter with a list of Hosts.
//Uses a VmAllocationPolicySimple by default to allocate VMs
        var dc0 = new DatacenterSimple(simulation, List.of(host0));
        var dc1 = new DatacenterSimple(simulation, List.of(host0));

        var dc2 = new DatacenterSimple(simulation, List.of(host1));
//Creates one VM with one CPU core to run applications.
//Uses a CloudletSchedulerTimeShared by default to schedule Cloudlets
        var vm0 = new VmSimple(1000, 1);
        vm0.setRam(1000).setBw(1000).setSize(1000);

        var vm1 = new VmSimple(1000, 1);
        vm1.setRam(10).setBw(100).setSize(100);

//Creates Cloudlets that represent applications to be run inside a VM.
//It has a length of 1000 Million Instructions (MI) and requires 1 CPU core
//UtilizationModel defining the Cloudlets use only 50% of any resource all the time
        var utilizationModel = new UtilizationModelDynamic(0.5);
        var cloudlet0 = new CustomCloudlet(100000, 1);
        var cloudlet1 = new CustomCloudlet(1000000, 1);
        var cloudlet2 = new CustomCloudlet(100000, 1);
        var cloudlet3 = new CustomCloudlet(1000000, 1);
        var cloudlet4 = new CustomCloudlet(100000, 1);
        var cloudlet5 = new CustomCloudlet(1000000, 1);
        var cloudletList = List.of(cloudlet0, cloudlet1, cloudlet2, cloudlet3, cloudlet4, cloudlet5);

        broker0.submitVmList(List.of(vm0, vm1));
        broker0.submitCloudletList(cloudletList);


        /*Starts the simulation and waits all cloudlets to be executed, automatically
stopping when there is no more events to process.*/
         simulation.start();

        //Function<Cloudlet, Object> cloudletTypeFunction = cloudlet -> cloudlet.getBroker();
        // 2. Define the custom TableColumn object
        // You can specify title, alignment, and type (optional).
        // TableColumn customColumn = new MarkdownTableColumn("--------Policy-------");
        /*Prints the results when the simulation is over
(you can use your own code here to print what you want from this cloudlet list).*/
        new CustomCloudletsTableBuilder(broker0.getCloudletFinishedList())
                .build();
    }

}
