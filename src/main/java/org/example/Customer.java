package org.example;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customer")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Purchase> purchases;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
    }

    public String getPurchasesString() {
        String allPurchases = "";
        for (Purchase o: purchases) {

        }
        return allPurchases;
    }

    public Customer() {
    }

    @Override
    public String toString() {
        String allPurchases = "";
        for (int i = 0; i < purchases.size(); i++) {
            allPurchases += purchases.get(i).getId();
            if (i < purchases.size() - 1)
                allPurchases += ", ";
        }
        return "Customer: [" + "\n" +
                "\t" + "id: " + id + "\n" +
                "\t" + "name: " + name + "\n" +
                "\t" + "purchasesIds: " + allPurchases + "\n" +
        "]";
    }

}
