package project.dto;

public class ClientReportDTO {
    private String name;
    private String city;
    private String address;
    private String establishmentType; // Tipo de estabelecimento
    private int orders; // Número de pedidos

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEstablishmentType() {
        return establishmentType;
    }

    public void setEstablishmentType(String establishmentType) {
        this.establishmentType = establishmentType;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }
}
