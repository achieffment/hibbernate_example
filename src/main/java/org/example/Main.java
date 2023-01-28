package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

public class Main {

    /*
    1. В базе данных необходимо иметь возможность хранить информацию о покупателях (id, имя) и товарах (id, название, стоимость);
    2. У каждого покупателя свой набор купленных товаров, одна покупка одного товара это отдельная запись в таблице (группировать не надо);
    3. Написать  тестовое  консольное  приложение  (просто  Scanner  и  System.out.println()), которое позволит выполнять команды: /showProductsByPerson имя_покупателя-посмотреть какие товары покупал клиент; /findPersonsByProductTitle название_товара-какие  клиенты  купили  определенный  товар; /removePerson(removeProduct) имя_элемента-предоставитьвозможность удалять из базы товары/покупателей,  /buy имя_покупателя название_товара-организовать возможность “покупки товара”.
    4. Добавить  детализацию  по  паре  покупатель-товар:  сколько  стоил  товар,  в  момент покупки клиентом;
    */

    private SessionFactory factory;

    public Main() {
        this.factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Item.class)
                .addAnnotatedClass(Purchase.class)
                .buildSessionFactory();
    }

    private int getItemsCount() {
        Session session = this.factory.getCurrentSession();
        session.beginTransaction();
        List<Item> items = session.createQuery("SELECT i FROM Item i", Item.class).getResultList();
        session.getTransaction().commit();
        session.close();
        return items.size();
    }

    private int getCustomersCount() {
        Session session = this.factory.getCurrentSession();
        session.beginTransaction();
        List<Customer> customers = session.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
        session.getTransaction().commit();
        session.close();
        return customers.size();
    }

    private void addTestcustomers(int count) {
        for (int i = 0; i < count; i++) {
            Customer customer = new Customer();
            customer.setName("Customer #" + i);
            Session session = this.factory.getCurrentSession();
            session.beginTransaction();
            session.save(customer);
            session.getTransaction().commit();
            session.close();
        }
    }

    private void addTestItems(int count, int priceMin, int priceMax) {
        for (int i = 0; i < count; i++) {
            Item item = new Item();
            item.setName("Item #" + i);
            float randPrice = (float) (Math.random() * (priceMax - priceMin)) + priceMin;
            item.setPrice(randPrice);
            Session session = this.factory.getCurrentSession();
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
            session.close();
        }
    }

    private void addTestPurchases(int count) {
        int customersCount = getCustomersCount();
        int itemsCount = getItemsCount();
        for (int i = 0; i < count; i++) {

            int randcustomer = (int) (Math.random() * (customersCount - 1)) + 1;
            int randItem = (int) (Math.random() * (itemsCount - 1)) + 1;

            Session session = this.factory.getCurrentSession();
            session.beginTransaction();
            Item item = session.get(Item.class, randItem);
            session.getTransaction().commit();

            session = this.factory.getCurrentSession();
            session.beginTransaction();
            Customer customer = session.get(Customer.class, randcustomer);
            session.getTransaction().commit();

            Purchase purchase = new Purchase();
            purchase.setPurchase(item, customer, item.getPrice());
            customer.addPurchase(purchase);
            item.addPurchase(purchase);

            session = this.factory.getCurrentSession();
            session.beginTransaction();
            session.save(purchase);
            session.getTransaction().commit();

            session.close();

        }
    }

    private void addTestData(int customersCount, int itemsCount, int purchasesCount, int priceMin, int priceMax) {
        addTestcustomers(customersCount);
        addTestItems(itemsCount, priceMin, priceMax);
        addTestPurchases(purchasesCount);
        System.out.println("Тестовые данные успешно добавлены!");
    }

    public static void main(String[] args) {

        Main main = new Main();
        main.addTestData(5, 10, 5, 100, 500);
        main.mainLoop();

    }

    private void mainLoop() {

        while (true) {

            System.out.println("Введите команду: ");
            System.out.println("1. Просмотреть товары (/showProductsByPerson)");
            System.out.println("2. Какие клиенты покупили определенный товар (/findPersonsByProductTitle)");
            System.out.println("3. Удалить покупателя (/removePerson)");
            System.out.println("4. Купить товар (/buy)");
            System.out.println("5. Выход (/exit)");

            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();

            if (command.equals("1") || command.equals("/showProductsByPerson")) {
                System.out.println("Введите имя пользователя: ");
                String customer = scanner.nextLine();
                if (this.checkEmptyField(customer, "Имя пользователя"))
                    this.getPurchasedItemsByCustomerName(customer);
            } else if (command.equals("2") || command.equals("/findPersonsByProductsTitle")) {
                System.out.println("Введите название товара: ");
                String item = scanner.nextLine();
                if (this.checkEmptyField(item, "Название товара"))
                    this.getCustomersByItemName(item);
            } else if (command.equals("3") || command.equals("/removePerson")) {
                System.out.println("Введите имя пользователя: ");
                String customer = scanner.nextLine();
                if (this.checkEmptyField(customer, "Имя пользователя"))
                    this.removeCustomerByName(customer);
            } else if (command.equals("4") || command.equals("/buy")) {
                System.out.println("Введите имя пользователя: ");
                String customer = scanner.nextLine();
                if (this.checkEmptyField(customer, "Имя пользователя")) {
                    System.out.println("Введите название товара: ");
                    String item = scanner.nextLine();
                    if (this.checkEmptyField(customer, "Имя пользователя"))
                        this.buyItem(customer, item);
                }
            } else if (command.equals("5") || command.equals("/exit")) {
                this.factory.close();
                break;
            } else
                System.out.println("Вы ввели несуществующую команду!");

        }

    }

    private boolean checkEmptyField(String input, String field) {
        if (input.isEmpty()) {
            System.out.println("Поле [" + field + "]" + " не может быть пустым");
            return false;
        }
        return true;
    }

    private List<Customer> getCustomersByName(String customer) {
        Session session = this.factory.getCurrentSession();
        session.beginTransaction();
        List<Customer> customers = session
                .createQuery("from Customer c where c.name = :name")
                .setParameter("name", customer)
                .getResultList();
        session.getTransaction().commit();
        session.close();
        return customers;
    }

    private List<Item> getItemsByName (String item) {
        Session session = this.factory.getCurrentSession();
        session.beginTransaction();
        List<Item> items = session
                .createQuery("from Item i where i.name = :name")
                .setParameter("name", item)
                .getResultList();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    private void getPurchasedItemsByCustomerName(String customer) {
        List<Customer> customers = this.getCustomersByName(customer);
        if (!customers.isEmpty()) {
            List<Purchase> purchases = customers.get(0).getPurchases();
            if (!purchases.isEmpty()) {
                String purchasedItem = "";
                for (Purchase o: purchases) {
                    purchasedItem += o.getPurchasedItems() + "\n";
                }
                System.out.println("Приобритенные товары: ");
                System.out.println(purchasedItem);
            } else
                System.out.println("У данного пользователя нет приобретенных товаров!");
        } else
            System.out.println("Введенный пользователь не найден!");
    }

    private void getCustomersByItemName(String item) {
        List<Item> items = this.getItemsByName(item);
        if (!items.isEmpty()) {
            List<Purchase> purchases = items.get(0).getPurchases();
            if (!purchases.isEmpty()) {
                String purchasedCustomers = "";
                for (Purchase o: purchases) {
                    purchasedCustomers += o.getCustomers() + "\n";
                }
                System.out.println("Товар приобрели пользователи: ");
                System.out.println(purchasedCustomers);
            } else
                System.out.println("Данный товар ещё не приобретался!");
        } else
            System.out.println("Введенный товар не найден!");
    }

    private void removeCustomerByName(String customer) {
        List<Customer> customers = this.getCustomersByName(customer);
        if (!customers.isEmpty()) {
            Session session = this.factory.getCurrentSession();
            session.beginTransaction();
            session.delete(customers.get(0));
            session.getTransaction().commit();
            session.close();
            System.out.println("Пользователь успешно удален");
        } else
            System.out.println("Введенный пользователь не найден!");
    }

    private void buyItem(String customer, String item) {
        List<Customer> customers = getCustomersByName(customer);
        if (!customers.isEmpty()) {
            List<Item> items = getItemsByName(item);
            if (!items.isEmpty()) {
                Purchase purchase = new Purchase();
                purchase.setPurchase(items.get(0), customers.get(0), items.get(0).getPrice());
                customers.get(0).addPurchase(purchase);
                items.get(0).addPurchase(purchase);
                Session session = this.factory.getCurrentSession();
                session.beginTransaction();
                session.save(purchase);
                session.getTransaction().commit();
                session.close();
                System.out.println("Поздравляем, товар успешно куплен!");
            } else
                System.out.println("Введенного товара не существует!");
        } else
            System.out.println("Введенного пользователя не существует!");
    }

}