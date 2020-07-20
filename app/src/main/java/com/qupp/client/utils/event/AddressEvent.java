package com.qupp.client.utils.event;


public class AddressEvent {

    private String addressId;
    private String address;
    private String name;
    private String phone;

    public AddressEvent(String addressId, String address, String name, String phone) {
        this.addressId = addressId;
        this.address = address;
        this.name = name;
        this.phone = phone;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
