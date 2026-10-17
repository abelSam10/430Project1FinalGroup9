/** This was one of the classes I was assigned. The other was warehouse. This class will
be used to store all client data. It works directly with my warehouse class, so that
worked out nicely. */

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientList {
    private List<Client> clients = new ArrayList<>();

    public Client createClient(String name, String address, String phone) {
        Client c = new Client(name, address, phone);
        clients.add(c);
        return c;
    }

    public Client getClient(String clientID) {
        for (Client c : clients)
            if (c.getId().equals(clientID)) return c;
        return null;
    }

    public Iterator<Client> getClients() {
        return clients.iterator();
    }

    // balance > 0 only, negative balance is credit
    public Iterator<Client> getClientsWithOutstandingBalance() {
        List<Client> result = new ArrayList<>();
        for (Client c : clients)
            if (c.getBalance() > 0) result.add(c);
        return result.iterator();
    }
}
