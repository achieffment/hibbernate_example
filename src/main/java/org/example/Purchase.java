package org.example;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "price")
    private float price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Purchase() {}

    public void setPurchase(Item pItem, Customer pCustomer, float pPrice) {
        this.item = pItem;
        this.customer = pCustomer;
        this.price = pPrice;
    }

    public String getPurchasedItems() {
        return this.item.getName() + " " + this.price;
    }

    public String getCustomers() {
        return this.customer.getName();
    }

    @Override
    public String toString() {
        return "Purchase: [" + "\n" +
                "\t" + "id: " + id + "\n" +
                "\t" + "item: " + item.toString() + "\n" +
                "\t" + "customer: " + customer.toString() + "\n" +
                "\t" + "price: " + price + "\n" +
                "]";
    }

}