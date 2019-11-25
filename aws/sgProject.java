//<!-- https://mvnrepository.com/artifact/javax.annotation/javax.annotation-api -->
//<dependency>
//    <groupId>javax.annotation</groupId>
//    <artifactId>javax.annotation-api</artifactId>
//    <version>1.3.1</version>
//</dependency>


package project;
//import javax.annotation.Generated;
//import javax.annotation.processing.Generated;
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

    }
    /* start instance */
    public static void startInstance()
    {

    }
    /* stop instance */
    public static void stopInstance()
    {

    }
    /* available regions */
    public static void availableRegions()
    {

    }
    /* create instance */
    public static void createInstance()
    {

    }
    /*reboot instance */
    public static void rebootInstance()
    {

    }
    /* list images */
    public static void listImages()
    {

    }

}

