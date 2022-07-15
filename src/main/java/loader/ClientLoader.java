package loader;

import domain.ClientOrder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLoader {
    private final List<ClientOrder> clientOrderList;

    public ClientLoader() {
        this.clientOrderList = new ArrayList<>();
    }

    public void loadClientOrders(String fileName) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(";");
                // assume that first and second elements (side and order type) are the same
                String[] orderQuantityString = data[2].split("=");
                int orderQuantity = Integer.parseInt(orderQuantityString[1]);

                String[] targetPercentageString = data[3].split("=");
                double targetPercentage = Double.parseDouble(targetPercentageString[1]) / 100;

                ClientOrder clientOrder = new ClientOrder(orderQuantity, targetPercentage);
                clientOrderList.add(clientOrder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasClientOrder() {
        return !this.clientOrderList.isEmpty();
    }

    public ClientOrder getClientOrder() {
        if (!this.clientOrderList.isEmpty()) {
            ClientOrder clientOrder = clientOrderList.get(0);
            clientOrderList.remove(clientOrder);
            return clientOrder;
        } else {
            System.out.println("unable to get next client order");
            return null;
        }
    }

    public List<ClientOrder> getClientOrderList() {
        return this.clientOrderList;
    }
}
