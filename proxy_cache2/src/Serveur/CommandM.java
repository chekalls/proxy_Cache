package src.Serveur;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandM {
    Serveur proxyServeur;
    Scanner inputScanner;
    Map<String, Runnable> listOfCommand;


    public CommandM(Serveur proxyServeur,Scanner inputScanner) {
        this.proxyServeur = proxyServeur;
        this.inputScanner = inputScanner;
        setCommandList();
    }

    public void setCommandList() {
        listOfCommand = new HashMap<>();
        listOfCommand.put("info", () -> {
            System.out.println("serveur running :"+proxyServeur.isRunning());
            System.out.println("running port :"+proxyServeur.getServerPort());
            System.out.println("origine des resource : "+proxyServeur.getNextServer());
            System.out.print("max serveur size :" + proxyServeur.getServeurCache().getMaxCacheSize());
            System.out.println("Ko ||| current cache size :" + proxyServeur.getTotalCacheSize()+" Ko");
            System.out.println("durée max du cache : "+ proxyServeur.getServeurCache().getDuration()+" Min");
        });

        listOfCommand.put("stop",()-> {
            try {
                proxyServeur.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        listOfCommand.put("start", ()->{
            proxyServeur.start();
        });

        listOfCommand.put("exit", ()->{
            System.out.println("bye");
            System.exit(0);
        });

        listOfCommand.put("client_list", ()->{
            for(Client s : proxyServeur.getClientManager().getClientList()){
                System.out.println(s.getClientAdress());
            }
        });

        listOfCommand.put("clear", ()->{
            System.out.print("UserAdress :");
            String address = inputScanner.nextLine();
            try {
                Inet4Address clInet4Address = (Inet4Address) Inet4Address.getByName(address);
                Client c = proxyServeur.getClientManager().getCLient(clInet4Address);
                if(c!=null){
                    c.clearUserCache();
                }else{
                    System.out.println("user not found");
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        });

        listOfCommand.put("help", ()->{
            System.out.println("start : démarrer le serveur avec les configuration préciser dans setting.json");
            System.out.println("stop : arrêter le serveur");
            System.out.println("info : donne des information sur le serveur");
            System.out.println("exit : quitter le programme");
            System.out.println("clear : éffacer le cache d'un utilisateur");
        });

        listOfCommand.put("set", ()->{
           System.out.println(" 1 : parametrer la durée du cache [en minute] ");
           System.out.println(" 2: changer le port du serveur proxy cache");
           System.out.println(" 3: changer le serveur d'origine [format : adress:port ]");
           String input = inputScanner.nextLine();
           if(input.equals("1")){
               System.out.print("nouvelle durée : ");
               int nouvelleDuree = Integer.parseInt(inputScanner.nextLine());

           }
        });
    }

    public void runCommand(String commande) {
        if (listOfCommand.containsKey(commande)) {
            listOfCommand.get(commande).run();
        }else{
            System.out.println("command not found");
        }
    }

}
