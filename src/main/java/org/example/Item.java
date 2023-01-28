package org.example;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "item")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Purchase> purchases;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "price")
    private float price;

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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Item() {
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
    }

    @Override
    public String toString() {
        String allPurchases = "";
        for (int i = 0; i < purchases.size(); i++) {
            allPurchases += purchases.get(i).getId();
            if (i < purchases.size() - 1)
                allPurchases += ", ";
        }
        return "Item: [" + "\n" +
                "\t" + "id: " + id + "\n" +
                "\t" + "name: " + name + "\n" +
                "\t" + "price: " + name + "\n" +
                "\t" + "purchasesIds: " + allPurchases + "\n" +
                "]";
    }

}
