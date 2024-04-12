package com.company;
import java.io.*;



import java.net.*;



import java.util.ArrayList;



public class TCPClient {



    //Method for generating an array of given size of random intergers from 0 to 256



    //This method will be removed when the array is input through the client



    public static int[] genArr(int size)



    {



        int[] arr = new int[size];



        for(int i = 0; i < size; i++)



        {



            arr[i] = (int)(Math.random() * 256);



        }



        return arr;



    }



    public static void main(String[] args) throws IOException {







        // Variables for setting up connection and communication



        Socket Socket = null; // socket to connect with ServerRouter



        PrintWriter out = null; // for writing to ServerRouter



        BufferedReader in = null; // for reading form ServerRouter



        InetAddress addr = InetAddress.getLocalHost();



        String host = addr.getHostAddress(); // Client machine's IP



        String routerName = "10.78.152.19"; // ServerRouter host name



        int SockNum = 5555; // port number







        // Tries to connect to the ServerRouter



        try {



            Socket = new Socket(routerName, SockNum);



            out = new PrintWriter(Socket.getOutputStream(), true);



            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));



        }



        catch (UnknownHostException e) {



            System.err.println("Don't know about router: " + routerName);



            System.exit(1);



        }



        catch (IOException e) {



            System.err.println("Couldn't get I/O for the connection to: " + routerName);



            System.exit(1);



        }







        // Variables for message passing



        String fromServer; // messages received from ServerRouter



        String fromUser; // messages sent to ServerRouter



        String address ="10.78.128.241"; // destination IP (Server)



        long t0, t1, t;







        // Communication process (initial sends/receives



        out.println(address);// initial send (IP of the destination Server)



        fromServer = in.readLine();//initial receive from router (verification of connection)



        System.out.println("ServerRouter: " + fromServer);



        out.println(host); // Client sends the IP of its machine as initial send



        t0 = System.currentTimeMillis();





        // Communication for array data to serverrouter



        //fromServer = in.readLine();



        System.out.println("Server: " + fromServer);



        t1 = System.currentTimeMillis();



        t = t1 - t0;



        System.out.println("Cycle time: " + t);



        int[] send = genArr(15);

        System.out.println("Reached");

        for(int i = 0; i<send.length; i++){



            out.println(Integer.toString(send[i]));

            System.out.println(Integer.toString(send[i]));

        }
        out.println("break");


        ArrayList<Integer> temp = new ArrayList<Integer>();



        String inputLine;



        // Communication while loop



        while ((inputLine = in.readLine()) != null) {
            System.out.println("Client said: " + inputLine);
            temp.add(Integer.valueOf(inputLine));



        }// end while







        int[] tempArray = new int[temp.size()];



        for(int i = 0; i<temp.size(); i++){



            tempArray[i] = temp.get(i);



        }



        for(int i = 0; i<tempArray.length; i++){



            System.out.println(tempArray[i]+" ");



        }







        // closing connections



        out.close();



        in.close();



        Socket.close();



    }



}









