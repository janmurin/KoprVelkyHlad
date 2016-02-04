package home.koprvelkyhlad;

import org.hsqldb.Server;

public class SpustacDatabazy {
    
    public static void  execute(){
        Server server =new Server();
        server.setDatabaseName(0,"hladdb");
        server.setDatabasePath(0 ,"db/hladdb");
        server.setPort(12350);
        server.start();
          
    }
    public static void main(String[] args) {
        Server server =new Server();
        server.setDatabaseName(0,"hladdb");
        server.setDatabasePath(0 ,"db/hladdb");
        server.setPort(12350);
        server.start();
    }
}
