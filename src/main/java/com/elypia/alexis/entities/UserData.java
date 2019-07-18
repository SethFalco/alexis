package com.elypia.alexis.entities;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "user")
@Table
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "user_xp")
    private int xp;

    @Column(name = "last_message")
    private Date lastMessage;

    public int getId() {
        return id;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public Date getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Date lastMessage) {
        this.lastMessage = lastMessage;
    }
}
