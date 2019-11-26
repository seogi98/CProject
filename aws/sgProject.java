//<!-- https://mvnrepository.com/artifact/javax.annotation/javax.annotation-api -->
//<dependency>
//    <groupId>javax.annotation</groupId>
//    <artifactId>javax.annotation-api</artifactId>
//    <version>1.3.1</version>
//</dependency>


package project;
//import javax.annotation.Generated;
//import javax.annotation.processing.Generated;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.AmazonClientException;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3control.AWSS3ControlClientBuilder;
import com.amazonaws.services.ec2.model.DryRunResult;
import com.amazonaws.services.ec2.model.DryRunSupportedRequest;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;

import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.Tag;

import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;

import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.CreateTagsResult;

import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.RebootInstancesResult;



import java.util.Scanner;

public class awsTest {
    /*
     * Cloud Computing, Data Computing Laboratory
     * Department of Computer Science
     * Chungbuk National University
     */
    static AmazonEC2 ec2;
    private static void init() throws Exception {
        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file located at
         * (~/.aws/credentials).
         */
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
        try {
            credentialsProvider.getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);

        }
        ec2 = AmazonEC2ClientBuilder.standard()
            .withCredentials(credentialsProvider)
            .withRegion("us-east-1") /* check the region at AWS console */
            .build();
    }
    public static void main(String[] args) throws Exception {
        init();
        Scanner menu = new Scanner(System.in);
        Scanner id_string = new Scanner(System.in);
        int number = 0;
        boolean isExit=false;
        while(isExit == false)
        {
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("------------------------------------------------------------");
            System.out.println(" Amazon AWS Control Panel using SDK ");
            System.out.println(" ");
            System.out.println(" Cloud Computing, Computer Science Department ");
            System.out.println(" at Chungbuk National University ");
            System.out.println("------------------------------------------------------------");
            System.out.println(" 1. list instance 2. available zones ");
            System.out.println(" 3. start instance 4. available regions ");
            System.out.println(" 5. stop instance 6. create instance ");
            System.out.println(" 7. reboot instance 8. list images ");
            System.out.println(" 99. quit ");
            System.out.println("------------------------------------------------------------");
            System.out.print("Enter an integer: ");
            number = id_string.nextInt();
            switch(number) {

                case 1:
                    listInstances();
                    break;

                case 2:
                    availableZones();
                    break;

                case 3:
                    startInstance();
                    break;

                case 4:
                    availableRegions();
                    break;

                case 5:
                    stopInstance();
                    break;

                case 6:
                    createInstance();
                    break;

                case 7:
                    rebootInstance();
                    break;

                case 8:
                    listImages();
                    break;

                case 99:
                    isExit=true;
                    break;
                default:
                    break;
            }
        }
    }
    /*1. list instance */
    public static void listInstances()
    {
        System.out.println("Listing instances....");
        boolean done = false;
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        System.out.println("Request");
        while(!done) {
            DescribeInstancesResult response = ec2.describeInstances(request);
            System.out.println("Response");
            for(Reservation reservation : response.getReservations()) {
                for(Instance instance : reservation.getInstances()) {
                    System.out.printf(
                            "[id] %s, " +
                            "[AMI] %s, " +
                            "[type] %s, " +
                            "[state] %10s, " +
                            "[monitoring state] %s",
                            instance.getInstanceId(),
                            instance.getImageId(),
                            instance.getInstanceType(),
                            instance.getState().getName(),
                            instance.getMonitoring().getState());                     
                }
                System.out.println();
            }
            request.setNextToken(response.getNextToken());
            if(response.getNextToken() == null) {
                done = true;
            }
        }
    }
    /* available zones */
    public static void availableZones()
    {
        DescribeAvailabilityZonesResult zones_response =
            ec2.describeAvailabilityZones();

        for(AvailabilityZone zone : zones_response.getAvailabilityZones()) {
            System.out.printf(
                    "Found availability zone %s" +
                    "with status %s " +
                    "in region %s\n ",
                    zone.getZoneName(),
                    zone.getState(),
                    zone.getRegionName());
        }
    }
    /* start instance */
    public static void startInstance()
    {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        System.out.println("input start ID : ");
        Scanner scan = new Scanner(System.in);
        String instance_id = scan.nextLine();
        try{
            if(checkIdExist(instance_id,"START"))
            {
                System.out.println("Nonexistent ID, please check your ID");
                return;
            }
            StartInstancesRequest request = new StartInstancesRequest()
                .withInstanceIds(instance_id);
            ec2.startInstances(request);
            System.out.printf("Sucessfully start instance %s",instance_id);
        }
        catch (Exception e) {
            System.out.println("Error occurs while connecting process. please check your connection ");
        }

    }
    /* stop instance */
    public static void stopInstance()
    {
        try{
            final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
            System.out.println("input stop ID : ");
            Scanner scan = new Scanner(System.in);
            String instance_id = scan.nextLine();
            if(checkIdExist(instance_id,"STOP"))
            {
                System.out.println("Nonexistent ID, please check your ID");
                return;
            }
            StopInstancesRequest request = new StopInstancesRequest()
                .withInstanceIds(instance_id);
            ec2.stopInstances(request);
            System.out.printf("Successfully stop instance %s", instance_id);
        }
        catch (Exception e) {
            System.out.println("Error occurs while connecting process. please check your connection ");
        }
    }
    /* available regions */
    public static void availableRegions()
    {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        DescribeRegionsResult regions_response = ec2.describeRegions();

        for(Region region : regions_response.getRegions()) {
            System.out.printf(
                    "Found region %s " +
                    "with endpoint %s\n",
                    region.getRegionName(),
                    region.getEndpoint());
        }
    }
    /* create instance */
    public static void createInstance()
    {
        Scanner scan = new Scanner(System.in);

        final String USAGE =
            "To run this example, supply an instance name and AMI image id\n" +
            "Ex: CreateInstance <instance-name> <ami-image-id>\n";

        System.out.println("Input create name");
        String name = scan.nextLine();
        System.out.println("Input create id");
        String ami_id = scan.nextLine();

        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        RunInstancesRequest run_request = new RunInstancesRequest()
            .withImageId(ami_id)
            .withInstanceType(InstanceType.T1Micro)
            .withMaxCount(1)
            .withMinCount(1);

        RunInstancesResult run_response = ec2.runInstances(run_request);

        String reservation_id = run_response.getReservation().getInstances().get(0).getInstanceId();

        Tag tag = new Tag()
            .withKey("Name")
            .withValue(name);

        CreateTagsRequest tag_request = new CreateTagsRequest()
            .withTags(tag);

        CreateTagsResult tag_response = ec2.createTags(tag_request);

        System.out.printf(
            "Successfully started EC2 instance %s based on AMI %s",
            reservation_id, ami_id);
    }
    /*reboot instance */
    public static void rebootInstance()
    {
        try{
            final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
            System.out.println("input reboot ID : ");
            Scanner scan = new Scanner(System.in);
            String instance_id = scan.nextLine();
            if(checkIdExist(instance_id,"REBOOT"))
            {
                System.out.println("Nonexistent ID, please check your ID");
                return;
            }
            RebootInstancesRequest request = new RebootInstancesRequest()
            .withInstanceIds(instance_id);
            RebootInstancesResult response = ec2.rebootInstances(request);
            System.out.printf("Successfully reboot instance %s", instance_id);
        }
        catch (Exception e) {
            System.out.println("Error occurs while connecting process. please check your connection ");
        }
    }
    /* list images */
    public static void listImages()
    {

    }
    public static boolean checkIdExist(String instance_id,String request_Type)
    {
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        boolean done = false;
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        System.out.println("checking ID process ...");
        while(!done) {
            DescribeInstancesResult response = ec2.describeInstances(request);
            for(Reservation reservation : response.getReservations()) {
                for(Instance instance : reservation.getInstances()) {
                    if(instance_id.equals(instance.getInstanceId()))
                    {
                        return false;
                    }
                }
            }
            request.setNextToken(response.getNextToken());
            if(response.getNextToken() == null) {
                done = true;
            }
        }
        return true;
    }

}

