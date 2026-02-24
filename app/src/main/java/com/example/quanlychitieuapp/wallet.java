package com.example.quanlychitieuapp;

public class wallet {
    private int id_w;
    private int user_id;
    private String name_wallet;
    private int money;

    public wallet(int id_w, int user_id, String name_wallet, int money) {
        this.id_w = id_w;
        this.user_id = user_id;
        this.name_wallet = name_wallet;
        this.money = money;
    }

    public int getIdWallet() {
        return id_w;
    }

    public int getUserId() {
        return user_id;
    }

    public String getName() {
        return name_wallet;
    }

    public int getMoney() {
        return money;
    }
}
